package cs247.group15.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import android.util.Log;

import cs247.group15.data.Constants;
import cs247.group15.data.DataRequest;
import cs247.group15.data.ImportantInformation;

public class DataSender extends Thread {

	Document list;
	
	public DataSender(DataRequest requestInfo) throws UnknownHostException, IOException, JDOMException
	{
		Log.d(Constants.information, "A data sender has been initialised.");
		Socket socket = sendDocument(new Document(requestInfo.toXml()));
		list = Constants.readMessage(socket.getInputStream());
		Log.d(Constants.information, "Message has been received.");
	}
	
	private Socket sendDocument(Document docToSend) throws UnknownHostException, IOException
	{
		Socket sendSocket = new Socket(Constants.serverIp, Constants.serverPort);
		Log.d(Constants.information, "Created socket.");
		
		XMLOutputter outputter = new XMLOutputter();
		OutputStream out = sendSocket.getOutputStream();
		outputter.output(docToSend, out);
		out.flush();
		
		return sendSocket;
	}

	public List<ImportantInformation> getList() {
		//This is used by the service to get the list returned from the server back
		Element element = list.getRootElement();
		return new ArrayList<ImportantInformation>();
	}
	
}
