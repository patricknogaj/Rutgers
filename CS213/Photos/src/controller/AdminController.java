/**
 * AdminController.java
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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import model.PhotoModel;
import model.User;

/**
 * Controller class to manage functionality of admin.fxml
 * 
 * @version Mar 11, 2019
 * @author Patrick Nogaj
 */
public class AdminController {

	private PhotoModel model = LoginController.getModel();

	//@formatter:off
	@FXML TextField textFieldUsername;
	@FXML TextField textFieldPassword;

	@FXML Button buttonConfirmAdd;
	
	@FXML TableView<User> tableViewUsers;
	
	@FXML TableColumn<User, String> userColumn;
	@FXML TableColumn<User, String> passwordColumn;
	//@formatter:on

	/**
	 * Initializes the administrator control panel with the information needed from
	 * the model.
	 */
	@FXML
	public void initialize() {
		userColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		tableViewUsers.setItems(model.getUserList());
	}

	/**
	 * Creates a new User utilizing the text fields to gather information about the
	 * user.
	 */
	public void doAdd() {
		String username = textFieldUsername.getText();
		String password = textFieldPassword.getText();

		if (username.isEmpty() == false && password.isEmpty() == false) {
			User newUser = new User(username, password);

			if (model.addUser(username, password) == -1) {
				textFieldUsername.setText(null);
				textFieldPassword.setText(null);

				Alert duplicate = new Alert(Alert.AlertType.ERROR, "Duplicate username found. User not added!",
						ButtonType.OK);
				duplicate.showAndWait();
			} else {
				textFieldUsername.setText(null);
				textFieldPassword.setText(null);

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "User successfully added!", ButtonType.OK);
				success.showAndWait();

				int selectedIndex = model.getIndex(newUser);
				tableViewUsers.getSelectionModel().select(selectedIndex);
			}
		} else {
			Alert error = new Alert(AlertType.ERROR, "Please provide a username and password.", ButtonType.OK);
			error.showAndWait();
		}
	}

	/**
	 * Deletes the user from the tableViewUsers, and from the model.
	 */
	public void doDelete() {
		int selectedIndex = tableViewUsers.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			Alert error = new Alert(Alert.AlertType.ERROR, "There are no users to be deleted.", ButtonType.OK);
			error.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?",
				ButtonType.YES, ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() != ButtonType.YES) {
			return;
		}

		if (selectedIndex >= 0) {
			model.deleteUser(selectedIndex);

			if (selectedIndex <= model.getItemCount() - 1) {
				tableViewUsers.getSelectionModel().select(selectedIndex);
			}
		}
	}

	/**
	 * Executes upon visiting about option
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
		window.setTitle("Photos V1.0 -- About");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Executes upon visiting help option
	 * 
	 * @throws IOException if help_admin.fxml not found
	 */
	public void doHelp() throws IOException {
		Stage window = new Stage();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/help_admin.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		window.initModality(Modality.NONE);
		Scene scene = new Scene(root);
		window.setScene(scene);
		window.setTitle("Photos V1.0 -- About");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Saves the current model, and returns back to the login screen.
	 * 
	 * @throws IOException if login.fxml not found
	 */
	public void doLogOff() throws IOException {
		model.write();
		Parent login = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
		Scene loginScene = new Scene(login);
		Stage currentStage = (Stage) (tableViewUsers.getScene().getWindow());
		currentStage.hide();
		currentStage.setScene(loginScene);
		currentStage.setTitle("Photos -- V1.0");
		currentStage.show();
	}

	/**
	 * Saves the current model, and exits the program.
	 */
	public void doQuit() {
		LoginController.exit();
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
