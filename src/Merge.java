import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.File;

//import javax.activation.FileDataSource;
//import javax.activation.MimetypesFileTypeMap;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.io.FileInputStream;


public class Merge extends JPanel {

	JLabel pdf1Label, pdf2Label,Tooltext;
	static JTextField pdf1TextField, pdf2TextField;
	JButton browseButton1, browseButton2, mergeButton;
	static File output_location;
	final static int plainMessage = JOptionPane.PLAIN_MESSAGE;
	final static int errorMessage = JOptionPane.ERROR_MESSAGE;
	final static int warningMessage = JOptionPane.WARNING_MESSAGE;

	public Merge()
	{
		createGUI();
		setActions();
	}

	public void createGUI()
	{
		Tooltext = new JLabel("PDF Merger");
		Tooltext.setBounds(230,10,200,40);
		
		pdf1Label = new JLabel("First pdf:");
		pdf1Label.setBounds(100, 200, 150, 40);
		pdf1TextField = new JTextField(10);
		pdf1TextField.setBounds(300, 200, 150, 40);
		browseButton1 = new JButton("Browse!");
		browseButton1.setBounds(320, 250, 100, 25);
		Tooltext.setFont(new Font("Serif", Font.ITALIC, 20));
		pdf2Label = new JLabel("Second pdf:");
		pdf2Label.setBounds(100, 300, 150, 40);
		pdf2TextField = new JTextField(10);
		pdf2TextField.setBounds(300, 300, 150, 40);
		browseButton2 = new JButton("Browse!");
		browseButton2.setBounds(320, 350, 100, 25);

		mergeButton = new JButton("Merge!");
		mergeButton.setBounds(220,450,100,30);
		

		add(Tooltext);
		add(pdf1Label);
		add(pdf1TextField);
		add(browseButton1);
		add(pdf2Label);
		add(pdf2TextField);
		add(browseButton2);
		add(mergeButton);
		setLayout(new BorderLayout());
	}

	public void setActions()
	{
		browseButton1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
				fileChooser.setFileFilter(filter);
				int rVal = fileChooser.showOpenDialog(MainClass.frame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					String extension = check_extension(fileChooser.getSelectedFile()).toLowerCase();
					if(extension.equals("pdf"))
					{
						pdf1TextField.setText(fileChooser.getSelectedFile().toString());
						return;
					}
				}
				render_dialog("Invalid extension", warningMessage);

			} });

		browseButton2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
				fileChooser.setFileFilter(filter);
				int rVal = fileChooser.showOpenDialog(MainClass.frame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					String extension = check_extension(fileChooser.getSelectedFile()).toLowerCase();
					if(extension.equals("pdf"))
					{
						pdf2TextField.setText(fileChooser.getSelectedFile().toString());
						return;
					}
				}
				render_dialog("Invalid extension", warningMessage);
			} });

		mergeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				String pdf1= pdf1TextField.getText();
				String pdf2= pdf2TextField.getText();
				if(pdf1.isEmpty() || pdf2.isEmpty())
				{
					render_dialog("File not selected", warningMessage);
					return;	
				}

				try {
					List<InputStream> pdfs = new ArrayList<InputStream>();
					pdfs.add(new FileInputStream(pdf1));
					pdfs.add(new FileInputStream(pdf2));
					output_location = choose_file();
					if(output_location == null)
						return;
					String extension = check_extension(output_location).toLowerCase();
					if(extension.equals("pdf"))
					{
						OutputStream output = new FileOutputStream(output_location.getAbsolutePath());
						Merge.mergePDFs(pdfs, output, true);
					}
					else
					{
						render_dialog("Select valid destination", errorMessage);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} });

	}
	//Open the dialog to let user choose the destination of output file
	public File choose_file()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		fileChooser.setName("Save File as");
		fileChooser.showSaveDialog(MainClass.frame);	
		return fileChooser.getSelectedFile();
	}
	//Check the extension of the file selected
	public String check_extension(File location)
	{
		String[] locs = location.toString().split("/");
		String file = locs[locs.length -1];
		String[] extension = file.split("\\.");
		String file_extension = extension[extension.length-1];
		return file_extension;
	}

	protected static void mergePDFs(List<InputStream> streamOfPDFFiles,
			OutputStream outputStream, boolean paginate) {
		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}

			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF

			// data
			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}

			pdf1TextField.setText("");
			pdf2TextField.setText("");
			outputStream.flush();
			document.close();
			outputStream.close();
			render_dialog("Merge successful!", plainMessage);
			if (Desktop.isDesktopSupported()) {
	            try {
	                File myFile = new File(output_location.toString());
	                Desktop.getDesktop().open(myFile);
	            } catch (IOException ex) {
	                // no application registered for PDFs
	            }
	        }

		} catch (Exception e) {
			e.printStackTrace();
			render_dialog("Merge unsuccessful", errorMessage);
		} finally {
			if (document.isOpen())
				document.close();
		}
	}


	public static void render_dialog(String err_msg, int dialogType)
	{
		JOptionPane.showMessageDialog(MainClass.frame,
				err_msg,
				"Merge",
				dialogType);
		return;	
	}
}

