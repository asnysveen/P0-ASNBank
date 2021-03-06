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

import com.revature.beans.User;


/**
 * Implementation of UserDAO that reads and writes to a file
 */
public class UserDaoFile implements UserDao {
	
	public static String fileLocation = "/Users/asn/Desktop/Revature/P0-ASNBank-master/Users.txt";
	
	List<User> userList = new ArrayList<User>();	

	
	ObjectOutputStream objUserOut;
	ObjectInputStream objUserIn;
//	FileInputStream fileUserIn;
//	FileOutputStream fileUserOut;
		
	public UserDaoFile() {
		File file = new File(fileLocation);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
				objUserOut.writeObject(userList);
				objUserOut.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException:");
				e.printStackTrace();
			}
		}
	}
	
	
	
	public User addUser(User user) {
		
//		if (getAllUsers() == null) {
//			userList.add(user);
//		} else {
		userList = getAllUsers();
		
		//Required for File Implementation
		user.setId(Math.abs(user.hashCode()));

		userList.add(user);
//		}
		

		try {
			objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objUserOut.writeObject(userList);
			objUserOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
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
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found:");
			e.printStackTrace();
		}
		
		return userList;
	}
	
	
	
	public User updateUser(User u) {

		userList = getAllUsers();
		
		int index = 0;
		
		for (User user : userList) {
			if (user.getId().equals(u.getId())) {
				break;
			}
			index++;
		}
		userList.set(index, u);

//		for (User user : userList) {
//			if (user.getId().equals(u.getId())) {
//				user.setUsername(u.getUsername());
//				user.setPassword(u.getPassword());
//				user.setFirstName(u.getFirstName());
//				user.setLastName(u.getLastName());
//				user.setUserType(u.getUserType());
//				user.setAccounts(u.getAccounts());

		try {
			objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objUserOut.writeObject(userList);
			System.out.println("\n<^><^><^><^> User Successfully Updated <^><^><^><^>");
			objUserOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
		} 
		
		return u;
	}

	
	
	public boolean removeUser(User u) {
		
		userList = getAllUsers();
		
		int index = 0;

		for (User user : userList) {
			if (user.getId().equals(u.getId())) {
				break;
			}
			index++;
		}
		userList.remove(index);
				
		try {
			objUserOut = new ObjectOutputStream(new FileOutputStream(fileLocation));
			objUserOut.writeObject(userList);
			System.out.println("\n<^><^><^><^> User Successfully Deleted <^><^><^><^>");
			objUserOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException:");
			e.printStackTrace();
		} 
			
		if(userList.contains(u)) {
            return false;
        }else {
            return true;
        }
			
	}

}
