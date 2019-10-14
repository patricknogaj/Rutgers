package controller;

/**
 * VisualController.java
 * 
 * Copyright (c) 2019 Patrick Nogaj
 * All rights reserved.
 * 
 * Rutgers University: School of Arts and Sciences
 * Introduction to Artifical Intelligence, Fall 2019
 * Professor Boularias
 */

import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.AStarModel;
import model.Node;
import util.DistributedRandomNumberGenerator;

public class VisualController {
	
	@FXML Pane gridPane;
	@FXML TextArea consoleLog;
	
	@FXML RadioButton fwdAButton;
	@FXML RadioButton revAButton;
	@FXML RadioButton adaptAButton;
	
	ToggleGroup group = new ToggleGroup();
	
	AStarModel model;
	File file;
	
	public VisualController() {
		model = new AStarModel();
	}
	
	/**
	 * Enables a toggle group for the buttons
	 * -- Utilized to create a constraint that one radio button is selected at a given time.
	 * 
	 * Initializes the map by creating a ROW_SIZE x COL_SIZE grid and adding it into the GridPane.
	 * ROW_SIZE x COL SIZE is specified in AStarModel.java
	 */
	public void initialize() {
		fwdAButton.setToggleGroup(group);
		revAButton.setToggleGroup(group);
		adaptAButton.setToggleGroup(group);
		
		model.initMap();

		for(int i = 0; i < model.getRowSize(); i++) {
			for(int j = 0; j < model.getColSize(); j++) {
				gridPane.getChildren().add(model.getGrid()[i][j]);
			}
		}
	}
	
	/**
	 * Utilizes the DistributedRandomNumberGenerator to create probability based on specifics:
	 * 		To obtain a 1: 30%
	 * 				  a 0: 70%
	 * 
	 * Initially, the method resets the map by clearing the current map by calling model.clearMap()
	 * 
	 * Then, going through each cell in the grid, it randomly generates a number (1, 0) based on probability
	 * If the value equals to 0, set the Rectangle object to fill color WHITE.
	 * 				equals to 1, set the Rectangle object to fill color BLACK. 
	 */
	public void doGenerate() {
		DistributedRandomNumberGenerator randomNumber = new DistributedRandomNumberGenerator();
		randomNumber.addNumber(1, 0.3d);
		randomNumber.addNumber(0, 0.7d);
		
		model.clearMap();
		
		consoleLog.setText("Generating world...\n" + consoleLog.getText());
		for(Node[] row : model.getGrid()) {
			for(Node n : row) {
				if(n.getValue() == 0) {
					int random = randomNumber.getDistributedRandomNumber();
					n.setValue(random);
					
					if(n.getValue() == 0) {
						n.getRectangle().setFill(Color.WHITE);
					} else if(n.getValue() == 1) {
						n.getRectangle().setFill(Color.BLACK);
					}
				}
			}
		}
		consoleLog.setText("World generated!\n" + consoleLog.getText());
	}
	
	/**
	 * Generates the path based on path option selection.
	 * Each method also calculates time using milliseconds / 1000 to get time in seconds.
	 * 
	 * Outcomes:
	 * 		IF NO PATH SELECTED 
	 * 			- Throw an error message and wait for user to acknowledge the error.
	 * 		FORWARD A STAR
	 * 			- Call forwardAStar from AStarModel.java
	 * 		REVERSE A STAR
	 * 			- Call reverseAStar from AStarModel.java
	 * 		ADAPTIVE A STAR
	 * 			- Call adaptiveAStar from AStarModel.java
	 */
	public void doGeneratePath() {
		if(!fwdAButton.isSelected() && !revAButton.isSelected() && !adaptAButton.isSelected()) {
			Alert alert = new Alert(AlertType.ERROR, "No algorithm selected.", ButtonType.OK);
			alert.showAndWait();
		}
		
		if(fwdAButton.isSelected()) {
			consoleLog.setText("Performing Forward A*!\n" + consoleLog.getText());
			double startTime = System.currentTimeMillis();
			model.fwdAStar();
			double endTime = System.currentTimeMillis();
			consoleLog.setText("Forward A* took " + (endTime - startTime) / 1000 + " seconds!\n" + consoleLog.getText());
		} else if(revAButton.isSelected()) {
			consoleLog.setText("Performing Reverse A*!\n" + consoleLog.getText());
			double startTime = System.currentTimeMillis();
			model.reverseAStar();
			double endTime = System.currentTimeMillis();
			consoleLog.setText("Reverse A* took " + (endTime - startTime) / 1000 + " seconds!\n" + consoleLog.getText());
		} else if(adaptAButton.isSelected()) {
			consoleLog.setText("Performing Adaptive A*!\n" + consoleLog.getText());
			double startTime = System.currentTimeMillis();
			model.adaptiveAStar();
			double endTime = System.currentTimeMillis();
			consoleLog.setText("Adaptive A* took " + (endTime - startTime) / 1000 + " seconds!\n" + consoleLog.getText());
		}
	}
	
	/**
	 * Goes through each cell, and checks for value which equals 1.
	 * 		WALL VALUES:
	 * 			-1 = GOAL NODE
	 * 			 0 = REGULAR NODE (EMPTY)
	 * 			 1 = WALL NODE
	 * 			 2 = START NODE
	 * 
	 * If the node equals to 1, it will set the value to 0 and recolors the node.
	 */
	public void doClear() {
		consoleLog.setText("Clearing world...\n" + consoleLog.getText());
		model.clearMap();
		consoleLog.setText("World cleared!\n" + consoleLog.getText());
	}
	
	/**
	 * Goes through each cell and searches for if the cell value equals to 0.
	 * 		WALL VALUES:
	 * 			-1 = GOAL NODE
	 * 			 0 = REGULAR NODE (EMPTY)
	 * 			 1 = WALL NODE
	 * 			 2 = START NODE
	 * 
	 * If the value equals to 0, it will set the fill of the current nodes rectangle to Color.WHITE.
	 */
	public void doClearPath() {
		consoleLog.setText("Clearing paths...\n" + consoleLog.getText());
		model.clearPaths();
		consoleLog.setText("Paths cleared!\n" + consoleLog.getText());
	}
	
	/**
	 * Loads a file based on user input.
	 * Default area is the constraint of the project/data/worlds folder.
	 * It will not be able to handle a file that is not properly formatted.
	 * @throws IOException
	 * 		if: issue opening file (network/permissions/etc).
	 */
	public void doLoad() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open World File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text Files", "*.txt"));
		
		fileChooser.setInitialDirectory(new File("./data/worlds/"));
		file = fileChooser.showOpenDialog(null);
		
		if(file != null) {
			System.out.println(file.getPath());
			model.setFile(file);
		}
		model.read();
	}
	
	/**
	 * Saves a file based on user input.
	 * Default area is the constraint of the project/data/worlds folder.
	 * @throws IOException
	 * 		if: issue opening file (network/permissions/etc).
	 */
	public void doSave() throws IOException { 
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save World File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        
        fileChooser.setInitialDirectory(new File("./data/worlds/"));
        file = fileChooser.showSaveDialog(null);
        
        if(file != null) {
        	model.save(file);
        }
	}
	
	public void doClose() {
		Platform.exit();
	}

}
