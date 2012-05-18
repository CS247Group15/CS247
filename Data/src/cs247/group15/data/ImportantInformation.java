package cs247.group15.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Element;


/*
 * Contains the information that will be sent from the server to the app, when
 * important information is discovered by the data mining.
 * Author: KB
 */

public class ImportantInformation implements Serializable, XmlConverter, ListClass {

	private String heading = "";
	private final static String headingtag = "Heading";
	private PrintableDate date;
	private final static String datetag = "Date";
	private String inference= "";
	private final static String inferenceTag = "Inference";
	private List<String> sources = new ArrayList<String>();
	private final static String sourcesTag = "Sources";
	private final static String sourceTag = "Source";
	private String other = "";
	private final static String otherTag = "Other";
	
	public ImportantInformation(String heading, Date date)
	{
		this.heading = heading;
		if(date instanceof PrintableDate) {this.date = (PrintableDate)date;}
		else {this.date = new PrintableDate(date);}
	}
	public ImportantInformation(String heading, Date date, String inference, List<String> sources, String otherInfo)
	{
		this.heading = heading;
		if(date instanceof PrintableDate) {this.date = (PrintableDate)date;}
		else {this.date = new PrintableDate(date);}
		this.inference = inference;
		this.sources = sources;
		this.other = otherInfo;
	}
	
	public String toString(){return heading;}
	public List<String> getSources() {return sources;}
	public PrintableDate getDate(){return date;}
	public String getInference(){return inference;}
	public String getOther() {return other;}
	
	@Override
	public boolean equals(Object o)
	{
		//FIXME: may want to tweak this depending on its use
		if(o instanceof ImportantInformation)
		{
			ImportantInformation i = (ImportantInformation)o;
			return (i.toString().equals(toString())&&i.getInference().equals(getInference()));
		}
		return false;
	}
	
	public Element toXml() {
		
		Element rootElement = new Element(this.getClass().getSimpleName());
		rootElement.addContent(new Element(headingtag).setText(heading));
		if(date!=null)
		{
			rootElement.addContent(new Element(datetag).setText(""+date.getTime()));
		}
		rootElement.addContent(new Element(inferenceTag).setText(inference));
		
		Element sourcesElement = new Element(sourcesTag);
		for(String source : sources)
		{
			sourcesElement.addContent(new Element(sourceTag).setText(source));
		}
		rootElement.addContent(sourcesElement);
		
		rootElement.addContent(new Element(otherTag).setText(other));
		return rootElement;
	}

	public void fromXml(Element element) {
		
		heading = element.getChildText(headingtag);
		//TODO: write rest of this method
	}
}
