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

public class ImportantInformation implements Serializable, XmlConverter, ListClass, Comparable<ImportantInformation> {

	private String heading = "";
	private final static String headingtag = "Heading";
	private PrintableDate date;
	private final static String datetag = "Date";
	private int importanceLevel = 0;
	private final static String importanceLevelTag = "Importance";
	private int sentimentLevel = 0;
	private final static String sentimentTag = "Sentiment";
	private String description= "";
	private final static String inferenceTag = "Inference";
	private String sources = "";
	private final static String sourcesTag = "Sources";
	private final static String sourceTag = "Source";
	private String nouns = "";

	public ImportantInformation(String heading, int importance, int sentiment, Date date, String description, String source, String nouns)
	{
		this.heading = heading;
		this.importanceLevel = importance;
		this.sentimentLevel = sentiment;
		if(date instanceof PrintableDate) {this.date = (PrintableDate)date;}
		else {this.date = new PrintableDate(date);}
		this.description = description;
		this.sources = source;
		this.nouns = nouns;
	}
	
	public String toString(){return heading;}
	public String getSource() {return sources;}
	public PrintableDate getDate(){return date;}
	public String getDescription(){return description;}
	public int getImportance() {return importanceLevel;}
	public int getSentimence() {return sentimentLevel;}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof ImportantInformation)
		{
			ImportantInformation i = (ImportantInformation)o;
			return (i.toString().equals(toString()));
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	public Element toXml() {
		
		Element rootElement = new Element(this.getClass().getSimpleName());
		rootElement.addContent(new Element(headingtag).setText(heading));
		if(date!=null)
		{
			rootElement.addContent(new Element(datetag).setText(""+date.getTime()));
		}
		rootElement.addContent(new Element(inferenceTag).setText(description));
		
		rootElement.addContent(new Element(sentimentTag).setText(""+sentimentLevel));
		
		rootElement.addContent(new Element(sourcesTag).setText(sources));
		
		return rootElement;
	}

	public void fromXml(Element element) {
		
		heading = element.getChildText(headingtag);
		//TODO: write rest of this method
	}

	public int compareTo(ImportantInformation another) {
		return (-(this.getDate().compareTo(another.getDate())));
	}

	public String getNouns() {
		return nouns;
	}
}
