package cs247.group15.server;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class googleTrendsReader extends Thread {

	public void run(){
		String str;
		String html = "";
		Connection con = getDatabaseConnection();
		Statement stmt = null;
		ResultSet rs = null;
		Statement insert = null;
		
		int i = 0;
		while(true){				
				try {
					URL url = new URL("http://www.google.com/trends/");
					Document doc = Jsoup.parse(url, 3000);
					Elements tables = doc.select("table[class=hotTerm]");
					if (tables.size() == 0)
					{
						System.out.println("Unable to read google trends");
					}
					while (i < 10){
						Element table = tables.get(i);
						Iterator<Element> ite = table.select("td").iterator();
						ite.next();
						str = ite.next().text();
						
						stmt = con.createStatement();
						insert = con.createStatement();
						rs = stmt.executeQuery("SELECT Trend FROM Trends WHERE Trend = \"" + str + "\";");
						if (!rs.next())
						{	
							System.out.println("INSERT INTO Trends VALUES( \"" + str + "\"" + ", '"+ getDateTime() +"');");
							insert.executeUpdate("INSERT INTO Trends VALUES( \"" + str + "\"" + ", '"+ getDateTime() +"');");
							//System.out.println("Added Trend to database: " + str);
						}
						i++;
					}
					Thread.sleep(60000);
					
				} catch (IOException | SQLException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} 
	}
	
	private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
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
		
		
}
