package com.revature.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;


/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {
	
	public static String fileLocation = "/Users/asn/Desktop/Revature/P0-ASNBank-master/Users.txt";
	
	public static List<User> userList = new ArrayList<User>();	

	
	ObjectOutputStream objUserOut;
	ObjectInputStream objUserIn;
//	FileInputStream fileUserIn;
//	FileOutputStream fileUserOut;
		
	
	
	public User addUser(User user) {
		
		userList = getAllUsers();

		userList.add(user);

		try {
			objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objUserOut.writeObject(userList);
			//System.out.println("User Successfully Registered");
			objUserOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException:" + e.getMessage());
		} 

		return user;
	}

	

		
	public User getUser(Integer userId) {
				
		userList = getAllUsers();

		for (User user : userList) {
			if (user.getId().equals(userId)) {
				return user;
			}
		}
		
		return null;
	}

	
	
	public User getUser(String username, String pass) {
		
		userList = getAllUsers();

		for (User user : userList) {
			if (user.getUsername().equals(username) && user.getPassword().equals(pass)) {
				return user;
			}
		}
		
		return null;
	}


	
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {

		try {
			objUserIn = new ObjectInputStream(new FileInputStream(fileLocation));
			userList = (List<User>) objUserIn.readObject(); 
			objUserIn.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: " + e.getMessage());
		}
		
		return userList;
	}
	
	
	
	public User updateUser(User u) {
		// TODO Auto-generated method stub
		userList = getAllUsers();
		
		for (User user : userList) {
			if (user.getId().equals(u.getId())) {
				user.setUsername(u.getUsername());
				user.setPassword(u.getPassword());
				user.setFirstName(u.getFirstName());
				user.setLastName(u.getLastName());
			}
		}

		try {
			objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objUserOut.writeObject(userList);
			System.out.println("User Successfully Updated");
			objUserOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException:" + e.getMessage());
		} 
		
		return u;
	}

	
	
	public boolean removeUser(User u) {
		
		userList = getAllUsers();
		
		for (User user : userList) {
			if (user.equals(u)) {
				userList.remove(userList.indexOf(u));
				
				try {
					objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
					objUserOut.writeObject(userList);
					System.out.println("User Successfully Deleted");
					objUserOut.close();
				} catch (FileNotFoundException e) {
					System.out.println("File not found");
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println("IOException:" + e.getMessage());
				} 
				
				return true;
			}
		}
			
		return false;
	}

}
