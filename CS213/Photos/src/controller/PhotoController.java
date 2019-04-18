/**
 * PhotoController.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.PhotoModel;
import model.Tag;

/**
 * Controller class to manage functionality of import.fxml
 * 
 * @version Apr 12, 2019
 * @author Gemuele Aludino
 */
public class PhotoController {

	final double DEFAULT_INSET_VALUE = 10.0;
	final double DEFAULT_HEIGHT_VALUE = 150.0;
	final double DEFAULT_WIDTH_VALUE = 200.0;

	double imageInsetValue = DEFAULT_INSET_VALUE;
	double imageHeightValue = DEFAULT_HEIGHT_VALUE;
	double imageWidthValue = DEFAULT_WIDTH_VALUE;

	private Photo currentPhoto = null;
	private List<ImageView> currentImageViewList = null;

	FileChooser fileChooser;
	File file;

	ObservableList<Photo> photoList = FXCollections.observableArrayList();
	ObservableList<Photo> pList = FXCollections.observableArrayList();
	ObservableList<Tag> tList;

	PhotoModel model = LoginController.getModel();

	public static Photo currentSelectedPhoto;

	int index = 0;

	//@formatter:off
	@FXML Button buttonBrowse;
	@FXML Button buttonConfirmTag;
	@FXML Button buttonImportSelection;
	@FXML Button navigatorButtonBack;
	@FXML Button navigatorButtonNext;
	
	@FXML ImageView detailView;
	
	@FXML Label createdField;
	@FXML Label nameField;
	@FXML Label pathField;
	@FXML Label sizeField;
	
	@FXML ListView<Photo> imageQueueList;
	@FXML ListView<Tag> tagList;
	
	@FXML MenuItem fileReturnToAlbums;
	@FXML MenuItem fileOpenSelectedPhotoInViewer;
	@FXML MenuItem fileSaveAndLogout;
	@FXML MenuItem fileSaveAndExit;
	
	@FXML MenuItem viewAsThumbnails;
	@FXML MenuItem viewAsSingleImage;
	
	@FXML RadioButton radioButtonThisAlbum;
	@FXML RadioButton radioButtonSelectedAlbum;
	
	@FXML Slider zoomSlider;
	
	@FXML ChoiceBox<String> albumList;
	
	@FXML TextField captionField;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	
	@FXML Label infoData;
	
	@FXML TilePane tilePaneImages;
	//@formatter:on

	/**
	 * Opens a new window to examine a user-selected photo within a separate Viewer window
	 * 
	 * @throws IOException if viewer.fxml not found
	 */
	public void doOpenSelectedPhotoInViewer() throws IOException {
		PhotoController.currentSelectedPhoto = currentPhoto;
		UserController.currentSelectedPhoto = null;
		SearchController.currentSelectedPhoto = null;

		if (PhotoController.currentSelectedPhoto == null && UserController.currentSelectedPhoto == null
				&& SearchController.currentSelectedPhoto == null) {
			return;
		}

		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/viewer.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("Photos -- Viewer");
		window.setResizable(false);
		window.show();

	}

	/**
	 * Initializes the FXML document with information from previous FXML.
	 */
	@FXML
	public void initialize() {
		radioButtonThisAlbum.setText("Current album: " + model.getCurrentUser().getCurrentAlbum().getAlbumName());
		radioButtonThisAlbum.setSelected(true);
		updateInfoData();
		albumList.getItems().clear();
		
		for (Album a : model.getCurrentUser().getAlbumList()) {
			albumList.getItems().add(a.getAlbumName());
		}
	}

	/**
	 * Updates the bottom pane with information regarding the user model.
	 */
	public void updateInfoData() {
		int albumCount = model.getCurrentUser().getAlbumList().size();
		int totalPhotoCount = 0;
		long totalByteCount = 0;

		for (Album a : model.getCurrentUser().getAlbumMap().values()) {
			totalPhotoCount += a.getAlbumSize();

			for (Photo p : a.getPhotoMap().values()) {
				totalByteCount += p.getFileSize();
			}
		}

		String output = String.format("%d albums - %d photos - %d KB", albumCount, totalPhotoCount, totalByteCount);
		infoData.setText(output);
	}

	/**
	 * Deletes the photo from the list.
	 */
	public void deletePhoto() {
		if (currentPhoto != null) {
			int selectedIndex = index;

			if (selectedIndex < 0) {
				Alert error = new Alert(Alert.AlertType.ERROR, "There are no photos to be deleted.", ButtonType.OK);
				error.showAndWait();
				return;
			}

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this photo?",
					ButtonType.YES, ButtonType.NO);
			alert.showAndWait();

			if (alert.getResult() != ButtonType.YES) {
				return;
			}

			if (selectedIndex >= 0) {
				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Photo successfully removed!", ButtonType.OK);
				success.showAndWait();

				tilePaneImages.getChildren().remove(selectedIndex);
				imageQueueList.getItems().remove(selectedIndex);
				updateInfoData();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please select a photo to delete.", ButtonType.OK);
			error.showAndWait();
		}

	}

