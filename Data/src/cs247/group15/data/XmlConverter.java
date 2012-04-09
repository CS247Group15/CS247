package cs247.group15.data;

import org.jdom.Element;

public interface XmlConverter {

	public Element toXml();
	public void fromXml(Element element);
}
