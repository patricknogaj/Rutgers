/**
 * Queen.java
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
public final class Queen extends Piece {
	
	private PieceType promotionPawnType;

	/**
	 * Parameterized constructor
	 *
	 * @param color the Color of a Player's PieceSet
	 */
	public Queen(PieceType.Color color, PieceType promotionPawnType) {
		super(color);
		pieceType = PieceType.QUEEN;
		this.promotionPawnType = promotionPawnType;
		
		identifier += "Queen     ";
	}
	
	public PieceType getPromotionPawnType() {
		return promotionPawnType;
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
		
		int colOffset = 0;
		int rowOffset = 0;
		
		int x = 0;
		int y = 0;
		
		int thisPosRefRank = this.posRef.getRank();
		int thisPosRefFile = this.posRef.getFile();
		int posRefRank = posRef.getRank();
		int posRefFile = posRef.getFile();
		
		int deltaRank = Math.abs(thisPosRefRank - posRefRank);
		int deltaFile = Math.abs(thisPosRefFile - posRefFile);
		
		boolean unequalRank = thisPosRefRank != posRefRank;
		boolean unequalFile = thisPosRefFile != posRefFile;
		
		boolean moveRightXIncreases = false;
		boolean moveLeftXDecreases = false;
		boolean moveUpYIncreases = false;
		boolean moveDownYDecreases = false;
		
		boolean pieceFoundAlongPath = false;
		
		
		if (deltaRank == deltaFile) {

			
			colOffset = thisPosRefFile < posRefFile ? 1 : -1;
			rowOffset = thisPosRefRank < posRefRank ? -1 : -1;
			
			movingRight = colOffset == 1 ? true : false;
			
			x = thisPosRefFile + colOffset;
			y = thisPosRefRank + rowOffset;
			
			moveRightXIncreases = movingRight && x < posRefFile;
			moveLeftXDecreases = !movingRight && x > posRefFile;
			
			while (moveRightXIncreases || moveLeftXDecreases) {
				pieceFoundAlongPath = cell[x][y].getPiece() != null;
				
				if (pieceFoundAlongPath) {
					result = false;
					break;
				}
			
				x += colOffset;
				y += rowOffset;
			}
			
		} else {

			
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
					pieceFoundAlongPath = 
							cell[x][thisPosRefRank].getPiece() != null;
					
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
					pieceFoundAlongPath = 
							cell[thisPosRefFile][y].getPiece() != null;
					
					if (pieceFoundAlongPath) {
						result = false;
						break;
					}
					
					y += offset;
				}
			}
		}
		
		return result;
		*/
		
		boolean result = true;

		if (Math.abs(this.posRef.getRank() - posRef.getRank()) == Math
				.abs(this.posRef.getFile() - posRef.getFile())) {
			int rowOffset, colOffset;

			if (this.posRef.getFile() < posRef.getFile()) {
				colOffset = 1;
			} else {
				colOffset = -1;
			}

			if (this.posRef.getRank() < posRef.getRank()) {
				rowOffset = 1;
			} else {
				rowOffset = -1;
			}

			for (int x = this.posRef.getFile() + colOffset, y = this.posRef.getRank()
					+ rowOffset; x != posRef.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
				}

				y += rowOffset;
			}
		} else {
			// This is to check if it is moving on one path aka not diagonal
			if (posRef.getRank() != this.posRef.getRank()
					&& posRef.getFile() != this.posRef.getFile()) {
				result = false;
			}

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
		}
		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "Q";
	}
}
