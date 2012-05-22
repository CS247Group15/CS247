import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Analysis {
	public static void main(String[] args)
	{
		int forever=1;
		//#################################################################
		//
		//
		//					REMOVE CODE BEFORE SENDING OFF, THIS IS FOR TEST PURPOSES ONLY
		//
		//
		//##################################################################
		try
		{
			Connection con = getDatabaseConnection();
			Statement dropNounTrack;
			dropNounTrack = con.createStatement();
			dropNounTrack.executeUpdate("DELETE FROM Nounstrack");
			dropNounTrack.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("No new stories in rawdata");
		}
		
		//#################################################################
		//
		//
		//					END OF MUST BE REMOVED CODE.
		//
		//
		//##################################################################
			
			Statement insert;
			Statement search;
			Statement searchRaw;
			Statement searchFinal;
			ResultSet rawRes=null;
			ResultSet finalRes;
			Statement valueSearch;
			ResultSet valueRes;
			ResultSet searchRes;
			
		
			ArrayList<String> importantWords= new ArrayList<String>();
			String[] words=null;
			String story=null;
			String word=null;
			
			String title=null;
			String description=null;
			String source=null;
			String date=null;
			String tmp = "";
			String tmp2 = "";
			
		while(forever==1)
		{
			Connection con = getDatabaseConnection();
			
			int tempValue=0;
			int totalValue=0;
			int loop=1;
			int cont=1;
			int nounValue=0;
			int importanceValue=0;
			int firstTime=1;
			
			try
			{
				searchRaw = con.createStatement();
				if (firstTime==1)
				{
					rawRes = searchRaw.executeQuery("SELECT * FROM rawdata;");
					firstTime=0;
				}
				do
				{
					loop=0;
					if (rawRes.next())
					{
						title = rawRes.getString(1);
						tmp = escapeChars(title);
						//System.out.println("Entry found in table: " + tmp);
						searchFinal = con.createStatement();
						finalRes = searchFinal.executeQuery("SELECT * FROM finalData WHERE Title = \"" + tmp + "\";");
						if (finalRes.next())
						{
							//System.out.println("The entry already exists in finaldata: " + tmp);
							loop=1;
						}
						else
						{
							//System.out.println("The story: " + tmp + " doesn't exist in finaldata");
						}
						searchFinal.close();
						
						if (loop==0)
						{
							description = rawRes.getString(2);
							source = rawRes.getString(3);
							date = rawRes.getString(4);
						}
					}
					else
					{
						forever=0;
					}
				} while (loop==1);
			}
			catch (SQLException ex)
			{
				//System.out.println("No new stories in rawdata");
			}
				//Create and populate entry in final data table for the RSS story
			
			try
			{
				searchFinal = con.createStatement();
				finalRes = searchFinal.executeQuery("SELECT * FROM finalData WHERE Title = \"" + title + "\";");
				if (finalRes.next())
				{
					cont=0;
				}
				searchFinal.close();
			}
			catch (SQLException ex)
			{
				System.out.println("No new stories in rawdata");
			}

			
			if (cont==1)	//If the story doesn't exist in finaldata yet, continue.
			{
				String tmpDes = escapeChars(description);
				String tmpSource = escapeChars(source);
				insertStoryToFinalData(tmp,tmpDes,tmpSource,date);

				words = breakWords(description);
				
				try	//Sentiment analysis against words table
				{
					totalValue=0;
					for (int x=0; x<words.length;x++)
					{
						tempValue=0;
						search = con.createStatement();
						//System.out.println(words[x]);
						tmp2 = escapeChars(words[x]);
						searchRes = search.executeQuery("SELECT * FROM words WHERE Word = \"" + tmp2 + "\";");
						if (searchRes.next())
						{
							word = searchRes.getString(1);
							tempValue = searchRes.getInt(2);
							totalValue = tempValue + totalValue;
							//System.out.println("Found word: " + words[x] + " in the document. Has value of: " + tempValue);
						}
						search.close();
					}
					insert = con.createStatement();
					if (totalValue>50)		//If the semantic value crosses the max or min bounds, then set move the score back into the bounds.
					{
						totalValue=50;
					}
					if (totalValue<-50)
					{
						totalValue=-50;
					}
					insert.executeUpdate("UPDATE finalData SET Sentiment=\'" + totalValue + "\' WHERE Title=\"" + title + "\";");
					insert.close();
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					System.out.println("Could not find story with title " + title);
				}
			
				//Send through Nountrack to pull out nouns. Update the noun total.
				
				String[] wordList = breakWords(description);
				for (int x=0; x<wordList.length;x++)
				{
					if (determineProperNoun(wordList[x])==1)
					{
						insertNoun(wordList[x]);
					}
				}
				updateNounsTotal(); 	//Once all new nouns have been added, increment the total.
				
				//Determine the noun importance level
				tempValue = 0;
				nounValue=0;
				
				try	//Try calculating importance of the nouns
				{
					for (int x=0; x<wordList.length; x++)
					{
						search = con.createStatement();
						searchRes = search.executeQuery("SELECT Total FROM nounstotal WHERE Word = \"" + wordList[x] + "\";");
						if (searchRes.next())
						{
							//System.out.println("Found the word in the noun total list: " + wordList[x]);
							tempValue = searchRes.getInt(1);
							nounValue=nounValue+tempValue;
							//System.out.println("NounValue increased by:" + tempValue + " to: " + nounValue + " from " + (nounValue-tempValue));
						}
						search.close();
					}
				}
				catch (SQLException ex)
				{
					ex.printStackTrace();
					System.out.print("Word error: " + description);
					System.out.println("Nouns failed, somewhere");
				}
				
				tempValue=0;
				int twitterValue=0;
				
				for (int x=0; x<wordList.length; x++)	//Try calculating the importance of the twitter words
				{
					tempValue = trendValue(wordList[x]);
					twitterValue=tempValue+twitterValue;
				}
				
				tempValue=0;
				importanceValue=0;
				wordList = breakWords(title+ " " + description);		//Change wordList to be both the title
				String importantList=null;		//This is the list of important words in the story, which is output so that the app can display how the importance number was influenced
				
				for (int x=0; x<wordList.length; x++)	//Try calculating the important words amount
				{
					tempValue=0;
					tempValue = importantWordValue(wordList[x].toLowerCase());
					//System.out.println("Checked the importance of: " + wordList[x].toLowerCase() + " returned a value of: " + tempValue);
					importanceValue=tempValue+importanceValue;
					if (tempValue!=0)
					{
						if (importantList!=null)
						{
							importantList = importantList + ", " + wordList[x];
						}
						else
						{
							importantList = wordList[x];
						}
					}
				}
				
				try
				{
					insert = con.createStatement();
					insert.executeUpdate("UPDATE finalData SET Importance = \'" + importanceCalc((float)nounValue,(float)twitterValue, (float)importanceValue) + "\' WHERE Title = \"" + title + "\";");
					insert.executeUpdate("UPDATE finalData SET ImportantNouns = \"" + importantList + "\" WHERE Title = \"" + title + "\";");
					insert.close();
				}
				catch (SQLException ex)
				{
					System.out.println("Problem updating finaldata");
				}
			}	//End of if statement that checks if the story has been processed already
			
			
			try
			{
				con.close();
			}
			catch (SQLException ex)
			{
				System.out.println("Problem closing final connection");
			}
		}	//End of continous while loop
	}
	
	private static String escapeChars(String str) {
		str = str.replaceAll("'", "\\\\'");
		str = str.replaceAll("\"", "\\\\\"");
		str = str.replaceAll(",", "\\\\,");
		str = str.replaceAll("\\<.*?\\>", "");
		return str;
	}
	
	public static String punctuationRemover(String word)
	{
		String returnValue=word;
		if (word.endsWith(".") || word.endsWith(",") || word.endsWith(";") || word.endsWith("\"") || word.endsWith("\'"))
		{
			returnValue = word.substring(0,(word.length()-1));
		}
		if (word.startsWith("\"") || word.startsWith("\'"))
		{
			returnValue = returnValue.substring(1,returnValue.length());
		}
		return returnValue;
	}
	
	public static int importantWordValue(String word)
	{
		//System.out.println("Checking for " + word + " in important word list");
		int returnValue=0;
		Connection con = getDatabaseConnection();
		
		try
		{
			Statement search;
			ResultSet searchRes;
			
			search = con.createStatement();
			searchRes = search.executeQuery("SELECT * FROM importantWords WHERE word = \"" + word + "\";");
			if (searchRes.next())
			{
				System.out.println("The term: " + word + " was found in importantWords");
				returnValue = searchRes.getInt(2);
			}
			search.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error upadting noun into NounsTotal");
		}
		return returnValue;
	}
	
	
	public static int trendValue(String word)
	{
		int returnValue=0;
		Connection con = getDatabaseConnection();
		
		try
		{
			Statement search;
			ResultSet searchRes;
			
			search = con.createStatement();
			searchRes = search.executeQuery("SELECT * FROM trends WHERE Trend = \"" + word + "\";");
			if (searchRes.next())
			{
				//System.out.println("The term: " + word + " was found in trends");
				returnValue = 10;
			}
			search.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error upadting noun into NounsTotal");
		}
		return returnValue;
	}
	
	public static void updateNounsTotal()
	{
		//System.out.println("Updating nouns total");
		Connection con = getDatabaseConnection();
		Statement searchTrack;
		Statement searchTotal;
		Statement updateTotal;
		Statement clearTotal;
		
		ResultSet trackResult;
		ResultSet totalResult;
		
		String word;
		
		int tempTotal=0;
		
		try
		{
			searchTrack = con.createStatement();
			searchTotal = con.createStatement();
			updateTotal = con.createStatement();
			clearTotal = con.createStatement();
			
			clearTotal.executeUpdate("DELETE FROM NounsTotal");
			
			trackResult = searchTrack.executeQuery("SELECT Word FROM NounsTrack");	//Selects the Words from Nouns Track
			while (trackResult.next()) 
			{
				word = (trackResult.getString(1));
				totalResult = searchTotal.executeQuery("SELECT Word FROM NounsTotal WHERE Word = \"" + word + "\";");	//See if the current noun is already listed in the totals table
				if (!totalResult.next())
				{
					insertNounTotal(word);	//If noun does not exist in NounsTotal, add it to the table
				}

				totalResult = searchTotal.executeQuery("SELECT Total FROM NounsTotal WHERE Word = \"" + word + "\";");	//Get the current value of the noun
				if (totalResult.next())
				{
					tempTotal = totalResult.getInt(1);
				}
				updateTotal.executeUpdate("UPDATE NounsTotal SET Total = " + (tempTotal+1) + " WHERE Word = \"" + word + "\";");	//Update the entry in the table
			}
			searchTrack.close();
			searchTotal.close();
			updateTotal.close();
			con.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			System.out.println("Error upadting noun into NounsTotal");
		}
	}
	
	public static void insertNounTotal(String word)
	{
		Connection con = getDatabaseConnection();
		Statement insert;
		
		try
		{
			insert = con.createStatement();
			insert.executeUpdate("INSERT INTO NounsTotal VALUES(\"" + word + "\", \'0\');");
			insert.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error inserting noun into NounsTotal");
		}
	}
	
	public static void insertStoryToFinalData(String title, String description, String source, String date)	//Gets the title of a news story in rawdata, places this information into finaldata
	{
		int sentiment = 0;
		int importance = 0;
		String importantNouns=" ";
		
		Connection con = getDatabaseConnection();
		Statement insert;
		
		try
		{
			insert = con.createStatement();
			insert.executeUpdate("INSERT INTO finalData VALUES(\"" + title + "\", \"" + description + "\", \"" + source + "\", \"" + date + "\", \'" + sentiment + "\', \'" + importance + "\', \"" + importantNouns + "\");");
			insert.close();
			con.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			System.out.println("Error inserting into finalData");
		}
	}
	
	public static String[] breakWords(String word)
	{
		String[] wordList;
		String delimiter = " ";
		wordList = word.split(delimiter);
		for (int x=0; x<wordList.length; x++)
		{
			wordList[x]=punctuationRemover(wordList[x]);
		}
		return wordList;
	}
	
	public static int determineProperNoun(String word)
	{
		int returnValue =0;	//If the word passed in appears to be a proper noun then this will be changed to a 1
		char start = word.charAt(0);
		if (start==('A')|start==('B')|start==('B')|start==('C')|start==('D')|start==('E')|start==('F')|start==('G')|start==('H')|start==('I')|start==('J')|start==('K')|start==('L')|start==('M')|start==('N')|start==('O')|start==('P')|start==('Q')|start==('R')|start==('S')|start==('T')|start==('U')|start==('V')|start==('W')|start==('X')|start==('Y')|start==('Z'))
		{
			returnValue=1;
			//System.out.println("NOUN FOUND - " + word);
		}
		
		return returnValue;
	}
	
	public static void insertNoun(String word)
	{
		Connection con = getDatabaseConnection();
		Statement insert;
		try
		{
			insert = con.createStatement();
			insert.executeUpdate("INSERT INTO NounsTrack VALUES(\"" + word + "\", \'" + getDateTime() + "\');");
			insert.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error inserting noun into NounsTrack");
		}
	}
	
	private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0, 0, 0);
        return dateFormat.format(date);
    }
	
	public static int importanceCalc(float noun, float twitter, float importance)
	{
		Connection con = getDatabaseConnection();
		
		int returnValue=0;
		float calcRes=0;
		int tempMaxNouns=0;
		int tempImportance=0;
		int maxNouns=0;
		int maxTweets=10;
		int maxImportance=0;
		
		float nouns=0;
		float twits=0;
		float words=0;
		
		Statement search;
		ResultSet searchRes;
		
		try
		{
			search = con.createStatement();
			searchRes = search.executeQuery("SELECT total FROM NounsTotal");
			while (searchRes.next())
			{
				tempMaxNouns = searchRes.getInt(1);
				if (maxNouns<tempMaxNouns)
				{
					maxNouns = tempMaxNouns;	//Add up the total max score that could be had from the nouns list.
				}
			}
			if (maxNouns>20)
			{
				maxNouns=20;
			}
			
			if (importance>60)
			{
				importance=60;
			}
			search.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error inserting noun into NounsTrack");
		}
		
		System.out.println("");
		System.out.println("");
		System.out.println("Calculating the importance with:");
		System.out.println("Noun:" + noun);
		System.out.println("Twitter: " + twitter + "            maxTweets: " + maxTweets);
		System.out.println("Importance: " + importance);
		twits=twitter/maxTweets;
		
		System.out.println("Nouns:" + noun + " twits: " + twits + " words: " + words);
		
		
		calcRes = (noun)+(20*twits)+(importance);
		System.out.println("Nouns part:" + (noun) + " twits part: " + (30*twits) + " words parts: " + (importance));
		returnValue = (int)calcRes;
		System.out.println("Retruning:" + returnValue + " from calcRes: " + calcRes);
		System.out.println("");
		System.out.println("");
		
		
		return returnValue;
	}
	
	private static Connection getDatabaseConnection(){
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","password");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return con;
	}


}
