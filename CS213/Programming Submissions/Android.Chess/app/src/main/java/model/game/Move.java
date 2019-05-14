/**
 * Move.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

import java.time.LocalTime;

import model.PieceType;
import model.chess_set.Piece;
import model.chess_set.piecetypes.Bishop;
import model.chess_set.piecetypes.Knight;
import model.chess_set.piecetypes.Queen;
import model.chess_set.piecetypes.Rook;

/**
 * Represents a move within a Chess game, for logging purposes. Instances of
 * Move are created within Board and stored in a List within Board.
 * 
 * @version Mar 24, 2019
 * @author gemuelealudino
 */
public class Move {
	private Piece piece;

	private Position startPos;
	private Position endPos;
	private PieceType promotedFrom;

	private LocalTime localTime;

	private int moveNumber;
	
	private boolean willDraw;
	private boolean didDraw;

	/**
	 * Parameterized constructor
	 * 
	 * @param piece        the Piece participating in a Move
	 * @param startPos     the starting Position of piece
	 * @param endPos       the ending Position of piece
	 * @param moveNumber   the sequential move number within the game
	 * @param promotedFrom if piece is a Pawn, and a promotion took place, what
	 *                     pawn type
	 */
	public Move(Piece piece, Position startPos, Position endPos, int moveNumber,
			PieceType promotedFrom) {
		this.piece = piece;
		this.startPos = startPos;
		this.endPos = endPos;
		this.promotedFrom = promotedFrom;

		this.moveNumber = moveNumber;
		localTime = LocalTime.now();
		
		this.willDraw = false;
	}
	
	/**
	 * Mutator to log a player's acceptance of an opponent's draw
	 * 
	 * @param didDraw the state of didDraw
	 */
	public void setDidDraw(boolean didDraw) {
		this.didDraw = didDraw;
	}
	
	/**
	 * Accessor to retrieve a player's acceptance of an opponent's draw
	 * 
	 * @return the state of didDraw
	 */
	public boolean getDidDraw() {
		return didDraw;
	}
	
	/**
	 * Accessor to retrieve a player's request for a draw
	 * 
	 * @return the state of willDraw
	 */
	public boolean getWillDraw() {
		return willDraw;
	}
	
	/**
	 * Mutator to log a player's request for a draw
	 * 
	 * @param willDraw the state of willDraw
	 */
	public void setWillDraw(boolean willDraw) {
		this.willDraw = willDraw;
	}

	/**
	 * Accessor method to retrieve a Piece associated with a Move
	 * 
	 * @return the Piece corresponding to a Move
	 */
	public Piece getLastPiece() {
		return piece;
	}

	/**
	 * Accessor method to retrieve the starting Position of a Move
	 * 
	 * @return the Position object associated with a Move
	 */
	public Position getStartPosition() {
		return startPos;
	}

	/**
	 * Accessor method to retrieve the ending Position of a Move
	 * 
	 * @return the Position object associated with a Move
	 */
	public Position getEndPosition() {
		return endPos;
	}

	/**
	 * Accessor method to retrieve the former pawn type of a promoted piece
	 * 
	 * @return the PieceType (PAWN) of a promotee
	 */
	public PieceType getPromotedFrom() {
		return promotedFrom;
	}

	/**
	 * Accessor method to retrieve the LocalTime of a Move
	 * 
	 * @return the LocalTime object associated with a Move
	 */
	public LocalTime getLocalTime() {
		return localTime;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move m = (Move) (o);

			if (piece.equals(m.piece) && startPos.equals(m.startPos)
					&& localTime.equals(m.localTime)
					&& moveNumber == m.moveNumber) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		String str = "";

		if (promotedFrom != null) {
			str = "(not promoted)";
			if (piece instanceof Queen) {
				Queen q = (Queen) (piece);

				str = q.getPromotionPawnType().toString();
			} else if (piece instanceof Bishop) {
				Bishop b = (Bishop) (piece);

				str = b.getPromotionPawnType().toString();
			} else if (piece instanceof Knight) {
				Knight n = (Knight) (piece);

				str = n.getPromotionPawnType().toString();
			} else if (piece instanceof Rook) {
				Rook r = (Rook) (piece);

				str = r.getPromotionPawnType().toString();
			} else {
				str = "";
			}
		}

		return localTime.toString() + "\t" + moveNumber + "\t" + piece + "\t"
				+ startPos + "\t" + endPos + "\t" + str;
	}
}
