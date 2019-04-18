/**
 * LoginController.java
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
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import model.Album;
import model.PhotoModel;
import model.User;

/**
 * Controller class to manage functionality of login.fxml
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class LoginController {

	public static final String DAT_FILE_PATH = "photos.dat";

	public static PhotoModel model;

	//@formatter:off
	@FXML Button buttonLogin;
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;
	//@formatter:on

	/**
	 * Initialization process to ensure everything is set to null including user
	 * model.
	 * @throws IOException 
	 */
	@FXML
	public void initialize() throws IOException {
		model = new PhotoModel();
		textFieldUsername.clear();
		textFieldPassword.clear();
	}

	/**
	 * Grabs the model object to pass to different scenes.
	 * 
	 * @return a model object
	 */
	public static PhotoModel getModel() {
		return model;
	}

	/**
	 * Allows the user to press ENTER to login the program.
	 * 
	 * @param keyEvent: collects data on what Key is pressed.
	 * @throws IOException in case file not found.
	 */
	@FXML
	private void keyPressed(KeyEvent keyEvent) throws IOException {
		if (keyEvent.getCode() == KeyCode.ENTER) {
			doLogin();
		}
	}

	/**
	 * Login method -- obtains the information from the textfields, and attempts to
	 * map a user object.
	 * 
	 * @throws IOException: will throw an IOException if file is not found (FXML).
	 */
	public void doLogin() throws IOException {
		final String username = textFieldUsername.getText().trim();
		final String password = textFieldPassword.getText().trim();

		User user = model.getUser(username);

		if (user != null) {
			if (user.getUsername().equals("admin")) {
				if (password.equals("admin")) {
					Parent adminLogin = FXMLLoader.load(getClass().getResource("/view/admin.fxml"));
					Scene adminScene = new Scene(adminLogin);
					Stage currentStage = (Stage) (buttonLogin.getScene().getWindow());
					currentStage.hide();
					currentStage.setScene(adminScene);
					currentStage.setTitle("Photos -- Admin Tool");
					currentStage.show();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");
					alert.showAndWait();

					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			} else {
				if (user.getPassword().equals(password)) {
					model.setCurrentUser(user);

					ObservableList<Album> aList = FXCollections.observableArrayList();

					for (Entry<String, Album> a : user.getAlbumMap().entrySet()) {
						aList.add(a.getValue());
					}

					user.setAlbumList(aList);

					Parent userLogin = FXMLLoader.load(getClass().getResource("/view/user.fxml"));
					Scene userScene = new Scene(userLogin);

					Stage currentStage = (Stage) (buttonLogin.getScene().getWindow());

					currentStage.hide();
					currentStage.setScene(userScene);
					currentStage.setTitle("Photos V1.0 - Welcome " + user.getUsername());
					currentStage.show();
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);

					alert.setTitle("Error");
					alert.setHeaderText("Unsuccessful Login");
					alert.setContentText("Invalid password.");
					alert.showAndWait();

					textFieldUsername.setText(username);
					textFieldPassword.setText("");
				}
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);

			alert.setTitle("Error");
			alert.setHeaderText("Unsuccessful Login");
			alert.setContentText("Enter a valid username and try again.");
			alert.showAndWait();

			textFieldUsername.setText("");
			textFieldPassword.setText("");
		}
	}

	/**
	 * Saves the model, and exits the program via Platform.
	 */
	public static void exit() {
		model.write();
		Platform.exit();
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
