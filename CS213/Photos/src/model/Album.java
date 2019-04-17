/**
 * Album.java
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents an album for a photo gallery application
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */

public class Album implements Comparable<Album>, Serializable {

	private static final long serialVersionUID = -6862414900620876387L;

	private String albumName;

	private long min;
	private long max;
	
	private transient ObservableList<Photo> photoList;
	private TreeMap<String, Photo> photoMap;

	/**
	 * Constructor which takes an album name and creates a new Album object.
	 * @param albumName: String containing name of Album
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		photoList = FXCollections.observableArrayList();
		photoMap = new TreeMap<String, Photo>();
	}

	/**
	 * Adds a Photo object to the TreeMap<String, Photo> in order.
	 * @param photo: Photo object
	 */
	public int addPhoto(Photo photo) {
		String photoKey = Photo.makeKey(photo.getFilepath());
		Photo temp = photoMap.get(photoKey);
		
		if(temp == null) {
			temp = new Photo(photo.getFilepath());
			photoMap.put(photoKey, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
	/**
	 * Inserts the Photo in a sorted way by utilizing the Photo compareTo().
	 * @param photo: an Photo object to compare with the rest of the Photos in the ObservableList<Photo>.
	 * @return the index of where it was placed in the ObservableList<Photo>.
	 */
	private int indexInsertedSorted(Photo photo) {
		if (photoList.isEmpty()) {
			photoList.add(photo);
			return 0;
		} else {
			for (int i = 0; i < photoList.size(); i++) {
				if (photo.compareTo(photoList.get(i)) < 0) {
					photoList.add(i, photo);
					return i;
				}
			}
			photoList.add(photo);
			return photoList.size() - 1;
		}
	}
	
	/**
	 * Deletes the Photo object from the ObservableList<Photo> and TreeMap<String, Photo>.
	 * @param index: index of where Photo is in List/Map derived from UserController.
	 * @return true | false if Photo was deleted.
	 */
	public boolean deletePhoto(int index) {
		String key = photoList.get(index).getKey();
		Photo temp = photoMap.get(key);
		
		if(temp != null) {
			photoMap.remove(key);
			photoList.remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines the earliest and latest photos within an album.
	 */
    @SuppressWarnings("unused")
	public void setCounterDatetime() {
		boolean start = true;
		int count = 0;
		long min = 0;
		long max = 0;
		for (Map.Entry<String, Photo> p: photoMap.entrySet()) {
			if (start) {
				count = 1;
				min	= p.getValue().getModifiedDate();
				max	= p.getValue().getModifiedDate();
				start = false;
			} else {
				count++;
				long pd = p.getValue().getModifiedDate();
				if (pd > max) {
					max = pd;
				}
				if (pd < min) {
					min = pd;
				}
			}
		}
		this.min = min;
		this.max = max;
	}
	
    /**
     * Converts long gathered from date modified() and converts it into a readable format.
     * @param time: long objected extracted from File.dateModified()
     * @return LocalDateTime object in format: YYYY-MM-DD
     */
    public static String epochToLocalTime(long time) {
        LocalDateTime datetime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return datetime.format(formatter);
    }
    
	/**
	 * 
	 * @param albumName
	 * @return
	 */
	public static String makeKey(String albumName) {
		return albumName.toLowerCase();
	}
	
	/**
	 * Gets the album name of the current Album object.
	 * @return String containing album name.
	 */
	public String getAlbumName() {
		return albumName;
	}
	
	/**
	 * Generates the key for the current Album object.
	 * @return String of album name lowercased to ensure constraints.
	 */
	public String getKey() {
		return makeKey(albumName);
	}
	
	/**
	 * Returns the index of where the Photo is located in the ObservableList<Photo>
	 * @param photo: Photo object
	 * @return index of Photo in ObservableList<Photo>.
	 */
	public int getSelectedPhoto(Photo photo) {
		return photoList.indexOf(photo);
	}

	/**
	 * Gets the Album size by grabbing size() from TreeMap<String, Photo>
	 * @return size of TreeMap<String, Photo>
	 */
	public int getAlbumSize() {
		return photoMap.size();
	}
	
	/**
	 * Gets the minimum date range of the current Album
	 * @return min: long
	 */
	public long getMin() { 
        return min;
    }

	/**
	 * Gets the maximum date range of the current Album
	 * @return max: long
	 */
    public long getMax() {
        return max;
    }

	/**
	 * Gets the ObservableList<Photo> from the Album object.
	 * @return ObservableList<Photo>
	 */
	public ObservableList<Photo> getPhotoList() {
		return photoList;
	}
	
	/**
	 * Gets the TreeMap<String, Photo> of the Album object.
	 * @return TreeMap<String, Photo>
	 */
	public TreeMap<String, Photo> getPhotoMap() {
		return photoMap;
	}

	/**
	 * Sets the PhotoList to a new ObservableList<Photo>
	 * @param photoList: ObservableList<Photo>
	 */
	public void setPhotoList(ObservableList<Photo> photoList) {
		this.photoList = photoList;
	}

	/**
	 * Sets the Album name to the new Album name.
	 * @param albumName: String containing new Album name.
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	
	@Override
	public int compareTo(Album other) {
		return this.albumName.compareToIgnoreCase(other.albumName);
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Album)) {
			return false;
		}	
		Album other = (Album) o;
		return (this.albumName.equalsIgnoreCase(other.albumName));
	}
	
	public String toString() {
		return "Album name: " + albumName + "\nPhoto count: " + photoMap.size() + "\nRanges from: " + epochToLocalTime(min) + " to " + epochToLocalTime(max);
	}

}
