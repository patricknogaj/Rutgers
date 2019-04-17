/**
 * User.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.Serializable;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an end-user for a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class User implements Comparable<User>, Serializable {

	private static final long serialVersionUID = 8689422424799836920L;

	private String username;
	private String password;

	private transient ObservableList<Album> albumList;
	private TreeMap<String, Album> albumMap;

	private Album currentAlbum;

	/**
	 * Default User constructor
	 * @param username -- username of User utilized for login.
	 * @param password -- password of User utilized for login.
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		albumList = FXCollections.observableArrayList();
		albumMap = new TreeMap<String, Album>();
		currentAlbum = null;
	}

	/**
	 * Method to add an album.
	 * @param albumName : String object to check to see if Album currently exists
	 * @return index of where it was placed in the ObservableList<Album>.
	 */
	public int addAlbum(String albumName) {
		String albumKey = Album.makeKey(albumName);
		Album temp = albumMap.get(albumKey);

		if (temp == null) {
			temp = new Album(albumName);
			albumMap.put(albumKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	/**
	 * Method to delete an album.
	 * @param index : index is gathered from the UserController and specifies the index of the Album to delete.
	 * @return true || false depending on action.
	 */
	public boolean deleteAlbum(int index) {
		String key = albumList.get(index).getKey();
		Album temp = albumMap.get(key);

		if (temp != null) {
			albumMap.remove(key);
			albumList.remove(index);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Inserts the album in a sorted way by utilizing the Album compareTo().
	 * @param album : an Album object to compare with the rest of the Albums in the ObservableList<Album>.
	 * @return the index of where it was placed in the ObservableList<Album>.
	 */
	private int indexInsertedSorted(Album album) {
		if (albumList.isEmpty()) {
			albumList.add(album);
			return 0;
		} else {
			for (int i = 0; i < albumList.size(); i++) {
				if (album.compareTo(albumList.get(i)) < 0) {
					albumList.add(i, album);
					return i;
				}
			}
			albumList.add(album);
			return albumList.size() - 1;
		}
	}

	/**
	 * Edits the album name by utilizing keys of the current album, and prospective album.
	 * @param index : index of location in the album list gathered from UserController.java
	 * @param albumName : String containing new album name.
	 * @return index of where it was placed in the ObservableList<Album> or -1 if not added.
	 */
	public int edit(int index, String albumName) {
		String oldKey = albumList.get(index).getKey();
		String newKey = Album.makeKey(albumName);

		Album oldAlbum = albumMap.get(oldKey);

		if (oldAlbum == null || oldKey.equals(newKey)) {
			return -1;
		} else {
			Album newAlbum = albumMap.get(newKey);

			if (newAlbum == null) {
				oldAlbum.setAlbumName(albumName);

				albumMap.remove(oldKey);
				albumMap.put(newKey, oldAlbum);

				albumList.remove(index);
				return indexInsertedSorted(oldAlbum);
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Generates the key for a specific User.
	 * @param username : String from the User regarding the username
	 * @return username which is all lowercased to ensure all keys are compared with the same constraints.
	 */
	public static String makeKey(String username) {
		return username.toLowerCase();
	}
	
	/**
	 * Generates a key for a user utilizing the username to differentiate users.
	 * @return a String of the username which is lowercased.
	 */
	public String getKey() {
		return makeKey(username);
	}

	/**
	 * Returns the username.
	 * @return a String object with the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the password.
	 * @return a String object with the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns the albumList for a specific user
	 * @return an ObservableList<Album> object which contains the users albums.
	 */
	public ObservableList<Album> getAlbumList() {
		return albumList;
	}

	/**
	 * Return the current album object.
	 * @return an Album object
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	/**
	 * Returns the TreeMap<String, Album> of the current user which is utilized to save data.
	 * @return an TreeMap<String, Album> object. String is the Album key, and the value is the actual Album object.
	 */
	public TreeMap<String, Album> getAlbumMap() {
		return albumMap;
	}

	/**
	 * Sets the ObservableList<Album> to the parameter.
	 * @param albumList an ObservableList<Album> object to be passed and set.
	 */
	public void setAlbumList(ObservableList<Album> albumList) {
		this.albumList = albumList;
	}

	/**
	 * Sets the current Album object to the parameter.
	 * @param currentAlbum : Album object to set to current.
	 */
	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}
	
	/**
	 * Sets the current Album object to the parameter.
	 * @param currentAlbum : String to create an album, and check in the TreeMap<String, Album> for that value.
	 */
	public void setCurrentAlbum(String currentAlbum) {
		String key = Album.makeKey(currentAlbum);
		Album temp = albumMap.get(key);
		
		if(temp != null) {
			this.currentAlbum = albumMap.get(key);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof User)) {
			return false;
		}

		User other = (User) o;
		return this.username.equalsIgnoreCase(other.username);
	}
	
	@Override
	public int compareTo(User user) {
		return this.username.compareToIgnoreCase(user.username);
	}
	
	@Override
	public String toString() {
		return "(username) " + username + " " + "(password) " + password;
	}

}
