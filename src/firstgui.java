

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class firstgui extends JFrame {
	JLabel file1;
	JLabel file2;
	JTextField txtfile1;
	JTextField txtfile2;
	JButton btnfile1;
	JButton btnfile2;
	JButton btncompare;
	JFileChooser filechsr1;
	JFileChooser filechsr2;
	File f1,f2;
	TitledBorder titlefile1;
	TitledBorder titlefile2;
	public firstgui()
	{
		super("Document compare tool");
		file1=new JLabel("First File");
		file2=new JLabel("Second File");
		txtfile1=new JTextField(20);
		txtfile2=new JTextField(20);
		btnfile1=new JButton("Browse..");
		btnfile2=new JButton("Browse..");
		btncompare=new JButton("Compare");
		filechsr1=new JFileChooser();
		filechsr2=new JFileChooser();
		JPanel pnl1=new JPanel();
		titlefile1=BorderFactory.createTitledBorder("First File");
		titlefile2=BorderFactory.createTitledBorder("Second File");
	
		pnl1.add(txtfile1);
		pnl1.add(btnfile1);
		pnl1.setBorder(titlefile1);
		JPanel pnl2=new JPanel();
		//pnl.add(file2);
		pnl2.add(txtfile2);
		pnl2.add(btnfile2);
		pnl2.setBorder(titlefile2);
		Container con=getContentPane();
		JPanel pnl=new JPanel();
		pnl.add(pnl1);
		pnl.add(pnl2);
		pnl.add(btncompare);
		con.add(pnl);
		//con.add(btncompare);
		btnfile1.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent event) {
					chooseFile1(event);
					
				}
				});
		btnfile2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				chooseFile2(event);
				
			}
			});
		btncompare.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				Compare cmp=new Compare(f1,f2);
				cmp.setVisible(true);
				cmp.setSize(700, 500);
				cmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				cmp.CompareFiles();
				
			}
			});
		
	}
	public void chooseFile1(ActionEvent event)
	{
		int returnVal = filechsr1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
           f1 = filechsr1.getSelectedFile();
           txtfile1.setText(f1.getName());
        }
            
	}
	public void chooseFile2(ActionEvent event)
	{
		int returnVal = filechsr2.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
           f2 = filechsr2.getSelectedFile();
           txtfile2.setText(f2.getName());
        }
            
	}
	

}
