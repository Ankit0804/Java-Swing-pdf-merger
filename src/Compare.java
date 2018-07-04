
import java.awt.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.io.*;

public class Compare extends JFrame {
	JTextPane txtpane1;
	JTextPane txtpane2;
	JLabel lbl1;
	JLabel lbl2;
	JLabel lblcompare;
	//JButton btnback;
	File file1;
	File file2;
	int count1,count2,diffcount;
	JScrollPane pane1;
	JScrollPane pane2;
	public Compare(File f1,File f2)
	{

		super("Document compare tool");
		lbl1=new JLabel(f1.getName());
		lbl2=new JLabel(f2.getName());
		txtpane1=new JTextPane();
		txtpane2=new JTextPane();
		pane1=new JScrollPane(txtpane1);
		pane2=new JScrollPane(txtpane2);
		pane1.setPreferredSize(new Dimension(300,300));
		pane2.setPreferredSize(new Dimension(300,300));
		file1=f1;
		file2=f2;
		txtpane1.setEditable(false);
		txtpane2.setEditable(false);
		JPanel pnl=new JPanel();
		
		pnl.add(pane1);
		pnl.add(pane2);
		pnl.add(lbl1);
		pnl.add(lbl2);
		//pnl.add(btnback);
		setContentPane(pnl);
		JScrollBar bar1=pane1.getVerticalScrollBar();
		pane1.getVerticalScrollBar().setModel(
	            pane2.getVerticalScrollBar().getModel());
	
		
	}
	public void CompareFiles() 
	{   		JFrame frame=new JFrame();

		diffcount=0;
		BufferedReader rd1=null;
		BufferedReader rd2=null;
		StyledDocument doc1=txtpane1.getStyledDocument();
		StyledDocument doc2=txtpane2.getStyledDocument();
		Boolean vin=true;
		
		String s1,s2;
		try
		{
			Style style7 = doc1.addStyle("StyleName", null);
			StyleConstants.setForeground(style7, Color.red);
			Style style8 = doc2.addStyle("StyleName", null);
			StyleConstants.setForeground(style8, Color.red);
			rd1=new BufferedReader(new FileReader(file1));
			rd2=new BufferedReader(new FileReader(file2));
			doc1.insertString(doc1.getLength(),"\nCONTENT OF "+file1,style7);
			doc2.insertString(doc2.getLength(),"\nCONTENT OF "+file2,style8);


			while(((s1=rd1.readLine())!=null) && ((s2=rd2.readLine())!=null))
			{
				
				if(s1.equals(s2))
				{   vin=vin&true;
					doc1.insertString(doc1.getLength(),"\n"+s1,null);
					doc2.insertString(doc2.getLength(),"\n"+s2,null);
				}
				else
				{
				    vin=vin&false;
					Style style1 = doc1.addStyle("StyleName", null);
					StyleConstants.setForeground(style1, Color.red);
					Style style2 = doc2.addStyle("StyleName", null);
					StyleConstants.setForeground(style2, Color.red);
					doc1.insertString(doc1.getLength(),"\n"+s1,style1);
					doc2.insertString(doc2.getLength(),"\n"+s2,style2);
					diffcount++;
				}
				
			}
			Style style5 = doc1.addStyle("StyleName", null);
			StyleConstants.setForeground(style5, Color.red);
			Style style6 = doc2.addStyle("StyleName", null);
			StyleConstants.setForeground(style6, Color.red);

			Style style3 = doc1.addStyle("StyleName", null);
			StyleConstants.setForeground(style3, Color.blue);
			Style style4 = doc2.addStyle("StyleName", null);
			StyleConstants.setForeground(style4, Color.blue);
			doc1.insertString(doc1.getLength(),"\n\nFile Information for "+file1, style5);
			doc2.insertString(doc2.getLength(),"\n\nFile Information for "+file2, style6);
			doc1.insertString(doc1.getLength(),"\n Can File Read : "+file1.canRead(), style3);
			doc1.insertString(doc1.getLength(),"\ncan File execute : "+file1.canExecute(), style3);
			doc1.insertString(doc1.getLength(),"\ncan File write :"+file1.canWrite(), style3);
			doc1.insertString(doc1.getLength(),"\ncan File hidden :"+file1.isHidden(), style3);

			doc2.insertString(doc2.getLength(),"\n Can File Read :"+file1.canRead(), style4);
			doc2.insertString(doc2.getLength(),"\ncan File execute :"+file1.canExecute(), style4);
			doc2.insertString(doc2.getLength(),"\ncan File write :"+file1.canWrite(), style4);
			doc2.insertString(doc2.getLength(),"\ncan File hidden :"+file1.isHidden(), style4);

			if(vin){
				JOptionPane.showMessageDialog(frame,
					    "Files are equal",
					    "Compare Report",
					    JOptionPane.PLAIN_MESSAGE);
			}else
			{
				JOptionPane.showMessageDialog(frame,
					    "Files are not equal",
					    "Compare Report",
					    JOptionPane.PLAIN_MESSAGE);
			}
			while((s1=rd1.readLine())!=null)
			{
				Style style1 = doc1.addStyle("StyleName", null);
				StyleConstants.setForeground(style1, Color.red);
				doc1.insertString(doc1.getLength(),"\n"+s1,style1);
			}
			while((s2=rd2.readLine())!=null)
			{
				Style style2 = doc2.addStyle("StyleName", null);
				StyleConstants.setForeground(style2, Color.red);
				doc2.insertString(doc2.getLength(),"\n"+s2,style2);
			}
		}
		catch (FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "File not found");
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null,"Error in reading file");
		}
		catch(BadLocationException e)
		{
			JOptionPane.showMessageDialog(null,"bad location exception");
		}
		finally
		{
			try{
				if(rd1!=null) rd1.close();
				if(rd2!=null) rd2.close();
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
		}

}
}
