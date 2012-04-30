package cs247.group15.server;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import java.io.*;
import javax.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class rssReader extends Thread{

	String URL = "";
	String Source = "";
	
	public rssReader(String tmpURL, String tmpSource) {
		URL = tmpURL;
		Source = tmpSource;
	}
	
	public void run() {
		URLConnection feedUrl = null;
		Connection con = getDatabaseConnection();
		Statement stmt = null;
		ResultSet rs = null;
		Statement insert = null;
		String Title;
		//SyndContent DescriptionContent = new SyndEntryImpl();
		String Description;
		int counter = 0;
		while (true) {
			try 
			{
				feedUrl = new URL(URL).openConnection();
			} catch (MalformedURLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			XmlReader reader = null;
			
			try {
					  reader = new XmlReader(feedUrl);
				      SyndFeed feed = new SyndFeedInput().build(reader);
				 
					     for (Iterator i = feed.getEntries().iterator(); i.hasNext();) 
					     {
					       SyndEntry entry = (SyndEntry) i.next();
					       Title = entry.getTitle();
					       SyndContent DescriptionContent = entry.getDescription();
					       DescriptionContent.setType("text/plain");
					       Description = DescriptionContent.getValue();
					       Title = escapeChars(Title);
					       Description = escapeChars(Description);
					       
					       
					     	try 
					     	{
									stmt = con.createStatement();
									insert = con.createStatement();
									rs = stmt.executeQuery("SELECT Headline FROM rawdata WHERE Headline = \"" + Title + "\";");
									
									if (!rs.next())
									{	
										System.out.println("INSERT INTO rawdata " + "VALUES( " + "\"" + Title +"\", \"" + Description +"\", \"" + Source + "\", \'" + getDateTime() + "\'" + ");");
										insert.executeUpdate("INSERT INTO rawdata " + "VALUES( " + "\"" + Title +"\", \"" + Description +"\", \"" + Source + "\", \'" + getDateTime() + "\'" + ");");
										System.out.println("Added row to database: Title: " + Title + " " + counter + " Added");
									}
					     	} catch (SQLException e) {e.printStackTrace();}
							
					     }
					     
					     Thread.sleep(60000);
					     System.out.println("Sleeping 1 Min.");
			 }    
			
			 catch (IllegalArgumentException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FeedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally 
			{
		           if (reader != null)
					try 
		           {
						reader.close();
		           } catch (IOException e) 
		           {
						// TODO Auto-generated catch block
						e.printStackTrace();
		           }
		    }
		}	
		
	}
	
	
	private Connection getDatabaseConnection(){
		Connection con = null;  

		
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
			try {
				con =DriverManager.getConnection 
				("jdbc:mysql://localhost:3306/rawdata","user","root");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return con;
	}
	
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
	
	private String escapeChars(String str) {
		str = str.replaceAll("'", "\\\\'");
		str = str.replaceAll("\"", "\\\\\"");
		str = str.replaceAll(",", "\\\\,");
		str = str.replaceAll("\\<.*?\\>", "");
		return str;
	}
	
}
