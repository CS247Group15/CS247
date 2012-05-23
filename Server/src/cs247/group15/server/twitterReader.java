package cs247.group15.server;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Trends;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.*;

public class twitterReader extends Thread {

	public void run () {
		Connection con = getDatabaseConnection();
		Statement stmt = null;
		ResultSet rs = null;
		Statement insert = null;
		Statement delete = null;
		String tmpStr = "";
        try {
        	while (true)
        	{
	            Twitter twitter = new TwitterFactory().getInstance();
	            List<Trends> trendsList;
	           
	            trendsList = twitter.getDailyTrends();
	            
	            System.out.println("Showing daily trends");
	            for (Trends trends : trendsList) {
	                System.out.println("As of : " + trends.getAsOf());
	                for (Trend trend : trends.getTrends()) {
	                	stmt = con.createStatement();
						insert = con.createStatement();
						delete = con.createStatement();
						tmpStr = escapeChars(trend.getName());
						delete.execute("DELETE FROM trends where datediff(now(), trends.Date_Added) > 1");
						rs = stmt.executeQuery("SELECT Trend FROM Trends WHERE Trend = \"" + tmpStr + "\";");
						if (!rs.next())
						{	
							System.out.println("INSERT INTO Trends VALUES( \"" + tmpStr + "\"" + ", '"+ getDateTime() +"');" + "from twitter");
							
							insert.executeUpdate("INSERT INTO Trends VALUES( \"" + tmpStr + "\"" + ", '"+ getDateTime() +"');");
							//System.out.println("Added Trend to database: " + str);
						}
	                   // System.out.println(" " + trend.getName());
	                }
	            }

	            Thread.sleep(360500);
        	}
        } catch (TwitterException | InterruptedException | SQLException te) {
            te.printStackTrace();
            System.out.println("Failed to get trends: " + te.getMessage());
            System.exit(-1);

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
		private String escapeChars(String str) {
			str = str.replaceAll("'", "\\\\'");
			str = str.replaceAll("\"", "\\\\\"");
			str = str.replaceAll(",", "\\\\,");
			str = str.replaceAll("\\<.*?\\>", "");
			return str;
		}
	
}
