/**
 * SongLibraryController.java
 *
 * Copyright © 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package controller;

import java.io.IOException;

import application.SongLib;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Song;
import model.SongLibraryModel;

public class SongLibraryController implements ChangeListener<Song> {

	SongLibraryModel model = new SongLibraryModel();
	
	@FXML ListView<Song> listView;
	
	@FXML MenuItem exit;
	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML Button cancel;
	@FXML Button clear;
	@FXML Button addSong;
	
	@FXML Text title_display;
	@FXML Text artist_display;
	@FXML Text album_display;
	@FXML Text year_display;
	@FXML Text edit_title_display;
	@FXML Text edit_artist_display;
	@FXML Text edit_album_display;
	@FXML Text edit_year_display;
	
	@FXML TextField edit_title;
	@FXML TextField edit_artist;
	@FXML TextField edit_album;
	@FXML TextField edit_year;
	@FXML TextField add_title;
	@FXML TextField add_artist;
	@FXML TextField add_album;
	@FXML TextField add_year;
	
	@FXML
	public void initialize() {
		
		listView.setItems(model.getItems());
		
		listView.getSelectionModel().selectedItemProperty().addListener(this);
		
		if(model.getItemCount() > 0 ) {
			listView.getSelectionModel().select(0);
		}
	}
	
	public void doAdd() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/song_lib_add.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		add.getScene().setRoot(root);
	}
	
	public void doEdit() throws IOException {
		Song temp = listView.getSelectionModel().getSelectedItem();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/song_lib_edit.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		edit.getScene().setRoot(root);
		
		edit_title_display.setText(temp.getTitle());
		edit_artist_display.setText(temp.getArtist());
		edit_album_display.setText(temp.getAlbum());
		edit_year_display.setText(temp.getYear());
		
		edit_title.setText(temp.getTitle());
		edit_artist.setText(temp.getArtist());
		edit_album.setText(temp.getAlbum());
		edit_year.setText(temp.getYear());
	}
	
	public void doCancel() throws IOException {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES) {
			return;
		} else {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			cancel.getScene().setRoot(root);
		}
	}
	
	public void doEditClear() {
		Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to proceed? This will clear all information.", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
			
		if(alert.getResult() != ButtonType.YES) {
			return;
		} else {
			edit_title.setText("");
			edit_artist.setText("");
			edit_album.setText("");
			edit_year.setText("");
		}
	}
	
	public void addSong() {
		if (add_title.getText().isEmpty() == false && add_artist.getText().isEmpty() == false && add_album.getText().isEmpty() == false && add_year.getText().isEmpty() == false) { 
			if(model.add(add_title.getText(), add_artist.getText(), add_album.getText(), add_year.getText()) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate Song -- Song not added.", ButtonType.OK);
				duplicate.showAndWait();
			} else {
				Alert addSuccessful = new Alert(Alert.AlertType.CONFIRMATION, "Song added successfully", ButtonType.OK);
				addSuccessful.showAndWait();
			}
		} else {
			Alert error = new Alert(Alert.AlertType.ERROR, "One of the fields is empty.", ButtonType.OK);
			error.showAndWait();
		}
	}
	
	public void doAddClear() {
		Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to proceed? This will clear all information.", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES) {
			return;
		} else {
			add_title.setText("");
			add_artist.setText("");
			add_album.setText("");
			add_year.setText("");
		}
	}
	
	public void doDelete() {
		int index = listView.getSelectionModel().getSelectedIndex();
		
		if(index < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no songs to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to delete this song?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();
		
		if(alert.getResult() != ButtonType.YES) {
			return;
		}
		
		if(index >= 0) {
			model.delete(index);
		} 
	}
	
	public void exit() {
		SongLib app = new SongLib();
		app.stop();
	}

	@Override
	public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
		if(newValue != null) {
			title_display.setText(newValue.getTitle());
			artist_display.setText(newValue.getArtist());
			album_display.setText(newValue.getAlbum());
			year_display.setText(newValue.getYear());
		} else {
			title_display.setText("");
			artist_display.setText("");
			album_display.setText("");
			year_display.setText("");
		}
	}
	
}
