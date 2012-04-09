package cs247.group15.data;

import java.io.Serializable;

import org.jdom.Element;

/*
 * Contains the information that will be sent from the server to the app, when
 * important information is discovered by the data mining.
 * Author: KB
 */

public class ImportantInformation implements Serializable, XmlConverter {

	String heading;
	final static String headingtag = "heading";
	
	public ImportantInformation(String heading)
	{
		this.heading = heading;
	}

	@Override
	public Element toXml() {
		
		Element rootElement = new Element(this.getClass().getSimpleName());
		rootElement.addContent(new Element(headingtag).setText(heading));
		
		return rootElement;
	}

	@Override
	public void fromXml(Element element) {
		
		heading = element.getChildText(headingtag);
		
	}
}
