package com.revature.driver;

import java.util.Scanner;

import com.revature.beans.Account;
import com.revature.beans.Account.AccountType;
import com.revature.beans.User;
import com.revature.beans.User.UserType;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoFile;
import com.revature.dao.UserDao;
import com.revature.dao.UserDaoFile;
import com.revature.services.UserService;

/**
 * This is the entry point to the application
 */
public class BankApplicationDriver {

	public static void main(String[] args) {
		
		UserDao udao = new UserDaoFile();
		
		//AccountDao adao = new AccountDaoDB();

		AccountDao adao = new AccountDaoFile();
		UserService us = new UserService(udao, adao);
		
		Scanner scan = new Scanner(System.in);
		
		
		//Welcome
		System.out.println("Welcome to ASN Bank");
		int s1;
		String use;
		String pass;
		
		
		//Sign In / Register Menu
		//put 1500ms timer here
		do {
			System.out.println("Enter the number of your desired baning option: ");
			System.out.println("1. Sign In");
			System.out.println("2. Register as a new User");

//			System.out.println("3. Retrieve an Account");
//			System.out.println("4. Update an Account");
//			System.out.println("5. Delete an Account");
//			System.out.println("6. Retrieve All Accounts");
//			System.out.println("0. Exit");

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
							System.out.println("3. Update an Account");
							System.out.println("4. Delete an Account");
							System.out.println("5. Retrieve User Profile");
							System.out.println("6. Update User Profile");
							System.out.println("7. Delete User Profile");
							System.out.println("0. Exit");
							System.out.print("Enter the number of your desired banking option: ");
	
							s1 = scan.nextInt();
	
							switch (s1) {
								case 1:
									//Creating Account
									// TODO automate accountID

									int id;
									
									int oi = udao.getUser(use, pass).getId();
									
									AccountType at = null;
									System.out.println("1. Checking");
									System.out.println("2. Savings");
									System.out.print("Choose the number of your desired account type:");
									int in1 = scan.nextInt();
									
									while (in1 != 1 && in1 != 2) {
										System.out.print("Choose the number of your desired account type:");
										in1 = scan.nextInt();
									}
									
									switch (in1) {
										case 1:
											at = AccountType.CHECKING;
											break;
											
										case 2:
											at = AccountType.SAVINGS;
											break;
									}
									//TODO set starting balance
									System.out.print("Enter your Starting Balance (format: 0.00): ");
									Double ab = scan.nextDouble();
									
									adao.addAccount(new Account(id, oi, ab, at, true, null));
									
									break;
									
								case 2:
									//Retrieving Account Info
									System.out.println("Here is your account information:");
									System.out.println(adao.getAccountsByUser(udao.getUser(use, pass)));
									break;
									
								case 3:
									//Update Account
									
									break;
									
								case 4:
									//Delete Account
									//how to retrieve specific account by user
									
									break;
									
								case 5:
									//Retrieve User Information
									System.out.println("Here is your User information");
									System.out.println(udao.getUser(use, pass));
	
									break;
									
								case 6:
									//Update User Information
									
									udao.updateUser(null);
									break;
									
								case 7:
									//Delete User
									System.out.println("1. Yes");
									System.out.println("2. No");
									System.out.print("Are you sure you would like to delete this user profile?: ");
									
									s1 = scan.nextInt();
									
									switch (s1) {
									case 1:
										udao.removeUser(udao.getUser(use, pass));
										break;
										
									case 2:
										break;
									}
									
									break;
							}
						} while (s1 != 0);
						
					//Employee Menu
					} else {
							System.out.println("Employee Menu:");
							System.out.println("1. Approve or Reject Account");
							System.out.println("2. View Log of Transactions");
							System.out.println("0. Exit");
							System.out.print("Enter the number of your desired baning option: ");
							
							s1 = scan.nextInt();
							
							switch (s1) {
								case 1:
									//Approve / Reject Accounts
									break;
									
								case 2:
									//View Transaction Log
									break;
							}
					}
					
					break;
				
				//Register New User
				case 2:
					int id = UserDaoFile.userList.size() + 1;
					
					User look4UserID = udao.getUser(id);
					
					while (!look4UserID.equals(null)) {
						id++;
					}
//					while (id == udao.getUser(id).getId()) {
//						id++;
//					}
					
					System.out.print("Enter a Username: ");
					String un = scan.next();
					
					System.out.print("Enter a Password: ");
					String pw = scan.next();
					
					System.out.print("Enter First Name: ");
					String fn = scan.next();
					
					System.out.print("Enter Last Name: ");
					String ln = scan.next();
					
					UserType ut = null;
					System.out.print("Are you an 'Employee' or a 'Customer': "); 
					String in1 = scan.next().toLowerCase();
					while (!in1.equals("employee") && !in1.equals("customer")) {
						System.out.print("Are you an 'Employee' or a 'Customer': "); 
						in1 = scan.next().toLowerCase();
					}
					switch (in1) {
						case "employee":
							System.out.print("Enter administrative Username: ");
							String adUser = scan.next().toLowerCase();
							System.out.print("Enter administrative Password: ");
							String adPass = scan.next();
							
							if (adUser.equals("admin") && adPass.equals("admin")) {
								ut = UserType.EMPLOYEE;
							} else {
								ut = UserType.CUSTOMER;
							}
							break;
							
						case "customer":
							ut = UserType.CUSTOMER;
							break;
					}
					
					us.register(new User(id, un, pw, fn, ln, ut, null));;
					//us.register(new User());
					break;
					
//				case 3:
//				System.out.print("Would you like to retrieve the account with 1) 'User Id' or 2) 'Username' & 'Password': ");
//				s1 = scan.nextInt();
//				
//				switch (s1) {
//					case 1:
//						System.out.print("Enter the UserID of the account to retrieve: ");
//						s1 = scan.nextInt();
//						
//						System.out.println(udao.getUser(s1));
//						break;
//					case 2:
//						System.out.print("Enter Username: ");
//						use = scan.next();
//						System.out.print("Enter Password: ");
//						pass = scan.next();
//						
//						System.out.println(udao.getUser(use, pass));
//				}
//					
//				case 4:
//					break;
//				
//				case 5:
//					System.out.print("Enter User ID of the account to Delete: ");
//					s1 = scan.nextInt();
//
//					udao.removeUser(udao.getUser(s1));
//					break;
//				
//				case 6:
//					System.out.println(udao.getAllUsers());
//					break;
					
					

					
//					List<AccountType> ac = null;
//					System.out.print("What type of account do you require: 'Checking', 'Savings' or 'Both': ");
//					String in2 = scan.next().toLowerCase();
//					while (!in2.equals("checking") && !in2.equals("savings") && !in2.equals("both")) {
//						System.out.print("What type of account would you like to open: 'Checking', 'Savings' or 'Both': ");
//						in2 = scan.next().toLowerCase();
//					}	
//					switch (in2) {
//						case "checking":
//							ac = Arrays.asList(AccountType.CHECKING);
//							break;
//						case "savings":
//							ac = Arrays.asList(AccountType.SAVINGS);
//							break;
//						case "both":
//							ac = Arrays.asList(AccountType.CHECKING, AccountType.SAVINGS);
//							break;
//					}
					
					
				

				
			}
		} while (s1 != 0);
//		
		System.out.println("Thank you for choosing ASN Bank, goodbye.");
		
		scan.close();
	}

}
