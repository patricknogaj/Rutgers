/**
 * Pawn.java
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
public final class Pawn extends Piece {

	private boolean firstMove = true;
	private boolean checkEnpassant = false;

	/**
	 * Parameterized constructor
	 * 
	 * @param pieceType the PieceType to assign
	 * @param color     the Color of a Player's PieceSet
	 */
	public Pawn(PieceType pieceType, PieceType.Color color) {
		super(color);

		switch (pieceType) {
		case PAWN_0:
		case PAWN_1:
		case PAWN_2:
		case PAWN_3:
		case PAWN_4:
		case PAWN_5:
		case PAWN_6:
		case PAWN_7:
			this.pieceType = pieceType;
			identifier += "Pawn";
			break;
		default:
			this.pieceType = null;
			identifier += " (invalid)";

			System.err.println("ERROR: Set this piece to either "
					+ "PieceType.PAWN_n, n being [0 - 7]!");

			break;
		}

		switch (this.pieceType) {
		case PAWN_0:
			identifier += "   (1)";
			break;
		case PAWN_1:
			identifier += "   (2)";
			break;
		case PAWN_2:
			identifier += "   (3)";
			break;
		case PAWN_3:
			identifier += "   (4)";
			break;
		case PAWN_4:
			identifier += "   (5)";
			break;
		case PAWN_5:
			identifier += "   (6)";
			break;
		case PAWN_6:
			identifier += "   (7)";
			break;
		case PAWN_7:
			identifier += "   (8)";
			break;
		default:
			break;
		}
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

		if (this.isWhite()) {
			if (this.posRef.getRank() < posRef.getRank()) {
				if (firstMove) {
					if ((Math.abs(posRef.getRank() - this.posRef.getRank()) == 2
							|| Math.abs(
									posRef.getRank() - this.posRef.getRank()) == 1)
							&& (this.posRef.getFile() == posRef.getFile()
									&& (cell[posRef.getFile()][posRef.getRank()]
											.getPiece() == null))) {
						result = true;
						firstMove = false;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() != null
							&& cell[posRef.getFile()][posRef.getRank()].getPiece()
									.isBlack()) {
						result = true;
						firstMove = false;
					}
				} else {
					if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& this.posRef.getFile() == posRef.getFile()
							&& (cell[posRef.getFile()][posRef.getRank()]
									.getPiece() == null)) {
						result = true;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() != null) {
						if (cell[posRef.getFile()][posRef.getRank()].getPiece()
								.isBlack())
							result = true;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() == null) {
						checkEnpassant = true;
						if (checkEnpassant) {
							if (cell[posRef.getFile()][posRef.getRank()].getLastMove()
									.getLastPiece().isPawn() == this.isPawn()) {
								if (this.posRef
										.getRank() == cell[posRef.getFile()][posRef
												.getRank()].getLastMove()
														.getEndPosition()
														.getRank()) {
									Position pieceEndPos = cell[posRef
											.getFile()][posRef.getRank()]
													.getLastMove()
													.getEndPosition();
									Position pieceStartPos = cell[posRef
											.getFile()][posRef.getRank()]
													.getLastMove()
													.getStartPosition();
									if (this.posRef.getRank() == 4
											&& pieceEndPos.getRank() == 4) {
										if (Math.abs(pieceStartPos.getRank()
												- pieceEndPos.getRank()) == 2) {
											Piece taken = cell[pieceEndPos.getFile()][pieceEndPos.getRank()].getPiece();
											taken.makeDead();
											
											cell[pieceEndPos
													.getFile()][pieceEndPos
															.getRank()]
																	.setPieceNullAtPosition(
																			pieceEndPos
																					.getFile(),
																			pieceEndPos
																					.getRank());
											result = true;
										}
									}
								} else {
									result = false;
								}
							}
						}
					}
				}
			}
		} else if (this.isBlack()) {
			if (posRef.getRank() < this.posRef.getRank()) {
				if (firstMove) {
					if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 2
							|| Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
									&& (this.posRef.getFile() == posRef.getFile()
											&& (cell[posRef.getFile()][posRef
													.getRank()]
															.getPiece() == null))) {
						result = true;
						firstMove = false;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() != null
							&& cell[posRef.getFile()][posRef.getRank()].getPiece()
									.isWhite()) {
						result = true;
						firstMove = false;
					}
				} else {
					if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& this.posRef.getFile() == posRef.getFile()
							&& (cell[posRef.getFile()][posRef.getRank()]
									.getPiece() == null)) {
						result = true;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() != null) {
						if (cell[posRef.getFile()][posRef.getRank()].getPiece()
								.isWhite())
							result = true;
					} else if (Math.abs(posRef.getRank() - this.posRef.getRank()) == 1
							&& Math.abs(posRef.getFile() - this.posRef.getFile()) == 1
							&& cell[posRef.getFile()][posRef.getRank()]
									.getPiece() == null) {
						checkEnpassant = true;
						if (checkEnpassant) {
							if (cell[posRef.getFile()][posRef.getRank()].getLastMove()
									.getLastPiece().isPawn() == this.isPawn()) {
								if (this.posRef
										.getRank() == cell[posRef.getFile()][posRef
												.getRank()].getLastMove()
														.getEndPosition()
														.getRank()) {
									Position pieceEndPos = cell[posRef
											.getFile()][posRef.getRank()]
													.getLastMove()
													.getEndPosition();
									Position pieceStartPos = cell[posRef
											.getFile()][posRef.getRank()]
													.getLastMove()
													.getStartPosition();
									if (this.posRef.getRank() == 3
											&& pieceEndPos.getRank() == 3) {
										if (Math.abs(pieceStartPos.getRank()
												- pieceEndPos.getRank()) == 2) {
											Piece taken = cell[pieceEndPos.getFile()][pieceEndPos.getRank()].getPiece();
											taken.makeDead();
											cell[pieceEndPos
													.getFile()][pieceEndPos
															.getRank()]
																	.setPieceNullAtPosition(
																			pieceEndPos
																					.getFile(),
																			pieceEndPos
																					.getRank());
											result = true;
										}
									}
								} else {
									result = false;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public void toggleFirstMove() {
		firstMove = firstMove ? false : true;
	}

	@Override
	public String toString() {
		return super.toString() + "P";
	}

}
