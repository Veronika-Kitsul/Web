import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.lang.model.util.Elements;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Wikipedia 
{

	String search;
	public Wikipedia() 
	{
		
		JFrame frame = new JFrame();
		JPanel main = new JPanel();
		JPanel panel = new JPanel();
		frame.add(main);
		main.add(panel);
		
		BoxLayout boxlayout = new BoxLayout(main, BoxLayout.Y_AXIS);
		main.setLayout(boxlayout);
		
		JTextArea displayarea = new JTextArea();
		displayarea.setEditable(false);
		displayarea.setPreferredSize(new Dimension(600, 400));
		displayarea.setLineWrap(true);
		displayarea.setWrapStyleWord(true);
		panel.add(displayarea);
		
		//scrollbar for output text
				JScrollPane scroll = new JScrollPane (displayarea);
				scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scroll.setPreferredSize(new Dimension(600, 400));
				panel.add(scroll);

		JPanel buttons = new JPanel();
		main.add(buttons);
		buttons.setPreferredSize(new Dimension());
		
		JButton wikipedia = new JButton("Search");
		buttons.add(wikipedia);
		wikipedia.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						search = JOptionPane.showInputDialog("Search Wikipedia: ");
						
						try 
			    		{
			    			// connects to the given url
							Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + search).get();
							
							// gets the first section of the page labeled 'bodyContent'
							org.jsoup.select.Elements paragraphs = doc.select("p");
							String info = "";
							boolean edge = false;
							for (Element paragraph : paragraphs)
							{
								if (paragraphs.text().contains("may also refer to:"))
								{
									edge = true;
									break;
								}
							}
							
							if (edge == false)
							{
								for (Element paragraph : paragraphs)
								{
									info = info + "\n" + paragraph.text();
								}
							}
							
							if (edge == true)
							{
								Element list = doc.selectFirst("li");
								Element link = list.selectFirst("a");
								String abshref = link.attr("abs:href");
								Document page = Jsoup.connect(abshref).get();
								
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
							displayarea.setText(" Couldn't connect");
			    		}
						catch (NullPointerException e1)
						{
							displayarea.setText("Page you requested probably does not exist. ");
						}
					}
				});
		

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
