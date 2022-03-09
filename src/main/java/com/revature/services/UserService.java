package com.revature.services;

import com.revature.beans.User;
import com.revature.dao.AccountDao;
import com.revature.dao.UserDao;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.SessionCache;

/**
 * This class should contain the business logic for performing operations on users
 */
public class UserService {
	
	UserDao userDao;
	AccountDao accountDao;
	
	
	public UserService(UserDao udao, AccountDao adao) {
		this.userDao = udao;
		this.accountDao = adao;
	}
	
	
	
	/**
	 * Validates the username and password, and return the User object for that user
	 * @throws InvalidCredentialsException if either username is not found or password does not match
	 * @return the User who is now logged in
	 */
	public User login(String username, String password) {
		
		User look4User = userDao.getUser(username, password);
		
		if (look4User == null) {
			System.out.println("\n           ~~~~~ User Not Found ~~~~~");
				throw new InvalidCredentialsException();
		} else if (!look4User.getPassword().equals(password)) {
			System.out.println("\n        ~~~~~~ Incorrect Password ~~~~~~");
				throw new InvalidCredentialsException();
		} else {
			System.out.println("\n<^><^><^><^><^> Login Successful <^><^><^><^><^>");
			SessionCache.setCurrentUser(look4User);
			return look4User;
		}
				
	}
	
	
	
	/**
	 * Creates the specified User in the persistence layer
	 * @param newUser the User to register
	 * @throws UsernameAlreadyExistsException if the given User's username is taken
	 */
	public void register(User newUser) {

		User look4User = userDao.getUser(newUser.getUsername(), newUser.getPassword());
		
			if (look4User != null) {
				System.out.println("\n      ~~~~~~ Username Already Exists ~~~~~~");
				throw new UsernameAlreadyExistsException();
			} else {
				//Needed for File implementation
//				newUser.setId(Math.abs(newUser.hashCode()));
				
				userDao.addUser(newUser);
				System.out.println("\n<^><^><^><^> User Successfully Added <^><^><^><^>");
			}
		
	}
	
}
