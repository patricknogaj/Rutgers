/**
 * PieceSet.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set;

import model.PieceType;
import model.chess_set.piecetypes.*;
import model.game.Position;

/**
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 *
 */
public class PieceSet {

	private int PIECE_COUNT = 16;

	private PieceType.Color color;
	private Piece[] piece;

	/**
	 * Parameterized constructor
	 * 
	 * @param color the Color of a Player's PieceSet
	 */
	PieceSet(PieceType.Color color) {
		this.color = color;

		piece = new Piece[PIECE_COUNT];

		Piece king = new King(color);
		Piece queen = new Queen(color);
		Piece bishop_l = new Bishop(PieceType.BISHOP_L, color);
		Piece bishop_r = new Bishop(PieceType.BISHOP_R, color);
		Piece knight_l = new Knight(PieceType.KNIGHT_L, color);
		Piece knight_r = new Knight(PieceType.KNIGHT_R, color);
		Piece rook_l = new Rook(PieceType.ROOK_L, color);
		Piece rook_r = new Rook(PieceType.ROOK_R, color);
		Piece pawn_0 = new Pawn(PieceType.PAWN_0, color);
		Piece pawn_1 = new Pawn(PieceType.PAWN_1, color);
		Piece pawn_2 = new Pawn(PieceType.PAWN_2, color);
		Piece pawn_3 = new Pawn(PieceType.PAWN_3, color);
		Piece pawn_4 = new Pawn(PieceType.PAWN_4, color);
		Piece pawn_5 = new Pawn(PieceType.PAWN_5, color);
		Piece pawn_6 = new Pawn(PieceType.PAWN_6, color);
		Piece pawn_7 = new Pawn(PieceType.PAWN_7, color);

		piece[PieceType.KING.ordinal()] = king;
		piece[PieceType.QUEEN.ordinal()] = queen;
		piece[PieceType.BISHOP_L.ordinal()] = bishop_l;
		piece[PieceType.BISHOP_R.ordinal()] = bishop_r;
		piece[PieceType.KNIGHT_L.ordinal()] = knight_l;
		piece[PieceType.KNIGHT_R.ordinal()] = knight_r;
		piece[PieceType.ROOK_L.ordinal()] = rook_l;
		piece[PieceType.ROOK_R.ordinal()] = rook_r;
		piece[PieceType.PAWN_0.ordinal()] = pawn_0;
		piece[PieceType.PAWN_1.ordinal()] = pawn_1;
		piece[PieceType.PAWN_2.ordinal()] = pawn_2;
		piece[PieceType.PAWN_3.ordinal()] = pawn_3;
		piece[PieceType.PAWN_4.ordinal()] = pawn_4;
		piece[PieceType.PAWN_5.ordinal()] = pawn_5;
		piece[PieceType.PAWN_6.ordinal()] = pawn_6;
		piece[PieceType.PAWN_7.ordinal()] = pawn_7;
	}

	/**
	 * Accessor to retrieve a Piece by PieceType
	 * 
	 * @param pieceType the PieceType of a desired Piece
	 * 
	 * @return A Piece within the PieceSet
	 */
	public Piece getPiece(PieceType pieceType) {
		return piece[pieceType.ordinal()];
	}
	
	/**
	 * Accessor to retrieve pieceSet
	 * @return A PieceSet object
	 */
	public PieceSet getPieceSet() {
		return this;
	}

