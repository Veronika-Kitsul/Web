import org.jsoup.Jsoup;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeatherFetcher
{
	String location;
	
    public WeatherFetcher()
    {
    		JFrame frame = new JFrame();
    		JPanel panel = new JPanel();
    		
    		JTextArea displayarea = new JTextArea();
    		displayarea.setEditable(false);
    		panel.add(displayarea);
    		displayarea.setPreferredSize(new Dimension(400, 300));
    		displayarea.setLineWrap(true);
    		displayarea.setWrapStyleWord(true);

    		JButton weather = new JButton("Check The Weather");
    		panel.add(weather);
    		weather.addActionListener(new ActionListener()
    				{
						public void actionPerformed(ActionEvent e) 
						{
							location = JOptionPane.showInputDialog("Please enter the location to check the weather: ");
							
							try 
				    		{
				    			// connects to the given url
								Document doc = Jsoup.connect("https://www.bing.com/search?q=" + location + "+weather").get();
								
								// gets the first section of the page labeled 'bodyContent'
								Element datetime = doc.select(".wtr_dayTime").first();
								Element quality = doc.select(".wtr_caption").first();
								Element temperature = doc.select(".wtr_currTemp.b_focusTextLarge").first();
								Element units = doc.select(".wtr_currUnit").first();
								Element precipitation = doc.select(".wtr_currPerci").first();
								Element humidity = doc.select(".wtr_currHumi").first();
								Element wind = doc.select(".wtr_currWind").first();
								
								
								
								String weather = " The weather in " + location + " as of " + datetime.text() + " is " + quality.text() + ".\n " +
													"Temperature: " + temperature.text() + units.text() + "\n " 
													 + precipitation.text() + "\n " 
													 + humidity.text() + "\n " 
													 + wind.text();
								displayarea.setText(weather);
				    		
				    		} 
				    		catch (IOException e1) 
				    		{
								displayarea.setText(" Couldn't connect");
				    		}
							catch (NullPointerException e1)
							{
								displayarea.setText(" Your location probably does not exist.");
							}
						}
    				});
    		

    		frame.setSize(400, 400);
    		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		frame.add(panel);
    		frame.setLocationRelativeTo(null);
    		frame.setResizable(false);
    		frame.setVisible(true);
    		panel.setFocusable(true);
    }
    
    public static void main(String[] args) 
    {
    	new WeatherFetcher();
    }
}