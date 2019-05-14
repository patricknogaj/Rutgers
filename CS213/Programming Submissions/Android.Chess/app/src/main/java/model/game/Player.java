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
 * Represents a participant in a Chess game. Two instances of Player are owned
 * by Game.
 * 
 * @version Mar 5, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
final class Player {

	private PieceType.Color color;
	PieceSet pieceSetRef; // ref to Game's Board's PieceSet

	private Board boardRef; // ref to Game's Board

	/**
	 * Parameterized constructor
	 * 
	 * @param color    the Color associated with a Player's PieceSet
	 * @param boardRef the Board instance associated with a game
	 */
	Player(PieceType.Color color, Board boardRef) {
		this.color = color;
		this.boardRef = boardRef;

		assignPieceSet();
	}

	/**
	 * Assign a PieceSet to this Player based on their PieceType.Color
	 * 
	 * @return true if PieceSet is successfully assigned to this Player, false
	 *         otherwise
	 */
	private boolean assignPieceSet() {
		boolean result = false;

		if (pieceSetRef == null) {
			PieceSet whiteSet = boardRef.getWhiteSet();
			PieceSet blackSet = boardRef.getBlackSet();

			boolean playerIsWhite = color.equals(whiteSet.getPieceSetColor());

			pieceSetRef = playerIsWhite ? whiteSet : blackSet;

			result = true;
		}

		return result;
	}

	/**
	 * Player makes a request to play a move
	 * 
	 * @param piecePosition the Position of a chosen Piece
	 * @param newPosition   the Position desired by the Player for a chosen
	 *                      Piece
	 * @param promo         integer that represents the piece to promote to (if
	 *                      != -1)
	 * 
	 * @return true if successful, false otherwise
	 */
	boolean playMove(Position piecePosition, Position newPosition, int promo) {
		boolean result = false;

		boolean requestDiffersFromNewPosition = (piecePosition
				.equals(newPosition) == false);

		if (requestDiffersFromNewPosition) {
			Piece pieceRequested = pieceSetRef
					.getPieceByPosition(piecePosition);

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

				result = boardRef.movePiece(pieceRequested, pieceSetRef,
						newPosition, promoType);
			}
		}

		return result;
	}

	/**
	 * Prints the Player's pieceSet
	 */
	void printPieceSet() {
		System.out.print(pieceSetRef);
	}
}
