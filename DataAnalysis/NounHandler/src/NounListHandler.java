import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import java.io.*;
import javax.sql.*;

import java.sql.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class NounListHandler {
	
	public static void main(String[] args)
	{
		int cont = 1;
		while(cont==1)
		{
			System.out.println();
			System.out.println("Options");
			System.out.println("1 - Create Noun Totals table");
			System.out.println("2 - Create Noun's appearence table");
			System.out.println("3 - Read in stories for nouns");
			System.out.println("4 - Manually add nouns");
			System.out.println("5 - Update NounsTotal");
			System.out.println("6 - Exit");
			System.out.println();
			System.out.print("Enter option here: ");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input=null;
			
			try {
				input = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(input)
			{
				case "1":
					createNounsTotal();
					System.out.println("Noun total table created.");
					break;
				case "2":
					createNounsTrack();
					System.out.println("Noun tracking Table craeted.");
					break;
				case "3":
					System.out.print("Input story here: ");
					BufferedReader storyReader = new BufferedReader(new InputStreamReader(System.in));
					String story=null;
					try {
						story= storyReader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String[] wordList = breakWords(story);
					for (int x=0; x<wordList.length;x++)
					{
						if (determineProperNoun(wordList[x])==1)
						{
							insertNoun(wordList[x]);
						}
					}
					System.out.println();
					break;
				case "4":
					System.out.print("Input noun here: ");
					BufferedReader nounReader = new BufferedReader(new InputStreamReader(System.in));
					String noun=null;
					insertNoun(noun);
					System.out.println();
					break;
				case "5":
					updateNounsTotal();
					break;
				case "6":
					cont=0;
					break;
				default:
					System.out.println("");
			}
		}
	}
	
	public static void createNounsTotal()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table NounsTotal (" +
							"Word VARCHAR(30), " +
							"Total INTEGER)";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Noun Table Created");
	}
	
	public static void createNounsTrack()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table NounsTrack (" +
							"Word VARCHAR(30), " +
							"Appear DATE)";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Noun Table Created");
	}
	
	private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0, 0, 0);
        return dateFormat.format(date);
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
	
	public static void insertNounTotal(String word)
	{
		Connection con = getDatabaseConnection();
		Statement insert;
		
		try
		{
			insert = con.createStatement();
			insert.executeUpdate("INSERT INTO NounsTotal VALUES(\"" + word + "\", \'1\');");
			insert.close();
			con.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Error inserting noun into NounsTotal");
		}
	}
	
	public static void updateNounsTotal()
	{
		Connection con = getDatabaseConnection();
		Statement insert;
		Statement searchTrack;
		Statement searchTotal;
		Statement updateTotal;
		
		ResultSet trackResult;
		ResultSet totalResult;
		
		String story;
		String word;
		String tempWord;
		String[] words = null;
		
		int tempTotal;
		
		try
		{
			searchTrack = con.createStatement();
			searchTotal = con.createStatement();
			updateTotal = con.createStatement();
			
			trackResult = searchTrack.executeQuery("SELECT Word FROM NounsTrack");	//Selects the Words from Nouns Track
			while (trackResult.next()) {
				word = (trackResult.getString(1));
				System.out.println("Tracking: " + trackResult.getString(1));
				totalResult = searchTotal.executeQuery("SELECT Word FROM NounsTotal WHERE Word = \"" + word + "\";");	//See if the current noun is already listed in the totals table
				if (!totalResult.next())
				{
					insertNounTotal(word);	//If noun does not exist in NounsTotal, add it to the table
				}
				else
				{
					totalResult = searchTotal.executeQuery("SELECT Total FROM NounsTotal WHERE Word = \"" + word + "\";");	//Get the current value of the noun
					tempTotal = ((Number) totalResult.getObject(1)).intValue();
					updateTotal.executeUpdate("UPDATE NounsTotal SET Total = " + (tempTotal+1) + " WHERE Word = \"" + word + "\";");	//Update the entry in the table
				}
			}
		}
		catch (SQLException ex)
		{
			System.out.println("Error inserting noun into NounsTotal");
		}
	}
	
	public static String[] breakWords(String word)
	{
		String[] wordList;
		String delimiter = " ";
		wordList = word.split(delimiter);
		
		return wordList;
	}
	
	public static int determineProperNoun(String word)
	{
		int returnValue =0;	//If the word passed in appears to be a proper noun then this will be changed to a 1
		char start = word.charAt(0);
		if (start==('A')|start==('B')|start==('B')|start==('C')|start==('D')|start==('E')|start==('F')|start==('G')|start==('H')|start==('I')|start==('J')|start==('K')|start==('L')|start==('M')|start==('N')|start==('O')|start==('P')|start==('Q')|start==('R')|start==('S')|start==('T')|start==('U')|start==('V')|start==('W')|start==('X')|start==('Y')|start==('Z'))
		{
			returnValue=1;
			System.out.println("NOUN FOUND - " + word);
		}
		
		return returnValue;
	}
	
	private static Connection getDatabaseConnection()
	{
		System.out.println("Trying to get connection");
		/*
		 * Couldn't get the DB to work so I havn't edited the database information to connect to.
		 */
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			con =DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","password");
			} catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return con;
	}

}
