/**
 * Song.java
 *
 * Copyright © 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model;

public class Song {
	
	private String title;
	private String artist;
	private String album;
	private String year;
	
	public Song() {
		this("", "", "", "");
	}
	
	public Song(String title, String artist, String album, String year) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	public Song(Song other) {
		this(other.title, other.artist, other.album, other.year);
	}
	
	public static String makeKey(String title, String artist) {
		return (title + artist).toLowerCase();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getKey() {
		return makeKey(title, artist);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public void setAlbum(String album) {
		this.album = album;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String toString() {
		return title + "\t\t\t" + artist + "\t\t\t" + album + "\t\t\t" + year + "\n";
	}
	
	/**
	 * We can modify this later (???) not sure if we really need this functionality at the moment.
	 */
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Song)) {
			return false;
		}
		Song other = (Song) o;
		
		return this.title.equals(other.title);
	}
	
	public int compareTo(Song other) {
		return this.getKey().compareTo(other.getKey());
	}

}
