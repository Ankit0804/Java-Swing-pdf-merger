import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.util.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.*;

public class Split extends JPanel{

	private JLabel pdfLabel, startPageLabel, lastPageLabel,Tooltext;
	private static JTextField pdfTextField, startPageTextField, lastPageTextField;
	private JButton splitButton, browseButton;
	private static File output_location;
	final static int plainMessage = JOptionPane.PLAIN_MESSAGE;
	final static int errorMessage = JOptionPane.ERROR_MESSAGE;
	final static int warningMessage = JOptionPane.WARNING_MESSAGE;

	public Split()
	{
		prepareGUI();
		setActions();	
	}

	public void prepareGUI()
	{
		Tooltext = new JLabel("PDF Splitter");
		Tooltext.setBounds(230,10,200,40);
		Tooltext.setFont(new Font("Serif", Font.ITALIC, 20));

		pdfLabel = new JLabel("Selected pdf:");
		pdfLabel.setBounds(100,100,150,40);
		
		pdfTextField = new JTextField(10);
		pdfTextField.setBounds(220, 105, 150, 30);

		browseButton = new JButton("Browse");
		browseButton.setBounds(250, 150, 100, 25);

		startPageLabel = new JLabel("Enter start page: ");
		startPageLabel.setBounds(100, 250, 150, 40);

		startPageTextField = new JTextField(3);
		startPageTextField.setBounds(250,250,50,30);
		
		lastPageLabel = new JLabel("Enter last page: ");
		lastPageLabel.setBounds(100,300,200,40);

		lastPageTextField = new JTextField(3);
		lastPageTextField.setBounds(250, 300, 50, 30);

		splitButton = new JButton("Split");
		splitButton.setBounds(250, 500, 100, 30);

		add(Tooltext);
		add(pdfLabel);
		add(pdfTextField);
		add(browseButton);
		add(startPageLabel);
		add(startPageTextField);
		add(lastPageLabel);
		add(lastPageTextField);
		add(splitButton);

		setLayout(new BorderLayout());
	}

	public void setActions()
	{
		browseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
				fileChooser.setFileFilter(filter);
				int returnValue = fileChooser.showOpenDialog(MainClass.frame);
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					String extension = check_extension(fileChooser.getSelectedFile()).toLowerCase();
					if(extension.equals("pdf"))
					{
						pdfTextField.setText(fileChooser.getSelectedFile().toString());
						return;
					}
				}

				render_dialog("Select valid file", warningMessage);
			}	
		});

		splitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				String fileName= pdfTextField.getText();
				if(fileName.isEmpty())
				{
					render_dialog("File not selected", warningMessage);
					return;
				}
				if(startPageTextField.getText().isEmpty())
				{
					render_dialog("Enter start page", warningMessage);
					return;
				}
				int startPage= Integer.parseInt(startPageTextField.getText());
				if(lastPageTextField.getText().isEmpty())
				{
					render_dialog("Enter end page", warningMessage);
					return;
				}
				int lastPage= Integer.parseInt(lastPageTextField.getText());

				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					output_location = choose_file();
					if(output_location == null)
						return;
					String extension = check_extension(output_location).toLowerCase();
					if(extension.equals("pdf"))
					{
						OutputStream output = new FileOutputStream(output_location.getAbsolutePath());
						Split.splitPDF(inputStream,output,startPage,lastPage);					}
					else
					{
						render_dialog("Select valid destination", warningMessage);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}});
	}

	public static void splitPDF(InputStream inputStream,
			OutputStream outputStream, int startPage, int endPage) {
		Document document = new Document();
		try{
			try {
				PdfReader inputPDF = new PdfReader(inputStream);
				int totalPages = inputPDF.getNumberOfPages();
				//Start page should be lesser than or equal to the end page
				if(startPage > endPage ) {
					render_dialog("Invalid start page", warningMessage);
					return;
				}
				//Set the end page to the last page if it is more than the total num of pages 
				if(endPage > totalPages) {
					endPage = totalPages;
				}
				// Start page can't be greater than the total num of pages 
				if(startPage > totalPages) {
					render_dialog("Invalid start page", warningMessage);
					return;
				}

				PdfWriter writer = PdfWriter.getInstance(document, outputStream);
				document.open();
				// Holds the PDF data
				PdfContentByte cb = writer.getDirectContent(); 
				PdfImportedPage page;
				//Read the data from pdf page by page and write it to the output pdf
				while(startPage <= endPage) { 
					document.newPage();
					page = writer.getImportedPage(inputPDF, startPage);
					cb.addTemplate(page, 0, 0);
					startPage++;
				}
				
				//Clear the text fields after conversion
				pdfTextField.setText("");
				startPageTextField.setText("");
				lastPageTextField.setText("");
				render_dialog("Split successful", plainMessage);
				//Open the pdf after split
//				if (Desktop.isDesktopSupported()) {
//		            try {
//		                File pdfFile = new File(output_location.toString());
//		                Desktop.getDesktop().open(pdfFile);
//		            } catch (IOException ex) {
//		                // no application registered for PDFs
//		            }
//		        }
//				
			}finally {
				outputStream.flush();
				document.close();
				outputStream.close();
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			render_dialog("Split unsuccessful", errorMessage);
		} 
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

	//Render the dialog box
	public static void render_dialog(String err_msg, int dialogType)
	{
		JOptionPane.showMessageDialog(MainClass.frame,
				err_msg,
				"Merge",
				dialogType);
		return;	
	}
}