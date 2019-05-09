/**
 * PieceType.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model;

/**
 * Encapsulates the different kinds of Piece within a chess Game.
 * 
 * @version Mar 5, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public enum PieceType {
	
	KING, QUEEN, BISHOP_L, BISHOP_R, KNIGHT_L, KNIGHT_R, ROOK_L, ROOK_R, PAWN_0,
	PAWN_1, PAWN_2, PAWN_3, PAWN_4, PAWN_5, PAWN_6, PAWN_7;

	/**
	 * Represents a Piece's color
	 * 
	 * @version Mar 9, 2019
	 * @author gemuelealudino
	 */
	public enum Color {
		WHITE, BLACK
	}
}
