package cs247.group15.data;

import java.io.Serializable;
import java.util.Date;

import org.jdom.Element;


/*
 * Contains the information that will be sent from the server to the app, when
 * important information is discovered by the data mining.
 * Author: KB
 */

public class ImportantInformation implements Serializable, XmlConverter, ListClass {

	String heading;
	final static String headingtag = "heading";
	PrintableDate date;
	final static String datetag = "date";
	
	public ImportantInformation(String heading, Date date)
	{
		this.heading = heading;
		if(date instanceof PrintableDate) {this.date = (PrintableDate)date;}
		else {this.date = new PrintableDate(date);}
	}
	
	public String toString()
	{
		return heading;
	}
	
	public PrintableDate getDate()
	{
		return date;
	}
	
	public Element toXml() {
		
		Element rootElement = new Element(this.getClass().getSimpleName());
		rootElement.addContent(new Element(headingtag).setText(heading));
		rootElement.addContent(new Element(datetag).setText(""+date.getTime()));
		
		return rootElement;
	}

	public void fromXml(Element element) {
		
		heading = element.getChildText(headingtag);
		//TODO: get date
	}
}
