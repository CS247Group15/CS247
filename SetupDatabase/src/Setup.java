import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Setup {
	
	public static void main(String[] args)
	{
		String[] semanticWords;
		String[] importanceWords;
		
		createRawData();
		createSemanticWords();
		createNounsTotal();
		createNounsTrack();
		createFinalData();
		createImportantWords();
		createTwitterTrends();
		
		try{
			FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "\\bin\\semantics.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   
			{
				semanticWords = breakWords(strLine);
				insertSemanticWord(semanticWords[0],semanticWords[1]);
			}
			//Close the input stream
			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
		
		try{
			FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") + "\\bin\\importance.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)   
			{
				importanceWords = breakWords(strLine);
				insertImportanceWord(importanceWords[0],importanceWords[1]);
			}
			//Close the input stream
			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public static String[] breakWords(String word)
	{
		String[] wordList;
		String delimiter = "/";
		wordList = word.split(delimiter);
		
		return wordList;
	}
	
	private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(0, 0, 0);
        return dateFormat.format(date);
    }
	
	public static void insertSemanticWord(String word, String value) 
	{
		Connection con = getDatabaseConnection();

		Statement insert = null;

		try
		{
			insert = con.createStatement();
			insert.execute("INSERT INTO Words VALUES(\"" + word + "\", " + value + ");");
			insert.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
	}
	
	public static void insertImportanceWord(String word, String value) 
	{
		Connection con = getDatabaseConnection();

		Statement insert = null;

		try
		{
			insert = con.createStatement();
			insert.execute("INSERT INTO importantWords VALUES(\"" + word + "\", " + value + ");");
			insert.close();
			con.close();
		} catch (SQLException e) 
		{
				e.printStackTrace();
		}
	}
	
	public static void createRawData()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table rawdata (" +
							"Title VARCHAR(300) NOT NULL, " +
							"Description VARCHAR(500), " +
							"Source VARCHAR(500), " +
							"Date DATE);";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Raw Data table Created");
	}
	
	public static void createSemanticWords()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table Words (" +
							"Word VARCHAR(30) NOT NULL, " +
							"Value INTEGER);";
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
	
	public static void createFinalData()
	{
		Connection con = getDatabaseConnection();
		Statement create;

		String createString;
		createString = "create table finalData (" +
							"Title VARCHAR(300), " +
							"Description VARCHAR(500), " +
							"Source VARCHAR(500), " +
							"Date DATE, " +
							"Sentiment INTEGER, " +
							"Importance INTEGER)";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Final Data table Created");
	}
	
	public static void createImportantWords()
	{
		Connection con = getDatabaseConnection();
		Statement create;
	
		String createString;
		createString = "create table importantwords (Word VARCHAR(50), Value INTEGER);";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();
	
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Important words table Created");
	}
	
	public static void createTwitterTrends()
	{
		Connection con = getDatabaseConnection();
		Statement create;
	
		String createString;
		createString = "create table trends (trend VARCHAR(50), date_added DATE);";
		try {
			create = con.createStatement();
			create.executeUpdate(createString);
			create.close();
			con.close();
	
		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		System.out.println("Trends table Created");
	}
	
	private static Connection getDatabaseConnection(){	//Handles retrieveing the databse connection
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