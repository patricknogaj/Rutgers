package com.rutgers.chess22;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import model.game.Game;

public class PlaybackGame extends ChessActivity implements View.OnClickListener {

    private TextView playersTurn;

    private Button btnBackward;
    private Button btnForward;
    private Button btnMainMenu;

    private List<String> stringMoveList;

    private String previous;
    private String next;

    private int[] fileRankArray;

    private int currentMove;

    private int maxSize;

    private static final String pieceRegex = "[qQbBnNrR]";

    private static final String whiteSpaceRegex = "[ \\\\t\\\\n\\\\x0b\\\\r\\\\f]";
    private static final String fileRankRegex = "[a-h][1-8]";
    private static final String validFileRankRegex = String.format("%s%s%s",
            fileRankRegex, whiteSpaceRegex, fileRankRegex);

    private static final String drawRegex = "draw\\?";
    private static final String validFileRankWithDrawRegex = String
            .format("%s%s%s", validFileRankRegex, whiteSpaceRegex, drawRegex);

    private static final String validFileRankWithPromotion = String
            .format("%s%s%s", validFileRankRegex, whiteSpaceRegex, pieceRegex);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_game);

        playersTurn = findViewById(R.id.playersTurn);
        playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");

        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(this);

        btnForward = findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);

        btnMainMenu = findViewById(R.id.btnMainMenu);
        btnMainMenu.setOnClickListener(this);

        initializeChessboard(this);

        String input = "";

        Intent intent = getIntent();
        String fileName = intent.getExtras().getString(LoadGame.INTENT_DATA_KEY_FILENAME);
        Object obj = readObjectFromFile(getFilesDir(), fileName);
        if (obj != null) {
            System.out.println("Opened: " + fileName);
            input += (String) obj;
        }

        try {
            stringMoveList = game.generateMoveListForPlayback(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        maxSize = stringMoveList.size();

        fileRankArray = new int[5];

        currentMove = 0;

        updateValues();
    }

    public void updateValues() {
        next = stringMoveList.get(currentMove);

        if (next.matches(validFileRankRegex)) {
            fileRankArray = game.getFileRankArray(next);
        } else if (next.matches(validFileRankWithPromotion)) {
            fileRankArray = game.getFileRankArray(next);
        } else if (next.equals("resign")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlaybackGame.this);
            builder.setCancelable(true);
            builder.setTitle("GAME OVER: Resign.");
            builder.setMessage("The winner is: NOBODY.\nContinue to main menu...");
            builder.setPositiveButton("Continue",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mainMenu = new Intent(PlaybackGame.this, MainActivity.class);
                            startActivity(mainMenu);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
    }

    private int charToInt(char ch) {
        int intFile = -1;

        switch (ch) {
            case 'a':
                intFile = 0;
                break;
            case 'b':
                intFile = 1;
                break;
            case 'c':
                intFile = 2;
                break;
            case 'd':
                intFile = 3;
                break;
            case 'e':
                intFile = 4;
                break;
            case 'f':
                intFile = 5;
                break;
            case 'g':
                intFile = 6;
                break;
            case 'h':
                intFile = 7;
                break;
        }

        return intFile;
    }

    public static Object readObjectFromFile(File parentDir, String fileName) {
        String filePath = parentDir.getPath() + File.separator + fileName;
        Object obj = null;
        //
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(filePath);
            in = new ObjectInputStream(fileIn);
            obj = in.readObject();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            if(v == btnBackward) {
                if (currentMove > 0) {
                    doUndo();
                    currentMove--;
                    updateValues();

                    playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");
                }
            } else if(v == btnForward) {
                if (currentMove < maxSize) {

                    if (next.equals("resign")) {

                    } else if (next.equals("draw?")) {

                    } else if (next.equals("draw")) {

                    } else {
                        if (game.canPromote(fileRankArray[0], fileRankArray[1], fileRankArray[2], fileRankArray[3])) {
                            promotion(fileRankArray[1], fileRankArray[2]);
                        }

                        updateValues();

                        for (int i = 0; i < 5; i++) {
                            System.out.println(fileRankArray[i]);
                        }

                        movePiece(fileRankArray[0], fileRankArray[1], fileRankArray[2], fileRankArray[3]);

                        ++currentMove;

                        playersTurn.setText(game.isWhitesMove() ? "Whites turn" : "Blacks turn");
                    }
                }
            } else if(v == btnMainMenu) {
                Intent mainMenu = new Intent(PlaybackGame.this, MainActivity.class);
                startActivity(mainMenu);
            } else {

            }
        }
    }
}
