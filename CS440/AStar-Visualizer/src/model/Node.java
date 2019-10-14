package model;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Node.java
 * 
 * Copyright (c) 2019 Patrick Nogaj
 * All rights reserved.
 * 
 * Rutgers University: School of Arts and Sciences
 * Introduction to Artifical Intelligence, Fall 2019
 * Professor Boularias
 */

public class Node extends StackPane {
	
	private int x, y, g, h = 0, f, value;
	private Node parent = this;
	
	/**
	 * Utilized to draw the actual node. 
	 * If amount of nodes in grid change, NODE_SIZE can increase.
	 * In this case, a 101x101 :: NODE_SIZE = 8 is a perfect choice.
	 */
	private final double NODE_SIZE = 8;
	private Rectangle border = new Rectangle(NODE_SIZE, NODE_SIZE);
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		
		border.setStroke(Color.BLACK);
		getChildren().addAll(border);
		setTranslateX(x * NODE_SIZE);
		setTranslateY(y * NODE_SIZE);
		updateToolTip();
	}
	
	/**
	 * Creates a tool tip for each Rectangle that displays information about the Node.
	 */
	public void updateToolTip() {
		Tooltip tool = new Tooltip("X: " + getX() + "\nY: " + getY() + "\nG: " + getG() + "\nH: " + getH() + "\nF: " + getF() + "\nParent: " + getNodeParent());
		Tooltip.install(border, tool);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getG() {
		return g;
	}
	
	public int getH() {
		return h;
	}
	
	public int getF() {
		return f;
	}
	
	public int getValue() {
		return value;
	}
	
	public Rectangle getRectangle() {
		return border;
	}

	public Node getNodeParent() {
		return parent;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setG(int g) {
		this.g = g;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public void setF(int f) {
		this.f = f;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setNodeParent(Node parent) {
		this.parent = parent;
	}
	
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Node)) {
			return false;
		}
		
		Node other = (Node) o;
		return (this.getX() == other.getX() && this.getY() == other.getY());
	}
	
	public String toString() {
		return "(" +this.getX() + ", " + this.getY() + ")";
	}

}
