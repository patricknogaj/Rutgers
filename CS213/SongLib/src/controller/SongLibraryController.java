/**
 * SongLibraryController.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Song;
import model.SongLibraryModel;

public class SongLibraryController implements ChangeListener<Song> {

	private SongLibraryModel model = new SongLibraryModel();
	private int selectedIndex;

	@FXML ListView<Song> listView;
	
	@FXML AnchorPane x;
	
	@FXML Menu help;
	
	@FXML MenuItem mainMenu;
	@FXML MenuItem exit;
	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML Button cancel;
	@FXML Button clear;
	@FXML Button addSong;
	@FXML Button save;
	
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

		if (model.getItemCount() > 0) {
			listView.getSelectionModel().select(0);
		}
	}
	
	public void help() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/song_lib_help.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		window.initModality(Modality.NONE);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("Help");
		
		window.setX(x.getScaleX() + 1000);
		
		window.setResizable(false);
		window.show();
	}
	
	public void about() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/about.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		window.initModality(Modality.NONE);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("About");
		window.setResizable(false);
		
		window.show();
	}

	public void doAdd() throws IOException {
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(getClass().getResource("/view/song_lib_add.fxml"));
		loader.setController(this);

		Parent root = loader.load();
		add.getScene().setRoot(root);
	}

	public void doMainMenu() throws IOException {
		FXMLLoader loader = new FXMLLoader();

		loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
		loader.setController(this);

		Parent root = loader.load();
		cancel.getScene().setRoot(root);
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
	
	public int getSelectedIndex(Song song) {
		return model.getIndex(song);
	}

	public void doCancel() throws IOException {
		/*
		 * final boolean ADD_FIELDS_EMPTY = add_title.getText().isEmpty() &&
		 * add_artist.getText().isEmpty() && add_album.getText().isEmpty() &&
		 * add_year.getText().isEmpty(); final boolean EDIT_FIELDS_EMPTY =
		 * edit_title.getText().isEmpty() && edit_artist.getText().isEmpty() &&
		 * edit_album.getText().isEmpty() && edit_year.getText().isEmpty();
		 */
		/*
		 * Only ADD_FIELDS EMPTY or EDIT_FIELDS_EMPTY seems to work here, not both.
		 * Without making any major design revisions, we will have to choose as to
		 * whether the "automatic cancel" (without an alert) will occur during add mode
		 * or edit mode. Because -- why should the user be notified if they want to quit
		 * if the fields are empty? Though, they are more likely to be "empty" during
		 * add, as opposed to edit. if (ADD_FIELDS_EMPTY || EDIT_FIELDS_EMPTY) {
		 * FXMLLoader loader = new FXMLLoader();
		 * loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
		 * loader.setController(this); Parent root = loader.load();
		 * cancel.getScene().setRoot(root); return; }
		 */

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to cancel?", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
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
		if (edit_title.getText().isEmpty() && edit_artist.getText().isEmpty() && edit_album.getText().isEmpty()
				&& edit_year.getText().isEmpty()) {
			return;
		}

		Alert alert = new Alert(Alert.AlertType.WARNING,
				"Are you sure you want to proceed?\nThis will clear all information.\n(Song title, artist name, album title, and year)",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		} else {
			edit_title.setText("");
			edit_artist.setText("");
			edit_album.setText("");
			edit_year.setText("");
		}
	}

	public void addSong() throws IOException {
		if (add_title.getText().isEmpty() == false && add_artist.getText().isEmpty() == false) {

			Song temp = new Song(add_title.getText(), add_artist.getText(), add_album.getText(), add_year.getText());

			if (model.add(add_title.getText(), add_artist.getText(), add_album.getText(), add_year.getText()) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate song found. Song not added.",
						ButtonType.OK);
				duplicate.showAndWait();
			} else {
				Alert addSuccessful = new Alert(Alert.AlertType.CONFIRMATION, "New song added successfully.",
						ButtonType.OK);
				addSuccessful.showAndWait();

				selectedIndex = model.getIndex(temp);

				FXMLLoader loader = new FXMLLoader();

				loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
				loader.setController(this);

				Parent root = loader.load();
				cancel.getScene().setRoot(root);

				listView.getSelectionModel().select(selectedIndex);
			}
		} else {
			Alert error = new Alert(Alert.AlertType.ERROR, "Please provide at least a title and an artist name.",
					ButtonType.OK);
			error.showAndWait();
		}
	}

	public void doAddClear() {
		if (add_title.getText().isEmpty() && add_artist.getText().isEmpty() && add_album.getText().isEmpty()
				&& add_year.getText().isEmpty()) {
			return;
		}

		Alert alert = new Alert(Alert.AlertType.WARNING,
				"Are you sure you want to proceed? This will clear all information.", ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		} else {
			add_title.setText("");
			add_artist.setText("");
			add_album.setText("");
			add_year.setText("");
		}
	}

	public void doSave() throws IOException {
		if (edit_title.getText().isEmpty() == true || edit_artist.getText().isEmpty() == true) {
			if (edit_title.getText().isEmpty() == true && edit_artist.getText().isEmpty() == false) {
				Alert errorTitle = new Alert(Alert.AlertType.ERROR, "Title field is blank.\nPlease provide a title.",
						ButtonType.OK);
				errorTitle.showAndWait();
			} else if (edit_title.getText().isEmpty() == false && edit_artist.getText().isEmpty() == true) {
				Alert errorArtist = new Alert(Alert.AlertType.ERROR,
						"Artist field is blank.\nPlease provide an artist name.", ButtonType.OK);
				errorArtist.showAndWait();
			} else {
				Alert errorBoth = new Alert(Alert.AlertType.ERROR,
						"Please provide at least a title and an artist name.", ButtonType.OK);
				errorBoth.showAndWait();
			}
		} else {
			Song temp = new Song(edit_title_display.getText(), edit_artist_display.getText(), edit_album_display.getText(), edit_year_display.getText());
			
			if (model.edit(getSelectedIndex(temp), edit_title.getText(), edit_artist.getText(), edit_album.getText(),
					edit_year.getText()) != -1) {
				edit_title_display.setText(edit_title.getText());
				edit_artist_display.setText(edit_artist.getText());
				edit_album_display.setText(edit_album.getText());
				edit_year_display.setText(edit_year.getText());
					

				Alert save = new Alert(Alert.AlertType.CONFIRMATION, "Song information saved.", ButtonType.OK);
				save.showAndWait();

				FXMLLoader loader = new FXMLLoader();

				loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
				loader.setController(this);

				Parent root = loader.load();
				cancel.getScene().setRoot(root);
			}
		}
	}

	public void doDelete() {
		selectedIndex = listView.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no songs to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this song?", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		if (selectedIndex >= 0) {
			model.delete(selectedIndex);

			if (selectedIndex <= model.getItemCount() - 1) {
				listView.getSelectionModel().select(selectedIndex);
			}

		}
	}

	public void exit() throws IOException {
		model.writeToFile();
		Platform.exit();
	}

	@Override
	public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
		if (newValue != null) {
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