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
public final class Bishop extends Piece {
	
	private PieceType promotionPawnType;

	/**
	 * Parameterized constructor
	 * 
	 * @param pieceType the PieceType to assign
	 * @param color     the Color of a Player's PieceSet
	 */
	public Bishop(PieceType pieceType, PieceType promotionPawnType, PieceType.Color color) {
		super(color);

		this.pieceType = pieceType.equals(PieceType.BISHOP_R)
				|| pieceType.equals(PieceType.BISHOP_L) ? pieceType : null;
		
		this.promotionPawnType = promotionPawnType;

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
	
	/**
	 * Returns null if an original Piece, but if from promotion -- return
	 * a PieceType.PAWN
	 * 
	 * @return the former PAWN type of this Bishop, if from a promotion
	 */
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
	public boolean isMoveLegal(Cell[][] cell, Position pos) {
		/*
		boolean result = true;
		
		boolean movingRight = false;
		
		int colOffset = 0;
		int rowOffset = 0;
		
		int x = 0;
		int y = 0;
		
		int thisPosRefRank = this.posRef.getRank();
		int thisPosRefFile = this.posRef.getFile();
		int posRefRank = pos.getRank();
		int posRefFile = pos.getFile();
		
		int deltaRank = Math.abs(thisPosRefRank - posRefRank);
		int deltaFile = Math.abs(thisPosRefFile - posRefFile);
		
		boolean equalRank = thisPosRefRank == posRefRank;
		boolean equalFile = thisPosRefFile == posRefFile;
		
		boolean moveRightXIncreases = false;
		boolean moveLeftXDecreases = false;
		
		boolean pieceFoundAlongPath = false;
		

		result = (equalRank || equalFile) ? false : result;
		result = (deltaRank != deltaFile) ? false : result;
		
		colOffset = thisPosRefFile < posRefFile ? 1 : -1;
		rowOffset = thisPosRefRank < posRefRank ? 1 : -1;
		
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

		return result;
		*/
		
		
		boolean result = true;

		if (this.posRef.getRank() == pos.getRank()
				|| this.posRef.getFile() == pos.getFile()) {
			result = false;
		}

		if (Math.abs(this.posRef.getRank() - pos.getRank()) != Math
				.abs(this.posRef.getFile() - pos.getFile())) {
			result = false;
		}

		int rowOffset, colOffset;
		boolean movingRight = false;
		
		if (this.posRef.getFile() < pos.getFile()) {
			// 1 to the right
			colOffset = 1;
			movingRight = true;
		} else {
			// 1 to the left
			colOffset = -1;
		}

		if (this.posRef.getRank() < pos.getRank()) {
			// Moving 1 up
			rowOffset = 1;
		} else {
			// Moving 1 down
			rowOffset = -1;
		}
		
		if (movingRight) {
			for (int x = this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset;
					x < pos.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
					break;
				}
				
				y += rowOffset;
			}
		} else {
			for (int x =  this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset;
					x > pos.getFile(); x += colOffset) {
				if (cell[x][y].getPiece() != null) {
					result = false;
					break;
				}
				
				y += rowOffset;
			}
		}
		

		/* ORIGINAL CODE
		for (int x = this.posRef.getFile() + colOffset, 
					y = this.posRef.getRank() + rowOffset; 
				x != pos.getFile(); 
				x += colOffset) {			
			if (cell[x][y].getPiece() != null) {
				result = false;
				break;
			}

			y += rowOffset;
		}
		*/

		return result;
	}

	@Override
	public String toString() {
		return super.toString() + "B";
	}

}