	/**
	 * Routine to promote a Pawn to a desired PieceType
	 * 
	 * @param pawnPiece the Pawn Piece to promote
	 * @param promoType the PieceType of the Piece to promote
	 * @return returns a Pice that was promoted
	 */
	public Piece promotePawn(Piece pawnPiece, PieceType promoType) {
		Piece promo = null;
		
		if (pawnPiece.isPawn() && promoType != null) {

			switch (promoType) {
			case QUEEN:
				promo = pawnPiece.isWhite() ? new Queen(PieceType.Color.WHITE)
						: new Queen(PieceType.Color.BLACK);

				promo.pos = pawnPiece.pos;
				piece[pawnPiece.pieceType.ordinal()] = promo;

				break;
			case BISHOP_R:
				switch (pawnPiece.pieceType) {
				case PAWN_0:
				case PAWN_1:
				case PAWN_2:
				case PAWN_3:
					promo = pawnPiece.isWhite()
							? new Bishop(PieceType.BISHOP_L,
									PieceType.Color.WHITE)
							: new Bishop(PieceType.BISHOP_L,
									PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				case PAWN_4:
				case PAWN_5:
				case PAWN_6:
				case PAWN_7:
					promo = pawnPiece.isWhite()
							? new Bishop(PieceType.BISHOP_R,
									PieceType.Color.WHITE)
							: new Bishop(PieceType.BISHOP_R,
									PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				default:
					break;
				}

				break;
			case KNIGHT_R:
				switch (pawnPiece.pieceType) {
				case PAWN_0:
				case PAWN_1:
				case PAWN_2:
				case PAWN_3:
					promo = pawnPiece.isWhite()
							? new Knight(PieceType.KNIGHT_L,
									PieceType.Color.WHITE)
							: new Knight(PieceType.KNIGHT_L,
									PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				case PAWN_4:
				case PAWN_5:
				case PAWN_6:
				case PAWN_7:
					promo = pawnPiece.isWhite()
							? new Knight(PieceType.KNIGHT_R,
									PieceType.Color.WHITE)
							: new Knight(PieceType.KNIGHT_R,
									PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				default:
					break;
				}
			case ROOK_R:
				switch (pawnPiece.pieceType) {
				case PAWN_0:
				case PAWN_1:
				case PAWN_2:
				case PAWN_3:
					promo = pawnPiece.isWhite()
							? new Rook(PieceType.ROOK_L, PieceType.Color.WHITE)
							: new Rook(PieceType.ROOK_L, PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				case PAWN_4:
				case PAWN_5:
				case PAWN_6:
				case PAWN_7:
					promo = pawnPiece.isWhite()
							? new Rook(PieceType.ROOK_R, PieceType.Color.WHITE)
							: new Rook(PieceType.ROOK_R, PieceType.Color.BLACK);

					promo.pos = pawnPiece.pos;
					piece[pawnPiece.pieceType.ordinal()] = promo;

					break;
				default:
					break;
				}
			default:
				System.err.println("Can only promote to QUEEN, BISHOP, "
						+ "KNIGHT, or ROOK.");
				break;
			}
		} else {
			System.err.println("Cannot promote non-Pawn Piece instances.");
		}
		
		return promo == null ? pawnPiece : promo;
	}

	/**
	 * Accessor to retrieve the Color of a Player's PieceSet
	 * 
	 * @return the Color of a Player's PieceSet
	 */
	public PieceType.Color getPieceSetColor() {
		return color;
	}

	/**
	 * Accessor to retrieve a Piece using the file and rank of a Position
	 * 
	 * @param pos The Position of the desired Piece
	 * 
	 * @return if found, the desired Piece, otherwise null
	 */
	public Piece getPieceByPosition(Position pos) {
		// Perhaps we should consider storing the Pieces
		// in a Dictionary (map) -- instead of this linear traversal
		// using an array. This would be easy to fix and it would
		// not affect outside classes.
		for (int i = 0; i < piece.length; i++) {
			if (piece[i].pos.equals(pos)) {
				return piece[i];
			}
		}

		return null;
	}

	/**
	 * Returns a string representation of the PieceSet (used for debugging)
	 */
	@Override
	public String toString() {
		String str = "";
		str += "PIECESET LOG (" + color + ") -------------------\n";
		str += "Symbol\tIdentifier\t\tPosition\n";
		str += "----------------------------------------\n";

		for (int i = 0; i < piece.length; i++) {
			str += piece[i].toString() + "\t";
			str += piece[i].identifier + "\t";
			str += piece[i].pos + "\t";

			str += "\n";
		}

		str += "\n";

		return str;
	}
}
