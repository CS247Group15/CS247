import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class tweetProcessor extends Thread {
	
	public void run () 
	{
		while (true)
		{
				
				try {
					Twitter twitter = new TwitterFactory().getInstance();
					Query query;
					Connection con = getDatabaseConnection();
					Statement stmt = con.createStatement();
					Statement getProcessed = con.createStatement();
					Statement getTrends = con.createStatement();
					Statement insert = con.createStatement();
					Statement populateTweets = con.createStatement();
					String tempString = "";
					Statement checkTweets = con.createStatement();
					//Statement findProcessed = con.createStatement();
					//Statement insert = con.createStatement();
					ResultSet rs = null;
					ResultSet processed = null;
					ResultSet allTrends = null;
					ResultSet tweetExists = null;
					int i = 0;

					
					processed = getProcessed.executeQuery("SELECT trend FROM processedTrends;");
					allTrends = getTrends.executeQuery("SELECT trend FROM trends;");
					
					while ( allTrends.next() )
					{

						System.out.println(i++);
						System.out.println("SELECT trend FROM processedTrends WHERE trend = \"" + allTrends.getString(1) + "\";");
						rs = stmt.executeQuery("SELECT * FROM processedTrends WHERE trend = \"" + allTrends.getString(1) + "\";");
						System.out.println("Got here.");
						
					if (!rs.next())
						{
							//populate tweet database
							query = new Query(allTrends.getString(1));
							QueryResult result = twitter.search(query);
							
							for (Tweet tweet : result.getTweets()) {
								tempString = tweet.getText();
								tempString = tempString.replace("\\", "");
								tempString = escapeChars(tempString);
								System.out.println("1");
								System.out.println("SELECT * FROM tweets WHERE tweet = \"" + tempString + "\" ;");
								tweetExists = checkTweets.executeQuery("SELECT * FROM tweets WHERE tweet = \"" + tempString + "\" ;");
								System.out.println("2");
								
								if (!tweetExists.next()){
								System.out.println("INSERT INTO tweets VALUES ( \"" + tempString + "\" , \"" + getDateTime() + "\");");
								populateTweets.execute("INSERT INTO tweets VALUES ( \"" + tempString + "\" , \"" + getDateTime() + "\");" );
								}
							}
						
							//end populate tweet database
							System.out.println("INSERT INTO processedTrends VALUES ( \"" + allTrends.getString(1) + "\" , " + getDateTime() + "\");");
							insert.execute("INSERT INTO processedTrends VALUES ( \"" + allTrends.getString(1) + "\" , \"" + getDateTime() + "\");");
						}

					}
					Thread.sleep(60000);
					rs.close();
					processed.close();
					allTrends.close();
					
				} catch (SQLException | TwitterException | InterruptedException e) {
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
	
	private Connection getDatabaseConnection()
	{
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
