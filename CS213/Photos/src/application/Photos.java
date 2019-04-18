/**
 * Photos.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package application;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Represents the client/driver class of Photos
 * 
 * @version Apr 12, 2019
 * @author Patrick Nogaj
 */
public class Photos extends Application {
	
	/**
	 * main calls start based on Application, and launches the login.FXML
	 * 
	 * @throws Exception if login.fxml is not found.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(e -> exit());
		primaryStage.setTitle("Photos -- V1.0");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * Program execution begins here.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Executes upon termination of program.
	 */
	public void exit() {
		LoginController.exit();
	}
	
}
