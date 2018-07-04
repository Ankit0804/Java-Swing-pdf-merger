import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;

public class WordToPdfConverter extends JPanel{

	JLabel docLabel,Tooltext;
	static JTextField docTextField;
	JButton browseButton, convertButton;
	final static int plainMessage = JOptionPane.PLAIN_MESSAGE;
	final static int errorMessage = JOptionPane.ERROR_MESSAGE;
	final static int warningMessage = JOptionPane.WARNING_MESSAGE;
	File file;
	File output_location;

	public WordToPdfConverter()
	{
		prepareGUI();
		setActions();
	}

	public void prepareGUI()
	{
		Tooltext = new JLabel("Converter");
		Tooltext.setBounds(230,10,200,40);
		Tooltext.setFont(new java.awt.Font("Serif", java.awt.Font.ITALIC, 20));

		docLabel = new JLabel("Selected document:");
		docLabel.setBounds(100, 200, 200, 40);
		docTextField = new JTextField(10);
		docTextField.setBounds(300, 200, 150, 40);
		browseButton = new JButton("Browse!");
		browseButton.setBounds(320, 280, 100, 25);
		convertButton = new JButton("Convert");
		convertButton.setBounds(320, 380, 100, 25);

		add(Tooltext);
		add(docLabel);
		add(docTextField);
		add(browseButton);
		add(convertButton);
		setLayout(new BorderLayout());
	}

	public void setActions()
	{

		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".docx, .doc Files", "docx", "doc");
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(MainClass.frame);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					String extension = check_extension(fileChooser.getSelectedFile()).toLowerCase();
					if(extension.equals("docx") || extension.equals("doc"))
					{
						docTextField.setText(fileChooser.getSelectedFile().toString());
						file=fileChooser.getSelectedFile();
						return;
					}
				}
				render_dialog("Select valid file", warningMessage);
			}
		});

		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				output_location = choose_file();
				if(output_location == null)
					return;
				String extension = check_extension(output_location).toLowerCase();
				OutputStream output = null;
				if(extension.equals("pdf"))
				{
					try {
						output = new FileOutputStream(output_location.getAbsolutePath());
					} catch (FileNotFoundException exception) {
						// TODO Auto-generated catch block
						exception.printStackTrace();
						render_dialog("Error occured", errorMessage);
						return;
					}
				}
				else
				{
					render_dialog("Select valid destination", errorMessage);
				}
		
				convertWordToPdf(file.toString(),output);
			}
		});		
	}


	public  void convertWordToPdf(String src, OutputStream desc){
		//72 units=1 inch
		Document pdfdoc=new Document(PageSize.A4,72,72,72,72);
		try{
			//create file input stream object to read data from file 
			FileInputStream fs=new FileInputStream(src);
			//create document object to wrap the file input stream object
			XWPFDocument doc = new XWPFDocument(fs); 
			
			//create a pdf writer object to write text to mypdf.pdf file
			PdfWriter pwriter=PdfWriter.getInstance(pdfdoc, desc);

			//specify the vertical space between the lines of text
			pwriter.setInitialLeading(10);
			//get all paragraphs from word docx
			List<XWPFParagraph> plist=doc.getParagraphs();

			//open pdf document for writing
			pdfdoc.open();
			for (int i = 0; i < plist.size(); i++) {
				//read through the list of paragraphs
				XWPFParagraph pa = plist.get(i);
				//get all run objects from each paragraph
				List<XWPFRun> runs = pa.getRuns();
				//read through the run objects
				for (int j = 0; j < runs.size(); j++) {       
					XWPFRun run=runs.get(j);
					//get pictures from the run and add them to the pdf document
					List<XWPFPicture> piclist=run.getEmbeddedPictures();
					//traverse through the list and write each image to a file
					Iterator<XWPFPicture> iterator=piclist.iterator();
					while(iterator.hasNext()){
						XWPFPicture pic=iterator.next();
						XWPFPictureData picdata=pic.getPictureData();
						byte[] bytepic=picdata.getData(); 
						Image imag=Image.getInstance(bytepic);
						pdfdoc.add(imag);

					}
					//get color code
					int color=getCode(run.getColor());
					//construct font object
					Font f=null;
					if(run.isBold() && run.isItalic())
						f=FontFactory.getFont(FontFactory.TIMES_ROMAN,run.getFontSize(),Font.BOLDITALIC, new BaseColor(color));
					else if(run.isBold())
						f=FontFactory.getFont(FontFactory.TIMES_ROMAN,run.getFontSize(),Font.BOLD, new BaseColor(color));
					else if(run.isItalic())
						f=FontFactory.getFont(FontFactory.TIMES_ROMAN,run.getFontSize(),Font.ITALIC, new BaseColor(color));
					else if(run.isStrike())
						f=FontFactory.getFont(FontFactory.TIMES_ROMAN,run.getFontSize(),Font.STRIKETHRU, new BaseColor(color));
					else
						f=FontFactory.getFont(FontFactory.TIMES_ROMAN,run.getFontSize(),Font.NORMAL, new BaseColor(color));
					//construct unicode string
					String text=run.getText(-1);
					byte[] bs;
					if (text!=null){
						bs=text.getBytes();
						String str=new String(bs,"UTF-8");
						//add string to the pdf document
						//Chunk chObj1=new Chunk(str,f);

						//System.out.println(chObj1.getContent());
						pdfdoc.add(new Paragraph(str));
					}      

				}
				//output new line
				pdfdoc.add(new Chunk(Chunk.NEWLINE));
			}
//			close pdf document  
			pdfdoc.close();
			docTextField.setText("");
			render_dialog("Conversion successfull", plainMessage);
			//Open the pdf after conversion
			if (Desktop.isDesktopSupported()) {
	            try {
	                File myFile = new File(output_location.toString());
	                Desktop.getDesktop().open(myFile);
	            } catch (IOException ex) {
	                // no application registered for PDFs
	            }
	        }
			
		}catch(Exception e){
			e.printStackTrace();
			pdfdoc.close();
			render_dialog("Conversion unsuccessful", errorMessage);
		}finally {
			if (pdfdoc.isOpen())
			{
				pdfdoc.close();
				
			}
				
		}
	} 

	public int getCode(String code){
		int colorCode;
		if(code!=null)
			colorCode=Long.decode("0x"+code).intValue();
		else
			colorCode=Long.decode("0x000000").intValue();
		return colorCode;
	}

	//Open the dialog to let user choose the destination of output file
	public File choose_file()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		fileChooser.setName("Save File as");
		fileChooser.showSaveDialog(MainClass.frame);
		System.out.println(fileChooser.getCurrentDirectory());
		System.out.println(fileChooser.getSelectedFile());	
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

	public static void render_dialog(String err_msg, int dialogType)
	{
		JOptionPane.showMessageDialog(MainClass.frame,
				err_msg,
				"Merge",
				dialogType);
		return;	
	}

}
