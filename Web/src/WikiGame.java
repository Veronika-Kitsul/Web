import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import graphs.EdgeGraph.Edge;
//import graphs.EdgeGraph.Vertex;

public class WikiGame 
{
	String startTopic = "";
	String finishTopic = "";

	public WikiGame() 
	{
		int size = 600;
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		JPanel top = new JPanel();
		JPanel bottom = new JPanel();
		top.setPreferredSize(new Dimension(size, size - size/4));
		bottom.setPreferredSize(new Dimension(size, size/4));
		frame.add(panel);
		panel.add(top);
		panel.add(bottom);
		
		JTextPane displayarea = new JTextPane();
		displayarea.setEditable(false);
		displayarea.setPreferredSize(new Dimension(size, size - size/4));
		displayarea.setBorder(BorderFactory.createLineBorder(new Color(252, 186, 3), 10));
		top.add(displayarea);
		
		
		JButton start = new JButton("Enter the starting topic");
		bottom.add(start);
		start.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						startTopic = JOptionPane.showInputDialog("Please enter the topic you want to start with: ");
						if (startTopic == null || startTopic.equals(""))
						{
							displayarea.setText("Looks like your starting topic is ~nothing~ which we obviously cannot work with!");
						}
					}
				});
		
		JButton finish = new JButton("Enter the terminal topic");
		bottom.add(finish);
		finish.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						finishTopic = JOptionPane.showInputDialog("Please enter the topic you want to end with: ");
						if (finishTopic == null || finishTopic.equals(""))
						{
							displayarea.setText("Looks like your end topic is ~nothing~ which we obviously cannot work with!");
						}
						
						// handle edge case of finish == start
						if (finishTopic.equals(startTopic))
						{
							displayarea.setText("Looks like you are already on the page you've requested! Congrats!");
						}
						
						try 
						{
							if (!startTopic.equals("") && !finishTopic.equals(""))
							{
								Document first;
								
								// add BFS
								ArrayList<String> toVisit = new ArrayList<String>();
								String start = "https://en.wikipedia.org/wiki/" + startTopic;
								toVisit.add(start);
								
								ArrayList<String> visited = new ArrayList<String>();
								visited.add(start);
								
								HashMap<String, String> leadsTo = new HashMap<String, String>();
								
								while (!toVisit.isEmpty())
								{
									// we don't need to visit current location again
									String curr = toVisit.remove(0);
									
									System.out.println(curr);
									System.out.println("----------------------------------------------------------------");
									
									// connect to the current web-page
									first = Jsoup.connect(curr).get();
									Element body = first.getElementById("bodyContent");
									Elements links = body.select("a");
									
									// for all of the links on the current page
									for (Element newLink: links)
									{	
										// get their absolute links
										String abshref = newLink.attr("abs:href").toLowerCase();
										if (abshref.contains("wikipedia.org/wiki/") && abshref.contains("wikipedia.org/wiki/file") == false)
										{
											// if you have already visited then just continue
											if (visited.contains(abshref)) continue;
											
											// put the path to the map
											leadsTo.put(abshref, curr);
											
											// if we found what we are looking for then call backTrace to trace all the path back to the start
											if (abshref.contains(finishTopic.toLowerCase()))
											{
												System.out.println(leadsTo);
												displayarea.setText(backTrace(abshref, leadsTo).toString());
												return;
											}
											else
											{
												toVisit.add(abshref);
												visited.add(abshref);
											}
										}
									}
								}
								displayarea.setText("Sorry, we were not able to process your request.");
							}
						}
						catch (HttpStatusException e2)
						{
							
						} 
						catch (IOException e1) {
							displayarea.setText("We've got some unforseen mistake. Please try to repeat your request!");
						}
					} 
				});
		
		
		frame.setSize(size, size);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setFocusable(true);
	}
	

	private ArrayList<String> backTrace(String finishTopic, HashMap<String, String> leadsTo)
	{
		// link of the current topic we finished on
		String curr = finishTopic;
		ArrayList<String> path = new ArrayList<String>();
	
		while (curr != null) 
		{
			path.add(0, curr);
			curr = leadsTo.get(curr);
			
		}
		System.out.println(path);
		return path;
	}
	
	public static void main(String[] args)
	{
		new WikiGame();
	}
}
