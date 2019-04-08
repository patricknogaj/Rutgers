/**
 * Player.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

import model.PieceType;
import model.chess_set.Board;
import model.chess_set.Piece;
import model.chess_set.PieceSet;

/**
 * Represents a participant in a Chess game
 * 
 * @version Mar 5, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
class Player {

	private PieceType.Color color;
	private PieceSet pieceSet;

	/**
	 * Parameterized constructor
	 * 
	 * @param color the Color associated with a Player's PieceSet
	 */
	Player(PieceType.Color color) {
		this.color = color;
		pieceSet = null;
	}

	/**
	 * Assign a PieceSet to a Player given a Board
	 * 
	 * @param board the current Board instance used during a match
	 * 
	 * @return true if successful, false otherwise
	 */
	boolean assignPieceSet(Board board) {
		boolean result = false;

		if (pieceSet == null) {
			PieceSet whiteSet = board.getWhiteSet();
			PieceSet blackSet = board.getBlackSet();

			boolean playerIsWhite = color.equals(whiteSet.getPieceSetColor());

			pieceSet = playerIsWhite ? whiteSet : blackSet;

			result = true;
		}

		return result;
	}

	/**
	 * Player makes a request to play a move
	 * 
	 * @param board         the current Board instance used during a match
	 * @param piecePosition the Position of a chosen Piece
	 * @param newPosition   the Position desired by the Player for a chosen
	 *                      Piece
	 * @param promo         integer that represents the piece to promote to (if
	 *                      != -1)
	 * 
	 * @return true if successful, false otherwise
	 */
	boolean playMove(Board board, Position piecePosition, Position newPosition,
			int promo) {
		boolean result = false;

		boolean requestDiffersFromNewPosition = (piecePosition
				.equals(newPosition) == false);

		if (requestDiffersFromNewPosition) {
			Piece pieceRequested = pieceSet.getPieceByPosition(piecePosition);

			if (pieceRequested == null) {
				String error = String.format(
						"ERROR: No %s piece at position %s exists.", color,
						piecePosition);
				System.err.println(error);
			} else {
				PieceType promoType = null;

				switch (promo) {
				case 1:
					promoType = PieceType.QUEEN;
					break;
				case 3:
					promoType = PieceType.BISHOP_R;
					break;
				case 5:
					promoType = PieceType.KNIGHT_R;
					break;
				case 7:
					promoType = PieceType.ROOK_R;
					break;
				}

				result = board.movePiece(pieceRequested, pieceSet, newPosition,
						promoType);
			}
		}

		return result;
	}

	/**
	 * Prints the Player's pieceSet
	 */
	public void printPieceSet() {
		System.out.println(pieceSet);
	}
}
