/**
 * UserController.java
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import model.Album;
import model.Photo;
import model.PhotoModel;
import model.Tag;
import model.User;

/**
 * Controller class to manage functionality of user.fxml
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj, Gemuele Aludino
 */
public class UserController {

	private PhotoModel model = LoginController.getModel();
	private Photo currentPhoto = null;
	private Album currentAlbum = null;
	private User currentUser = model.getCurrentUser();

	public static Photo currentSelectedPhoto = null;
	
	private static boolean currentStatus = false;
	private static boolean allStatus = false;

	int index = 0;

	final double DEFAULT_INSET_VALUE = 10.0;
	final double DEFAULT_HEIGHT_VALUE = 150.0;
	final double DEFAULT_WIDTH_VALUE = 200.0;

	double imageInsetValue = DEFAULT_INSET_VALUE;
	double imageHeightValue = DEFAULT_HEIGHT_VALUE;
	double imageWidthValue = DEFAULT_WIDTH_VALUE;

	ObservableList<Photo> photoList = FXCollections.observableArrayList();

	private List<ImageView> currentImageViewList = null;

	//@formatter:off
	//@FXML Menu menuAlbums;
	@FXML Menu menuSearch;
	@FXML Menu copyMenu;
	@FXML Menu moveMenu;
	
	@FXML MenuItem fileCreateNewAlbum;
	@FXML MenuItem fileImportImagesSelectedAlbum;
	@FXML MenuItem fileOpenSelectedPhotoInViewer;
	@FXML MenuItem fileSaveAndLogout;
	@FXML MenuItem fileSaveAndExit;
	@FXML MenuItem viewAsThumbnails;
	@FXML MenuItem viewAsSingleImage;
	@FXML MenuItem viewModeThumbnail;
	@FXML MenuItem viewModeSingleImage;
	@FXML MenuItem searchCurrentAlbum;
	@FXML MenuItem searchAllAlbums;
	@FXML MenuItem menuItemHelp;
	@FXML MenuItem menuItemAboutPhotos;
	
	@FXML Button cancelEdit;
	@FXML Button createAlbum;
	@FXML Button buttonAddCaption;
	@FXML Button buttonAddTag;
	@FXML Button buttonCopyPhoto;
	@FXML Button buttonDeletePhoto;
	@FXML Button buttonMovePhoto;
	@FXML Button editAlbum;
	@FXML Button navigatorButtonBack;
	@FXML Button navigatorButtonNext;
	@FXML Button movePhoto;
	@FXML Button copyPhoto;
	@FXML Button cancelButton;
	
	@FXML Label albumHeader;
	@FXML Label createdField;
	@FXML Label displayCaption;
	@FXML Label infoData;
	@FXML Label nameField;
	@FXML Label pathField;
	@FXML Label sizeField;
	
	@FXML TextField captionField;
	@FXML TextField tagName;
	@FXML TextField tagValue;
	@FXML TextField createAlbumName;
	@FXML TextField renameAlbumName;	
	
	@FXML CheckBox checkBoxDeleteFromDisk;
	@FXML CheckBox checkBoxPromptBeforeDelete;
	
	@FXML Hyperlink addAlbumHL;
	@FXML Hyperlink addHL;
	
	@FXML ListView<Album> albumView;
	@FXML ListView<Tag> tagList;
	
	@FXML Slider zoomSlider;
	
	@FXML Text oldName;

	@FXML ImageView detailView;
	
	@FXML TilePane tilePaneImages;
	
	@FXML ChoiceBox<String> albumPicker;
	//@formatter:on

	/**
	 * Initializes USER.fxml with data from the current user
	 */
	@FXML
	public void initialize() {
		addHL.setVisible(false);
		fileImportImagesSelectedAlbum.setDisable(true);
		albumView.setItems(currentUser.getAlbumList());
		updateInfoData();
		
		checkBoxPromptBeforeDelete.setSelected(true);

		
		  for(Album a : albumView.getItems()) {

		 a.setCounterDatetime(); }
		 
	}

