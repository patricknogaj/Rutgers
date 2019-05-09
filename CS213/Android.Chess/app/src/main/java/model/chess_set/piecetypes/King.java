/**
 * King.java
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
import model.chess_set.Board.Cell;
import model.chess_set.Piece;
import model.game.Position;

/**
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class King extends Piece {

	private boolean castled;

	/**
	 * Parameterized constructor
	 *
	 * @param color the Color of a Player's PieceSet
	 */
	public King(PieceType.Color color) {
		super(color);
		pieceType = PieceType.KING;
		this.castled = false;

		identifier += "King      ";
	}

	/**
	 * Determines if an instance of King is castled, or not
	 * 
	 * @return true if King is castled, false otherwise
	 */
	public boolean isCastled() {
		return castled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
	 * model.game.Position)
	 */
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position posRef) {
		boolean result = false;

		if (Math.abs(this.posRef.getFile() - posRef.getFile()) <= 1
				&& (Math.abs(this.posRef.getRank() - posRef.getRank()) <= 1)) {
			if (cell[posRef.getFile()][posRef.getRank()].getPiece() == null) {
				result = true;
			} else if (cell[posRef.getFile()][posRef.getRank()].getPiece() != null
					&& this.matchesColor(
							cell[posRef.getFile()][posRef.getRank()].getPiece())) {
				result = false;
			} else if (cell[posRef.getFile()][posRef.getRank()].getPiece() != null
					&& !this.matchesColor(
							cell[posRef.getFile()][posRef.getRank()].getPiece())) {
				result = true;
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "K";
	}
}
