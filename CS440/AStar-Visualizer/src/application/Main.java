package application;

/**
 * Main.java
 * 
 * Copyright (c) 2019 Patrick Nogaj
 * All rights reserved.
 * 
 * Rutgers University: School of Arts and Sciences
 * Introduction to Artifical Intelligence, Fall 2019
 * Professor Boularias
 */
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/visualizer.fxml"));
			Parent root = loader.load();			
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("A-Star Visualizer V1.0");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