	/**
	 * Selects the album list from menuItems and sets it to the current album for
	 * the current user.
	 */
	public void doAlbumList() {
		albumList.setOnAction(event -> {
			Album test = new Album(albumList.getValue());
			for (Album a : model.getCurrentUser().getAlbumList()) {
				if (a.equals(test)) {
					model.getCurrentUser().setCurrentAlbum(test);
				}
			}
		});
	}

	/**
	 * Creates a tag for the current photo.
	 */
	public void createTag() {
		if (currentPhoto != null) {
			if (tagName.getText().isEmpty() == false && tagValue.getText().isEmpty() == false) {
				if (currentPhoto.addTag(tagName.getText().trim(), tagValue.getText().trim()) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Duplicate tag found. Tag not added!", ButtonType.OK);
					error.showAndWait();
				} else {
					tagName.setText(null);
					tagValue.setText(null);

					Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Tag successfully added!", ButtonType.OK);
					success.showAndWait();
				}
			} else {
				Alert error = new Alert(AlertType.ERROR, "Please provide a tag name and value.", ButtonType.OK);
				error.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please select a photo to create a tag for.", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Deletes the tag for the current user.
	 */
	public void deleteTag() {
		int selectedIndex = tagList.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no tags to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this tag?",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		if (selectedIndex >= 0) {
			currentPhoto.deleteTag(selectedIndex);

			Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Tag successfully removed!", ButtonType.OK);
			success.showAndWait();
		}
	}

	/**
	 * Changes the other radio button to false if one is currently selected.
	 */
	public void doRadioButtonSelectedAlbum() {
		if (radioButtonSelectedAlbum.isSelected()) {
			albumList.getSelectionModel().select(0);
			radioButtonThisAlbum.setSelected(false);
		}
	}

	/**
	 * Changes the other radio button to false if one is currently selected.
	 */
	public void doRadioButtonThisAlbum() {
		if (radioButtonThisAlbum.isSelected()) {
			radioButtonSelectedAlbum.setSelected(false);
			albumList.getSelectionModel().clearSelection();
		}
	}

	/**
	 * Adjusts the ImageViews based on value of zoom sized.
	 */
	public void doZoomSlider() {
		zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();

				imageInsetValue = factor * DEFAULT_INSET_VALUE; // default 10.00
				imageHeightValue = factor * DEFAULT_HEIGHT_VALUE; // default
																	// 150.00
				imageWidthValue = factor * DEFAULT_WIDTH_VALUE; // default
																// 200.00

				for (ImageView iv : currentImageViewList) {
					iv.setFitHeight(imageHeightValue);
					iv.setFitWidth(imageWidthValue);
					iv.setPreserveRatio(true);
				}
			}
		});
	}

	/**
	 * Executes upon activating navigator back button
	 */
	public void doNavigatorButtonBack() {
		if (currentPhoto != null) {
			ImageView old;
			ImageView iv;

			if (index == 0) {
				old = currentImageViewList.get(index);
				index = currentImageViewList.size() - 1;
				iv = currentImageViewList.get(index);
			} else {
				old = currentImageViewList.get(index);
				--index;
				iv = currentImageViewList.get(index);
			}

			// everything up to here is fine

			navigatorButtonBack.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					old.setStyle(null);

					if (iv.getBoundsInParent() != null) {
						iv.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					// everything up to here is fine

					currentPhoto = imageQueueList.getItems().get(index);
					// currentPhoto = pList.get(index);
					currentSelectedPhoto = currentPhoto;

					detailView.setImage(currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections.observableArrayList();

					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap().entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});
		} else {

		}

	}

	/**
	 * Executes upon activating navigator next button
	 */
	public void doNavigatorButtonNext() {
		if (currentPhoto != null) {
			ImageView old;
			ImageView iv;

			if (index == currentImageViewList.size() - 1) {
				old = currentImageViewList.get(index);
				index = 0;
				iv = currentImageViewList.get(index);
			} else {
				old = currentImageViewList.get(index);
				++index;
				iv = currentImageViewList.get(index);
			}

			navigatorButtonNext.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					currentPhoto = imageQueueList.getItems().get(index);
					// currentPhoto = photoList.get(index);
					// currentPhoto = pList.get(index);
					currentSelectedPhoto = currentPhoto;

					detailView.setImage(currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections.observableArrayList();

					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap().entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});
		}
	}

	/**
	 * Selects the file from the FileChooser and adds the Photo to the tilePane.
	 */
	public void selectFile() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
				new ExtensionFilter("All Files", "*.*"));

		file = fileChooser.showOpenDialog(null);

		if (file != null) {
			pList.add(new Photo(file));
		}

		imageQueueList.setItems(pList);

		tilePaneImages.getChildren().clear();
		tilePaneImages.setStyle("-fx-focus-color: transparent;");
		tilePaneImages.setPadding(new Insets(imageInsetValue, imageInsetValue, imageInsetValue, imageInsetValue));
		tilePaneImages.setHgap(imageInsetValue);

		currentImageViewList = new ArrayList<ImageView>();

		for (Photo p : imageQueueList.getItems()) {
			File test = new File(p.getFilepath());
			Image test2 = new Image(test.toURI().toString());
			ImageView iv = new ImageView(test2);

			p.setDataPhoto(test.lastModified());
			p.setSizePhoto(test.length());

			iv.setFitHeight(imageHeightValue);
			iv.setFitWidth(imageWidthValue);
			iv.setPreserveRatio(true);

			iv.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isSecondaryButtonDown()) {

				} else {
					if (iv.getBoundsInParent() != null) {
						iv.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					setSelectedIndex(tilePaneImages.getChildren().indexOf(iv));
					currentPhoto = p;
					currentSelectedPhoto = currentPhoto;

					detailView.setImage(currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText(currentPhoto.getFileSize() + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					tList = FXCollections.observableArrayList();

					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap().entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);
				}
			});

			tilePaneImages.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					iv.setStyle(null);

					nameField.setText("(No image selected)");
					pathField.setText("(No image selected)");
					sizeField.setText("(No image selected)");
					createdField.setText("(No image selected)");

					tagName.setText(null);
					tagValue.setText(null);
					tagList.setItems(null);
				}
			});

			tilePaneImages.getChildren().add(iv);
			currentImageViewList.add(iv);
			updateInfoData();
		}
	}

	/**
	 * This goes through the imageQueueList (ListView<User>) and adds the photos to
	 * the current album or selected album.
	 */
	public void addSelectedPhotos() {
		Album currentAlbum = model.getCurrentUser().getCurrentAlbum();
		currentAlbum.setPhotoList(photoList);

		if (currentAlbum != null) {
			for (int i = 0; i < imageQueueList.getItems().size(); i++) {

				Photo p = imageQueueList.getItems().get(i);

				if (currentAlbum.addPhoto(p) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Photo not added, duplicate photo.", ButtonType.OK);
					error.showAndWait();
				} else {
					Alert success = new Alert(AlertType.INFORMATION,
							"Photos successfully added to: " + currentAlbum.getAlbumName());
					success.showAndWait();
				}

				for (Tag t : p.getTagList()) {
					currentAlbum.getPhotoMap().get(p.getKey()).addTag(t.getTagName(), t.getTagValue());
				}

				imageQueueList.getItems().remove(p);
				tilePaneImages.getChildren().remove(i);
				i--;
			}
		}

		tagName.setText(null);
		tagValue.setText(null);

		if (!tagList.getItems().isEmpty()) {
			tList.clear();
		}
		model.write();
	}

	/**
	 * Executes upon selecting about section
	 * 
	 * @throws IOException if user.fxml not found
	 */
	public void doAlbum() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/user.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		imageQueueList.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos V1.0 - Welcome " + model.getCurrentUser().getUsername());
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon selecting about section
	 * 
	 * @throws IOException if about.fxml not found
	 */
	public void doAbout() throws IOException {
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

	/**
	 * Executes upon selecting help section
	 * 
	 * @throws IOException if help.fxml not found
	 */
	public void doHelp() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/help_import.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		window.initModality(Modality.NONE);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("Help");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon selecting logout section
	 * 
	 * @throws IOException if login.fxml not found
	 */
	public void doLogOut() throws IOException {
		model.write();
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (imageQueueList.getScene().getWindow());
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}

	/**
	 * Quits the application and calls exit() from the LoginController.
	 */
	public void doQuit() {
		LoginController.exit();
	}

	/**
	 * Sets the selected index
	 * 
	 * @param selectedIndex is the index
	 */
	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
	}

	/**
	 * Used for console message/testing functionality/method calls
	 * 
	 * @param message String that denotes a message for the console
	 */
	public void debugLog(String message) {
		System.out.println("[" + this.getClass().getSimpleName() + "] " + message);
	}
}
