/**
 * SongLibraryModel.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongLibraryModel {

	private final String FILE_LOC = "data/song_list.txt";

	private ObservableList<Song> itemsInList;
	private TreeMap<String, Song> songMap;

	public SongLibraryModel() {
		itemsInList = FXCollections.observableArrayList();
		songMap = new TreeMap<String, Song>();
		readFromFile();
	}

	public int add(String title, String artist, String album, String year) {
		String key = Song.makeKey(title, artist);
		Song temp = songMap.get(key);

		if (temp == null) {
			temp = new Song(title, artist, album, year);
			songMap.put(key, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}

	private int indexInsertedSorted(Song input) {
		if (itemsInList.isEmpty()) {
			itemsInList.add(input);
			return 0;
		} else {
			for (int i = 0; i < itemsInList.size(); i++) {
				if (input.compareTo(itemsInList.get(i)) < 0) {
					itemsInList.add(i, input);
					return i;
				}
			}

			itemsInList.add(input);
			return itemsInList.size() - 1;
		}
	}

	public int edit(int index, String title, String artist, String album, String year) {
		String oldK = itemsInList.get(index).getKey();
		String newK = Song.makeKey(title, artist);

		Song oldS = songMap.get(oldK);

		if (oldS == null) {
			return -1;
		} else {
			if (oldK.equals(newK)) {
				oldS.setAlbum(album);
				oldS.setYear(year);
			} else {
				Song newS = songMap.get(newK);

				if (newS == null) {
					oldS.setTitle(title);
					oldS.setArtist(artist);
					oldS.setAlbum(album);
					oldS.setYear(year);

					songMap.remove(oldK);
					songMap.put(newK, oldS);

					itemsInList.remove(index);
					return indexInsertedSorted(oldS);
				} else {
					return -1;
				}
			}
		}
		return 0;
	}

	public boolean delete(int index) {
		String key = itemsInList.get(index).getKey();
		Song temp = songMap.get(key);

		if (temp != null) {
			songMap.remove(key);
			itemsInList.remove(index);
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Song> getItems() {
		return itemsInList;
	}

	public int getItemCount() {
		return itemsInList.size();
	}

	public int getIndex(Song song) {
		return itemsInList.indexOf(song);
	}

	public void writeToFile() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_LOC));

		writer.write("[SONG_LIST]");
		writer.newLine();
		writer.newLine();

		for (Song song : songMap.values()) {
			writer.write("\t[SONG_ELE]");
			writer.newLine();

			writer.write("\t\tTitle: " + song.getTitle());
			writer.newLine();
			writer.write("\t\tArtist: " + song.getArtist());
			writer.newLine();
			writer.write("\t\tAlbum: " + song.getAlbum());
			writer.newLine();
			writer.write("\t\tYear: " + song.getYear());
			writer.newLine();

			writer.write("\t[\\SONG_ELE]");
			writer.newLine();
			writer.newLine();
		}
		writer.write("[/SONG_LIST]");
		writer.newLine();
		writer.flush();
		writer.close();
	}

	public void readFromFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE_LOC));

			String line = reader.readLine();

			if (line.equals("[SONG_LIST]")) {
				while ((line = reader.readLine()) != null) {
					if (line.equals("\t[SONG_ELE]")) {
						String title = reader.readLine();
						String parseTitle[] = title.split(": ");

						String artist = reader.readLine();
						String parseArtist[] = artist.split(": ");

						String album = reader.readLine();
						String parseAlbum[] = album.split(": ");

						String year = reader.readLine();
						String parseYear[] = year.split(": ");

						if (parseAlbum.length == 1 && parseYear.length == 2) {
							add(parseTitle[1], parseArtist[1], "", parseYear[1]);
						} else if (parseAlbum.length == 2 && parseYear.length == 1) {
							add(parseTitle[1], parseArtist[1], parseAlbum[1], "");
						} else if (parseAlbum.length == 1 && parseYear.length == 1) {
							add(parseTitle[1], parseArtist[1], "", "");
						} else {
							add(parseTitle[1], parseArtist[1], parseAlbum[1], parseYear[1]);
						}

					} else if (line.equals("[/SONG_LIST]")) {
						return;
					}
				}
			} else {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
