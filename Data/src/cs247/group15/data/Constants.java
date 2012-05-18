package cs247.group15.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/*
 * Contains any important constant data that may be used by the app or the server
 * Author: KB
 */
public class Constants {
	
	//Constants for use when passing information around the app in an Intent
	public static final String ImportantInformationTag = "important_information";
	
	//Constants relating to passing information to and from the server
	public static final String endString = "</EndOfMessage>";
	public static Document readMessage(InputStream inStream) throws IOException, JDOMException
	{
		SAXBuilder docBuilder = new SAXBuilder();
		int nextByte = inStream.read();
		StringBuilder sBuilder = new StringBuilder();
		while(nextByte != -1)
		{
			sBuilder.append((char)nextByte);
			if(sBuilder.length() >= Constants.endString.length() && sBuilder.toString().endsWith(Constants.endString))
			{
				break;
			}
			else
			{
				nextByte = inStream.read();
			}
		}
		System.out.println(sBuilder.toString());
		StringReader reader = new StringReader(sBuilder.toString());
		return docBuilder.build(reader);
	}
	
	//Constants relating to connecting to the server
	public static final String serverURL = "http://81.109.100.98:8085/JavaServlet/Servlet";
	
	//Constants for debugging
	public static final String error = "ERROR";
	public static final String information = "INFO";

}
