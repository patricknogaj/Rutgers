/**
 * Rook.java
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
public final class Rook extends Piece {

	private PieceType promotionPawnType;
	private boolean canCastle;

	/**
	 * Parameterized constructor
	 * 
	 * @param pieceType the PieceType to assign
	 * @param color     the Color of a Player's PieceSet
	 */
	public Rook(PieceType pieceType, PieceType promotionPawnType, PieceType.Color color) {
		super(color);
		canCastle = true;

		this.pieceType = pieceType.equals(PieceType.ROOK_R)
				|| pieceType.equals(PieceType.ROOK_L) ? pieceType : null;
		
		this.promotionPawnType = promotionPawnType;

		if (this.pieceType == null) {
			System.err.println("ERROR: Set this piece to either "
					+ "PieceType.ROOK_R or PieceType.ROOK_L!");
			identifier += " (invalid)";
		} else {
			identifier += "Rook";

			identifier += pieceType.equals(PieceType.ROOK_R) ? "   (right)"
					: "   (left)";
		}
	}
	
	/**
	 * Returns null if an original Piece, but if from promotion -- return
	 * a PieceType.PAWN
	 * 
	 * @return the former PAWN type of this Rook, if from a promotion
	 */
	public PieceType getPromotionPawnType() {
		return promotionPawnType;
	}

	public boolean canCastle() {
		return canCastle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.chess_set.Piece#isMoveLegal(model.chess_set.Board.Cell[][],
	 * model.game.Position)
	 */
	@Override
	public boolean isMoveLegal(Cell[][] cell, Position posRef) {
		/*
		boolean result = true;

		boolean movingRight = false;
		boolean movingUp = false;

		int x = 0;
		int y = 0;

		int thisPosRefRank = this.posRef.getRank();
		int thisPosRefFile = this.posRef.getFile();
		int posRefRank = posRef.getRank();
		int posRefFile = posRef.getFile();

		boolean unequalRank = thisPosRefRank != posRefRank;
		boolean unequalFile = thisPosRefFile != posRefFile;

		boolean moveRightXIncreases = false;
		boolean moveLeftXDecreases = false;
		boolean moveUpYIncreases = false;
		boolean moveDownYDecreases = false;

		boolean pieceFoundAlongPath = false;
		
		if (unequalRank && unequalFile) {
			result = false;
		}

		int offset = 0;

		if (unequalFile) {
			offset = thisPosRefFile < posRefFile ? 1 : -1;
			movingRight = offset == 1 ? true : false;

			x = thisPosRefFile + offset;

			moveRightXIncreases = movingRight && x < posRefFile;
			moveLeftXDecreases = !movingRight && x > posRefFile;

			while (moveRightXIncreases || moveLeftXDecreases) {
				pieceFoundAlongPath = cell[x][thisPosRefRank]
						.getPiece() != null;

				if (pieceFoundAlongPath) {
					result = false;
					break;
				}

				x += offset;
			}
		} else if (unequalRank) {
			offset = thisPosRefRank < posRefRank ? 1 : -1;
			movingUp = offset == 1 ? true : false;

			y = thisPosRefRank + offset;

			moveUpYIncreases = movingUp && y < posRefRank;
			moveDownYDecreases = !movingUp && y > posRefRank;

			while (moveUpYIncreases || moveDownYDecreases) {
				pieceFoundAlongPath = cell[thisPosRefFile][y]
						.getPiece() != null;

				if (pieceFoundAlongPath) {
					result = false;
					break;
				}

				y += offset;
			}
		}

		return result;
		*/
		
		boolean result = true;

		// This is to check if it is moving on one path aka not diagonal
		if (posRef.getRank() != this.posRef.getRank()
				&& posRef.getFile() != this.posRef.getFile()) {
			result = false;
		}

		// Utilized to check if next piece will be null
		int offset;

		if (posRef.getFile() != this.posRef.getFile()) {
			if (this.posRef.getFile() < posRef.getFile()) {
				offset = 1;
			} else {
				offset = -1;
			}

			for (int x = this.posRef.getFile() + offset; x != posRef
					.getFile(); x += offset) {
				if (cell[x][this.posRef.getRank()].getPiece() != null) {
					return false;
				}
			}
		}

		if (posRef.getRank() != this.posRef.getRank()) {
			if (this.posRef.getRank() < posRef.getRank()) {
				offset = 1;
			} else {
				offset = -1;
			}

			for (int x = this.posRef.getRank() + offset; x != posRef
					.getRank(); x += offset) {
				if (cell[this.posRef.getFile()][x].getPiece() != null) {
					return false;
				}
			}
		}

		return result;

	}

	@Override
	public String toString() {
		return super.toString() + "R";
	}

}
