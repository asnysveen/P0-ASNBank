package com.revature.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton utility for creating and retrieving database connection
 */
public class ConnectionUtil {
	
	private static ConnectionUtil cu = null;
	private static Properties prop;
	private static Connection conn;
	
	
	/**
	 * This method should read in the "database.properties" file and load
	 * the values into the Properties variable
	 */
	private ConnectionUtil() {
		
		File fs = new File("src/main/resources/database.properties");
		FileReader fr;
		
		try {
			fr = new FileReader(fs);
			prop = new Properties(); 
			prop.load(fr);
		} catch (FileNotFoundException e) {
			e.getMessage();
		} catch (IOException e) {
			e.getMessage();
		}
//		prop = new Properties(); 
//
//		try {
//			FileInputStream fileStream = new FileInputStream("src/main/resources/database.properties"); 
//			prop.load(fileStream);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("url = " + prop.getProperty(url));
//		url = prop.getProperty("url");	
//		username = prop.getProperty("usr"); 
//		password = prop.getProperty("pswd"); 

	}
	
	public static synchronized ConnectionUtil getConnectionUtil() {
		
		if(cu == null)
			cu = new ConnectionUtil();
		return cu;
	}
	
	/**
	 * This method should create and return a Connection object
	 * @return a Connection to the database
	 */
	public Connection getConnection() {
		// Hint: use the Properties variable to setup your Connection object
		 
		
		try {
			conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("usr"), prop.getProperty("pswd"));
		} catch (SQLException e) {
			e.getMessage();
		}
		
		return conn;
	}
}
