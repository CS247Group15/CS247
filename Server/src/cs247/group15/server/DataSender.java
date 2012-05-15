package cs247.group15.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import cs247.group15.data.Constants;
import cs247.group15.data.DataRequest;
import cs247.group15.data.ImportantInformation;

public class DataSender extends Thread {

	public DataSender(Socket clientSocket) {
		super();
		System.out.println("Request has been received & is being handled.");
		sendData(clientSocket);
	}

	private void sendData(Socket clientSocket) {
		
		Date date = null;
		try {
			Document doc = Constants.readMessage(clientSocket.getInputStream());
			Element root = doc.getRootElement();
			DataRequest request = new DataRequest(root);
			date = new Date(request.getFromWhen());
			System.out.println("Date received is " + date.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
		
		/*if(date!=null) {
			
			ListOfImportantInformations list = new ListOfImportantInformations(getImportantInformations(date));
			Document important = new Document(list.toXml());
			XMLOutputter outputter = new XMLOutputter();
			
			try
			{
				OutputStream out = clientSocket.getOutputStream();
				outputter.output(important, out);
				out.flush();
				clientSocket.close();
			}
			catch(Exception e)
			{ 
				e.printStackTrace();
			}
			
		}*/
	}

	private List<ImportantInformation> getImportantInformations(Date date) {
		/*
		 * TODO: write this method - should get all data from the database 
		 * newer than the date given & put into ImportantInformation classes
		 * for transmission to the app 
		 * */
		return new ArrayList<ImportantInformation>();
	}
}
