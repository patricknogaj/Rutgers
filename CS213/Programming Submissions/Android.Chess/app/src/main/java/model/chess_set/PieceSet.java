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
 * Represents a complete 'set' of Pieces for the chess Board. Two instances of
 * PieceSet are owned by Board and referred to by two Players.
 * 
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class PieceSet {

	private final int PIECE_COUNT = 16;

	private PieceType.Color color;
	private Piece[] pieceArray;		// all instances of Piece reside here

	/**
	 * Parameterized constructor
	 * 
	 * @param color the Color of a Player's PieceSet
	 */
	PieceSet(PieceType.Color color) {
		this.color = color;

		pieceArray = new Piece[PIECE_COUNT];

		Piece king = new King(color);
		Piece queen = new Queen(color, null);
		Piece bishop_l = new Bishop(PieceType.BISHOP_L, null, color);
		Piece bishop_r = new Bishop(PieceType.BISHOP_R, null, color);
		Piece knight_l = new Knight(PieceType.KNIGHT_L, null, color);
		Piece knight_r = new Knight(PieceType.KNIGHT_R, null, color);
		Piece rook_l = new Rook(PieceType.ROOK_L, null, color);
		Piece rook_r = new Rook(PieceType.ROOK_R, null, color);
		Piece pawn_0 = new Pawn(PieceType.PAWN_0, color);
		Piece pawn_1 = new Pawn(PieceType.PAWN_1, color);
		Piece pawn_2 = new Pawn(PieceType.PAWN_2, color);
		Piece pawn_3 = new Pawn(PieceType.PAWN_3, color);
		Piece pawn_4 = new Pawn(PieceType.PAWN_4, color);
		Piece pawn_5 = new Pawn(PieceType.PAWN_5, color);
		Piece pawn_6 = new Pawn(PieceType.PAWN_6, color);
		Piece pawn_7 = new Pawn(PieceType.PAWN_7, color);

		pieceArray[PieceType.KING.ordinal()] = king;
		pieceArray[PieceType.QUEEN.ordinal()] = queen;
		pieceArray[PieceType.BISHOP_L.ordinal()] = bishop_l;
		pieceArray[PieceType.BISHOP_R.ordinal()] = bishop_r;
		pieceArray[PieceType.KNIGHT_L.ordinal()] = knight_l;
		pieceArray[PieceType.KNIGHT_R.ordinal()] = knight_r;
		pieceArray[PieceType.ROOK_L.ordinal()] = rook_l;
		pieceArray[PieceType.ROOK_R.ordinal()] = rook_r;
		pieceArray[PieceType.PAWN_0.ordinal()] = pawn_0;
		pieceArray[PieceType.PAWN_1.ordinal()] = pawn_1;
		pieceArray[PieceType.PAWN_2.ordinal()] = pawn_2;
		pieceArray[PieceType.PAWN_3.ordinal()] = pawn_3;
		pieceArray[PieceType.PAWN_4.ordinal()] = pawn_4;
		pieceArray[PieceType.PAWN_5.ordinal()] = pawn_5;
		pieceArray[PieceType.PAWN_6.ordinal()] = pawn_6;
		pieceArray[PieceType.PAWN_7.ordinal()] = pawn_7;
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
		for (int i = 0; i < pieceArray.length; i++) {
			if (pieceArray[i].posRef.equals(pos)) {
				return pieceArray[i];
			}
		}

		return null;
	}

	/**
	 * Accessor to retrieve a Piece by PieceType
	 * 
	 * @param pieceType the PieceType of a desired Piece
	 * 
	 * @return A Piece within the PieceSet
	 */
	Piece getPieceByType(PieceType pieceType) {
		return pieceArray[pieceType.ordinal()];
	}
	
	/**
	 * Mutator, called by Board::undoMovePiece, to demote a promoted
	 * pawn
	 * 
	 * @param pawnToRestore the desired Pawn to restore
	 */
	void demotePawn(Pawn pawnToRestore) {
		PieceType previousType = pawnToRestore.pieceType;
		pieceArray[previousType.ordinal()] = pawnToRestore;
	}
	
	/**
	 * Promotes a Pawn to a Queen, Bishop, Knight, or Rook.
	 * Precondition: @see Board.java, line 276
	 * promoType must be QUEEN, BISHOP_R/L, KNIGHT_R/L, or ROOK_R/L.
	 * 
	 * @param piece the Pawn to promote
	 * @param promoType Queen, Bishop, Knight, or Rook
	 * @param color White, or Black
	 * 
	 * @return a promoted Pawn -- a new Queen, Bishop, Knight, or Rook
	 */
	Piece promotePawn(Piece piece, PieceType promoType, PieceType.Color color) {
		Piece promo = null;
		
		switch (promoType) {
		case QUEEN:
			//promo = new Queen(color);
			promo = new Queen(color, piece.pieceType);
			break;
		case BISHOP_R:
			switch (piece.pieceType) {
			case PAWN_0:
			case PAWN_1:
			case PAWN_2:
			case PAWN_3:
				promo = new Bishop(PieceType.BISHOP_L, piece.pieceType, color);				
				break;
			case PAWN_4:
			case PAWN_5:
			case PAWN_6:
			case PAWN_7:
				promo = new Bishop(PieceType.BISHOP_R, piece.pieceType,color);
				break;
			default:
				break;
			}

			break;
		case KNIGHT_R:
			switch (piece.pieceType) {
			case PAWN_0:
			case PAWN_1:
			case PAWN_2:
			case PAWN_3:
				promo = new Knight(PieceType.KNIGHT_L, piece.pieceType, color);
				break;
			case PAWN_4:
			case PAWN_5:
			case PAWN_6:
			case PAWN_7:
				promo = new Knight(PieceType.KNIGHT_R, piece.pieceType, color);
				break;
			default:
				break;
			}
			
			break;
		case ROOK_R:
			switch (piece.pieceType) {
			case PAWN_0:
			case PAWN_1:
			case PAWN_2:
			case PAWN_3:
				promo = new Rook(PieceType.ROOK_L, piece.pieceType, color);
				break;
			case PAWN_4:
			case PAWN_5:
			case PAWN_6:
			case PAWN_7:
				promo = new Rook(PieceType.ROOK_R, piece.pieceType, color);
				break;
			default:
				break;
			}
			
			break;
		default:
			System.err.println(
					"Can only promote to QUEEN, BISHOP, " + "KNIGHT, or ROOK.");
			break;
		}
		
		if (promo != null) {
			promo.makeAlive();
			promo.posRef = piece.posRef;
			pieceArray[piece.pieceType.ordinal()] = promo;			
		}
		
		return promo == null ? piece : promo;
	}

	/**
	 * Returns a string representation of the PieceSet (used for debugging)
	 */
	@Override
	public String toString() {
		String str = "";
		str += "PIECESET LOG (" + color + ") ------------------------------------------------\n";
		str += "Symbol\tIdentifier\t\tPosition\tAlive\tPromoted From\n";
		str += "---------------------------------------------------------------------\n";

	
		for (int i = 0; i < pieceArray.length; i++) {
			str += pieceArray[i].toString() + "\t";
			str += pieceArray[i].identifier + "\t";
			str += pieceArray[i].posRef + "\t\t";
			str += pieceArray[i].alive + "\t";
			
			
			if (pieceArray[i] instanceof Queen) {
				Queen q = (Queen)(pieceArray[i]);
				PieceType promoFrom = q.getPromotionPawnType();
				
				str += promoFrom == null ? "(original)" : promoFrom;
			} else if (pieceArray[i] instanceof Bishop) {
				Bishop b = (Bishop)(pieceArray[i]);
				
				PieceType promoFrom = b.getPromotionPawnType();
				
				str += promoFrom == null ? "(original)" : promoFrom;
			} else if (pieceArray[i] instanceof Knight) {
				Knight n = (Knight)(pieceArray[i]);
				
				PieceType promoFrom = n.getPromotionPawnType();
				
				str += promoFrom == null ? "(original)" : promoFrom;
			} else if (pieceArray[i] instanceof Rook) {
				Rook r = (Rook)(pieceArray[i]);
				
				PieceType promoFrom = r.getPromotionPawnType();
				
				str += promoFrom == null ? "(original)" : promoFrom;
			}

			str += "\n";
		}

		return str + "\n";
	}
}
