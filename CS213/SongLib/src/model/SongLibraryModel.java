/**
 * SongLibraryModel.java
 *
 * Copyright © 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SongLibraryModel {
	
	private final String FILE_LOC = "song_list.txt";
	
	ObservableList<Song> itemsInList;
	TreeMap<String, Song> songMap;

	public SongLibraryModel() {
		itemsInList = FXCollections.observableArrayList();
		songMap = new TreeMap<String, Song>();
		readFromFile();
	}
	
	public int add(String title, String artist, String album, String year) {
		String key = Song.makeKey(title, artist);
		Song temp = songMap.get(key);
		
		if(temp == null) {
			temp = new Song(title, artist, album, year);
			songMap.put(key, temp);
			return indexInsertedSorted(temp);
		} else {
			return -1;
		}
	}
	
    private int indexInsertedSorted (Song input) {
        if (itemsInList.isEmpty()) {
            itemsInList.add(input);
            return 0;
        }
        else {
            for (int i=0; i < itemsInList.size(); i++) {
                if (input.compareTo(itemsInList.get(i)) < 0) {
                    itemsInList.add(i, input);
                    return i;
                }
            }
            itemsInList.add(input);
            return itemsInList.size() - 1;
        }
    }
    
    public void edit() {
    	
    }

	
	public boolean delete(int index) {
		String key = itemsInList.get(index).getKey();
		Song temp = songMap.get(key);
		
		if(temp != null) {
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
	
	public void writeToFile() {
		try {
			Writer writer = new FileWriter(FILE_LOC);
			
			for(Song song : songMap.values()) {
				writer.write(song.toString());
			}
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFromFile() {
		try {
			String line;
			
			FileReader fileReader = new FileReader(FILE_LOC);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				String[] parse = line.split("\\t\t\t");
				add(parse[0], parse[1], parse[2], parse[3]);
			}
			
			bufferedReader.close();
			fileReader.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
