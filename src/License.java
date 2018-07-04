import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Enumeration;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.*;


public class License extends JPanel{

	private JLabel label, licenseLabel;
	private static String agree ="Agree" ,disagree= "Disagree";
	private JButton nextButton;
	private FileReader filereader;
	private JTextArea textArea;
	private ButtonGroup group;
	private JRadioButton acceptButton,rejectButton;
	private JPanel radioButtonPanel;
	private static final String AGREE = "1";
	private static final String DISAGREE = "0";

	public License() throws IOException
	{

		label = new JLabel("Welcome to PDF and Doc Tool \n");
		label.setBounds(140, 10, 400, 20);
		label.setFont(new Font("Courier New", Font.ITALIC, 18));
		label.setForeground(Color.BLACK);
		
		acceptButton = new JRadioButton(agree, true);
		rejectButton = new JRadioButton(disagree);

		licenseLabel = new JLabel("I accept the terms and agreement.");
		licenseLabel.setBounds(50, 550, 300, 15);
		
		nextButton = new JButton("Next");
		nextButton.setBounds(450, 650, 100, 30);

		String text = read_from_file();
		textArea = new JTextArea();
		textArea.setFont(new Font("Serif", Font.ITALIC, 13));
		textArea.setBounds(20,20,500,500);
		textArea.setText(text);
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(50,50,500, 500);

		group = new ButtonGroup();
		group.add(acceptButton);
		group.add(rejectButton);
		radioButtonPanel = new JPanel();
		radioButtonPanel.setLayout(new GridLayout(0,1));
		radioButtonPanel.add(acceptButton);
		radioButtonPanel.add(rejectButton);
		radioButtonPanel.setBounds(50,580,200,100);

		nextButton.addActionListener(new ActionListener()
		{

			public void actionPerformed(ActionEvent event)
			{
				if(event.getSource()==nextButton)  
				{  
					Enumeration<AbstractButton> allRadioButton=group.getElements();  
					while(allRadioButton.hasMoreElements())  
					{  
						JRadioButton temp=(JRadioButton)allRadioButton.nextElement();  
						if(temp.isSelected())  
						{  
							if(temp.getText().equals(agree))
							{
								//If users agrees to the license then write 1 to file
								try{
									file_write(AGREE);
								}
								catch(Exception e){
									JOptionPane.showMessageDialog(MainClass.frame,
											"Error",
											"License Agreement",
											JOptionPane.ERROR_MESSAGE);
								}	
								MainClass.callHome();
							}
							else
							{
								JOptionPane.showMessageDialog(MainClass.frame,
										"Accept the license to proceed",
										"License Agreement",
										JOptionPane.ERROR_MESSAGE);
								//If users disagrees to the license then write 0 to file
								try{
									file_write(DISAGREE);
								}
								catch(Exception e){}
							}
						}  
					}            
				}
			}
		});
		setLayout(null);
		add(label);
		add(scroll);
		add(licenseLabel);
		add(radioButtonPanel);
		add(nextButton);
		setBorder(BorderFactory.createEmptyBorder());
	}	

	//To write to the file
	public static void file_write(String num) throws IOException
	{
		File file = new File("config.txt");
		if(!file.exists())
		{
			file.createNewFile();
		}
		PrintWriter out = new PrintWriter(file);
		try 
		{
			out.write(num);
		} 
		finally
		{
			out.close();	
		}
	}

	//Read the license ters from the file
	public String read_from_file()
	{
		String FILENAME= "license.txt";
		BufferedReader br = null;
		FileReader fr = null;
		StringBuilder sb = new StringBuilder();
		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
				sb.append(System.lineSeparator());
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return sb.toString();

	}

}
