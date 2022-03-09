package com.revature.services;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.AccountDaoDB;
import com.revature.dao.TransactionDaoDB;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

//Required for File implementation
import com.revature.dao.AccountDaoFile;
import com.revature.dao.TransactionDaoFile;

/**
 * This class should contain the business logic for performing operations on Accounts
 */

@SuppressWarnings("unused")
public class AccountService {
	
	public AccountDao actDao;
	
	//For File implementation
//	public AccountDaoFile adao = new AccountDaoFile();
//	public TransactionDaoFile tdao = new TransactionDaoFile();
	
	//For DB implementation
	public AccountDaoDB adao = new AccountDaoDB();
	public TransactionDaoDB tdao = new TransactionDaoDB();
	
	public static final double STARTING_BALANCE = 25d;
	
	public static String fileLocation = "/Users/asn/Desktop/Revature/P0-ASNBank-master/Transactions.txt";
	ObjectOutputStream objTranOut;
	
	List<Transaction> userTranList = new ArrayList<Transaction>();
	List<Account> accountList = new ArrayList<Account>();
	List<Transaction> allTranList = new ArrayList<Transaction>();

	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		
		Transaction transaction = new Transaction();
		userTranList = new ArrayList<Transaction>();
		
		
		if (!a.isApproved()) {
			System.out.println("\n~~~~~ Account is not approved for transactions ~~~~~");
			throw new UnsupportedOperationException();
		} else if (a.getBalance() < amount) {
			System.out.println("\n~~~~~~~~~ Insufficient funds in account ~~~~~~~~~");
			throw new OverdraftException();
		} else if (amount < 0) {
			System.out.println("\n~~~~~~~ Unable to deposit a negative amount ~~~~~~~");
			throw new UnsupportedOperationException();
		} else {
			a.setBalance(a.getBalance() - amount);
			System.out.println("\n<^><^> $" + amount + " Successfully Withdrawn from Account " + a.getId() + " <^><^>");
		}

		transaction.setSender(a);
		transaction.setRecipient(a);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.WITHDRAWAL);
		transaction.setTimestamp();
		
		tdao.updateTransactions(transaction);
		
		if (a.getTransactions() == null) {
			userTranList.add(transaction);
		} else {
			userTranList = a.getTransactions();
			userTranList.add(transaction);
		}
		
		a.setTransactions(userTranList);

		actDao.updateAccount(a);
		
	}
	
	
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		
		Transaction transaction = new Transaction();
		userTranList = new ArrayList<Transaction>();

		if (!a.isApproved()) {
			System.out.println("\n~~~~~~~ Account is not approved for transactions ~~~~~~~");
			throw new UnsupportedOperationException();
		} else if (amount < 0) {
			System.out.println("\n~~~~~~~~~ Unable to deposit a negative amount ~~~~~~~~~");
			throw new UnsupportedOperationException();
		} else {
			a.setBalance(a.getBalance() + amount);
			System.out.println("\n<^><^> $" + amount + " Successfully Deposited into Account " + a.getId() + " <^><^>");
		}

		transaction.setSender(a);
		transaction.setRecipient(a);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setTimestamp();
		
		tdao.updateTransactions(transaction);
		
		if (a.getTransactions() == null) {
			userTranList.add(transaction);
		} else {
			userTranList = a.getTransactions();
			userTranList.add(transaction);
		}
		
		a.setTransactions(userTranList);
		
		actDao.updateAccount(a);

	}
	
	
	
	/**
	 * Transfers funds between accounts
	 * @throws UnsupportedOperationException if amount is negative or 
	 * the transaction would result in a negative balance for either account
	 * or if either account is not approved
	 * @param fromAct the account to withdraw from
	 * @param toAct the account to deposit to
	 * @param amount the monetary value to transfer
	 */
	public void transfer(Account fromAct, Account toAct, double amount) {
		
		Transaction transaction = new Transaction();
		userTranList = new ArrayList<Transaction>();
		
		if (amount > fromAct.getBalance()) {
			System.out.println("\n~~~~ Insufficient funds in transferring account ~~~~");
			throw new UnsupportedOperationException();
		} else if (amount < 0) {
			System.out.println("\n~~~~~~ Cannot transfer a negative amount ~~~~~~");
			throw new UnsupportedOperationException();
		} else if (!fromAct.isApproved() && !toAct.isApproved()) {
			System.out.println("\n~ At least one account involved is not approved for transactions ~");
			throw new UnsupportedOperationException();
		} else {
			fromAct.setBalance(fromAct.getBalance() - amount);
			toAct.setBalance(toAct.getBalance() + amount);
			System.out.println("\n<^> $" + amount + " Successfully transferred from Account " + fromAct.getId() + " to Account " + toAct.getId() + " <^>");

		}


		transaction.setSender(fromAct);
		transaction.setRecipient(toAct);
		transaction.setAmount(amount);
		transaction.setType(Transaction.TransactionType.TRANSFER);
		transaction.setTimestamp();
		
		tdao.updateTransactions(transaction);
		
		if (toAct.getTransactions() == null) {
			userTranList.add(transaction);
		} else {
			userTranList = toAct.getTransactions();
			userTranList.add(transaction);
		}
		toAct.setTransactions(userTranList);
		
		if (fromAct.getTransactions() == null) {
			userTranList.add(transaction);
		} else {
			userTranList = fromAct.getTransactions();
			userTranList.add(transaction);
		}
		fromAct.setTransactions(userTranList);

		actDao.updateAccount(toAct);
		actDao.updateAccount(fromAct);

	}
	
	
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
				
		Account newAccount = new Account();
		
		newAccount.setOwnerId(u.getId());
		newAccount.setBalance(STARTING_BALANCE);
		if (adao.getAccountsByUser(u) == null) {
			newAccount.setType(Account.AccountType.CHECKING);
		} else {
			newAccount.setType(Account.AccountType.SAVINGS);
		}
		newAccount.setApproved(false);
		newAccount.setTransactions(null);
		
		//Required for File implementation
//		newAccount.setId((int) Math.abs((newAccount.hashCode() * Math.random())));

		
		if (actDao.getAccountsByUser(u) == null) {
			accountList.add(newAccount);
		} else {
			accountList = actDao.getAccountsByUser(u);
			accountList.add(newAccount);
		}
		
		u.setAccounts(accountList);
		
		actDao.addAccount(newAccount);
		System.out.println("\n<^><^><^><^> Account Successfully Created <^><^><^><^>");

		return newAccount;
	}
	
	
	
	/**
	 * Approve or reject an account.
	 * @param a
	 * @param approval
	 * @throws UnauthorizedException if logged in user is not an Employee
	 * @return true if account is approved, or false if unapproved
	 */
	public boolean approveOrRejectAccount(Account a, boolean approval) {
		
		if (!SessionCache.getCurrentUser().get().getUserType().equals(User.UserType.EMPLOYEE)) {
			System.out.println("\n~~~~~ Current user not authorized to perform action ~~~~~");
			throw new UnauthorizedException();
		} else {	
			a.setApproved(approval);
			System.out.println("\n<^><^><^> Account approval has been changed <^><^><^>");
			adao.updateAccount(a);
		}
		
		return a.isApproved();
	}
}
