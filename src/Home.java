import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Home extends JPanel {
	
	private JButton splitButton, convertButton, mergeButton,compareButton;
	private JTextPane textPane1, textPane2,textPane3;

	public Home()
	{		
		textPane1 = new JTextPane();
		StyledDocument doc = textPane1.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		textPane1.setText("           PDF merging and splitting . \n To Merge click merge, and to split click Split. \n ");
		textPane1.setEditable(false);
		textPane1.setBackground(Color.lightGray);
		textPane1.setBounds(150,100, 300,50);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		
		splitButton = new JButton("Split");
		splitButton.setBounds(180, 200, 100, 30);
		
		mergeButton = new JButton("Merge");
		mergeButton.setBounds(320, 200, 100, 30);

		textPane2 = new JTextPane();
		doc = textPane2.getStyledDocument();
		textPane2.setText("            PDF Converter to Doc format \n To convert a pdf file to document file \n ");
		textPane2.setEditable(false);
		textPane2.setBackground(Color.lightGray);
		textPane2.setBounds(150,350,300,50);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		convertButton = new JButton("DocToPdf");
		convertButton.setBounds(235, 450, 120, 30);
		
		textPane3 = new JTextPane();
		StyledDocument doc1 = textPane3.getStyledDocument();
		SimpleAttributeSet center1 = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		textPane3.setText("           Comparing two pdf file \n ");
		textPane3.setEditable(false);
		textPane3.setBackground(Color.lightGray);
		textPane3.setBounds(150,500, 300,50);
		doc.setParagraphAttributes(0, doc1.getLength(), center1, false);
		
		
		compareButton=new JButton("Compare two doc");
		compareButton.setBounds(235,600,120,30);
		
		splitButton.setToolTipText("Click to split the pdf");
		mergeButton.setToolTipText("Click to merge PDF");
		convertButton.setToolTipText("Click to Convert DOC to PDF");
		compareButton.setToolTipText("Click to compare two files");
		splitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				MainClass.callSplit();
			}
		});
		convertButton.addActionListener(new ActionListener()
		{
			 public void actionPerformed(ActionEvent ae)
			 {
				 MainClass.callConvert();
			 }
		});

		mergeButton.addActionListener(new ActionListener()
		{
			 public void actionPerformed(ActionEvent ae)
			 {	
				 MainClass.callMerge();
				 
			 }
		});
		
		compareButton.addActionListener(new ActionListener()
		{
			 public void actionPerformed(ActionEvent ae)
			 {	
				 MainClass.callCompare();
				 
			 }
		});
		
		setLayout(null);

		add(textPane1);
		add(splitButton);
		add(mergeButton);
		add(textPane2);
		add(textPane3);
		add(compareButton);
		add(convertButton);
	}
}
