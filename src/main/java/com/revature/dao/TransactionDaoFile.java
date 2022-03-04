package com.revature.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Transaction;

public class TransactionDaoFile implements TransactionDao {
	
	public static String fileLocation = "/Users/asn/Desktop/Revature/P0-ASNBank-master/Transactions.txt";

	public static List<Transaction> transactionList = new ArrayList<Transaction>();	

	
	ObjectInputStream objTranIn;

	
	
	@SuppressWarnings("unchecked")
	public List<Transaction> getAllTransactions() {

		try {
			objTranIn = new ObjectInputStream(new FileInputStream(fileLocation));
			transactionList = (List<Transaction>) objTranIn.readObject(); 
			objTranIn.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + e.getMessage());
		}
		return null;
	}

}
