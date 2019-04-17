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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.control.SplitMenuButton;
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
	
	int index = 0;
	
	final double DEFAULT_INSET_VALUE = 10.0;
	final double DEFAULT_HEIGHT_VALUE = 150.0;
	final double DEFAULT_WIDTH_VALUE = 200.0;
		
	double imageInsetValue = DEFAULT_INSET_VALUE;
	double imageHeightValue = DEFAULT_HEIGHT_VALUE;
	double imageWidthValue = DEFAULT_WIDTH_VALUE;
	
	FileChooser fileChooser;
	File file;
	
	private Photo currentPhoto = null;
	private Album currentAlbum = null;
	private List<ImageView> currentImageViewList = null;
	
	ObservableList<Photo> photoList = FXCollections.observableArrayList();
	ObservableList<Photo> pList = FXCollections.observableArrayList();
	ObservableList<Tag> tList;
	
	public PhotoModel model = LoginController.getModel();
	
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
	@FXML RadioButton radioButtonThumbnail;
	@FXML RadioButton radioButtonSimpleImage;
	
	@FXML Slider zoomSlider;
	
	@FXML ChoiceBox<String> albumList;
	
	@FXML TextField captionField;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	
	@FXML Label infoData;
	
	@FXML TilePane tilePaneImages;
	
	//@formatter:on
	
	@FXML
	public void initialize() {
		radioButtonThisAlbum.setText("Current album: " + model.getCurrentUser().getCurrentAlbum().getAlbumName());
		radioButtonThisAlbum.setSelected(true);
		updateInfoData();
		albumList.getItems().clear();
		for(Album a : model.getCurrentUser().getAlbumList()) {
			albumList.getItems().add(a.getAlbumName());
		}
		
		/**
		 * CONSOLE DIAGNOSTICS
		 */
		System.out.println();
		debugLog("Entering " + getClass().getSimpleName());
	}
	
	public void updateInfoData() {
		int albumCount = model.getCurrentUser().getAlbumList().size();
		int totalPhotoCount = 0;
		long totalByteCount = 0;

		for (Album a : model.getCurrentUser().getAlbumMap().values()) {
			totalPhotoCount += a.getAlbumSize();

			for (Photo p : a.getPhotoMap().values()) {
				totalByteCount += p.getFileSize();
				debugLog("" + totalByteCount);
			}
		}

		String output = String.format("%d albums - %d photos - %d KB",
				albumCount, totalPhotoCount, totalByteCount);

		infoData.setText(output);
	}
	
	public void doViewModeThumbnail() {
		
	}
	
	public void doViewModeSingleImage() {
		
	}
	
	public void doOpenSelectedPhotoInViewer() {
		
	}
	
	public void doImageQueueList() {
		
	}
	
	public void doTagList() {
		
	}
	
	public void deletePhoto() {
		if (currentPhoto != null) {
			int selectedIndex = index;

			if (selectedIndex < 0) {
				Alert error = new Alert(Alert.AlertType.ERROR,
						"There are no photos to be deleted.", ButtonType.OK);

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("There are no photos to be deleted.");

				error.showAndWait();
				return;
			}

			Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
					"Are you sure you want to delete this photo?",
					ButtonType.YES, ButtonType.NO);

			alert.showAndWait();

			/**
			 * User no longer wants to delete the selected album
			 */
			if (alert.getResult() != ButtonType.YES) {
				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Quit from deleting photo.");
				return;
			}

			/**
			 * Photo is found within list
			 */
			if (selectedIndex >= 0) {
				debugLog("Selected Index (Photo to be deleted): "
						+ selectedIndex);

				Alert success = new Alert(Alert.AlertType.CONFIRMATION,
						"Photo successfully removed!", ButtonType.OK);

				tilePaneImages.getChildren().remove(selectedIndex);
				imageQueueList.getItems().remove(selectedIndex);

				/**
				 * CONSOLE DIAGNOSTICS
				 */
				debugLog("Photo successfully removed!");

				updateInfoData();

				success.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to delete.", ButtonType.OK);

			debugLog("ERROR: Please select a photo.");

			error.showAndWait();
		}

	}
	
	@SuppressWarnings("unchecked")
	public void doAlbumList() {
		albumList.setOnAction(event -> {
			Album test = new Album(albumList.getValue());
			
			for(Album a : model.getCurrentUser().getAlbumList()) {
				
				if(a.equals(test)) {
					model.getCurrentUser().setCurrentAlbum(test);
					currentAlbum = test;
				}
			}
		});
	}
	
	/**
	 * Executes upon creating a tag
	 */
	public void createTag() {
		if (currentPhoto != null) {
			if (tagName.getText().isEmpty() == false
					&& tagValue.getText().isEmpty() == false) {
				/**
				 * SCENARIO 1: tagName and tagValue are not empty
				 */
				if (currentPhoto.addTag(tagName.getText().trim(),
						tagValue.getText().trim()) == -1) {
					/**
					 * SCENARIO 1a: tag entered already exists
					 */
					Alert error = new Alert(AlertType.ERROR,
							"Duplicate tag found. Tag not added!",
							ButtonType.OK);

					/**
					 * CONSOLE DIAGNOSTICS
					 */
					debugLog("Duplicate tag found. Tag not added!");

					debugLog(tagName.getText().trim() + " "
							+ tagValue.getText().trim());

					error.showAndWait();
				} else {
					/**
					 * SCENARIO 1b: tag entered does not exist
					 */
					
					tagName.setText(null);
					tagValue.setText(null);

					Alert success = new Alert(Alert.AlertType.CONFIRMATION,
							"Tag successfully added!", ButtonType.OK);

					debugLog("Tag " + tagName.getText()
							+ " was successfully added!");

					success.showAndWait();
				}
			} else {
				/**
				 * SCENARIO 2: either tagName or tagValue or both are empty
				 */
				Alert error = new Alert(AlertType.ERROR,
						"Please provide a tag name and value.", ButtonType.OK);

				debugLog("ERROR: Please provide a tag name and value.");

				error.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR,
					"Please select a photo to create a tag for.",
					ButtonType.OK);

			debugLog("ERROR: Please select a photo.");

			error.showAndWait();
		}
	}

	public void deleteTag() {
		int selectedIndex = tagList.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR,
					"There are no tags to be deleted.", ButtonType.OK);

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("There are no tags to be deleted.");

			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
				"Are you sure you want to delete this tag?", ButtonType.YES,
				ButtonType.NO);

		alert.showAndWait();

		/**
		 * User no longer wants to delete the selected tag
		 */
		if (alert.getResult() != ButtonType.YES) {
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Quit from deleting tag.");
			return;
		}

		/**
		 * Tag is found within list
		 */
		if (selectedIndex >= 0) {
			debugLog("Selected Index (tag to be deleted): " + selectedIndex);
			currentPhoto.deleteTag(selectedIndex);

			Alert success = new Alert(Alert.AlertType.CONFIRMATION,
					"Tag successfully removed!", ButtonType.OK);
			
			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("ATag successfully removed!");

			success.showAndWait();
		}
	}
	
	public void doRadioButtonSelectedAlbum() {
		if(radioButtonSelectedAlbum.isSelected()) {
			albumList.getSelectionModel().select(0);
			radioButtonThisAlbum.setSelected(false);
		}
	}
	
	public void doRadioButtonThisAlbum() {
		if(radioButtonThisAlbum.isSelected()) {
			radioButtonSelectedAlbum.setSelected(false);
			albumList.getSelectionModel().clearSelection();
		}
	}
	
	public void doButtonConfirmTag() {
		
	}
	
	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
	}
	
	public void doZoomSlider() {				
		zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				double factor = newValue.doubleValue();
				
				imageInsetValue = factor * DEFAULT_INSET_VALUE; // default 10.00
				imageHeightValue = factor * DEFAULT_HEIGHT_VALUE; // default 150.00
				imageWidthValue = factor * DEFAULT_WIDTH_VALUE; // default 200.00
				
				for (ImageView iv : currentImageViewList) {
					iv.setFitHeight(imageHeightValue);
					iv.setFitWidth(imageWidthValue);
					iv.setPreserveRatio(true);
				}

				debugLog("" + newValue.doubleValue());
				
				debugLog(imageInsetValue + " inset");
				debugLog(imageHeightValue + " height");
				debugLog(imageWidthValue + " width");
			}
			
		});
	}
	
	/**
	 * Executes upon activating navigator back button
	 */
	public void doNavigatorButtonBack() {
		
		/**
		 * Album is selected and has at least 1 photo
		 */
		if (currentPhoto != null) {

			/**
			 * Establish new index. If current index equals 0, set the index to
			 * the last index of the currentImageViewList. (loop to end)
			 * 
			 * Otherwise, decrement index by one to go the previous image.
			 */
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

			/**
			 * Add an event filter for navigatorButtonBack, for when the primary
			 * mouse button is pressed.
			 */
			navigatorButtonBack.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					debugLog(
							"[navigatorButtonBack] Selected index field reads: "
									+ index);

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle(
								"-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					/**
					 * Retrieve the current photo based on the current index
					 */
					currentPhoto = imageQueueList.getItems().get(index);

					/**
					 * Update selected image in detail pane
					 */
					detailView.setImage(
							currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					/**
					 * Update selected image's detail pane
					 */
					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections
							.observableArrayList();

					/**
					 * Update tags in image detail pane
					 */
					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap()
							.entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("[navigatorButtonBack] Image " + currentPhoto.getFilename()
					+ " was selected");

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Clicked navigator button back");
		} else {

		}
	}

	/**
	 * Executes upon activating navigator next button
	 */
	public void doNavigatorButtonNext() {

		/**
		 * Album is selected and has at least 1 photo
		 */
		if (currentPhoto != null) {
			/**
			 * Establish new index. If current index equals the
			 * currentImageViewList size, minus 1 (new index == last index),
			 * then set the index to zero. (loop to beginning)
			 * 
			 * Otherwise, increment index by one to go to the next image.
			 */
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

			/**
			 * Add an event filter for navigatorButtonNext, for when the primary
			 * mouse button is pressed.
			 */
			navigatorButtonNext.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {
					debugLog(
							"[navigatorButtonNext] Selected index field reads: "
									+ index);

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle(
								"-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					/**
					 * Retrieve the current photo based on the current index
					 */
					currentPhoto = photoList.get(index);

					/**
					 * Update selected image in detail pane
					 */
					detailView.setImage(
							currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					/**
					 * Update selected image's detail pane
					 */
					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText("(size)" + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections
							.observableArrayList();

					/**
					 * Update tags in image detail pane
					 */
					for (Map.Entry<String, Tag> tag : currentPhoto.getTagMap()
							.entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);
					tagList.setItems(tList);

				}
			});

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("[navigatorButtonNext] Image " + currentPhoto.getFilename()
					+ " was selected");

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Clicked navigator button next");
		}

	}
	
	public void selectFile() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
				new ExtensionFilter("All Files", "*.*")
		);
		
		file = fileChooser.showOpenDialog(null);
		
		if(file != null) {
			pList.add(new Photo(file));
		}
		
		imageQueueList.setItems(pList);
		
		tilePaneImages.getChildren().clear();
		
		tilePaneImages.setStyle("-fx-focus-color: transparent;");
		
		/**
		 * Adds padding and insets to thumbnails. Ought to be
		 * adjusted later.
		 */
		tilePaneImages.setPadding(
				new Insets(imageInsetValue, imageInsetValue,
						imageInsetValue, imageInsetValue));

		tilePaneImages.setHgap(imageInsetValue);

		currentImageViewList = new ArrayList<ImageView>();

		for (Photo p : imageQueueList.getItems()) {

			File test = new File(p.getFilepath());
			Image test2 = new Image(test.toURI().toString());
			ImageView iv = new ImageView(test2);

			p.setDataPhoto(test.lastModified());

			p.setSizePhoto(test.length());

			/**
			 * These 'magic numbers' are temporary. They ought
			 * to be mutable values -- using the "zoom-lever"
			 * within the GUI (these are for the image
			 * thumbnails)
			 */
			iv.setFitHeight(imageHeightValue);
			iv.setFitWidth(imageWidthValue);
			iv.setPreserveRatio(true);

			iv.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isSecondaryButtonDown()) {
					debugLog("Image " + p.getFilename()
							+ " right clicked");
				} else {

					if (iv.getBoundsInParent() != null) {
						iv.setStyle(
								"-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					setSelectedIndex(tilePaneImages
							.getChildren().indexOf(iv));
					currentPhoto = p;

					debugLog("Image " + p.getFilename()
							+ " selected");
					
					System.out.println(currentImageViewList.size());

					nameField.setText(
							currentPhoto.getFilename());
					pathField.setText(
							currentPhoto.getFilepath());
					sizeField.setText(
							currentPhoto.getFileSize() + " KB");
					createdField.setText(
							currentPhoto.getDatePhoto() + " ");

					tList = FXCollections.observableArrayList();
					
					for (Map.Entry<String, Tag> tag : currentPhoto
							.getTagMap().entrySet()) {
						tList.add(tag.getValue());
					}

					currentPhoto.setTagList(tList);

					tagList.setItems(tList);

				}
			});

			tilePaneImages.addEventFilter(
					MouseEvent.MOUSE_PRESSED, e -> {
						if (e.isPrimaryButtonDown()) {
							iv.setStyle(null);

							nameField.setText(
									"(No image selected)");
							pathField.setText(
									"(No image selected)");
							sizeField.setText(
									"(No image selected)");
							createdField.setText(
									"(No image selected)");
							
							tagName.setText(null);
							tagValue.setText(null);
							tagList.setItems(null);
						}
					});

			tilePaneImages.getChildren().add(iv);

			currentImageViewList.add(iv);
			
			

			updateInfoData();

			/**
			 * CONSOLE DIAGNOSTICS
			 */
			debugLog("Photo " + p + " added to tilePaneImages");
		}
	}
	
	public void addSelectedPhotos() {
		Album currentAlbum = model.getCurrentUser().getCurrentAlbum();
			
		System.out.println("CURR ALB: " + currentAlbum);
		
		currentAlbum.setPhotoList(photoList);
		
		if(currentAlbum != null) {
			for(int i = 0; i < imageQueueList.getItems().size(); i++) {
				
				Photo p = imageQueueList.getItems().get(i);
				
				if(currentAlbum.addPhoto(p) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Photo not added, duplicate photo.", ButtonType.OK);
					error.showAndWait();
				} else {
					Alert success = new Alert(AlertType.INFORMATION, "Photos successfully added to: " + currentAlbum.getAlbumName());
					success.showAndWait();
				}
				
				for(Tag t : p.getTagList()) {
					currentAlbum.getPhotoMap().get(p.getKey()).addTag(t.getTagName(), t.getTagValue());
				}
				
				imageQueueList.getItems().remove(p);
				tilePaneImages.getChildren().remove(i);
				i--;
			}
			
		}
		
		tagName.setText(null);
		tagValue.setText(null);
		
		if(!tagList.getItems().isEmpty()) {
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
		window.setTitle("Photos -- Import");
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
		
		loader.setLocation(getClass().getResource("/view/help.fxml"));
		loader.setController(this);
		
		Parent root = loader.load();
		
		window.initModality(Modality.NONE);
		
		Scene scene = new Scene(root);
		
		window.setScene(scene);
		window.setTitle("Help");
		window.setResizable(false);
		window.show();
	}
	
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
	
	public void doQuit() {
		LoginController.exit();
	}

	/**
	 * Used for console message/testing functionality/method calls
	 * 
	 * @param message String that denotes a message for the console
	 */
	public void debugLog(String message) {
		System.out.println(
				"[" + this.getClass().getSimpleName() + "] " + message);
	}
}
