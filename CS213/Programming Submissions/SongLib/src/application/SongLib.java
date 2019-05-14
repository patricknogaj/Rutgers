/**
 * SongLib.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import controller.SongLibraryController;

public class SongLib extends Application {

	SongLibraryController controller = new SongLibraryController();

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
			loader.setController(controller);

			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			primaryStage.setTitle("Song Library");
			primaryStage.setResizable(false);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() {
		try {
			controller.exit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
