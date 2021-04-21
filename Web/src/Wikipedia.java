import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Wikipedia 
{

	String search;
	public Wikipedia() 
	{
		// main frame and panels
		JFrame frame = new JFrame();
		JPanel main = new JPanel();
		JPanel panel = new JPanel();
		JPanel buttons = new JPanel();
		frame.add(main);
		main.add(buttons);
		main.add(panel);
		
		// layout
		BoxLayout boxlayout = new BoxLayout(main, BoxLayout.Y_AXIS);
		main.setLayout(boxlayout);
		
		// set up the display area
		JTextPane displayarea = new JTextPane();
		displayarea.setEditable(false);
		displayarea.setPreferredSize(new Dimension(600, 400));
		panel.add(displayarea);

		// explanation button
		JButton explanations = new JButton("Explanations");
		buttons.add(explanations);
		explanations.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						JOptionPane message = new JOptionPane();
						message.showMessageDialog(frame, "Hello and welcome to the Wikipedia!\n" +
						                                  "One important thing is that in case you request a search " +
						                                  "that might have several options, we choose the first one.\n" +
						                                  "We also select all of the important text information.","Wikipedia", JOptionPane.INFORMATION_MESSAGE);
						
					}
				});
		
		// search button that runs the search
		JButton wikipedia = new JButton("Search");
		buttons.add(wikipedia);
		wikipedia.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						// input panel for user to input the search 
						search = JOptionPane.showInputDialog("Search Wikipedia: ");
						
						try 
			    		{
			    			// connects to the given url
							Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + search).get();
							
							// select all text paragraphs
							org.jsoup.select.Elements paragraphs = doc.select("p");
							
							// variable for all collected text to display
							String info = "";
							
							// if it's an edge case where wikipedia takes you to the list of things that have the same name
							boolean edge = false;
							
							// for all paragraph elements check if it's the edge case
							for (Element paragraph : paragraphs)
							{
								if (paragraph.text().contains("refer to:")|| paragraph.text().contains("may refer to:"))
								{
									edge = true;
									break;
								}
							}
							
							// if it's not an edge case, then select all text elements
							if (edge == false)
							{
								for (Element paragraph : paragraphs)
								{
									info = info + paragraph.text() + "\n";
								}
							}
							
							// if it's an edge case
							if (edge == true)
							{
								// Wikipedia always puts edge cases as list elements
								// select the first element in the list 
								Element list = doc.selectFirst("li");
								
								// select a link of this element and get it's absolute link
								Element link = list.selectFirst("a");
								String abshref = link.attr("abs:href");
								Document page = Jsoup.connect(abshref).get();
								
								// select new paragraph from new page
								paragraphs = page.select("p");
								info = "";
								
								for (Element paragraph : paragraphs)
								{
									info = info + "\n" + paragraph.text();
								}
							}
				
							displayarea.setText(info);
			    		
			    		} 
			    		catch (IOException e1) 
			    		{
							displayarea.setText("Couldn't connect");
			    		}
						catch (NullPointerException e1)
						{
							displayarea.setText("Page you requested probably does not exist. ");
						}
					}
				});
		
		//scrollbar for output text
		JScrollPane scroll = new JScrollPane (displayarea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(600, 400));
		panel.add(scroll);
		
		// frame setup
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		panel.setFocusable(true); 
	}
	
	public static void main(String[] args) 
    {
    	new Wikipedia();
    }
}
