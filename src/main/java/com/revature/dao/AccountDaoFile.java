package com.revature.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.User;


/**
 * Implementation of AccountDAO which reads/writes to files
 */
public class AccountDaoFile implements AccountDao {

	public static String fileLocation = "/Users/asn/Desktop/Revature/P0-ASNBank-master/Accounts.txt";
	
	List<Account> accountList = new ArrayList<Account>();
	
	
	ObjectOutputStream objAccOut;
	ObjectInputStream objAccIn;
	
	
	
	public AccountDaoFile() {
		File file = new File(fileLocation);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				objAccOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
				objAccOut.writeObject(accountList);
				objAccOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	//create account based on user account: e.g., (userId + "-1") for checking or (userId + "-2") for savings
	public Account addAccount(Account a) {
		
		accountList = getAccounts();
		
		accountList.add(a);
		
		try {
			objAccOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objAccOut.writeObject(accountList);
			objAccOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

		return a;
	}
		
		

	public Account getAccount(Integer actId) {
		
		accountList = getAccounts();
		
		for (Account account : accountList) {
			if (account.getId().equals(actId)) {
				return account;
			}
		}
		
		return null;
	}

	
	
	@SuppressWarnings("unchecked")
	public List<Account> getAccounts() {

		try {
			objAccIn = new ObjectInputStream(new FileInputStream(fileLocation));
			accountList = (List<Account>) objAccIn.readObject(); 
			objAccIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return accountList;
	}

	
	
	public List<Account> getAccountsByUser(User u) {
		
		accountList = getAccounts();

		List<Account> userAccounts = new ArrayList<Account>();

		for (Account account : accountList) {
			if (u.getId().equals(account.getOwnerId())) {
				return userAccounts;
			}
		}
		
		return null;
	}

	

	public Account updateAccount(Account a) {
		accountList = getAccounts();
		
		for (Account account : accountList) {
			if (account.getId().equals(a.getId())) {
				account.setBalance(a.getBalance());
				account.setType(a.getType());
				account.setApproved(a.isApproved());
				account.setTransactions(a.getTransactions());
			}
		}
		
		try {
			objAccOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objAccOut.writeObject(accountList);
			objAccOut.close();
			return a;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return a;
	}

	
	
	public boolean removeAccount(Account a) {

		accountList = getAccounts();
		
		for (Account account : accountList) {
			if (account.equals(a)) {
				accountList.remove(accountList.indexOf(a));
				
				try {
					objAccOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
					objAccOut.writeObject(accountList);
					System.out.println("Account Successfully Deleted");
					objAccOut.close();
					return true;

				} catch (FileNotFoundException e) {
					System.out.println("File not found");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException:" + e.getMessage());
				} 
			}
		}
		
		return false;
	}
	
}
