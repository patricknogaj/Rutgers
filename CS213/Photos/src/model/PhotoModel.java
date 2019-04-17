/**
 * PhotoModel.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an ADT composed of user data for a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */

public class PhotoModel implements Serializable {

	private static final long serialVersionUID = -450152308719125499L;

	public final String DAT_FILE_PATH = "photos.dat";

	private ObservableList<User> userList;
	private TreeMap<String, User> userMap;
	private User currentUser;

	/**
	 * Default constructor which reads data from the DAT file and loads all the user data.
	 */
	public PhotoModel() {
		userList = read();
		userMap = new TreeMap<String, User>();
		currentUser = null;

		for (User u : userList) {
			userMap.put(u.getKey(), u);
		}
	}
	
	/**
	 * Adds a User to the TreeMap<String, User> in a sorted order.
	 * @param username: String of User containing username.
	 * @param password: String of User containing password.
	 * @return index of location in ObservableList<User> of current User object.
	 */
	public int addUser(String username, String password) {
		String key = User.makeKey(username);
		User temp = userMap.get(key);

		if (temp == null) {
			temp = new User(username, password);
			
			userMap.put(key, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}

	/**
	 * Finds the index that the user will be input into the ObservableList<User> using compareTo from User.
	 * @param user: User object to add into ObservableList<User>.
	 * @return index location of where User was put in ObservableList<User>.
	 */
	private int indexInsertedSorted(User user) {
		if (userList.isEmpty()) {
			userList.add(user);		
			return 0;
		} else {
			for (int i = 0; i < userList.size(); i++) {
				if (user.compareTo(userList.get(i)) < 0) {
					userList.add(i, user);
					return i;
				}
			}
			userList.add(user);
			return userList.size() - 1;
		}
	}

	/**
	 * Removes User from ObservableList<User> and TreeMap<String, User> via index
	 * @param index: index location of User in ObservableList<User> and TreeMap<String, User> derived from UserController
	 * @return true | false if user was deleted or not.
	 */
	public boolean deleteUser(int index) {
		String key = userList.get(index).getKey();
		User temp = userMap.get(key);

		if (temp != null) {
			userMap.remove(key);
			userList.remove(index);	
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Reads the file from DAT_FILE_PATH to deserialize and load data.
	 * @return an observable list that has all the old data, or a new list with
	 *         admin/stock preloaded.
	 */
	@SuppressWarnings("resource")
	public ObservableList<User> read() {
		try {
			FileInputStream fIn = new FileInputStream(DAT_FILE_PATH);
			ObjectInputStream in = new ObjectInputStream(fIn);
			@SuppressWarnings("unchecked")
			List<User> readList = (List<User>) in.readObject();
			return FXCollections.observableArrayList(readList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException i) {
			i.printStackTrace();
		}

		User admin = new User("admin", "admin");
		User stock = new User("stock", "stock");

		stock.addAlbum("test");
		stock.setCurrentAlbum(stock.getAlbumList().get(0));

		Photo photo1 = new Photo(new File("src/view/stock/one.jpg"));
		Photo photo2 = new Photo(new File("src/view/stock/two.jpg"));
		Photo photo3 = new Photo(new File("src/view/stock/three.jpg"));
		Photo photo4 = new Photo(new File("src/view/stock/four.jpg"));
		
		stock.getCurrentAlbum().addPhoto(photo1);
		stock.getCurrentAlbum().addPhoto(photo2);
		stock.getCurrentAlbum().addPhoto(photo3);
		stock.getCurrentAlbum().addPhoto(photo4);
		
		return FXCollections.observableArrayList(admin, stock);
	}

	/**
	 * Creates a serialized file with an ArrayList of all User objects.
	 * [ObservableList is not serializable]
	 */
	public void write() {
		try {
			FileOutputStream fOut = new FileOutputStream(DAT_FILE_PATH);
			ObjectOutputStream out = new ObjectOutputStream(fOut);
			out.writeObject(new ArrayList<User>(userList));
			System.out.println("Saving..." + this);
			out.close();
			fOut.close();
		} catch (IOException e) {
			e.getMessage();
		}
	}
	
	/**
	 * Used for console message/testing functionality/method calls 
	 * @param message String that denotes a message for the console
	 */
	public void debugLog(String message) {
		System.out.println(
				"[" + this.getClass().getSimpleName() + "] " + message);
	}

	/**
	 * Gets the ObservableList<User> userList.
	 * @return ObservableList<User>.
	 */
	public ObservableList<User> getUserList() {
		return userList;
	}

	/**
	 * Gets the TreeMap<String, User> containing all Users.
	 * @return TreeMap<String, User>.
	 */
	public TreeMap<String, User> getUserMap() {
		return userMap;
	}

	/**
	 * Gets the amount of users within the program.
	 * @return size of ObservableList<User>.
	 */
	public int getItemCount() {
		return userList.size();
	}
	
	/**
	 * Gets the index of a User object within the ObservableList<User>.
	 * @param user: User object
	 * @return index of User within ObservableList<User>.
	 */
	public int getIndex(User user) {
		return userList.indexOf(user);
	}

	/**
	 * Gets the current User object.
	 * @return User object.
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * Gets the User object from a given username.
	 * @param username: String containing the username of a possible User object.
	 * @return User object if true, null if false.
	 */
	public User getUser(String username) {
		return userList.stream()
				.filter(user -> user.getUsername().equalsIgnoreCase(username))
				.findFirst().orElse(null);
	}

	/**
	 * Sets the current user Object to currentUser.
	 * @param currentUser: User object
	 */
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Sets the ObservableList<User> to userList.
	 * @param userList: ObservableList<User>
	 */
	public void setUserList(ObservableList<User> userList) {
		this.userList = userList;
	}
	
	public String toString() {
		return "User: " ;
	}
}
