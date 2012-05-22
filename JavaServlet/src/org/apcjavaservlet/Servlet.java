package org.apcjavaservlet;

import javax.servlet.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.net.*;
import java.io.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.sql.*;
/**
 * Servlet implementation class Servlet
 */
public class Servlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		Connection con = null;  
		Statement stmt = null;
		ResultSet rs = null;
		
		// Testing reading data and printing it.
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con =DriverManager.getConnection 
			("jdbc:mysql://localhost:3306/rawdata","user","root");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM finalData");
			PrintWriter out = response.getWriter();
			while (rs.next()) {
				out.println("{ " + rs.getString(1) + " }");
				out.println("{ " + rs.getString(2) + " }");
				out.println("{ " + rs.getString(3) + " }");
				out.println("{ " + rs.getString(4) + " }");
				out.println("{ " + rs.getString(5) + " }");
				out.println("{ " + rs.getString(6) + " }");
				
				out.println();
				out.println();
				out.println();
				out.println();
			}

        
		}
		catch (SQLException e) {
			 throw new ServletException("Servlet Could not display records.", e);
			  } catch (ClassNotFoundException e) {
			  throw new ServletException("JDBC Driver not found.", e);
		}
	
	}
	
}