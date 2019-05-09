/**
 * MainActivity.java
 *
 * Copyright (c) 2019 Patrick Nogaj, Gemuele Aludino
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */
package com.rutgers.chess22;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonPlayGame;
    private Button buttonLoadGame;
    private Button buttonSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonPlayGame = (Button) findViewById(R.id.buttonPlayGame);
        buttonPlayGame.setOnClickListener(this);

        buttonLoadGame = (Button) findViewById(R.id.buttonLoadGame);
        buttonLoadGame.setOnClickListener(this);

        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonPlayGame:
                Intent playGame = new Intent(this, ChessActivity.class);
                startActivity(playGame);
                break;
            case R.id.buttonLoadGame:
                Intent loadGame = new Intent(this, LoadGame.class);
                startActivity(loadGame);
                break;
            case R.id.buttonSettings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                break;
            default:
                break;
        }
    }
}
