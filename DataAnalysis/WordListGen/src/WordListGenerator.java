import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class WordListGenerator {
	
	static int cont;
	static int placementIndex;
	static int retEntries;
	
	public static void main(String[] args) {
		cont=1;
		placementIndex=1;
		retEntries=1;
		
		while(cont==1)
		{
			System.out.println("Options:");
			System.out.println("1 - Create words table");
			System.out.println("2 - Read in a good word and assign value");
			System.out.println("3 - Read in a bad word and assign value");
			System.out.println("4 - Place Good words");
			System.out.println("5 - Place bad words");
			System.out.println("6 - Manually enter word & value");
			System.out.println("7 - Manually change words value");
			System.out.println("9 - Exit");
			System.out.println("");
			System.out.print("Input: ");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input=null;
			String[] wordList;
			ArrayList<String> words=null;
			String word=null;
			int value=0;
			
			try {
				input = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			switch(input)
			{
				case "1":
					createWords();
					break;
					
				case "2":
					words = getWord();
					System.out.println("Loop length: " + words.size());
					System.out.println(words.get(0));
					System.out.println(words.get(1));
					for (int x = 0; x<2; x++)
					{
						word = words.get(x);
						System.out.println("The current word sentence is: " + word);
						wordList = breakWords(word);
						System.out.println("Proceeding to add words to the list");
						for (int n=0; n<wordList.length; n++)
						{
							placeGoodWord(wordList[n]);	//Put the word into the DB
						}
					}
					System.out.println("Given up adding stuff");
					break;
					
				case "3":
					words = getWord();
					for (int x = 0; x<words.size(); x++)
					{
						word = words.get(x);
						wordList = breakWords(word);
						System.out.println("Proceeding to add words to the list");
						for (int n=0; n<wordList.length; n++)
						{
							placeBadWord(wordList[n]);	//Put the word into the DB
						}
					}
					break;
					
				case "4":
					System.out.println();
					System.out.print("Enter good RSS feed story: ");
					BufferedReader goodInput = new BufferedReader(new InputStreamReader(System.in));

					try {
						input = goodInput.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					wordList = breakWords(input);
					System.out.println("Proceeding to add good words to the list");
					for (int x=0; x<wordList.length; x++)
					{
						placeGoodWord(wordList[x]);	//Put the word into the DB
					}
					break;
					
				case "5":
					System.out.println();
					System.out.print("Enter bad RSS feed story: ");
					BufferedReader badInput = new BufferedReader(new InputStreamReader(System.in));

					try {
						input = badInput.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					wordList = breakWords(input);
					System.out.println("Proceeding to add bad words to the list");
					for (int x=0; x<wordList.length; x++)
					{
						placeBadWord(wordList[x]);	//Put the word into the DB
					}
					break;
					
				case "6":
					System.out.println();
					System.out.print("Enter new word: ");
					BufferedReader newInput = new BufferedReader(new InputStreamReader(System.in));

					try {
						input = newInput.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					insertNewWord(input);
					
					System.out.println();
					System.out.print("Enter new value: ");
					BufferedReader valueInput = new BufferedReader(new InputStreamReader(System.in));
					try {
						value = Integer.parseInt(valueInput.readLine());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					changeValue(input, value);
					break;
					
				case "7":
					System.out.println();
					System.out.print("Enter word: ");
					newInput = new BufferedReader(new InputStreamReader(System.in));

					try {
						input = newInput.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println();
					System.out.print("Enter new value: ");
					valueInput = new BufferedReader(new InputStreamReader(System.in));
					try {
						value = Integer.parseInt(valueInput.readLine());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					changeValue(input, value);
					break;
				case "8":
					cont=0;
					break;
					
				default:
					System.out.println("INVALID INPUT - Please try again");
					break;
			}
		}
	}
	
	public static String[] breakWords(String word)
	{
		String[] wordList;
		String delimiter = " ";
		wordList = word.split(delimiter);
		
		return wordList;
	}
	
	public static ArrayList<String> getWord()
	{
		Connection con = getDatabaseConnection();
		System.out.println("Connected to database");

		Statement getVal = null;
		ResultSet rsValue = null;
		ArrayList<String> word= new ArrayList<String>();
		try
		{
			getVal = con.createStatement();
			rsValue = getVal.executeQuery("SELECT Description FROM rawdata;");	//Must get a word
			while (rsValue.next()) {
				word.add(rsValue.getString(1));
				System.out.println("Adding: " + rsValue.getString(1));
			}
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
		return word;
	}
	
	public static void placeGoodWord(String word) {
		Connection con = getDatabaseConnection();
		System.out.println("Dealing with the word: " + word);

		Statement insert = null;
		Statement getVal = null;
		Statement testWordExists=null;
		ResultSet rsValue = null;
		ResultSet wordExist=null;
		int curValue=0;

			/*
			 * The structure for the database should be two collumns
			 * The first has the word
			 * The second has the occurances of the word. 
			 * The good article words will add points to the list,
			 * the bad article words will take points away.
			 * Post processing can be done later to sort words with high or low enough number thresholds.
			 */
		try
		{
			insert = con.createStatement();
			getVal = con.createStatement();
			testWordExists = con.createStatement();
			wordExist = testWordExists.executeQuery("SELECT Word FROM Words WHERE Word = \"" + word + "\";");

			if (!wordExist.next())
			{
				insertNewWord(word);
			}
			rsValue = getVal.executeQuery("SELECT VALUE FROM Words WHERE Word = \"" + word + "\";");	//Get the value of the word atm
			if (rsValue.next())
			{
				curValue =  ((Number) rsValue.getObject(1)).intValue();		//Set the resultSet value to an integer so it can be incremented and added back to the database.
			}
			insert.executeUpdate("UPDATE Words SET Value = " + (curValue+1) + " WHERE Word = \"" + word + "\";");
			
			wordExist.close();
			insert.close();
			getVal.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
	}
	
	public static void placeBadWord(String word) {
		Connection con = getDatabaseConnection();
		System.out.println("Dealing with the word: " + word);

		Statement insert = null;
		Statement getVal = null;
		Statement testWordExists=null;
		ResultSet rsValue = null;
		ResultSet wordExist=null;
		int curValue=0;

			/*
			 * The structure for the database should be two collumns
			 * The first has the word
			 * The second has the occurances of the word. 
			 * The good article words will add points to the list,
			 * the bad article words will take points away.
			 * Post processing can be done later to sort words with high or low enough number thresholds.
			 */
		try
		{
			insert = con.createStatement();
			getVal = con.createStatement();
			testWordExists = con.createStatement();
			wordExist = testWordExists.executeQuery("SELECT Word FROM Words WHERE Word = \"" + word + "\";");

			if (!wordExist.next())
			{
				insertNewWord(word);
			}
			rsValue = getVal.executeQuery("SELECT VALUE FROM Words WHERE Word = \"" + word + "\";");	//Get the value of the word atm
			if (rsValue.next())
			{
				curValue =  ((Number) rsValue.getObject(1)).intValue();		//Set the resultSet value to an integer so it can be incremented and added back to the database.
			}
			insert.executeUpdate("UPDATE Words SET Value = " + (curValue-1) + " WHERE Word = \"" + word + "\";");
			
			wordExist.close();
			insert.close();
			getVal.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
	}
	
	public static void insertNewWord(String word) {
		Connection con = getDatabaseConnection();

		Statement insert = null;

		try
		{
			insert = con.createStatement();
			insert.execute("INSERT INTO Words VALUES(\"" + word + "\", 0);");
			insert.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
		System.out.println("New Word input");
	}
	
	public static void changeValue(String word, int value)
	{
		Connection con = getDatabaseConnection();
		
		Statement insert = null;
		try
		{
			insert = con.createStatement();
			insert.execute("UPDATE Words SET Value=" + value + " WHERE Word = \"" + word + "\";");
			insert.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
		System.out.println("Value added");
		
	}
	
	public static void createWords()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table Words (" +
							"Word VARCHAR(30), " +
							"Value INTEGER)";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Employees Table Created");
	}
	

	private static Connection getDatabaseConnection(){
		System.out.println("Trying to get connection");
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