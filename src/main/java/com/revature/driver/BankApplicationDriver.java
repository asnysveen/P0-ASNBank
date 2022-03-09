package com.revature.driver;

import java.util.Scanner;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoDB;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoDB;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoDB;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.services.AccountService;
import com.revature.services.UserService;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	public static void main(String[] args) {
		
		//For File implementation
//		UserDao udao = new UserDaoFile();
//		AccountDao adao = new AccountDaoFile();
//		TransactionDao tdao = new TransactionDaoFile();
		
		//For DB implementation
		UserDao udao = new UserDaoDB();
		AccountDao adao = new AccountDaoDB();
		TransactionDao tdao = new TransactionDaoDB();
		
		UserService us = new UserService(udao, adao);
		AccountService as = new AccountService(adao);
		
		Scanner scan = new Scanner(System.in);
		
		
		//Welcome
		System.out.println("<*><*><*><*><*><*> Welcome to ASN Bank <*><*><*><*><*><*>");
		int s1;
		String use;
		String pass;
		
		
		//Sign In / Register Menu
		//put 1500ms timer here
		do {
			System.out.println();
			System.out.println("\t 1. Sign In");
			System.out.println("\t 2. Register as a new User");
			System.out.println("\t 0. Exit");

			System.out.print("\nEnter the number of your desired baning option [1-2]: ");

			s1 = scan.nextInt();

			System.out.println();
			
			switch (s1) {
				case 1:
					//Logging In
					System.out.print("\t Enter Username: ");
					use = scan.next();
					System.out.print("\t Enter Password: ");
					pass = scan.next();
					
					try {
						us.login(use, pass);
					
					
					System.out.println();

					//Customer Menu
					if (udao.getUser(use, pass).getUserType() == UserType.CUSTOMER) {
						
						int s2;

						do {
							System.out.println();
							
							System.out.println("\t Customer Menu:");
							System.out.println("\t 1. Create an Account");
							System.out.println("\t 2. Retrieve Accounts");
							System.out.println("\t 3. Make a Withdraw");
							System.out.println("\t 4. Make a Deposit");
							System.out.println("\t 5. Make a Transfer");
							System.out.println("\t 6. Retrieve balance with account ID");

							

							System.out.println("\t 0. Go Back");
							System.out.print("\nEnter the number of your desired banking option [0-6]: ");
							
							s2 = scan.nextInt();
	
							switch (s2) {
								case 1:
									//Creating Account
									as.createNewAccount(udao.getUser(use, pass));
									break;
									
								case 2:
									//Retrieving Account Info
									System.out.println();
									System.out.println("Account information for current user:");
									System.out.println();

									for (Account account : adao.getAccountsByUser(udao.getUser(use, pass))) {
										System.out.println(account);
									}
									break;
									
								case 3:
									//Withdraw
									System.out.print("\t Enter your account number: ");
									s2 = scan.nextInt();
									System.out.print("\t Enter the amount you wish to withdraw: ");
									double with = scan.nextDouble();
									
									try {
										as.withdraw(adao.getAccount(s2), with);

									} catch (UnsupportedOperationException e) {
									} catch (OverdraftException e) {
									}
									
									break;
									
								case 4:
									//Deposit
									System.out.print("\t Enter your account number: ");
									s2 = scan.nextInt();
									System.out.print("\t Enter the amount you wish to deposit: ");
									double depo = scan.nextDouble();
									
									try {
										as.deposit(adao.getAccount(s2), depo);

									} catch (UnsupportedOperationException e) {
									} catch (OverdraftException e) {
									}
									
									break;
									
								case 5:
									//Transfer
									System.out.print("\t Enter account number to transfer from: ");
									int from = scan.nextInt();
									System.out.print("\t Enter account number to transfer to: ");
									int to = scan.nextInt();
									System.out.print("\t Enter the amount you wish to transfer: ");
									double tran = scan.nextDouble();
									
									try {
										as.transfer(adao.getAccount(from), adao.getAccount(to), tran);

									} catch (UnsupportedOperationException e) {
									} catch (OverdraftException e) {
									}
									
									break;
									
								case 6:
									System.out.println();
									System.out.print("Enter the account number you wish to retrieve: ");
									s2 = scan.nextInt();
									System.out.println();

									System.out.println("The balance for account " + s2 + ": $" +adao.getAccount(s2).getBalance());
									break;
									
							}
							
						} while (s2 != 0);
						
					//Employee Menu
					} else {
						
						int s3;

						do {
							System.out.println();
							System.out.println("\t Employee Menu:");
							System.out.println("\t 1. Approve or Reject Account");
							System.out.println("\t 2. View Log of Transactions");
							
							System.out.println("\t 3. Get all users");
							System.out.println("\t 4. Get all accounts");
							
							System.out.println("\t 0. Go Back");
							System.out.print("\nEnter the number of your desired baning option [0-4]: ");
							
							s3 = scan.nextInt();
							
							System.out.println();
							
							switch (s3) {
								case 1:
									//Approve / Reject Accounts
									boolean approval = false;
									
									System.out.print("\t Enter account number to alter approval of: ");
									int actNo = scan.nextInt();
									
									System.out.print("Would you like to 1) Approve or 2) Reject this account [1-2]: ");
									s3 = scan.nextInt();
									
									switch (s3) {
										case 1:
											approval = true;
											break;
										case 2:
											approval = false;
											break;
											
									}

									as.approveOrRejectAccount(adao.getAccount(actNo), approval);
									
									break;
									
								case 2:
									//View Transaction Log
									for (Transaction transaction : tdao.getAllTransactions()) {
										System.out.println(transaction);
									}
									break;
									
								case 3:
									for (User user : udao.getAllUsers()) {
										System.out.println(user);
									}
									break;
								
								case 4:
									for (Account account : adao.getAccounts()) {
										System.out.println(account);
									}
									break;
								
							}
						} while (s3 != 0);
					}
					
					} catch (InvalidCredentialsException e) {
					} catch (NullPointerException e) {
					}
					
					break;
					
				//Register New User
				case 2:
					
					int s4;
					
					int id = 0;
					
					System.out.print("\t Enter a Username: ");
					String un = scan.next();
					
					System.out.print("\t Enter a Password: ");
					String pw = scan.next();
					
					System.out.print("\t Enter First Name: ");
					String fn = scan.next();
					
					System.out.print("\t Enter Last Name: ");
					String ln = scan.next();
					
					UserType ut = null;
					
					System.out.print("\nAre you an 1) Employee or a 2) Customer [1-2]: "); 
					s4 = scan.nextInt();
					
					switch (s4) {
						case 1:
							System.out.print("\n***** Enter administrative Username: ");
							String adUser = scan.next().toLowerCase();
							System.out.print("***** Enter administrative Password: ");
							String adPass = scan.next();
							
							if (adUser.equals("admin") && adPass.equals("admin")) {
								ut = UserType.EMPLOYEE;
							} else {
								ut = UserType.CUSTOMER;
								System.out.println("~~~~~Invalid Admin Credentials");
								throw new InvalidCredentialsException();
							}
							break;
							
						case 2:
							ut = UserType.CUSTOMER;
							break;
					}
					
					us.register(new User(id, un, pw, fn, ln, ut, null));;

					break;
				
			}
			
		} while (s1 != 0);
		
		System.out.println("<*><*><*> Thank you for choosing ASN Bank, goodbye <*><*><*>");
		
		scan.close();
	}

}
