package com.revature.driver;

import java.util.Scanner;

import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoFile;
import com.revature.dao.TransactionDao;
import com.revature.dao.TransactionDaoFile;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoFile;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.services.AccountService;
import com.revature.services.UserService;
import com.revature.utils.SessionCache;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	public static void main(String[] args) {
		
//		UserDao udao = new UserDaoDB();
//		AccountDao adao = new AccountDaoDB();
		
		UserDao udao = new UserDaoFile();
		AccountDao adao = new AccountDaoFile();
		TransactionDao tdao = new TransactionDaoFile();
		
		UserService us = new UserService(udao, adao);
		AccountService as = new AccountService(adao);
		
		Scanner scan = new Scanner(System.in);
		
		
		//Welcome
		System.out.println("Welcome to ASN Bank");
		int s1;
		String use;
		String pass;
		
		
		//Sign In / Register Menu
		//put 1500ms timer here
		do {
			System.out.println("1. Sign In");
			System.out.println("2. Register as a new User");
			System.out.print("Enter the number of your desired baning option [1-2]: ");

			s1 = scan.nextInt();


			switch (s1) {
				case 1:
					//Logging In
					System.out.print("Enter Username: ");
					use = scan.next();
					System.out.print("Enter Password: ");
					pass = scan.next();
					
					us.login(use, pass);
					
					//Customer Menu
					if (udao.getUser(use, pass).getUserType() == UserType.CUSTOMER) {
						do {
							System.out.println("Customer Menu:");
							System.out.println("1. Create an Account");
							System.out.println("2. Retrieve Accounts");
							System.out.println("3. Make a Withdraw");
							System.out.println("4. Make a Deposit");
							System.out.println("5. Make a Transfer");
							System.out.println("0. Exit");
							System.out.print("Enter the number of your desired banking option [0-5]: ");
	
							s1 = scan.nextInt();
	
							switch (s1) {
								case 1:
									//Creating Account
									as.createNewAccount(udao.getUser(use, pass));
									break;
									
								case 2:
									//Retrieving Account Info
									System.out.println("Here is your account information:");
									System.out.println(adao.getAccountsByUser(udao.getUser(use, pass)));
									break;
									
								case 3:
									//Withdraw
									System.out.print("Enter your account number:");
									s1 = scan.nextInt();
									System.out.print("Enter the amount you wish to withdraw [format 0.00]: ");
									double with = scan.nextDouble();
									
									as.withdraw(adao.getAccount(s1), with);
									
									System.out.println("$" + with + " has been withdrawn from account " + s1);
									break;
									
								case 4:
									//Deposit
									System.out.print("Enter your account number:");
									s1 = scan.nextInt();
									System.out.print("Enter the amount you wish to deposit [format 0.00]: ");
									double depo = scan.nextDouble();
									
									as.withdraw(adao.getAccount(s1), depo);
									
									System.out.println("$" + depo + " has been deposited from account " + s1);
									break;
									
								case 5:
									//Transfer
									System.out.print("Enter account number to transfer from: ");
									int from = scan.nextInt();
									System.out.print("Enter account number to transfer to: ");
									int to = scan.nextInt();
									System.out.print("Enter the amount you wish to transfer [format 0.00]: ");
									double tran = scan.nextDouble();
									
									as.transfer(adao.getAccount(from), adao.getAccount(to), tran);
									break;
									
							}
							
						} while (s1 != 0);
						
					//Employee Menu
					} else {
							System.out.println("Employee Menu:");
							System.out.println("1. Approve or Reject Account");
							System.out.println("2. View Log of Transactions");
							System.out.println("0. Go Back");
							System.out.print("Enter the number of your desired baning option [0-2]: ");
							
							s1 = scan.nextInt();
							
							switch (s1) {
								case 1:
									//Approve / Reject Accounts
									boolean approval = false;
									
									System.out.print("Enter account number alter approval of: ");
									int actNo = scan.nextInt();
									
									System.out.println("Would you like to 1) Approve or 2) Reject this account [1-2]: ");
									s1 = scan.nextInt();
									
									switch (s1) {
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
									System.out.println(tdao.getAllTransactions());
									break;
							}
					}
					
					break;
				
				//Register New User
				case 2:
					int id = 0;
					
					System.out.print("Enter a Username: ");
					String un = scan.next();
					
					System.out.print("Enter a Password: ");
					String pw = scan.next();
					
					System.out.print("Enter First Name: ");
					String fn = scan.next();
					
					System.out.print("Enter Last Name: ");
					String ln = scan.next();
					
					UserType ut = null;
					
					System.out.print("Are you an 1) Employee or a 2) Customer [1-2]: "); 
					s1 = scan.nextInt();
					
					switch (s1) {
						case 1:
							System.out.print("Enter administrative Username: ");
							String adUser = scan.next().toLowerCase();
							System.out.print("Enter administrative Password: ");
							String adPass = scan.next();
							
							if (adUser.equals("admin") && adPass.equals("admin")) {
								ut = UserType.EMPLOYEE;
							} else {
								ut = UserType.CUSTOMER;
								System.out.println("Invalid Admin Credentials");
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
		
		System.out.println("Thank you for choosing ASN Bank, goodbye.");
		
		scan.close();
	}

}
