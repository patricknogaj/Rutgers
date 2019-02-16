/**
 * SongLib.java
 *
 * Copyright © 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package application;

import controller.SongLibraryController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.SongLibraryModel;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SongLib extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			SongLibraryController controller = new SongLibraryController();
			loader.setLocation(getClass().getResource("/view/song_lib.fxml"));
			loader.setController(controller);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);

			// Set properties of stage
			primaryStage.setTitle("Song Library");
			primaryStage.setResizable(false);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void stop() {
		SongLibraryModel model = new SongLibraryModel();
		model.writeToFile();
		Platform.exit();
	}
}
