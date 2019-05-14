/**
 * Board.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.chess_set;

import java.util.ArrayList;
import java.util.List;

import model.PieceType;
import model.chess_set.piecetypes.King;
import model.chess_set.piecetypes.Pawn;
import model.game.Move;
import model.game.Position;

/**
 * Represents a Chess board for a Chess game. Instances of Board are owned by
 * Game.
 * 
 * @version Mar 9, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Board {

	/**
	 * Represents a 'square' within a Chess board
	 * 
	 * @version Mar 3, 2019
	 * @author gemuelealudino
	 */
	public final class Cell {
		private Position loc;
		private Piece pieceRef;

		/**
		 * Parameterized constructor
		 * 
		 * @param file x axis coordinate (0-7 only)
		 * @param rank y axis coordinate (0-7 only)
		 */
		public Cell(int file, int rank) {
			loc = new Position(file, rank);
			pieceRef = null;
		}

		/**
		 * Accessor method to retrieve a Piece within a Cell
		 * 
		 * @return the Piece occupying a Cell within the Board
		 */
		public Piece getPiece() {
			return pieceRef;
		}

		/**
		 * Accessor method to retrieve the Position object within a Cell
		 * 
		 * @return the Position object associated with a Cell
		 */
		public Position getPosition() {
			return loc;
		}

		/**
		 * Accessor method to retrieve the last Move executed during gameplay
		 * 
		 * @return the most recent Move that was logged by Board
		 */
		public Move getLastMove() {
			if (moveList.isEmpty()) {
				return null;
			}

			boolean nullValue = true;

			Move lastMove = null;
			int i = 1;

			/**
			 * Within the moveList, kills are also stored. Kills are denoted by:
			 * 
			 * Move # is (-1) endPosition is null
			 * 
			 * We want the last move performed, disregarding the kill (if there
			 * was one).
			 */
			do {
				lastMove = moveList.get(moveList.size() - i);

				if (lastMove.getEndPosition() == null) {
					nullValue = true;
					++i;
				} else {
					nullValue = false;
				}

				if (i > moveList.size()) {
					break;
				}
			} while (nullValue);

			return lastMove;
		}

		/**
		 * Mutator method to set a Piece at a particular Cell null
		 * 
		 * @param file the file to retrieve
		 * @param rank the rank to retrieve
		 */
		public void setPieceNullAtPosition(int file, int rank) {
			// loc = new Position(file, rank);
			pieceRef = null;
		}

		/**
		 * Returns a string representation of the Piece occupying the cell, if
		 * there is a Piece present
		 */
		@Override
		public String toString() {
			return pieceRef == null ? "--" : pieceRef.toString();
		}
	}

	private static final int MAX_LENGTH_WIDTH = 8;

	private Cell[][] cell;

	private List<Move> moveList;
	private int moveCounter;
	private int killCounter;

	private PieceSet whiteSet;
	private PieceSet blackSet;

	private Position[] kingMoves;
	private boolean kingChecked;
	private boolean kingSafe;

	private boolean promoteWhite;
	private boolean promoteBlack;
	
	private boolean checkmate;
	
	private String outputWinner;

	private PieceType pawnPromoteType;
	
	private Pawn pawnSaved;
	
	private Move lastMoveUndone;
	private Move lastKillUndone;

	/**
	 * Default constructor
	 */
	public Board() {
		cell = new Cell[MAX_LENGTH_WIDTH][MAX_LENGTH_WIDTH];

		for (int file = 0; file < MAX_LENGTH_WIDTH; file++) {
			for (int rank = 0; rank < MAX_LENGTH_WIDTH; rank++) {
				cell[file][rank] = new Cell(file, rank);
			}
		}

		whiteSet = new PieceSet(PieceType.Color.WHITE);
		blackSet = new PieceSet(PieceType.Color.BLACK);

		assignWhitePieces();
		assignBlackPieces();

		moveList = new ArrayList<Move>();

		moveCounter = 0;
		killCounter = 0;

		kingMoves = new Position[MAX_LENGTH_WIDTH];
		kingSafe = true;

		promoteWhite = false;
		promoteBlack = false;
		
		checkmate = false;
		
		outputWinner = "(game still active)";

		pawnPromoteType = null;
		
		pawnSaved = null;
		
		lastMoveUndone = null;
		lastKillUndone = null;
	}

	/**
	 * Accessor to retrieve the PieceSet for a white-piece Player
	 * 
	 * @return the white PieceSet
	 */
	public PieceSet getWhiteSet() {
		return whiteSet;
	}

	/**
	 * Accessor to retrieve the PieceSet for a black-piece Player
	 * 
	 * @return the black PieceSet
	 */
	public PieceSet getBlackSet() {
		return blackSet;
	}

	/**
	 * Accessor to retrieve a particular Cell within the Board
	 * 
	 * @param pos the Position of the desired Cell
	 * 
	 * @return the Cell with the given file and rank of a Position
	 */
	Cell getCell(Position pos) {
		return cell[pos.getFile()][pos.getRank()];
	}
	
	/**
	 * Accessor to retrieve the moveList of a game
	 * 
	 * @return a moveList of a finished game, otherwise if game active -- null
	 */
	public List<Move> getMoveList() {
		/*
		if (outputWinner.equals("(game still active)")) {
			return null;
		} else {
			return moveList;
		}
		*/
		
		return moveList;
	}
	
	/**
	 * Returns the last move undone, for ChessActivity
	 * 
	 * @return the last move undone
	 */
	public Move getLastMoveUndone() {
		return lastMoveUndone;
	}
	
	/**
	 * Returns the last kill undon, for ChessActivity
	 * 
	 * @return the last kill undone
	 */
	public Move getLastKillUndone() {
		return lastKillUndone;
	}

	/**
	 * Accessor method to retrieve the last Move executed during gameplay
	 * 
	 * @return the most recent Move that was logged by Board
	 */
	public Move getLastMove() {
		if (moveList.isEmpty()) {
			return null;
		}

		boolean nullValue = true;

		Move lastMove = null;
		int i = 1;

		/**
		 * Within the moveList, kills are also stored. Kills are denoted by:
		 * 
		 * Move # is (-1) endPosition is null
		 * 
		 * We want the last move performed, disregarding the kill (if there was
		 * one).
		 */
		do {
			lastMove = moveList.get(moveList.size() - i);

			if (lastMove.getEndPosition() == null) {
				// if getEndPosition() == null, this 'move' was a KILL.
				// we continue iterating until we find a non-kill.
				nullValue = true;
				++i;
			} else {
				nullValue = false;
			}

			// safety precaution, bounds check.
			if (i > moveList.size()) {
				break;
			}
		} while (nullValue);

		return lastMove;
	}

	/**
	 * Retrieves the last kill from the moveList
	 * 
	 * @return the Move that describes the last piece to be killed, otherwise
	 *         null if there are no pieces killed
	 */
	public Move getLastKill() {
		if (moveList.isEmpty()) {
			return null;
		}

		boolean nonNullValue = false;

		Move lastKill = null;
		int i = 1;

		/**
		 * Within the moveList, kills are also stored. Kills are denoted by:
		 * 
		 * Move # is (-1) endPosition is null
		 * 
		 * We want the last kill performed, disregarding any normal moves
		 * logged.
		 */
		do {
			lastKill = moveList.get(moveList.size() - i);

			if (lastKill.getEndPosition() != null) {
				nonNullValue = true;
				++i;
			} else {
				nonNullValue = false;
			}

			if (i > moveList.size()) {
				break;
			}
		} while (nonNullValue);

		return lastKill;
	}
	
	/**
	 * Called by Game::undoMove, this will undo the last move in the move list,
	 * and if the last move is paired with a kill, that kill is undone as well
	 * 
	 */
	public void undoMovePiece() {		
		Move lastMove = getLastMove();
		Move lastKill = getLastKill();
	
		Position startPosKill = lastKill.getStartPosition();
		Position startPos = lastMove.getStartPosition();
		Position endPos = lastMove.getEndPosition();
		
		Piece piece = lastMove.getLastPiece();
		Piece pieceKill = lastKill.getLastPiece();
			
		PieceType promotedFrom = lastMove.getPromotedFrom();
		PieceType.Color thisColor = 
				piece.isWhite() ? PieceType.Color.WHITE : PieceType.Color.BLACK;
		
		Cell startPosCell = getCell(startPos);
		Cell endPosCell = getCell(endPos);
				
		/**
		 * Start by reversing a move.
		 */
		piece.posRef = startPos;		// restore the former position to piece
		
		endPosCell.pieceRef = null;		// remove piece from old end position
		
		startPosCell.pieceRef = piece;  // return piece to former start position
				
		lastMoveUndone = lastMove;
		moveList.remove(lastMove);			// remove the move from the list
		--moveCounter;					// decrement the move counter
		
		/**
		 * If an undo results in a pawn reverting back to a position
		 * prior to its first move, we must determine this and toggle
		 * the "firstMove" state for this pawn back to true.
		 */
		boolean pawnMovedOnlyOnce = true;
		
		if (piece.isPawn()) {
			// If piece (the piece from the last move) was a pawn...
			
			for (int i = 0; i < moveList.size() - 1; i++) {
				// Search e
				Piece curr = moveList.get(i).getLastPiece();
				
				// If piece is found anywhere in the list
				// (we've already removed the last move),
				// this means the pawn has moved more than once.
				if (piece == curr) {
					pawnMovedOnlyOnce = false;
					break;
				}
			}
		}
		
		/**
		 * Restoring the "firstMove" status of the Pawn in question
		 */
		if (piece.isPawn() && pawnMovedOnlyOnce) {
			Pawn pawn = (Pawn)(piece);
			pawn.toggleFirstMove();
		}
		
		if (moveList.size() == 0) {
			cell = new Cell[MAX_LENGTH_WIDTH][MAX_LENGTH_WIDTH];

			for (int file = 0; file < MAX_LENGTH_WIDTH; file++) {
				for (int rank = 0; rank < MAX_LENGTH_WIDTH; rank++) {
					cell[file][rank] = new Cell(file, rank);
				}
			}

			whiteSet = new PieceSet(PieceType.Color.WHITE);
			blackSet = new PieceSet(PieceType.Color.BLACK);

			assignWhitePieces();
			assignBlackPieces();

			//moveList = new ArrayList<Move>();

			moveCounter = 0;
			killCounter = 0;

			//kingMoves = new Position[MAX_LENGTH_WIDTH];
			kingSafe = true;

			promoteWhite = false;
			promoteBlack = false;
			
			checkmate = false;
			
			outputWinner = "(game still active)";

			pawnPromoteType = null;
			
			pawnSaved = null;
		}
		
		/**
		 * Reverse a promotion.
		 */
		if (promotedFrom != null) {
			// create a new pawn
			//Pawn pawn = new Pawn(promotedFrom, thisColor);
			Pawn pawn = pawnSaved;
						
			// assign the pawn position to its former position
			// before promotion
			//pawn.posRef = startPos;
			
			// piece is now pawn
			piece = pawn;
			
			// assign cell's piece reference to piece
			startPosCell.pieceRef = piece;
			
			
			piece.alive = true;
			
			PieceSet pieceSet = pawn.isWhite() ? whiteSet : blackSet;
			
			// Restore the promoted pawn to its previous form
			pieceSet.demotePawn(pawn);
		}
		
		
		/**
		 * Then reverse a kill.
		 */
		if (lastMove.getLocalTime().equals(lastKill.getLocalTime())) {			
			pieceKill.posRef = startPosKill;	// pieceKill's former pos
			
			endPosCell.pieceRef = pieceKill;	// put pieceKill back on board
			
			pieceKill.makeAlive();				// make the piece "alive"
			
			lastKillUndone = lastKill;
			moveList.remove(lastKill);	// remove the kill from the list
			--killCounter;				// decrement the kill counter
		}		
	}
	
	/**
	 * Requested Piece will be moved to a Cell corresponding to the file and
	 * rank of newPosition, provided newPosition is a legal move for a given
	 * Piece
	 * 
	 * @param piece       accessed by a Player for a move
	 * @param newPosition the new Position desired by the Player for a Piece
	 * @param promoType   the PieceType to promote param piece to (if eligible)
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean movePiece(Piece piece, PieceSet pieceSet,
			Position newPosition, PieceType promoType) {

		boolean result = false;

		Cell oldPositionCell = getCell(piece.posRef);
		Cell newPositionCell = getCell(newPosition);
		
		boolean pieceMoveLegal = piece.isMoveLegal(cell, newPosition);
		
		//System.out.println(piece.posRef + " " + newPosition);
		
		/* Prints state of board (cell[][]) line by line */
		for (int i = 0; i < cell.length; i++) {
			for (int j = 0; j < cell[i].length; j++) {
				System.out.println(cell[i][j].loc + " " + cell[i][j].pieceRef);
			}
		}
		
	
		King king = null;

		if (piece.isBlack()) {
			king = (King) whiteSet.getPieceByType(PieceType.KING);
		} else {
			king = (King) blackSet.getPieceByType(PieceType.KING);
		}

		kingSafe = isKingSafe(king, king.posRef);

		if (kingChecked) {
			if (piece.isKing()) {
				for (int i = 0; i < kingMoves.length; i++) {

					// System.out.println(kingMoves[i] + " " + isKingSafe(k,
					// kingMoves[i]));

					if (newPosition.equals(kingMoves[i])
							&& isKingSafe(king, kingMoves[i])) {
						kingChecked = false;
						pieceMoveLegal = true;
						result = true;

						break;
					} else {
						pieceMoveLegal = false;
						result = false;
					}
				}
			} else {
				pieceMoveLegal = false;
				result = false;
			}
		}

		if (pieceMoveLegal) {
			Piece other = newPositionCell.pieceRef;
			boolean pieceFoundAtNewPosition = other != null;

			if (pieceFoundAtNewPosition) {
				boolean allyPieceFound = piece.matchesColor(other);

				if (allyPieceFound) {
					// This prevents a Piece of the same color "taking"
					// another piece.
					result = false;
				} else {
					other.makeDead();
					// At this point, a "piece" is taken.
					newPositionCell.pieceRef = null;

					// result = true means we will allow our requested
					// piece to move to the cell with newPosition.
					result = true;
				}
			} else {
				// result = true means we will allow our requested
				// piece to move to the cell with newPosition.
				result = true;
			}

			// Since the Piece moved from the old location to the new
			// location,
			// the Cell will no longer have a reference to that Piece.

			/**
			 * If a successful move is made, piece will be evaluated by pieceSet
			 * to determine if piece is a promotable Pawn.
			 */
			if (result) {
				piece = promotePawn(piece, pieceSet, newPosition, promoType);

				if (kingSafe) {
					// This statement nullifies any reference to a Piece
					// for this Cell object. (Next line: piece will be
					// reassigned
					// to the newPositionCell.piece field).
					oldPositionCell.pieceRef = null;

					// This statement affects what Pieces print
					// at which cells when board.toString() is called.
					newPositionCell.pieceRef = piece;

					// This statement affects the internal position
					// data within a Piece object.
					piece.posRef = newPosition;
				} else {
					return false;
				}
			}

			++moveCounter;

			Move newestMove = new Move(piece, oldPositionCell.loc, piece.posRef,
					moveCounter, pawnPromoteType);
			moveList.add(newestMove);
			
			pawnPromoteType = null;

			if (other != null) {
				if (other.isAlive() == false) {
					--killCounter;
					Move death = new Move(other, piece.posRef, null,
							killCounter, null);
					moveList.add(death);
				}
			}
		}

		if (canCheck(piece)) {
			if (hasValidMoves(king)) {
				kingChecked = true;
			} else {
				checkmate(king);
			}
			
			if (checkmate) {
				return true;
			}

			System.out.println("Check");
		}

		return result;
	}
	
	/**
	 * Determines whether the WHITE player has won the game
	 * 
	 * @return true if WHITE player has won, false otherwise
	 */
	public boolean isWhiteWinner() {
		return checkmate && outputWinner.indexOf("White") >= 0;
	}
	
	/**
	 * Determines whether the BLACK player has won the game
	 * 
	 * @return true if BLACK player has won, false otherwise
	 */
	public boolean isBlackWinner() {
		return checkmate && outputWinner.indexOf("Black") >= 0;
	}

	/**
	 * Promotes a pawn to a desired piece, provided conditions are met
	 * 
	 * @param piece       the Piece, which is a Pawn to be promoted
	 * @param pieceSet    the pieceSet from which the Piece belongs
	 * @param newPosition the Position of the desired Cell for the PAWN
	 * @param promoType   the PieceType to promote the PAWN to
	 * 
	 * @return the promoted Piece
	 */
	public Piece promotePawn(Piece piece, PieceSet pieceSet,
			Position newPosition, PieceType promoType) {
		
		promoteWhite = promoType != null && piece.isWhite()
				&& newPosition.getRank() == 7;

		promoteBlack = promoType != null && piece.isBlack()
				&& newPosition.getRank() == 0;

		if (promoteWhite || promoteBlack) {
			PieceType.Color color = promoteWhite ? PieceType.Color.WHITE
					: PieceType.Color.BLACK;

			pawnPromoteType = piece.pieceType;
			
			if (piece instanceof Pawn) {
				pawnSaved = (Pawn)(piece);
			}
			piece = pieceSet.promotePawn(piece, promoType, color);
		}

		return piece;
	}
	
	/**
	 * Accessor to determine string output when checkmate occurs
	 * 
	 * @return "White wins" or "Black wins" or ""
	 */
	public String getOutputWinner() {
		return outputWinner;
	}
	
	/**
	 * Mutator to change the output message, if the game is still active
	 * 
	 * @param message the message to change
	 */
	public void setOutputWinner(String message) {
		if (outputWinner.equals("(game still active)")) {
			outputWinner = message;
		}
	}
	
	/**
	 * Accessor to determine if a checkmate has occurred
	 * 
	 * @return true, if the game has ended in a checkmate, false otherwise
	 */
	public boolean isCheckmate() {
		return checkmate;
	}

	/**
	 * Called by Game::readInput, after Game::whitePlayMove completes its call,
	 * this is used to send a message to a Game instance that a WHITE pawn was
	 * promoted.
	 * 
	 * @return If promoteWhite is true, the promoteWhite field will be reset to
	 *         false, and the method returns true -- otherwise, returns false
	 */
	public boolean checkPromoteWhite() {
		if (promoteWhite) {
			promoteWhite = false;
			return true;
		}

		return false;
	}

	/**
	 * Called by Game::readInput, after Game::blackPlayMove completes its call,
	 * this is used to send a message to a Game instance that a BLACK pawn was
	 * promoted.
	 * 
	 * @return If promoteBlack is true, the promoteBlack field will be reset to
	 *         false, and the method returns true -- otherwise, returns false
	 */
	public boolean checkPromoteBlack() {
		if (promoteBlack) {
			promoteBlack = false;
			return true;
		}

		return false;
	}

	/**
	 * Called by Game::readInput to determine the PieceType of a PAWN
	 * (PAWN_0, PAWN_1, ...)
	 * 
	 * @return the PieceType of a PAWN to be promoted
	 */
	public PieceType getPawnPromoteType() {
		return pawnPromoteType;
	}

	/**
	 * Prints the log of moves as per the moveList field (ArrayList)
	 */
	public String moveLogToString() {
		String str = "MOVE LOG (ALL PIECES) ---------------------------------------\n";

		str += "Time\t\tMove #\tPiece\tStart\tEnd\tPromoted From\n";
		str += "-------------------------------------------------------------\n";

		for (Move mp : moveList) {
			str += mp + "\n";
		}

		return str;
	}

	/**
	 * This checks to see if the King on opposing side is checked
	 * 
	 * @param piece - takes the current piece
	 * 
	 * @return true if opponent King is checked || false is not.
	 */
	private boolean canCheck(Piece piece) {
		boolean result = false;

		Position oppositeKing = null;
		Piece oppositeK = null;

		if (piece.isWhite()) {
			oppositeK = getBlackSet().getPieceByType(PieceType.KING);
			oppositeKing = oppositeK.posRef;
		} else if (piece.isBlack()) {
			oppositeK = getWhiteSet().getPieceByType(PieceType.KING);
			oppositeKing = oppositeK.posRef;
		}

		boolean isMoveLegal = piece.isMoveLegal(cell, oppositeKing);

		if (isMoveLegal) {
			result = true;
			kingChecked = true;
		}

		return result;
	}

	/**
	 * This checks to see if the King is safe.
	 * 
	 * @param k - king object
	 * @param p - position object
	 * 
	 * @return true if any piece on opponent can move to the King's position
	 */
	private boolean isKingSafe(King k, Position p) {
		boolean result = true;

		PieceSet opponent = null;
		opponent = k.isWhite() ? getBlackSet() : getWhiteSet();

		Piece BISH_L = opponent.getPieceByType(PieceType.BISHOP_L);
		Piece BISH_R = opponent.getPieceByType(PieceType.BISHOP_R);
		Piece KNIGHT_L = opponent.getPieceByType(PieceType.KNIGHT_L);
		Piece KNIGHT_R = opponent.getPieceByType(PieceType.KNIGHT_R);
		Piece ROOK_R = opponent.getPieceByType(PieceType.ROOK_R);
		Piece ROOK_L = opponent.getPieceByType(PieceType.ROOK_L);
		Piece PAWN_0 = opponent.getPieceByType(PieceType.PAWN_0);
		Piece PAWN_1 = opponent.getPieceByType(PieceType.PAWN_1);
		Piece PAWN_2 = opponent.getPieceByType(PieceType.PAWN_2);
		Piece PAWN_3 = opponent.getPieceByType(PieceType.PAWN_3);
		Piece PAWN_4 = opponent.getPieceByType(PieceType.PAWN_4);
		Piece PAWN_5 = opponent.getPieceByType(PieceType.PAWN_5);
		Piece PAWN_6 = opponent.getPieceByType(PieceType.PAWN_6);
		Piece PAWN_7 = opponent.getPieceByType(PieceType.PAWN_7);
		Piece QUEEN = opponent.getPieceByType(PieceType.QUEEN);

		if (ROOK_R.isMoveLegal(cell, p) || ROOK_L.isMoveLegal(cell, p)
				|| BISH_L.isMoveLegal(cell, p) || BISH_R.isMoveLegal(cell, p)
				|| PAWN_0.isMoveLegal(cell, p) || PAWN_1.isMoveLegal(cell, p)
				|| PAWN_2.isMoveLegal(cell, p) || PAWN_3.isMoveLegal(cell, p)
				|| PAWN_4.isMoveLegal(cell, p) || PAWN_5.isMoveLegal(cell, p)
				|| PAWN_6.isMoveLegal(cell, p) || PAWN_7.isMoveLegal(cell, p)
				|| QUEEN.isMoveLegal(cell, p) || KNIGHT_L.isMoveLegal(cell, p)
				|| KNIGHT_R.isMoveLegal(cell, p)) {
			result = false;
		}

		return result;
	}

	/**
	 * Checks to see the valid moves a King can make to decide if he will be
	 * checked or checkmated.
	 * 
	 * @param k - king object
	 * 
	 * @return true if King is able to move somewhere without being in risk of
	 *         check
	 */
	private boolean hasValidMoves(King k) {
		// caching away possible valid moves for a king
		kingMoves[0] = new Position(k.posRef.getFile() + 1, k.posRef.getRank());
		kingMoves[1] = new Position(k.posRef.getFile() - 1, k.posRef.getRank());
		kingMoves[2] = new Position(k.posRef.getFile(), k.posRef.getRank() + 1);
		kingMoves[3] = new Position(k.posRef.getFile(), k.posRef.getRank() - 1);
		kingMoves[4] = new Position(k.posRef.getFile() + 1,
				k.posRef.getRank() - 1);
		kingMoves[5] = new Position(k.posRef.getFile() - 1,
				k.posRef.getRank() - 1);
		kingMoves[6] = new Position(k.posRef.getFile() + 1,
				k.posRef.getRank() + 1);
		kingMoves[7] = new Position(k.posRef.getFile() - 1,
				k.posRef.getRank() + 1);

		// if all proposed moves are legal and king is safe with said moves,
		// the king has valid moves
		return ((k.isMoveLegal(cell, kingMoves[0])
				&& isKingSafe(k, kingMoves[0])
				|| (k.isMoveLegal(cell, kingMoves[1])
						&& isKingSafe(k, kingMoves[1])
						|| (k.isMoveLegal(cell, kingMoves[2])
								&& isKingSafe(k, kingMoves[2]))
						|| (k.isMoveLegal(cell, kingMoves[3])
								&& isKingSafe(k, kingMoves[3]))
						|| (k.isMoveLegal(cell, kingMoves[4])
								&& isKingSafe(k, kingMoves[4]))
						|| (k.isMoveLegal(cell, kingMoves[5])
								&& isKingSafe(k, kingMoves[5]))
						|| (k.isMoveLegal(cell, kingMoves[6])
								&& isKingSafe(k, kingMoves[6]))
						|| (k.isMoveLegal(cell, kingMoves[7])
								&& isKingSafe(k, kingMoves[7])))));
	}
	
	/**
	 * Executes upon checkmate
	 * 
	 * @param king king that was checked
	 */
	private void checkmate(King king) {
		outputWinner = "Checkmate\n";
		
		outputWinner += king.isWhite() ? "Black" : "White";
		outputWinner += " wins";
		checkmate = true;
	}

	/**
	 * Used during Board instantiation: initialize all Piece and Cell instances
	 * to their default starting positions prior to beginning a Chess match
	 */
	private void assignWhitePieces() {
		Piece king = whiteSet.getPieceByType(PieceType.KING);
		king.alive = true;
		king.posRef = cell[4][0].loc;
		cell[4][0].pieceRef = king;

		Piece queen = whiteSet.getPieceByType(PieceType.QUEEN);
		queen.alive = true;
		queen.posRef = cell[3][0].loc;
		cell[3][0].pieceRef = queen;

		Piece bishop_r = whiteSet.getPieceByType(PieceType.BISHOP_R);
		bishop_r.alive = true;
		bishop_r.posRef = cell[5][0].loc;
		cell[5][0].pieceRef = bishop_r;

		Piece bishop_l = whiteSet.getPieceByType(PieceType.BISHOP_L);
		bishop_l.alive = true;
		bishop_l.posRef = cell[2][0].loc;
		cell[2][0].pieceRef = bishop_l;

		Piece knight_r = whiteSet.getPieceByType(PieceType.KNIGHT_R);
		knight_r.alive = true;
		knight_r.posRef = cell[6][0].loc;
		cell[6][0].pieceRef = knight_r;

		Piece knight_l = whiteSet.getPieceByType(PieceType.KNIGHT_L);
		knight_l.alive = true;
		knight_l.posRef = cell[1][0].loc;
		cell[1][0].pieceRef = knight_r;

		Piece rook_r = whiteSet.getPieceByType(PieceType.ROOK_R);
		rook_r.alive = true;
		rook_r.posRef = cell[7][0].loc;
		cell[7][0].pieceRef = rook_r;

		Piece rook_l = whiteSet.getPieceByType(PieceType.ROOK_L);
		rook_l.alive = true;
		rook_l.posRef = cell[0][0].loc;
		cell[0][0].pieceRef = rook_l;

		Piece pawn_0 = whiteSet.getPieceByType(PieceType.PAWN_0);
		pawn_0.alive = true;
		pawn_0.posRef = cell[0][1].loc;
		cell[0][1].pieceRef = pawn_0;

		Piece pawn_1 = whiteSet.getPieceByType(PieceType.PAWN_1);
		pawn_1.alive = true;
		pawn_1.posRef = cell[1][1].loc;
		cell[1][1].pieceRef = pawn_1;

		Piece pawn_2 = whiteSet.getPieceByType(PieceType.PAWN_2);
		pawn_2.alive = true;
		pawn_2.posRef = cell[2][1].loc;
		cell[2][1].pieceRef = pawn_2;

		Piece pawn_3 = whiteSet.getPieceByType(PieceType.PAWN_3);
		pawn_3.alive = true;
		pawn_3.posRef = cell[3][1].loc;
		cell[3][1].pieceRef = pawn_3;

		Piece pawn_4 = whiteSet.getPieceByType(PieceType.PAWN_4);
		pawn_4.alive = true;
		pawn_4.posRef = cell[4][1].loc;
		cell[4][1].pieceRef = pawn_4;

		Piece pawn_5 = whiteSet.getPieceByType(PieceType.PAWN_5);
		pawn_5.alive = true;
		pawn_5.posRef = cell[5][1].loc;
		cell[5][1].pieceRef = pawn_5;

		Piece pawn_6 = whiteSet.getPieceByType(PieceType.PAWN_6);
		pawn_6.alive = true;
		pawn_6.posRef = cell[6][1].loc;
		cell[6][1].pieceRef = pawn_6;

		Piece pawn_7 = whiteSet.getPieceByType(PieceType.PAWN_7);
		pawn_7.alive = true;
		pawn_7.posRef = cell[7][1].loc;
		cell[7][1].pieceRef = pawn_7;
	}

	/**
	 * Used during Board instantiation: initialize all Piece and Cell instances
	 * to their default starting positions prior to beginning a Chess match
	 */
	private void assignBlackPieces() {
		Piece king = blackSet.getPieceByType(PieceType.KING);
		king.alive = true;
		king.posRef = cell[4][7].loc;
		cell[4][7].pieceRef = king;

		Piece queen = blackSet.getPieceByType(PieceType.QUEEN);
		queen.alive = true;
		queen.posRef = cell[3][7].loc;
		cell[3][7].pieceRef = queen;

		Piece bishop_r = blackSet.getPieceByType(PieceType.BISHOP_R);
		bishop_r.alive = true;
		bishop_r.posRef = cell[5][7].loc;
		cell[5][7].pieceRef = bishop_r;

		Piece bishop_l = blackSet.getPieceByType(PieceType.BISHOP_L);
		bishop_l.alive = true;
		bishop_l.posRef = cell[2][7].loc;
		cell[2][7].pieceRef = bishop_l;

		Piece knight_r = blackSet.getPieceByType(PieceType.KNIGHT_R);
		knight_r.alive = true;
		knight_r.posRef = cell[6][7].loc;
		cell[6][7].pieceRef = knight_r;

		Piece knight_l = blackSet.getPieceByType(PieceType.KNIGHT_L);
		knight_l.alive = true;
		knight_l.posRef = cell[1][7].loc;
		cell[1][7].pieceRef = knight_r;

		Piece rook_r = blackSet.getPieceByType(PieceType.ROOK_R);
		rook_r.alive = true;
		rook_r.posRef = cell[7][7].loc;
		cell[7][7].pieceRef = rook_r;

		Piece rook_l = blackSet.getPieceByType(PieceType.ROOK_L);
		rook_l.alive = true;
		rook_l.posRef = cell[0][7].loc;
		cell[0][7].pieceRef = rook_l;

		Piece pawn_0 = blackSet.getPieceByType(PieceType.PAWN_0);
		pawn_0.alive = true;
		pawn_0.posRef = cell[0][6].loc;
		cell[0][6].pieceRef = pawn_0;

		Piece pawn_1 = blackSet.getPieceByType(PieceType.PAWN_1);
		pawn_1.alive = true;
		pawn_1.posRef = cell[1][6].loc;
		cell[1][6].pieceRef = pawn_1;

		Piece pawn_2 = blackSet.getPieceByType(PieceType.PAWN_2);
		pawn_2.alive = true;
		pawn_2.posRef = cell[2][6].loc;
		cell[2][6].pieceRef = pawn_2;

		Piece pawn_3 = blackSet.getPieceByType(PieceType.PAWN_3);
		pawn_3.alive = true;
		pawn_3.posRef = cell[3][6].loc;
		cell[3][6].pieceRef = pawn_3;

		Piece pawn_4 = blackSet.getPieceByType(PieceType.PAWN_4);
		pawn_4.alive = true;
		pawn_4.posRef = cell[4][6].loc;
		cell[4][6].pieceRef = pawn_4;

		Piece pawn_5 = blackSet.getPieceByType(PieceType.PAWN_5);
		pawn_5.alive = true;
		pawn_5.posRef = cell[5][6].loc;
		cell[5][6].pieceRef = pawn_5;

		Piece pawn_6 = blackSet.getPieceByType(PieceType.PAWN_6);
		pawn_6.alive = true;
		pawn_6.posRef = cell[6][6].loc;
		cell[6][6].pieceRef = pawn_6;

		Piece pawn_7 = blackSet.getPieceByType(PieceType.PAWN_7);
		pawn_7.alive = true;
		pawn_7.posRef = cell[7][6].loc;
		cell[7][6].pieceRef = pawn_7;
	}

	/**
	 * Returns a string representation of the Board's state
	 */
	@Override
	public String toString() {
		String str = "";

		for (int rank = cell.length - 1; rank >= 0; rank--) {
			for (int file = 0; file < cell[rank].length; file++) {
				// FOR RELEASE: Print piece at cell
				if (cell[file][rank].pieceRef == null) {
					if (rank % 2 != 0) {
						if (file % 2 != 0) {
							str += "##";
						} else {
							str += "  ";
						}
					} else {
						if (file % 2 == 0) {
							str += "##";
						} else {
							str += "  ";
						}
					}
				} else {
					// FOR RELEASE: Print Pieces
					str += cell[file][rank];

					// FOR DEBUGGING: See positions instead of Pieces
					// str += cell[file][rank].getPosition();
				}

				str += " "; // Two spaces in between cells

				if (file == cell[rank].length - 1) {
					// FOR RELEASE: Print real rank enumerations
					str += String.format("%d", rank + 1); // FOR RELEASE

					// FOR DEBUGGING: Print rank indices as per an array
					// str += String.format("%d", rank);
				}
			}

			str += "\n";
		}

		// FOR RELEASE: Print real file characters
		str += String.format(" a  b  c  d  e  f  g  h\n");

		// FOR DEBUGGING: Print file indices as per an array
		// str += String.format(" 0 1 2 3 4 5 6 7\n");

		return str;
	}
}
