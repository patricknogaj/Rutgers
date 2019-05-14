/**
 * ExampleUnitTest.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package com.rutgers.chess22;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import model.game.Game;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public static final String FILEPATH = "testgame.txt";

    /**
     * Program execution begins here
     *
     * @param args Command line arguments
     * @throws IOException if game.startFromFile is given a String representing
     *                     a non-existent plaintext input file
     */
    public static void main(String[] args) throws IOException {
        Game game = new Game();

        /**
         * By default:
         * -----------
         * printBoard == true
         * debugMoveLog == false
         * debugPostMoveLog == false
         * debugPieceSetLog == false
         *
         * toggleAllDebugLogs will toggle all of these booleans,
         * to allow for the respective console messages.
         *
         * You may also use toggleMoveLog(), togglePostMoveLog(),
         * and togglePieceSetLog() if only some of these messages
         * are required.
         *
         * togglePrintBoard() will toggle the console printout of the
         * chess board.
         */

        /**
         * Comment this out for release!
         */
        //game.toggleAllDebugLogs();
          game.toggleMoveLog();
          game.togglePieceSetLog();
          game.togglePostMoveLog();

        /**
         * If for some reason, you would
         * like to turn off the CLI chess board,
         * here it is:
         */
        //game.togglePrintBoard();




        //game.startFromFile("dat/testgame.txt");
        game.start();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}