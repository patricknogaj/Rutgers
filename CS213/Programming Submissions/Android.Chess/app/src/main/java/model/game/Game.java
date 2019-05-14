/**
 * Game.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import model.PieceType;
import model.PieceType.Color;
import model.chess_set.Board;
import model.chess_set.Piece;
import model.chess_set.PieceSet;

final class GameReplay {

}

/**
 * Represents the state of a Chess game and all of its components. Instances of
 * Game are to be created within the client program.
 * 
 * @version Mar 5, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Game {

	private Board board;

	private Player white;
	private Player black;

	private Position whitePlay;
	private Position whiteNewPosition;
	private Position blackPlay;
	private Position blackNewPosition;

	private boolean active;

	private boolean whitesMove;

	private boolean willDraw;
	private boolean willResign;

	private boolean didDraw;
	private boolean drawGranted;
	private boolean didResign;

	private boolean validMoveInput;
	private boolean validMoveInputWithDraw;
	private boolean validMoveInputWithPromotion;

	private static final String whiteSpaceRegex = "[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]";
	private static final String fileRankRegex = "[a-h][1-8]";
	private static final String validFileRankRegex = String.format("%s%s%s",
			fileRankRegex, whiteSpaceRegex, fileRankRegex);

	private static final String drawRegex = "draw\\?";
	private static final String validFileRankWithDrawRegex = String
			.format("%s%s%s", validFileRankRegex, whiteSpaceRegex, drawRegex);

	private static final String pieceRegex = "[qQbBnNrR]";

	private static final String validFileRankWithPromotion = String
			.format("%s%s%s", validFileRankRegex, whiteSpaceRegex, pieceRegex);

	private String input;
	private String output;

	private Scanner scan;
	private File inputFile;
	private Reader fileReader;
	private Reader bufferedReader;

	private LocalDateTime gameStartTime;
	private LocalDateTime gameEndTime;

	private boolean printBoard;
	private boolean debugMoveLog;
	private boolean debugPostMoveLog;
	private boolean debugPieceSetLog;

	private boolean promoteWhite;
	private boolean promoteBlack;

	private PieceType pawnPromoteType;

	private PieceType.Color gameWinner;
	
	private String gameTitleString;
	private String gameDateString;
	
	private DateTimeFormatter dateTimeFormatter;
	
	private static final String OUTPUT_DIR_CLI = "dat" + File.pathSeparator;
	private static final String OUTPUT_EXT = "chess22";
	
	private List<String> moveListInputFile;

	/**
	 * Default constructor
	 */
	public Game() {
		board = new Board();

		white = new Player(PieceType.Color.WHITE, board);
		black = new Player(PieceType.Color.BLACK, board);

		active = true;

		whitesMove = true;

		willDraw = false;
		willResign = false;

		didDraw = false;
		drawGranted = false;
		didResign = false;

		validMoveInput = false;
		validMoveInputWithDraw = false;
		validMoveInputWithPromotion = false;

		input = "";
		output = "";

		scan = null;
		inputFile = null;
		fileReader = null;
		bufferedReader = null;

		gameStartTime = LocalDateTime.now();
		gameEndTime = null;

		printBoard = true;
		debugMoveLog = false;
		debugPostMoveLog = false;
		debugPieceSetLog = false;

		promoteWhite = false;
		promoteBlack = false;

		pawnPromoteType = null;

		gameWinner = null;
		
		dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/YY hh:mm:ss a");
		
		gameTitleString = "<untitled>";
		gameDateString = gameStartTime.format(dateTimeFormatter);
	}
	
	/**
	 * Mutator to toggle all debug logs
	 */
	public void toggleAllDebugLogs() {
		toggleMoveLog();
		togglePostMoveLog();
		togglePieceSetLog();
	}

	/**
	 * Accessor to retrieve a game's start time
	 * 
	 * @return the instantiation time of this Game instance
	 */
	public LocalDateTime getStartTime() {
		return gameStartTime;
	}

	/**
	 * Accessor to retrieve a game's end time
	 * 
	 * @return the time during which a checkmate, resignation, or draw occurred,
	 *         or null - if the game is still active
	 */
	public LocalDateTime getEndTime() {
		return !active ? gameEndTime : null;
	}

	/**
	 * Accessor to retrieve the move list for a Game. Precondition: Game must no
	 * longer be active to retrieve the move list.
	 * 
	 * @return a move list for a retired game, otherwise null
	 */
	public List<Move> getMoveList() {
		if (active) {
			return null;
		}

		return board.getMoveList();
	}


	/**
	 * Generates a movelist that can be used for playback with an input file
	 * 
	 * @param filepath
	 * @return
	 * @throws IOException 
	 */
	public List<String> generateMoveListForPlayback(String inputFileString) throws IOException {
		
		moveListInputFile = new ArrayList<String>();
		
		Scanner scan = new Scanner(inputFileString);
		String str = "";
		while (scan.hasNext()) {
			str = scan.nextLine();
		
			
			if (str.equals("\t[TITLE]")) {

			} else if (str.equals("\t[DATE]")) {

			} else if (str.equals("\t[MOVES]")) {
				break;
			}
		}

		while (scan.hasNext()) {
			str = scan.nextLine().trim();
			if (str.equals("[/MOVES]")) {
				break;
			} else {
				moveListInputFile.add(str.trim());
			}
		}
				
		scan.close();
		return moveListInputFile;
		
		/*
		inputFile = new File(inputFilePath);
		
		if (inputFile.exists() == false) {
			output = "Error: " + inputFilePath + " does not exist.";
			System.err.println(output);
			System.exit(0);
		}
			
		fileReader = new FileReader(inputFile);
		bufferedReader = new BufferedReader(fileReader);
		input = "";
				
		input = ((BufferedReader)bufferedReader).readLine();
		
		
		
		if (input.equals("[GAME_LIST]")) {
			while ((input = ((BufferedReader)bufferedReader).readLine()) != null) {
				if (input.equals("\t[TITLE]")) {
					gameTitleString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[DATE]")) {
					gameDateString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[MOVES]")) {
					while (active) {
						do {
							validMoveInput = false;
							
							input = ((BufferedReader) bufferedReader).readLine().trim();
							
							moveListInputFile.add(input);

							if (input == null) {
								bufferedReader.close();
								fileReader.close();
								inputFile = null;
								start();
								break;
							}

							readInput(input);
							//System.out.println(output);

							if (didDraw || didResign || !active) {
								break;
							}
						} while (validMoveInput == false);

						if (active) {
							if (printBoard) {
								//System.out.println(boardToString());
							}

							if (debugPostMoveLog) {
								//printPostMoveLog();
							}

							if (debugMoveLog) {
								printMoveLog();
							}//

							if (debugPieceSetLog) {
								if (whitesMove == false) {
									//white.printPieceSet();
								} else {
									//black.printPieceSet();
								}
							}
						}
					}
				}
			}
		}
		
		inputFile = null;
		bufferedReader.close();
		*/

	}

	/**
	 * Accessor to print certain console messages for classes outside of this
	 * package
	 * 
	 * @return output string for console
	 */
	public String output() {
		return output;
	}

	/**
	 * Accessor to determine the winner of a game. Precondition: game must not
	 * be active (must not be in play)
	 * 
	 * @return The winning player's color, otherwise if game is active, null
	 */
	public PieceType.Color getGameWinner() {
		return !active ? gameWinner : null;
	}

	/**
	 * Mutator to toggle the printing of the CLI chess board
	 */
	public void togglePrintBoard() {
		printBoard = printBoard ? false : true;
	}

	/**
	 * Mutator to toggle the appearance of the move log after each move
	 */
	public void toggleMoveLog() {
		debugMoveLog = debugMoveLog ? false : true;
	}

	/**
	 * Mutator to toggle the appearance of the post-move console printout after
	 * each move
	 */
	public void togglePostMoveLog() {
		debugPostMoveLog = debugPostMoveLog ? false : true;
	}

	/**
	 * Mutator to toggle the appearance of the pieceset log after each move
	 */
	public void togglePieceSetLog() {
		debugPieceSetLog = debugPieceSetLog ? false : true;
	}
	
	/**
	 * Mutator to set the gameTitleString
	 * 
	 * @param gameTitleString the game's title
	 */
	public void setGameTitleString(String gameTitleString) {
		this.gameTitleString = gameTitleString;
	}
	
	/**
	 * Mutator to set the gameDateString
	 * 
	 * @param gameDateString the game's date string
	 */
	public void setGameDateString(String gameDateString) {
		this.gameDateString = gameDateString;
	}
	
	/**
	 * Accessor to get gameTitleString
	 * 
	 * @return the game title string
	 */
	public String getGameTitleString() {
		return gameTitleString;
	}
	
	/**
	 * Accessor to get gameDateString
	 * 
	 * @return the game date string
	 */
	public String getGameDateString() {
		return gameDateString;
	}

	/**
	 * Retrieves the last move from the Board's moveList
	 * 
	 * @return the Move that describes the last piece to be successfully moved,
	 *         otherwise null if there are no pieces moved
	 */
	public Move getLastMove() {
		return board.getLastMove();
	}

	/**
	 * Retrieves the last kill from the Board's moveList
	 * 
	 * @return the Move that describes the last piece to be killed, otherwise
	 *         null if there are no pieces killed
	 */
	public Move getLastKill() {
		return board.getLastKill();
	}

	/**
	 * Mutator to toggle the game's status. If false (game not active), active
	 * will be switched to true. Else (game is current active), active will be
	 * switched to false.
	 */
	public void toggleActive() {
		active = active ? false : true;
	}

	/**
	 * Accessor to retrieve status of game
	 * 
	 * @return true if game is active, false otherwise
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Accessor to determine if it is white player's move, or not
	 * 
	 * @return true if white's move, false otherwise
	 */
	public boolean isWhitesMove() {
		return whitesMove;
	}

	/**
	 * Accessor to determine if a player requested a draw to the other player
	 * 
	 * @return true if draw requested, false otherwise
	 */
	public boolean isWillDraw() {
		return willDraw;
	}

	/**
	 * Accessor to determine if player requested to resign from the game
	 * 
	 * @return true if resign requested, false otherwise
	 */
	public boolean isWillResign() {
		return willResign;
	}

	/**
	 * Accessor to determine if a draw has been mutually agreed upon and will be
	 * done (willDraw and drawGranted must both be true, and the values of those
	 * variables are determined during the game)
	 * 
	 * @return true if the draw has been agreed upon, false otherwise
	 */
	public boolean isDidDraw() {
		return didDraw;
	}

	/**
	 * Accessor to determine if a draw requested made by one player has been
	 * accepted by the other player. This is the moment when the requestee
	 * (recipient of the request) agrees to a draw after it was proposed by the
	 * requestee's opponent.
	 * 
	 * @return true if drawGranted is true, false otherwise
	 */
	public boolean isDrawGranted() {
		return drawGranted;
	}

	/**
	 * Accessor to determine if a resignation requested made by one player has
	 * been accepted by the other player. This is the moment when the requestee
	 * (recipient of the request) agrees to let their opponent resign from the
	 * game.
	 * 
	 * @return true if didResign is true, false otherwise
	 */
	public boolean isDidResign() {
		return didResign;
	}

	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player.
	 * 
	 * @return true if a valid move was made by a player, false otherwise
	 */
	public boolean isValidMoveInput() {
		return validMoveInput;
	}

	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player -- but has made a request to their opponent to end
	 * the game by draw.
	 * 
	 * @return true if validMoveInputWithDraw is true, false otherwise
	 */
	public boolean isValidMoveInputWithDraw() {
		return validMoveInputWithDraw;
	}

	/**
	 * Accessor to determine if a move request results in a valid move and was
	 * completed by a player -- but has made a request to promote their pawn
	 * Piece to a Queen, Bishop, Knight, or Rook.
	 * 
	 * @return true if validMoveInputWithPromotion is true, false otherwise
	 */
	public boolean isValidMoveInputWithPromotion() {
		return validMoveInputWithPromotion;
	}

	/**
	 * Called by ChessActivity::movePiece, after a valid move is completed by a
	 * player. In CLI chess, Board.java will automatically promote an eligible
	 * PAWN to a QUEEN if no preference was specified during input.
	 * 
	 * For GUI chess, this method is used to determine which Pawn to promote,
	 * after the user/player selects their preference via a dialog notification.
	 * 
	 * @return if promoteWhite is true, promoteWhite will be toggled back to
	 *         false and this method returns true -- otherwise, returns false
	 */
	public boolean didPromoteWhite() {
		if (promoteWhite) {
			promoteWhite = false;
			return true;
		}

		return false;
	}

	/**
	 * Called by ChessActivity::movePiece, after a valid move is completed by a
	 * player. In CLI chess, Board.java will automatically promote an eligible
	 * PAWN to a QUEEN if no preference was specified during input.
	 * 
	 * For GUI chess, this method is used to determine which Pawn to promote,
	 * after the user/player selects their preference via a dialog notification.
	 * 
	 * @return if promoteBlack is true, promoteBlack will be toggled back to
	 *         false and this method returns true -- otherwise, returns false
	 */
	public boolean didPromoteBlack() {
		if (promoteBlack) {
			promoteBlack = false;
			return true;
		}

		return false;
	}

	/**
	 * Called by ChessActivity::movePiece, determine if a promotion can take
	 * place given oldFile, oldRank, newFile, and newRank.
	 * 
	 * @param oldFile the file of a given piece to move
	 * @param oldRank the rank of a given piece to move
	 * @param newFile the desired file of a piece to move
	 * @param newRank the desired rank of a piece to move
	 * 
	 * @return true if promotable, false otherwise
	 */
	public boolean canPromote(int oldFile, int oldRank, int newFile,
			int newRank) {
		boolean canPromote = false;

		PieceSet pieceSet = whitesMove ? white.pieceSetRef : black.pieceSetRef;

		Piece piece = pieceSet
				.getPieceByPosition(new Position(oldFile, oldRank));

		if (piece != null) {
			if (piece.isPawn()) {
				if (piece.isWhite()) {
					if (newRank == 7) {
						canPromote = true;
					}
				} else {
					if (newRank == 0) {
						canPromote = true;
					}
				}
			}
		}

		return canPromote;
	}

	/**
	 * Called by ChessActivity::movePiece, to determine the PieceType of a PAWN
	 * (PAWN_0, PAWN_1, ...)
	 * 
	 * @return a PieceType of a PAWN to be promoted
	 */
	public PieceType getPawnPromoteType() {
		return pawnPromoteType;
	}

	/**
	 * Called by ChessActivity::movePiece, to override the default promotion by
	 * Board.java (allows user-selection of promotion type, since the promotion
	 * preference will not be typed in whilst inputting a move, as per CLI
	 * chess)
	 * 
	 * @param newPos      the new position of the promoted pawn
	 * @param promoteType the desired PieceType for promotion
	 */
	public void overridePawnPromotion(Position newPos, PieceType promoteType) {

		Move lastMove = getLastMove();
		Piece piece = lastMove.getLastPiece();

		PieceSet pieceSet = piece.isWhite() ? white.pieceSetRef
				: black.pieceSetRef;

		board.promotePawn(piece, pieceSet, newPos, promoteType);

		/*
		 * Move lastMove = getLastMove();
		 * 
		 * PieceType.Color color = lastMove.getLastPiece().isWhite() ?
		 * PieceType.Color.WHITE : PieceType.Color.BLACK;
		 * 
		 * PieceSet pieceSet = color.equals(PieceType.Color.WHITE) ?
		 * white.pieceSetRef : black.pieceSetRef;
		 * 
		 * Piece piece = pieceSet.getPieceByPosition(newPos);
		 * 
		 * board.promotePawn(piece, pieceSet, newPos, promoteType);
		 */
	}

	/**
	 * Prints the current state of the move list
	 */
	public void printMoveLog() {
		System.out.println(board.moveLogToString());
	}

	/**
	 * Prints the last move and the last kill performed
	 */
	public void printPostMoveLog() {
		if (validMoveInput) {
			Move move = getLastMove();
			String color = "";
			color = move.getLastPiece().isWhite() ? "WHITE" : "BLACK";
			output = "The " + color + " piece player has made a move...\n\n";
			output += "LAST MOVE PERFORMED"
					+ " -----------------------------------------\n";
			output += "Time\t\tMove #\tPiece\tStart\tEnd\tPromoted From\n";
			output += "-------------------------------------------------------------\n";
			output += move + "\n";

			Move kill = getLastKill();

			boolean confirmedKill = move.getLocalTime()
					.equals(kill.getLocalTime());

			boolean whiteKilledBlack = move.getLastPiece().isWhite()
					&& kill.getLastPiece().isBlack() && confirmedKill;

			boolean blackKilledWhite = move.getLastPiece().isBlack()
					&& kill.getLastPiece().isWhite() && confirmedKill;

			if (whiteKilledBlack || blackKilledWhite) {
				output += "\n\t\tDEATH:\n\t\t------\n" + kill;

				output += "\n\t*** " + kill.getLastPiece().toString()
						+ " was killed by " + move.getLastPiece().toString()
						+ "! ***\n";
			}

			String nextColor = "";
			nextColor = move.getLastPiece().isWhite() ? "BLACK" : "WHITE";
			output += "\nIt is now the " + nextColor
					+ " piece player's turn.\n";

			System.out.println(output);
		}
	}

	/**
	 * Prints the white player's PieceSet
	 */
	public void printWhiteSet() {
		System.out.println(white.pieceSetRef);
	}

	/**
	 * Prints the black player's PieceSet
	 */
	public void printBlackSet() {
		System.out.println(black.pieceSetRef);
	}

	/**
	 * Returns the current state of the Game as an ASCII chess board
	 * 
	 * @return string representation of the Game's Board instance
	 */
	public String boardToString() {
		return board.toString();
	}

	/**
	 * Begins game loop
	 */
	public void start() {
		scan = new Scanner(System.in);
		input = "";

		if (printBoard) {
			System.out.println(board);
		}

		while (active) {
			do {
				validMoveInput = false;

				output = whitesMove ? "White's " : "Black's ";

				System.out.print(output + "move: ");
				input = scan.nextLine();

				readInput(input);
				System.out.println(output);

				if (didDraw || didResign || !active) {
					break;
				}
			} while (validMoveInput == false);

			if (active) {
				if (printBoard) {
					System.out.println(boardToString());
				}

				if (debugPostMoveLog) {
					printPostMoveLog();
				}

				if (debugMoveLog) {
					printMoveLog();
				}

				if (debugPieceSetLog) {
					if (whitesMove == false) {
						white.printPieceSet();
					} else {
						black.printPieceSet();
					}
				}
			}
		}

		scan.close();
	}

	/**
	 * Starts a game from a String inputFilePath to an existing file.
	 * Precondition: File to inputFilePath must exist.
	 * 
	 * @param inputFilePath String representing the input file to be read
	 * @throws IOException On nonexistent inputFilePath
	 */
	/*
	public void startFromFile(String inputFilePath) throws IOException {
		inputFile = new File(inputFilePath);

		if (inputFile.exists() == false) {
			output = "Error: " + inputFilePath + " does not exist.";
			System.err.println(output);
			System.exit(0);
		}

		fileReader = new FileReader(inputFile);
		bufferedReader = new BufferedReader(fileReader);
		input = "";

		if (printBoard) {
			System.out.println(board);
		}

		while (active) {
			do {
				validMoveInput = false;

				input = ((BufferedReader) bufferedReader).readLine();
				System.out.println();

				if (input == null) {
					bufferedReader.close();
					fileReader.close();
					inputFile = null;
					start();
					break;
				}

				readInput(input);
				System.out.println(output);

				if (didDraw || didResign || !active) {
					break;
				}
			} while (validMoveInput == false);

			if (active) {
				if (printBoard) {
					System.out.println(boardToString());
				}

				if (debugPostMoveLog) {
					printPostMoveLog();
				}

				if (debugMoveLog) {
					printMoveLog();
				}

				if (debugPieceSetLog) {
					if (whitesMove == false) {
						white.printPieceSet();
					} else {
						black.printPieceSet();
					}
				}
			}
		}

		inputFile = null;
		bufferedReader.close();
		fileReader.close();
	}
	*/
	
	/**
	 * Starts a game from a String inputFilePath to an existing file.
	 * Precondition: File to inputFilePath must exist.
	 * 
	 * Reads a specific markup format.
	 * 
	 * @param inputFilePath String representing the input file to be read
	 * @throws IOException On nonexistent inputFilePath
	 */
	public void startFromFile(String inputFilePath) throws IOException {
		inputFile = new File(inputFilePath);
		
		if (inputFile.exists() == false) {
			output = "Error: " + inputFilePath + " does not exist.";
			System.err.println(output);
			System.exit(0);
		}

		fileReader = new FileReader(inputFile);
		bufferedReader = new BufferedReader(fileReader);
		input = "";

		if (printBoard) {
			System.out.println(board);
		}
				
		input = ((BufferedReader)bufferedReader).readLine();
		
		if (input.equals("[GAME_LIST]")) {
			while ((input = ((BufferedReader)bufferedReader).readLine()) != null) {
				if (input.equals("\t[TITLE]")) {
					gameTitleString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[DATE]")) {
					gameDateString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[MOVES]")) {
					while (active) {
						do {
							validMoveInput = false;
							
							input = ((BufferedReader) bufferedReader).readLine().trim();
							System.out.println();

							if (input == null) {
								bufferedReader.close();
								fileReader.close();
								inputFile = null;
								start();
								break;
							}

							readInput(input);
							System.out.println(output);

							if (didDraw || didResign || !active) {
								break;
							}
						} while (validMoveInput == false);

						if (active) {
							if (printBoard) {
								System.out.println(boardToString());
							}

							if (debugPostMoveLog) {
								printPostMoveLog();
							}

							if (debugMoveLog) {
								printMoveLog();
							}

							if (debugPieceSetLog) {
								if (whitesMove == false) {
									white.printPieceSet();
								} else {
									black.printPieceSet();
								}
							}
						}
					}
				}
			}
		}
		
		inputFile = null;
		bufferedReader.close();
		fileReader.close();
	}
	
	public void loadInputFile(String inputFilePath) throws IOException {
		moveListInputFile = new ArrayList<String>();
		
		inputFile = new File(inputFilePath);
		
		if (inputFile.exists() == false) {
			output = "Error: " + inputFilePath + " does not exist.";
			System.err.println(output);
			System.exit(0);
		}

		scan = new Scanner(System.in);
		fileReader = new FileReader(inputFile);
		bufferedReader = new BufferedReader(fileReader);
		input = "";

		System.out.println("*** PLAY BY PLAY MODE ***\n");
		
		if (printBoard) {
			System.out.println(board);
		}
		
		input = ((BufferedReader)bufferedReader).readLine();
		
		String line = "";
		
		if (input.equals("[GAME_LIST]")) {			
			while ((input = ((BufferedReader)bufferedReader).readLine()) != null) {
				if (input.equals("\t[TITLE]")) {
					gameTitleString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[DATE]")) {
					gameDateString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[MOVES]")) {
					while (active) {
						do {
							validMoveInput = false;
							
							output = whitesMove ? "White's " : "Black's ";

							System.out.print(output + "move (Hit ENTER to see the next move): ");
							line = scan.nextLine();
							
							while (!line.equals("")) {
								System.out.println("Hit ENTER to see the next move or type X and ENTER to quit.");
								
								line = scan.nextLine();
								
								if (line.equals("X")) {
									return;
								}
							}
							
							input = ((BufferedReader) bufferedReader).readLine().trim();
							System.out.println();

							if (input == null) {
								bufferedReader.close();
								fileReader.close();
								inputFile = null;
								start();
								break;
							}

							readInput(input);
							System.out.println(output);

							if (didDraw || didResign || !active) {
								break;
							}
						} while (validMoveInput == false);

						if (active) {
							if (printBoard) {
								System.out.println(boardToString());
							}

							if (debugPostMoveLog) {
								printPostMoveLog();
							}

							if (debugMoveLog) {
								printMoveLog();
							}

							if (debugPieceSetLog) {
								if (whitesMove == false) {
									white.printPieceSet();
								} else {
									black.printPieceSet();
								}
							}
						}
					}
				}
			}
		}
		
		inputFile = null;
		bufferedReader.close();
		fileReader.close();
	}

	/**
	 * Starts a game from a String inputFilePath to an existing file. One move
	 * is displayed at at time using the ENTER/RETURN key upon prompt.
	 * Precondition: File to inputFilePath must exist.
	 * 
	 * @param inputFilePath String representing the input file to be read
	 * @throws IOException On nonexistent inputFilePath
	 */
	public void startFromFilePlayByPlay(String inputFilePath)
			throws IOException {
		inputFile = new File(inputFilePath);
		
		if (inputFile.exists() == false) {
			output = "Error: " + inputFilePath + " does not exist.";
			System.err.println(output);
			System.exit(0);
		}

		scan = new Scanner(System.in);
		fileReader = new FileReader(inputFile);
		bufferedReader = new BufferedReader(fileReader);
		input = "";

		System.out.println("*** PLAY BY PLAY MODE ***\n");
		
		if (printBoard) {
			System.out.println(board);
		}
		
		input = ((BufferedReader)bufferedReader).readLine();
		
		String line = "";
		
		if (input.equals("[GAME_LIST]")) {			
			while ((input = ((BufferedReader)bufferedReader).readLine()) != null) {
				if (input.equals("\t[TITLE]")) {
					gameTitleString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[DATE]")) {
					gameDateString = ((BufferedReader)bufferedReader).readLine().trim();
				} else if (input.equals("\t[MOVES]")) {
					while (active) {
						do {
							validMoveInput = false;
							
							output = whitesMove ? "White's " : "Black's ";

							System.out.print(output + "move (Hit ENTER to see the next move): ");
							line = scan.nextLine();
							
							while (!line.equals("")) {
								System.out.println("Hit ENTER to see the next move or type X and ENTER to quit.");
								
								line = scan.nextLine();
								
								if (line.equals("X")) {
									return;
								}
							}
							
							input = ((BufferedReader) bufferedReader).readLine().trim();
							System.out.println();

							if (input == null) {
								bufferedReader.close();
								fileReader.close();
								inputFile = null;
								start();
								break;
							}

							readInput(input);
							System.out.println(output);

							if (didDraw || didResign || !active) {
								break;
							}
						} while (validMoveInput == false);

						if (active) {
							if (printBoard) {
								System.out.println(boardToString());
							}

							if (debugPostMoveLog) {
								printPostMoveLog();
							}

							if (debugMoveLog) {
								printMoveLog();
							}

							if (debugPieceSetLog) {
								if (whitesMove == false) {
									white.printPieceSet();
								} else {
									black.printPieceSet();
								}
							}
						}
					}
				}
			}
		}
		
		inputFile = null;
		bufferedReader.close();
		fileReader.close();
	}

	/**
	 * Interprets a string of input representing a piece's position and desired
	 * cell position for a move -- which may include a preference for a
	 * promotion type, or a request to draw, or alternatively, a declaration to
	 * resign from the game entirely
	 * 
	 * @param input EXAMPLES: "a1 a2" moves a piece from a1 to a2, "a1 a2 draw?"
	 *              requests a draw to the opponent, if the opponent accepts,
	 *              they will reply with "draw" otherwise, if the opponent
	 *              replies with a move, the requester will have their piece
	 *              moved from a1 to a2. "a1 a2 B" will move a piece from a1 to
	 *              a2 and promote a pawn (if a1 is a pawn) to a bishop "resign"
	 *              will end the game and the requester loses
	 */
	public void readInput(String input) {
		if (input.equals("undo")) {
			output = "";
			undoMove();
			return;
		}

		if (!input.equals("save")) {
			if (!active) {
				System.err.println("Game no longer active: " + gameWinner
						+ " has already won the game.");
				return;
			}
		}
		
		if (input.equals("save -cli")) {
			output = "";
			
			try {
				saveGameCLI();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return;
		} else if (input.equals("save")) {
			output = "";
			
			// for GUI chess
			
			return;
		}

		int[] fileRankArray = null;

		validMoveInput = false;

		validMoveInput = input.matches(validFileRankRegex);
		validMoveInputWithDraw = input.matches(validFileRankWithDrawRegex);
		validMoveInputWithPromotion = input.matches(validFileRankWithPromotion);

		drawGranted = willDraw && input.equals("draw");
		willResign = input.equals("resign");

		if (validMoveInput) {
			willDraw = false;
		} else if (validMoveInputWithPromotion) {
			willDraw = false;
		} else if (validMoveInputWithDraw) {
			willDraw = true;
		} else if (drawGranted) {
			didDraw = true;
		} else if (willResign) {
			didResign = true;
		} else {
			validMoveInput = false;
			output = "Invalid input, try again\n";
		}

		if (validMoveInput || validMoveInputWithPromotion
				|| validMoveInputWithDraw) {
			int file = -1;
			int rank = -1;
			int newFile = -1;
			int newRank = -1;
			int promo = -1;

			fileRankArray = getFileRankArray(input);

			file = fileRankArray[0];
			rank = fileRankArray[1];
			newFile = fileRankArray[2];
			newRank = fileRankArray[3];
			promo = fileRankArray[4];

			if (whitesMove) {
				validMoveInput = whitePlayMove(file, rank, newFile, newRank,
						promo);
			} else {
				validMoveInput = blackPlayMove(file, rank, newFile, newRank,
						promo);
			}

			if (board.checkPromoteWhite()) {
				promoteWhite = true;
			} else if (board.checkPromoteBlack()) {
				promoteBlack = true;
			}

			pawnPromoteType = board.getPawnPromoteType();

			output = validMoveInput ? "" : "Illegal move, try again\n";
		}

		if (didDraw || didResign || board.isCheckmate()) {
			active = false;
			gameEndTime = LocalDateTime.now();
			gameDateString = gameEndTime.format(dateTimeFormatter);

			if (didDraw) {
				output = "draw";
				board.setOutputWinner("draw");
				gameWinner = null;
			}

			if (didResign) {
				output = whitesMove ? "Black wins" : "White wins";
				board.setOutputWinner(whitesMove ? "Black wins" : "White wins");
				gameWinner = whitesMove ? Color.BLACK : Color.WHITE;
			}

			if (board.isCheckmate()) {
				output = board.getOutputWinner();
				gameWinner = board.isWhiteWinner() ? Color.WHITE : Color.BLACK;
			}
		}

		if (validMoveInput) {
			whitesMove = whitesMove ? false : true;
		}

		validMoveInputWithDraw = false;
		validMoveInputWithPromotion = false;

		drawGranted = false;
		willResign = false;

		if (willDraw) {
			board.getLastMove().setWillDraw(willDraw);
		}

		if (didDraw) {
			board.getLastMove().setDidDraw(didDraw);
		}

	}

	/**
	 * Called by ChessActivity, retrieves the last move undone
	 * 
	 * @return the last move undone
	 */
	public Move getLastMoveUndone() {
		return board.getLastMoveUndone();
	}

	/**
	 * Called by ChessActivity, retrieves the last kill undone
	 * 
	 * @return the last kill undone
	 */
	public Move getLastKillUndone() {
		return board.getLastKillUndone();
	}

	/**
	 * Undoes the last move in the move list -- if the last move is paired with
	 * a kill, that kill is undone as well.
	 * 
	 * The undo caller forfeits their turn to the player who had their previous
	 * move and/or kill undone.
	 */
	private void undoMove() {
		if (getLastMove() == null) {
			System.err.println("\nNo moves to undo!");
			return;
		}

		System.out.println("\n** UNDO **\n");

		// Undo the previous move made by the opponent
		// (opposite color of the caller)
		board.undoMovePiece();

		System.out.println(boardToString());

		printBlackSet();
		printWhiteSet();

		printMoveLog();

		// Change the turn to the opposite caller.
		whitesMove = whitesMove ? false : true;
	}
	
	/**
	 * A user should define a name for this game first
	 * using setGameTitleString(String gameTitleString) before
	 * running this method.
	 */
	public void saveGameCLI() throws IOException {
		System.out.println("\n[State of game saved at " + LocalTime.now() + "]");
		
		scan = new Scanner(System.in);
		
		String title = "";
		System.out.print(
				"Provide a title for the saved game and hit ENTER ==> ");
		title = scan.nextLine();
		
		String filename = 
				String.format("%s_%s.%s", title, gameStartTime, OUTPUT_EXT);
		
		String filepath = OUTPUT_DIR_CLI + filename;
		
		File file = new File(filepath);
		if (!file.exists()) {
			PrintWriter pw = new PrintWriter(filepath, "UTF-8");
			pw.print("");
			pw.close();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		bw.write(generateSaveGameString());
		
		bw.flush();
		bw.close();
	}
	
	/**
	 * Saving a game as per the GUI
	 * 
	 * (provide a name for the game title first)
	 * 
	 * @throws IOException if file not found
	 */
	public void saveGameGUI(String filepath) throws IOException {
		System.out.println("Entered saveGameGUI()");
		
		System.out.println("\n[State of game saved at " + LocalTime.now() + "]");
		
		scan = new Scanner(System.in);
		
		String title = gameTitleString;
		
		String filename = 
				String.format("%s_%s.%s", title, gameStartTime, OUTPUT_EXT);
		
		String path = filepath + filename;
		
		File file = new File(path);
		if (!file.exists()) {
			PrintWriter pw = new PrintWriter(path, "UTF-8");
			pw.print("");
			pw.close();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		bw.write(generateSaveGameString());
		
		bw.flush();
		bw.close();	
	}

	/**
	 * Generates a string that comprises the contents of a would-be
	 * input file representing a game
	 */
	public String generateSaveGameString() {
		String moveListString = generateMoveListString();

		String gameTitle = gameTitleString;
		String gameDate = gameDateString;
		
		String saveGameString = "";
		
		saveGameString += "[GAME_LIST]\n";
		saveGameString += "\n";
		saveGameString += "\t[TITLE]\n";
		saveGameString += "\t\t" + gameTitle + "\n";
		saveGameString += "\t[/TITLE]\n";
		saveGameString += "\n";
		saveGameString += "\t[DATE]\n";
		saveGameString += "\t\t" + gameDate + "\n";
		saveGameString += "\t[/DATE]\n";
		saveGameString += "\n";
		saveGameString += "\t[MOVES]\n";
		saveGameString += moveListString;
		saveGameString += "\t[/MOVES]\n";
		saveGameString += "\n[GAME_LIST]";
		saveGameString += "\n";
		
		return saveGameString;
	}
	

	/**
	 * Generates a string that consists of a move list that can be loaded into
	 * startFromFile()
	 * 
	 * @return a string that is formatted for use by startFromFile() as per the
	 *         current moveList
	 */
	private String generateMoveListString() {
		List<Move> moveList = board.getMoveList();

		String moveListString = "";
		
		Iterator<Move> iter = moveList.iterator();
		while (iter.hasNext()) {
			moveListString += "\t\t";
			Move m = iter.next();

			if (m.getEndPosition() != null) {
				moveListString += String.format("%s %s", m.getStartPosition(),
						m.getEndPosition());

				if (m.getPromotedFrom() != null) {
					moveListString += String.format(" %s",
							m.getLastPiece().toString().substring(1));
				}

				if (m.getWillDraw()) {
					moveListString += String.format(" %s", "draw?");
				}
			}

			if (!iter.hasNext()) {
				if (board.isCheckmate() == false) {
					if (didResign) {
						moveListString += "\t\tresign";
					}

					if (didDraw) {
						moveListString += "\n\t\tdraw";
					}
				}
			}

			moveListString += "\n";
		}

		return moveListString;
	}

	/**
	 * Parses a line of input into an integer array of a Piece's current file
	 * and rank, and the desired file and rank for a new Position
	 * 
	 * @param input a line of input
	 * 
	 * @return an integer array representing a Piece's current position and the
	 *         desired position to move to
	 */
	public int[] getFileRankArray(String input) {
		if (input.equals("draw") || input.equals("resign")) {
			return null;
		}

		String fileRankStr = "";
		String newFileNewRankStr = "";
		String promoStr = null;

		int file = -1;
		int rank = -1;
		int newFile = -1;
		int newRank = -1;
		int promo = -1;

		int[] result = new int[5];

		Scanner parse = new Scanner(input);
		fileRankStr = parse.next(fileRankRegex).toLowerCase();
		newFileNewRankStr = parse.next(fileRankRegex).toLowerCase();

		if (parse.hasNext()) {
			promoStr = parse.next();
			promoStr = promoStr.toUpperCase();
		}

		char chFile = fileRankStr.charAt(0);
		char chNewFile = newFileNewRankStr.charAt(0);
		char chPromo = promoStr != null ? promoStr.charAt(0) : '!';

		file = Game.charToInt(chFile);
		newFile = Game.charToInt(chNewFile);
		promo = Game.charToInt(chPromo);

		rank = Integer.parseInt(fileRankStr.substring(1)) - 1;
		newRank = Integer.parseInt(newFileNewRankStr.substring(1)) - 1;

		result[0] = file;
		result[1] = rank;
		result[2] = newFile;
		result[3] = newRank;
		result[4] = promo;

		parse.close();
		return result;
	}

	/**
	 * Takes a character and returns an int that may represent an index for a
	 * file, rank, or ordinal or a PieceType enum
	 * 
	 * @param ch the character to parse
	 * @return an integer that represents an array index or enum ordinal
	 */
	public static int charToInt(char ch) {
		int result = -1;

		switch (ch) {
		case 'a':
			result = 0;
			break;
		case 'b':
			result = 1;
			break;
		case 'c':
			result = 2;
			break;
		case 'd':
			result = 3;
			break;
		case 'e':
			result = 4;
			break;
		case 'f':
			result = 5;
			break;
		case 'g':
			result = 6;
			break;
		case 'h':
			result = 7;
			break;
		case 'Q':
			result = PieceType.QUEEN.ordinal();
			break;
		case 'B':
			result = PieceType.BISHOP_R.ordinal();
			break;
		case 'N':
			result = PieceType.KNIGHT_R.ordinal();
			break;
		case 'R':
			result = PieceType.ROOK_R.ordinal();
			break;
		default:
			result = PieceType.QUEEN.ordinal();
			break;
		}

		return result;
	}

	/**
	 * Piece from the white PieceSet will be moved to a new Cell on the Board
	 * 
	 * @param file    x axis coordinate of a requested Piece (0-7 only)
	 * @param rank    y axis coordinate of a requested Piece (0-7 only)
	 * @param newFile x axis coordinate of the desired move (0-7 only)
	 * @param newRank y axis coordinate of the desired move (0-7 only)
	 * @param promo   integer that represents the piece to promote to (if != -1)
	 * 
	 * @return true if move executed successfully, false otherwise
	 */
	private boolean whitePlayMove(int file, int rank, int newFile, int newRank,
			int promo) {
		whitePlay = new Position(file, rank);
		whiteNewPosition = new Position(newFile, newRank);

		return white.playMove(whitePlay, whiteNewPosition, promo);
	}

	/**
	 * Piece from the black PieceSet will be moved to a new Cell on the Board
	 * 
	 * @param file    x axis coordinate of a requested Piece (0-7 only)
	 * @param rank    y axis coordinate of a requested Piece (0-7 only)
	 * @param newFile x axis coordinate of the desired move (0-7 only)
	 * @param newRank y axis coordinate of the desired move (0-7 only)
	 * @param promo   integer that represents the piece to promote to (if != -1)
	 * 
	 * @return true if move executed successfully, false otherwise
	 */
	private boolean blackPlayMove(int file, int rank, int newFile, int newRank,
			int promo) {
		blackPlay = new Position(file, rank);
		blackNewPosition = new Position(newFile, newRank);

		return black.playMove(blackPlay, blackNewPosition, promo);
	}

}
