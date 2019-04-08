/**
 * Bishop.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set.piecetypes;

import model.PieceType;
import model.chess_set.Piece;
import model.chess_set.Board.Cell;
import model.game.Position;

/**
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public class Bishop extends Piece {

	/**
	 * Parameterized constructor
	 * 
	 * @param pieceType the PieceType to assign
	 * @param color     the Color of a Player's PieceSet
	 */
	public Bishop(PieceType pieceType, PieceType.Color color) {
		super(color);

		this.pieceType = pieceType.equals(PieceType.BISHOP_R)
				|| pieceType.equals(PieceType.BISHOP_L) ? pieceType : null;

		if (this.pieceType == null) {
			System.err.println("ERROR: Set this piece to either "
					+ "PieceType.BISHOP_R or PieceType.BISHOP_L!");
			identifier += " (invalid)";
		} else {
			identifier += "Bishop";

			identifier += pieceType.equals(PieceType.BISHOP_R) ? " (right)"
					: " (left)";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
	 * model.game.Position)
	 */
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position pos) {
		boolean result = true;

		if (this.pos.getRank() == pos.getRank()
				|| this.pos.getFile() == pos.getFile()) {
			result = false;
		}

		if (Math.abs(this.pos.getRank() - pos.getRank()) 
				!= Math.abs(this.pos.getFile() - pos.getFile())) {
			result = false;
		}

		int rowOffset, colOffset;

		if (this.pos.getFile() < pos.getFile()) {
			colOffset = 1;
		} else {
			colOffset = -1;
		}

		if (this.pos.getRank() < pos.getRank()) {
			rowOffset = 1;
		} else {
			rowOffset = -1;
		}

		for (int x = this.pos.getFile() + colOffset, y = this.pos.getRank() + rowOffset; x != pos.getFile(); x += colOffset) {
			if (cell[x][y].getPiece() != null) {
				result = false;
			}
			
			y += rowOffset;
		}

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "B";
	}

}
