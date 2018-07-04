
import java.awt.BorderLayout;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

//import project.firstgui;

public class MainClass {

	static JFrame frame;
	private static JButton backButton;
	private static JPanel mainPanel, panelContainer;
	private static final String AGREE = "1";
	private static final String DISAGREE = "0";
	
	public MainClass()
	{
		backButton = new JButton("Back");
		backButton.setBounds(150, 20, 100, 30);
		mainPanel = new JPanel();
		panelContainer = new JPanel();
		frame = new JFrame("PDF ToolBox");
	}

	public static void callHome()
	{
		//Back button is added to frame on home page, we don't need it on license page 
		frame.getContentPane().add(mainPanel, BorderLayout.SOUTH);
		backButton.setVisible(false);
		Home home = new Home();
		panelContainer.add(home);
		displayNextPanel();
	}

	public static void callMerge()
	{
		backButton.setVisible(true);
		Merge merge = new Merge();
		panelContainer.add(merge);
		displayNextPanel();	
	}

	public static void callSplit()
	{
		backButton.setVisible(true);
		Split split = new Split();
		panelContainer.add(split);
		displayNextPanel();
	}

	public static void callConvert()
	{
		backButton.setVisible(true);
		WordToPdfConverter converter = new WordToPdfConverter();
		panelContainer.add(converter);
		displayNextPanel();
	}
	
	public static void callCompare()
	{
		backButton.setVisible(true);
		firstgui fg=new firstgui();
		fg.setVisible(true);
		fg.setSize(400, 200);
		fg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panelContainer.add(fg);
		displayNextPanel();	
	}

	public static String file_read() throws IOException
	{
		String line;
		File file = new File("config.txt");
		//Create file if it doen't exist and write default value = 0
		if(!file.exists())
		{
			file.createNewFile();
			file_write(DISAGREE);
		}
		BufferedReader br = new BufferedReader(new FileReader(file));
		try 
		{
			line = br.readLine();
		} 
		finally
		{
			br.close();
		}
		return line;
//		return Integer.parseInt(line);
	}


	public static void displayNextPanel()
	{
		//Displays the next panel i.e. the option that user selects
		CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
		cardLayout.next(panelContainer);
	}

	private Component getVisibleCard()
	{
		//Returns the current panel thats displayed
		for(Component c: panelContainer.getComponents())
		{
			if (c.isVisible())
				return c;
		}

		return null;
	}

	public void run() throws IOException
	{
		mainPanel.add(backButton);
		//Panel container contains the panels to switch between panels
		panelContainer.setLayout(new CardLayout());
		frame.getContentPane().add(panelContainer, BorderLayout.CENTER);
		

		//File "config.txt" contains 1 if license is accepted, otherwise has 0
		if(file_read().equals(AGREE))
		{
			//If license is accepted, display the home page directly
			MainClass.callHome();
		}
		else{
			//Otherwise user must first accept the license
			License license =new License();
			panelContainer.add(license);
		}

		frame.setSize(600, 700);
		frame.setVisible(true);

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*Get the visible card and remove it from the panel container
				 * Panel container must contain the home panel and the current panel only
				 */
				Component visible = getVisibleCard();
				panelContainer.remove(visible);
				CardLayout cardLayout = (CardLayout) panelContainer.getLayout();
				//Displays the previous panel in the container
				cardLayout.previous(panelContainer);
				//Hide the back button on home page
				backButton.setVisible(false);
			}
		});


		WindowListener listener = new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				System.exit(0);
			}
		};
		frame.addWindowListener(listener);
	}

	public static void main(String[] args) throws Exception
	{
		MainClass obj = new MainClass();
		obj.run();

	}
	
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

}
