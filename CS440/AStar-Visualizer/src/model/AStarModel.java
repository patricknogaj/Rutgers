package model;

/**
 * AStarModel.java
 * 
 * Copyright (c) 2019 Patrick Nogaj
 * All rights reserved.
 * 
 * Rutgers University: School of Arts and Sciences
 * Introduction to Artifical Intelligence, Fall 2019
 * Professor Boularias
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import controller.VisualController;
import javafx.scene.paint.Color;

public class AStarModel {
	
	VisualController vc;
	Node[][] grid;
	File file;
	
    private PriorityQueue<Node> openList;
    private Set<Node> closedSet;
    private Node initialNode;
    private Node finalNode;
    
    private final int COL_SIZE = 101,
    				  ROW_SIZE = 101;
	
	public AStarModel() {
		grid = new Node[COL_SIZE][ROW_SIZE];
		for(int x = 0; x < ROW_SIZE; x++) {
			for(int y = 0; y < COL_SIZE; y++) {
				Node node = new Node(x, y);
				grid[x][y] = node;
			}
		}
		
		openList = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node node1, Node node2) {
				return Integer.compare(node1.getF(), node2.getF());
			}
		});
		closedSet = new HashSet<Node>();
		initialNode = grid[0][0];
		finalNode = grid[ROW_SIZE - 1][COL_SIZE - 1];
	}
	
	public void initMap() {		
		getInitialNode().setValue(2);
		getFinalNode().setValue(-1);
		read();
	}
	
	public void clearMap() { 
		for(Node[] row : grid) {
			for(Node n : row) {
				if(n.getValue() == 1) {
					n.setValue(0);
					n.getRectangle().setFill(Color.WHITE);
				}
			}
		}
		setColors();
	}
	
	public void clearPaths() {
		openList.clear();
		closedSet.clear();
		
		for(Node[] row : grid) {
			for(Node n : row) {
				if(n.getValue() == 0) {
					n.setValue(0);
					n.getRectangle().setFill(Color.WHITE);
				}
			}
		}
	}
	
	public void setColors() { 
		for(Node[] n : grid) {
			for(Node x : n) {
			
				if(x.getValue() == 2) {
					x.getRectangle().setFill(Color.RED);
				} else if(x.getValue() == 0) {
					x.getRectangle().setFill(Color.WHITE);
				} else if(x.getValue() == 1) {
					x.getRectangle().setFill(Color.BLACK);
				} else if(x.getValue() == -1) {
					x.getRectangle().setFill(Color.GREEN);
				}
			}
		}
	}
	
	public void fwdAStar() {
		clearPaths();
		
		initialNode = grid[0][0];
		finalNode = grid[100][100];
		
		initialNode.setG(0);
		initialNode.setF(initialNode.getG() + manhattanDistance(initialNode, finalNode));
		openList.add(initialNode);
		
		while(!openList.isEmpty()) {
			Node current = openList.poll();
			
			if(current.equals(finalNode)) {
				findPath(current, grid);
				break; //found the goal
			}
			
			openList.remove(current);
			closedSet.add(current);
			
			for(Node neighbor : getNeighbors(current, grid)) {
				
				if(neighbor.getValue() == 1)
					continue;
				
				int tentG = current.getG() + manhattanDistance(current, neighbor);
				
				//This is used for tie-breaking higher G values.
				if(closedSet.contains(neighbor))
					continue;
				
				if(!openList.contains(neighbor) || tentG > neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}
				
				/*if(closedSet.contains(neighbor) && tentG >= neighbor.getG())
					continue;
				
				if(!openList.contains(neighbor) || tentG < neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}*/
			}
		}
	}
	
	public void reverseAStar() {
		clearPaths();
		
		initialNode = grid[100][100];
		finalNode = grid[0][0];
		
		initialNode.setG(0);
		initialNode.setF(initialNode.getG() + manhattanDistance(initialNode, finalNode));
		openList.add(initialNode);
		
		while(!openList.isEmpty()) {
			Node current = openList.poll();
			
			if(current.equals(finalNode)) {
				findPath(current, grid);
				break; //found the goal
			}
			
			openList.remove(current);
			closedSet.add(current);
			
			for(Node neighbor : getNeighbors(current, grid)) {
				if(neighbor.getValue() == 1)
					continue;
				
				int tentG = current.getG() + manhattanDistance(current, neighbor);
				
				/*if(closedSet.contains(neighbor) && tentG >= neighbor.getG())
					continue;
				
				if(!openList.contains(neighbor) || tentG < neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}*/
				
				//This is used for tie-breaking higher G values.
				if(closedSet.contains(neighbor))
					continue;
				
				if(!openList.contains(neighbor) || tentG > neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}
			}
		}
	}
	
	public void adaptiveAStar() {
		clearPaths();
		
		initialNode = grid[0][0];
		finalNode = grid[100][100];
		
		initialNode.setG(0);
		initialNode.setF(initialNode.getG() + manhattanDistance(initialNode, finalNode));
		openList.add(initialNode);
		
		while(!openList.isEmpty()) {
			Node current = openList.poll();
			
			if(current.equals(finalNode)) {
				findPath(current, grid);
				break; //found the goal
			}
			
			openList.remove(current);
			closedSet.add(current);
			
			for(Node neighbor : getNeighbors(current, grid)) {
				
				if(neighbor.getValue() == 1)
					continue;
				
				int tentG = current.getG() + manhattanDistance(current, neighbor);
				current.setH(manhattanDistance(current, getFinalNode()));
				
				//This is used for tie-breaking higher G values.
				if(closedSet.contains(neighbor))
					continue;
				
				if(!openList.contains(neighbor) || tentG > neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}
				
				/*if(closedSet.contains(neighbor) && tentG >= neighbor.getG())
					continue;
				
				if(!openList.contains(neighbor) || tentG < neighbor.getG()) {
					neighbor.setG(tentG);
					neighbor.setH(manhattanDistance(neighbor, getFinalNode()));
					neighbor.setF(neighbor.getG() + neighbor.getH());
					neighbor.setNodeParent(current);
					neighbor.updateToolTip();
					neighbor.getRectangle().setFill(Color.CYAN);
					openList.add(neighbor);
				}*/
			}
		}
	}
	
	public int manhattanDistance(Node startNode, Node endNode) {
		return Math.abs(endNode.getX() - startNode.getX()) + Math.abs(endNode.getY() - startNode.getY());
	}
	
	public void read() {
		try {
			
			String FILE_LOC = null;
			
			if(file == null) {
				FILE_LOC = "./data/worlds/world_default.txt";
			} else {
				FILE_LOC = getFileLocation();
			}
			
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(FILE_LOC));
			String line;
			String[] input;
			int row = 0;
			int[][] matrix = new int[ROW_SIZE][COL_SIZE];
			
			while((line = reader.readLine()) != null) {
				input = line.split(",");
				
				for(int i = 0; i < COL_SIZE; i++) {
					matrix[i][row] = Integer.parseInt(input[i]);
				}
				row++;
			}
			
			for(int i = 0; i < ROW_SIZE; i++) {
				for(int j = 0; j < COL_SIZE; j++) {
					grid[i][j].setValue(matrix[i][j]);
				}
			}
			
			setColors();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(File file) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < ROW_SIZE; i++) {
		   for(int j = 0; j < COL_SIZE; j++) {
		      builder.append(grid[j][i].getValue());
		      
		      if(j < grid[0].length - 1)
		         builder.append(",");
		   }
		   builder.append("\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(builder.toString());
		writer.close();		
	}
	
	public List<Node> findPath(Node node, Node[][] grid) {
		Node temp = getFinalNode();
		List<Node> path = new ArrayList<>();
		do {
			path.add(temp);
			temp = temp.getNodeParent();
		} while(temp != getInitialNode());
		
		for(Node n : path) {
			n.getRectangle().setFill(Color.GREEN);
		}
		return path;
	}
	
	public List<Node> getNeighbors(Node node, Node[][] grid) {
		int[][] NEIGHBOR_POINTS = {
		        {-1, 0},
		{ 0,-1},        {0, 1}, 
		        { 1, 0}
		};
		
		List<Node> neighbors = new ArrayList<>();
		
	    for (int[] neighborPositions : NEIGHBOR_POINTS) {
	        int nrow = node.getX() + neighborPositions[0];
	        int ncol = node.getY() + neighborPositions[1];
	        if (nrow >= 0 && nrow < ROW_SIZE && ncol >= 0 && ncol < COL_SIZE) {
	            neighbors.add(grid[nrow][ncol]);
	        }
	    }
		return neighbors;
	}
	
	public int getRowSize() {
		return ROW_SIZE;
	}
	
	public int getColSize() { 
		return COL_SIZE;
	}
	
	public String getFileLocation() {
		return file.getPath();
	}
	
	public Node[][] getGrid() {
		return grid;
	}

	public Set<Node> getClosedSet() {
		return closedSet;
	}
	
	public Node getInitialNode() { 
		return initialNode;
	}
	
	public Node getFinalNode() {
		return finalNode;
	}
	
	public void setFile(File file) {
		this.file = file;
	}

}
