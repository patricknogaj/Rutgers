/**
 * Knight.java
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
public class Knight extends Piece {

	/**
	 * Parameterized constructor
	 * 
	 * @param pieceType the PieceType to assign
	 * @param color     the Color of a Player's PieceSet
	 */
	public Knight(PieceType pieceType, PieceType.Color color) {
		super(color);

		this.pieceType = pieceType.equals(PieceType.KNIGHT_R)
				|| pieceType.equals(PieceType.KNIGHT_L) ? pieceType : null;

		if (this.pieceType == null) {
			System.err.println("ERROR: Set this piece to either "
					+ "PieceType.KNIGHT_R or PieceType.KNIGHT_L!");
			identifier += " (invalid)";
		} else {
			identifier += "Knight";

			identifier += pieceType.equals(PieceType.KNIGHT_R) ? " (right)"
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
		boolean result = false;
		
		if (Math.abs(pos.getFile() - this.pos.getFile()) == 2
				&& Math.abs(pos.getRank() - this.pos.getRank()) == 1) {
			result = true;
		}

		if (Math.abs(pos.getFile() - this.pos.getFile()) == 1
				&& Math.abs(pos.getRank() - this.pos.getRank()) == 2) {
			result = true;
		}

		return result;
		
		//@formatter:off
		/*
		boolean result = false;
		
		final int deltaFile = Math.abs(pos.getFile() - this.pos.getFile());
		final int deltaRank = Math.abs(pos.getRank() - this.pos.getRank());
		
		result = (deltaFile == 2 && deltaRank == 1);
		result = (deltaFile == 1 && deltaRank == 2);
		
		return result;
		*/
		//@formatter:on
	}

	@Override
	public String toString() {
		return super.toString() + "N";
	}
}
