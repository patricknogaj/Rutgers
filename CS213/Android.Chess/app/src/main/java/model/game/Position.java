/**
 * Position.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package model.game;

/**
 * Represents a (file, rank) pair as per a Chess board. Instances of Position
 * are owned by Cell, and referred to by subclasses of Piece. Temporary Position
 * objects may be created during the execution and logging of Moves.
 * 
 * @version Mar 3, 2019
 * @author gemuelealudino
 * @author patricknogaj
 */
public final class Position implements Comparable<Position> {

	private int file;
	private int rank;

	Position() {
		file = -1;
		rank = -1;
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param file Integer representation for a file (0 is a, 1 is b, ...)
	 * @param rank Integer representation for a rank (0 is 1, 1 is 2, ...)
	 */
	public Position(int file, int rank) {
		if ((file < 0 || rank < 0) || (file > 7 || rank > 7)) {
			return;
		}

		this.file = file;
		this.rank = rank;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param toCopy Position instance to copy
	 */
	public Position(Position toCopy) {
		this(toCopy.file, toCopy.rank);
	}

	/**
	 * Retrieve the x-axis coordinate of a Position, as per a Chess board
	 * 
	 * @return file of the current Position instance
	 */
	public int getFile() {
		return file;
	}

	/**
	 * Retrieve the y-axis coordinate of a Position, as per a Chess board
	 * 
	 * @return rank of the current Position instance
	 */
	public int getRank() {
		return rank;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean result = false;

		if (o != null && o instanceof Position) {
			Position other = (Position) (o);

			if (file == other.file && rank == other.rank) {
				result = true;
			} else {
				result = false;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		char file = ' ';

		switch (this.file) {
		case 0:
			file = 'a';
			break;
		case 1:
			file = 'b';
			break;
		case 2:
			file = 'c';
			break;
		case 3:
			file = 'd';
			break;
		case 4:
			file = 'e';
			break;
		case 5:
			file = 'f';
			break;
		case 6:
			file = 'g';
			break;
		case 7:
			file = 'h';
			break;
		default:
			file = '-';
			break;
		}

		final String rankPrint = rank > -1 ? Integer.toString(rank + 1) : "-";
		return String.format("%c%s", file, rankPrint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Position o) {
		int result = 0;

		final int deltaFile = file - o.file;
		final int deltaRank = rank - o.rank;

		if (deltaFile == 0) {
			result = deltaRank;
		} else {
			if (deltaRank == 0) {
				result = deltaFile;
			} else {
				// result = deltaFile + deltaRank;
				final double deltaFileSq = Math.pow((int) (deltaFile), 2);
				final double deltaRankSq = Math.pow((int) (deltaRank), 2);

				final double resultant = Math.sqrt(deltaFileSq + deltaRankSq);

				result = (int) (resultant);
			}
		}

		return result;
	}
}