	/**
	 * Launches Viewer window for when a photo is selected to be opened in a Viewer
	 * 
	 * @throws IOException if viewer.fxml is not found
	 */
	public void doOpenSelectedPhotoInViewer() throws IOException {
		PhotoController.currentSelectedPhoto = null;
		UserController.currentSelectedPhoto = currentPhoto;
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
	
	/*
	public void doMenuAlbums() {
		debugLog("do menu albums");
	}
	*/

	/**
	 * Runs each time there is an update to the user's albums and/or photos.
	 */
	public void updateInfoData() {
		int albumCount = currentUser.getAlbumList().size();
		int totalPhotoCount = 0;
		long totalByteCount = 0;

		for (Album a : currentUser.getAlbumMap().values()) {
			totalPhotoCount += a.getAlbumSize();

			for (Photo p : a.getPhotoMap().values()) {
				totalByteCount += p.getFileSize();
			}
		}
		String output = String.format("%d albums - %d photos - %d KB", albumCount, totalPhotoCount, totalByteCount);
		infoData.setText(output);
	}

	/**
	 * Adjusts the zoom slider with ImageView sizes
	 */
	public void doZoomSlider() {
		if (currentAlbum == null) {
			return;
		}

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
	 * Executes upon setting a caption within captionField
	 */
	public void setCaption() {
		if (currentPhoto != null) {
			if (captionField.getText().isEmpty() == false) {
				currentPhoto.setCaption(captionField.getText().trim());
				displayCaption.setText(captionField.getText().trim());
			} else {
				Alert error = new Alert(AlertType.ERROR, "Please provide a caption.", ButtonType.OK);
				error.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please select a photo to create a caption for.", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Executes upon creating an album
	 */
	public void doCreate() {
		if (createAlbumName.getText().isEmpty() == false) {
			String albumName = createAlbumName.getText().trim();

			if (currentUser.addAlbum(albumName) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate album found. Album not added!",
						ButtonType.OK);

				duplicate.showAndWait();
			} else {
				// menuAlbums.getItems().clear();

				/*
				 * for(Album a : albumView.getItems()) { menuAlbums.getItems().add(new
				 * MenuItem(a.getAlbumName())); }
				 */

				createAlbumName.getScene().getWindow().hide();
				updateInfoData();

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully added!", ButtonType.OK);
				success.showAndWait();
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide an album name.", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Executes upon renaming an album
	 */
	public void doRename() {
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to rename this album?",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		if (selectedIndex >= 0) {
			if (currentUser.edit(selectedIndex, renameAlbumName.getText().trim()) == -1) {
				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate album found. Album not renamed!",
						ButtonType.OK);
				duplicate.showAndWait();
			} else {

				// menuAlbums.getItems().clear();

				/*
				 * for(Album a : albumView.getItems()) { menuAlbums.getItems().add(new
				 * MenuItem(a.getAlbumName())); }
				 */

				if (selectedIndex <= model.getItemCount() - 1) {
					albumView.getSelectionModel().select(selectedIndex);
				}

				renameAlbumName.getScene().getWindow().hide();

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully renamed!", ButtonType.OK);
				success.showAndWait();
			}
		}
	}

	/**
	 * Copies a photo from current album to another album.
	 */
	public void copyPhoto() {
		Photo toCopy = new Photo(currentPhoto);
		Album copyTo = currentUser.getAlbumMap().get(albumPicker.getValue().toLowerCase());

		for (Album a : currentUser.getAlbumList()) {
			if (a.equals(copyTo)) {
				if (copyTo.addPhoto(toCopy) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Photo found in this album. Copy canceled!",
							ButtonType.OK);
					error.showAndWait();
				} else {
					Alert success = new Alert(AlertType.INFORMATION, "Photo successfully copied!", ButtonType.OK);
					success.showAndWait();
				}
			}
		}

		for (Tag t : tagList.getItems()) {
			copyTo.getPhotoMap().get(toCopy.getKey()).addTag(t.getTagName(), t.getTagValue());
		}

		copyTo.getPhotoMap().get(toCopy.getKey()).setCaption(currentPhoto.getCaption());
		copyPhoto.getScene().getWindow().hide();
	}

	/**
	 * Copies a photo from current album to another album, and deletes the photo
	 * from the current album.
	 */
	public void movePhoto() {
		Photo toMove = new Photo(currentPhoto);
		Album moveTo = currentUser.getAlbumMap().get(albumPicker.getValue().toLowerCase());

		for (Album a : currentUser.getAlbumList()) {
			if (a.equals(moveTo)) {
				if (moveTo.addPhoto(toMove) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Photo found in this album. Move canceled!",
							ButtonType.OK);
					error.showAndWait();
				} else {
					currentAlbum.deletePhoto(index);
					tilePaneImages.getChildren().remove(index);
					Alert success = new Alert(AlertType.INFORMATION, "Photo successfully moved!", ButtonType.OK);
					success.showAndWait();
				}
			}
		}

		for (Tag t : tagList.getItems()) {
			moveTo.getPhotoMap().get(toMove.getKey()).addTag(t.getTagName(), t.getTagValue());
		}

		moveTo.getPhotoMap().get(toMove.getKey()).setCaption(currentPhoto.getCaption());
		currentPhoto = null;
		movePhoto.getScene().getWindow().hide();
	}

	/**
	 * Deletes a photo from the current album.
	 */
	public void deletePhoto() {
		if (currentPhoto != null) {
			int selectedIndex = index;

			if (selectedIndex < 0) {
				Alert error = new Alert(Alert.AlertType.ERROR, "There are no photos to be deleted.", ButtonType.OK);

				error.showAndWait();
				return;
			}

			if (checkBoxPromptBeforeDelete.isSelected()) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this photo?",
						ButtonType.YES, ButtonType.NO);
				alert.showAndWait();

				if (alert.getResult() != ButtonType.YES) {
					return;
				}
			}

			if (selectedIndex >= 0) {
				currentAlbum.deletePhoto(selectedIndex);

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Photo successfully removed!", ButtonType.OK);
				success.showAndWait();

				tilePaneImages.getChildren().remove(selectedIndex);
				updateInfoData();

				currentImageViewList.remove(selectedIndex);

				detailView.setImage(null);
				displayCaption.setText(null);
				nameField.setText("(No image selected)");
				pathField.setText("(No image selected)");
				sizeField.setText("(No image selected)");
				createdField.setText("(No image selected)");

				tagName.setText(null);
				tagValue.setText(null);
				tagList.setItems(null);
				captionField.setText(null);

				currentPhoto = null;
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please select a photo to delete.", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Executes upon creating a tag
	 */
	public void createTag() {
		if (currentPhoto != null) {
			if (tagName.getText().isEmpty() == false && tagValue.getText().isEmpty() == false) {

				if (currentPhoto.addTag(tagName.getText().trim(), tagValue.getText().trim()) == -1) {
					Alert error = new Alert(AlertType.ERROR, "Duplicate tag found. Tag not added!", ButtonType.OK);
					error.showAndWait();
				} else {
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
	 * Executes upon removing a tag Grabs the current tag object and sends it to the
	 * current photo object to see if it is possible to delete.
	 */
	public void removeTag() {
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
	 * Executes upon selection of an album within albumView
	 */
	public void doSelectAlbum() {
		albumView.setCellFactory(lv -> {
			ListCell<Album> cell = new ListCell<Album>() {
				@Override
				protected void updateItem(Album item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						setText(item.toString());
					}
				}
			};

			cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if ((!cell.isEmpty())) {
					Album item = cell.getItem();

					addHL.setVisible(true);
					fileImportImagesSelectedAlbum.setDisable(false);

					if (event.isSecondaryButtonDown()) {

					} else {

						detailView.setImage(null);
						displayCaption.setText(null);
						nameField.setText("(No image selected)");
						pathField.setText("(No image selected)");
						sizeField.setText("(No image selected)");
						createdField.setText("(No image selected)");

						tagName.setText(null);
						tagValue.setText(null);
						tagList.setItems(null);
						captionField.setText(null);

						currentPhoto = null;

						currentAlbum = item;
						model.getCurrentUser().setCurrentAlbum(currentAlbum);
						albumHeader.setText(currentAlbum.getAlbumName());

						photoList = FXCollections.observableArrayList();

						for (Map.Entry<String, Photo> test2 : currentAlbum.getPhotoMap().entrySet()) {
							photoList.add(test2.getValue());
						}

						currentAlbum.setPhotoList(photoList);

						tilePaneImages.getChildren().clear();
						tilePaneImages.setStyle("-fx-focus-color: transparent;");
						tilePaneImages.setPadding(
								new Insets(imageInsetValue, imageInsetValue, imageInsetValue, imageInsetValue));
						tilePaneImages.setHgap(imageInsetValue);

						currentImageViewList = new ArrayList<ImageView>();

						for (Photo p : photoList) {

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

									nameField.setText(currentPhoto.getFilename());
									pathField.setText(currentPhoto.getFilepath());
									sizeField.setText(currentPhoto.getFileSize() + " KB");
									createdField.setText(currentPhoto.getDatePhoto() + " ");

									displayCaption.setText(p.getCaption());

									captionField.setText("");

									detailView.setImage(new Image(test.toURI().toString()));
									detailView.setFitHeight(DEFAULT_HEIGHT_VALUE);
									detailView.setFitWidth(DEFAULT_WIDTH_VALUE);
									detailView.setVisible(true);
									detailView.setPreserveRatio(true);

									ObservableList<Tag> tList = FXCollections.observableArrayList();

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

									detailView.setImage(null);
									displayCaption.setText(null);
									nameField.setText("(No image selected)");
									pathField.setText("(No image selected)");
									sizeField.setText("(No image selected)");
									createdField.setText("(No image selected)");

									tagName.setText(null);
									tagValue.setText(null);
									tagList.setItems(null);
									captionField.setText(null);

									currentPhoto = null;
								}
							});

							tilePaneImages.getChildren().add(iv);
							currentImageViewList.add(iv);

							updateInfoData();
						}

					}
				}
			});
			return cell;
		});
	}

	/**
	 * Sets the selectedIndex to index.
	 * 
	 * @param selectedIndex
	 */
	private void setSelectedIndex(int selectedIndex) {
		index = selectedIndex;
	}

	/**
	 * Executes upon activating navigator back button
	 */
	public void doNavigatorButtonBack() {
		if (currentAlbum == null) {
			return;
		}

		if (currentAlbum != null && currentPhoto == null) {
			return;
		}

		if (currentAlbum.getAlbumSize() != 0 && currentAlbum != null) {
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

			navigatorButtonBack.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
				if (e.isPrimaryButtonDown()) {

					old.setStyle(null);
					if (iv.getBoundsInParent() != null) {
						iv.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 4, 2.0, 0, 0);");
					}

					currentPhoto = photoList.get(index);
					currentSelectedPhoto = currentPhoto;

					detailView.setImage(currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					displayCaption.setText(currentPhoto.getCaption());
					captionField.setText("");

					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText(currentPhoto.getFileSize() + " KB");
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
		if (currentAlbum == null) {
			return;
		}

		if (currentAlbum != null && currentPhoto == null) {
			return;
		}

		if (currentAlbum.getAlbumSize() != 0 && currentAlbum != null) {
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

					currentPhoto = photoList.get(index);
					currentSelectedPhoto = currentPhoto;

					detailView.setImage(currentImageViewList.get(index).getImage());
					detailView.setFitHeight(150);
					detailView.setFitWidth(200);
					detailView.setVisible(true);
					detailView.setPreserveRatio(true);

					/**
					 * Update the caption section.
					 */
					displayCaption.setText(currentPhoto.getCaption());
					captionField.setText("");

					/**
					 * Update selected image's detail pane
					 */
					nameField.setText(currentPhoto.getFilename());
					pathField.setText(currentPhoto.getFilepath());
					sizeField.setText(currentPhoto.getFileSize() + " KB");
					createdField.setText(currentPhoto.getDatePhoto());

					ObservableList<Tag> tList = FXCollections.observableArrayList();

					/**
					 * Update tags in image detail pane
					 */
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
	 * Executes upon deleting album
	 */
	public void doDeleteAlbum() {
		int selectedIndex = albumView.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no albums to be deleted.", ButtonType.OK);

			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this album?",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		if (selectedIndex >= 0) {
			currentUser.deleteAlbum(selectedIndex);

			if (selectedIndex <= model.getItemCount() - 1) {
				albumView.getSelectionModel().select(selectedIndex);
			}

			Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Album successfully removed!", ButtonType.OK);
			success.showAndWait();

			updateInfoData();

			/*
			 * menuAlbums.getItems().clear(); for(Album a : albumView.getItems()) {
			 * menuAlbums.getItems().add(new MenuItem(a.getAlbumName())); }
			 */
		}
	}

	/**
	 * Executes upon selecting add album mechanism
	 * 
	 * @throws IOException if add_album.fxml not found
	 */
	public void doAddAlbum() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/add_album.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		window.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("Photos -- Add Album");
		window.setResizable(false);
		window.show();
		updateInfoData();
	}

	/**
	 * Executes upon selecting edit album mechanism
	 * 
	 * @throws IOException if edit_album.fxml not found
	 */
	public void doEditAlbum() throws IOException {
		if (albumView.getSelectionModel().getSelectedIndex() < 0) {
			return;
		} else {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/edit_album.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			window.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.setTitle("Photos -- Edit Album");
			window.setResizable(false);
			window.show();
			oldName.setText(albumView.getSelectionModel().getSelectedItem().getAlbumName());
			updateInfoData();
		}
	}

	/**
	 * Executes upon selecting import section
	 * 
	 * @throws IOException if import.fxml not found
	 */
	public void importPhoto() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/import.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		addHL.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos -- Import");
		window.setResizable(false);
		window.show();
		updateInfoData();
	}

	/**
	 * Executes upon selecting copy section
	 * 
	 * @throws IOException if copy_photo.fxml not found
	 */
	public void openCopyPhoto() throws IOException {
		if (currentPhoto != null) {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/copy_photo.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			window.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(root);

			window.setScene(scene);
			window.setTitle("Photos -- Copy Photo");
			window.setResizable(false);
			window.show();

			for (Album a : model.getCurrentUser().getAlbumList()) {
				albumPicker.getItems().add(a.getAlbumName());
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "No photo was selected. Action not performed!", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Executes upon selecting move section
	 * 
	 * @throws IOException if move_photo.fxml not found
	 */
	public void openMovePhoto() throws IOException {
		if (currentPhoto != null) {
			Stage window = new Stage();
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/move_photo.fxml"));
			loader.setController(this);
			Parent root = loader.load();
			window.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(root);
			window.setScene(scene);
			window.setTitle("Photos -- Copy Photo");
			window.setResizable(false);
			window.show();

			for (Album a : model.getCurrentUser().getAlbumList()) {
				albumPicker.getItems().add(a.getAlbumName());
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "No photo was selected. Action not performed!", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Executes upon selecting search section
	 * 
	 * @throws IOException if search.fxml not found
	 */
	public void doSearchAllAlbums() throws IOException {
		enableAll();
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/search.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		addHL.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos -- Search");
		window.setResizable(false);
		window.show();
		updateInfoData();
	}

	/**
	 * Executes upon selecting search section
	 * 
	 * @throws IOException if search.fxml not found
	 */
	public void doSearchCurrentAlbum() throws IOException {
		enableCurrent();
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/search.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		addHL.getScene().getWindow().hide();
		window.setScene(scene);
		window.setTitle("Photos -- Search");
		window.setResizable(false);
		window.show();
		updateInfoData();
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

	/**
	 * Executes upon logging off
	 * 
	 * @throws IOException if login.fxml not found
	 */
	public void doLogOff() throws IOException {
		model.write();
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (albumView.getScene().getWindow());
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}

	/**
	 * Executes upon terminating program
	 */
	public void doQuit() {
		LoginController.exit();
	}

	/**
	 * Closes the current window.
	 */
	public void doCancel() {
		cancelButton.getScene().getWindow().hide();
	}

	/**
	 * Search current album
	 */
	private void enableCurrent() {
		UserController.currentStatus = true;
	}

	/**
	 * Search all albums
	 */
	private void enableAll() {
		UserController.allStatus = true;
	}

	/**
	 * Returns the status of variable currentStatus (is the currentAlbum to be searched, or all albums?)
	 * 
	 * @return true if currentAlbum is to be searched, false otherwise
	 */
	public static boolean searchCurrent() {
		return currentStatus;
	}

	/**
	 * Returns the status of variable allStatus (are all albums to be searched?)
	 * 
	 * @return true if all user albums are to be searched, false otherwise
	 */
	public static boolean searchAll() {
		return allStatus;
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