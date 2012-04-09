package cs247.group15.data;

import java.util.Calendar;

import org.jdom.Element;

public class DataRequest implements XmlConverter {

	long dateFromWhen;
	static final String dateFromWhenTag = "DateFromWhen";
	
	DataRequest(Calendar fromWhen)
	{
		dateFromWhen = fromWhen.getTimeInMillis();
	}
	
	@Override
	public Element toXml() {
		
		Element rootElement = new Element(this.getClass().getSimpleName());
		rootElement.addContent(new Element(dateFromWhenTag).setText(Long.toString(dateFromWhen)));
		return rootElement;
		
	}

	@Override
	public void fromXml(Element element) {
		
		dateFromWhen = Long.parseLong(element.getChildText(dateFromWhenTag));
		
	}

}
