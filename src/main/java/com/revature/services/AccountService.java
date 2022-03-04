package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.Transaction.TransactionType;
import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.exceptions.OverdraftException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on Accounts
 */
public class AccountService {
	
	public AccountDao actDao;
	public static final double STARTING_BALANCE = 25d;
	
	
	public AccountService(AccountDao dao) {
		this.actDao = dao;
	}
	
	
	
	/**
	 * Withdraws funds from the specified account
	 * @throws OverdraftException if amount is greater than the account balance
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void withdraw(Account a, Double amount) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Transaction transaction = new Transaction();

		if (a.getBalance() < amount) {
			throw new OverdraftException();
		} else if (amount < 0) {
			throw new UnsupportedOperationException();
		} else {
			a.setBalance(a.getBalance() - amount);
		}

		transaction.setSender(a);
		transaction.setRecipient(a);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.WITHDRAWAL);
		transaction.setTimestamp();
		
		transactionList = a.getTransactions();
		transactionList.add(transaction);
		a.setTransactions(transactionList);
		
		actDao.updateAccount(a);
		
	}
	
	
	
	/**
	 * Deposit funds to an account
	 * @throws UnsupportedOperationException if amount is negative
	 */
	public void deposit(Account a, Double amount) {
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Transaction transaction = new Transaction();

		if (amount < 0) {
			throw new UnsupportedOperationException();
		} else {
			a.setBalance(a.getBalance() + amount);
		}

		transaction.setSender(a);
		transaction.setRecipient(a);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.DEPOSIT);
		transaction.setTimestamp();
		
		transactionList = a.getTransactions();
		transactionList.add(transaction);
		a.setTransactions(transactionList);
		
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
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		Transaction transaction = new Transaction();

		if (amount < 0) {
			throw new UnsupportedOperationException();
		} else if (!fromAct.isApproved() && !toAct.isApproved()) {
			throw new UnsupportedOperationException();
		} else {
			fromAct.setBalance(fromAct.getBalance() - amount);
			toAct.setBalance(toAct.getBalance() + amount);
		}

		transaction.setSender(fromAct);
		transaction.setRecipient(toAct);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.TRANSFER);
		transaction.setTimestamp();
		
		transactionList = fromAct.getTransactions();
		transactionList.add(transaction);
		fromAct.setTransactions(transactionList);
		
		actDao.updateAccount(fromAct);
		
		transactionList = toAct.getTransactions();
		transactionList.add(transaction);
		toAct.setTransactions(transactionList);
		
		actDao.updateAccount(toAct);
	}
	
	
	
	/**
	 * Creates a new account for a given User
	 * @return the Account object that was created
	 */
	public Account createNewAccount(User u) {
		
		//List<Account> userAccounts = actDao.getAccountsByUser(u);
		
		Account newAccount = new Account();
		
		//newAccount.setId();
		newAccount.setOwnerId(u.getId());
		newAccount.setBalance(STARTING_BALANCE);
		newAccount.setType(Account.AccountType.CHECKING);
		newAccount.setApproved(true);
		newAccount.setTransactions(null);
		
		actDao.addAccount(newAccount);
		
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
			throw new UnauthorizedException();
		} else {	
			a.setApproved(approval);
		}
		
		return false;
	}
}
