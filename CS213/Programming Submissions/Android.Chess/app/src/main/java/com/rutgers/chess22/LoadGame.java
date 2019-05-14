/**
 * LoadGame.java
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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.rutgers.chess22.ChessActivity.GAME_FILE_EXT;
import static com.rutgers.chess22.ChessActivity.getTMSFromFileName;
import static com.rutgers.chess22.ChessActivity.getTitleFromFileName;

/**
 * Represents a handle to load the state of a chess game
 */
public class LoadGame extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG_TITLE        = "--TITLE--";
    public static final String TAG_DATE         = "--DATE--";
    public static final String TAG_NOACTION     = "--NO_ACTION--";
    public static final String TAG_DELETE       = "--DELETE--";
    public static final String TAG_PLAYBACK     = "--PLAYBACK--";

    private static final int WIDTH_TITLE        = 290;
    private static final int WIDTH_DATE         = 300;
    private static final int WIDTH_DELETE       = 40;
    private static final int HEIGHT_ALL         = 20;

    public static final String INTENT_DATA_KEY_FILENAME     = "FileName";
    public static final String INTENT_DATA_FILENAME_LAST    = "LastGame";

    Comparator<String> title_norm = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return getTitleFromFileName(o1).toUpperCase().compareTo(getTitleFromFileName(o2).toUpperCase());
        }
    };
    Comparator<String> date_norm =  new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return getTMSFromFileName(o1).compareTo(getTMSFromFileName(o2));
        };
    };
    Comparator<String> title_rev = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return -getTitleFromFileName(o1).toUpperCase().compareTo(getTitleFromFileName(o2).toUpperCase());
        };
    };
    Comparator<String> date_rev =  new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return -getTMSFromFileName(o1).compareTo(getTMSFromFileName(o2));
        };
    };

    boolean title_sort_norm = true;
    boolean date_sort_norm = true;



    TableLayout table;
    List<String> filenameList;

    String fileName;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        //
        table = (TableLayout) findViewById(R.id.table);
        //
        filenameList = getListFilesName(getFilesDir());
        //
        insertHeaderRow();
        insertRows();
    }




    public static List<String> getListFilesName(File parentDir) {
        List<String> inFiles = new ArrayList<>();
        //
        Queue<File> files = new LinkedList<>();
        //
        files.addAll(Arrays.asList(parentDir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.listFiles()));
            }
            else if (file.getName().endsWith(GAME_FILE_EXT)) {
                inFiles.add(file.getName());
            }
        }
        return inFiles;
    }



    private void insertHeaderRow() {
        TableRow tbrow = new TableRow(this);
        //
        {   TextView tv = new TextView(this);
            tv.setText("Title");
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(getResources().getColor(R.color.boardDark));
            tv.setTypeface(null, Typeface.BOLD);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(15);
            tv.setTag(TAG_TITLE);
            tv.setWidth(WIDTH_TITLE);
            tv.setOnClickListener(LoadGame.this);
            tbrow.addView(tv);
        }
        //
        {   TextView tv = new TextView(this);
            tv.setText("Date");
            tv.setTextColor(Color.BLACK);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setBackgroundColor(getResources().getColor(R.color.boardDark));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(15);
            tv.setTag(TAG_DATE);
            tv.setWidth(WIDTH_DATE);
            tv.setOnClickListener(LoadGame.this);
            tbrow.addView(tv);
        }
        //
        {   TextView tv = new TextView(this);
            tv.setText(" ");
            tv.setTextColor(Color.BLACK);
            tv.setBackgroundColor(getResources().getColor(R.color.boardDark));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(15);
            tv.setWidth(WIDTH_DELETE);
            tv.setTag(TAG_NOACTION);
            tbrow.addView(tv);
        }
        //
        table.addView(tbrow);
    }
    private void insertRows() {
        for (int i = 0; i < filenameList.size(); i++) {
            TableRow tbrow = new TableRow(this);
            fileName = filenameList.get(i);
            //
            {   TextView tv = new TextView(this);
                tv.setText(getTitleFromFileName(fileName));
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                tv.setWidth(WIDTH_TITLE);
                tv.setTag(TAG_PLAYBACK+fileName);
                tv.setOnClickListener(LoadGame.this);
                tbrow.addView(tv);
            }
            //
            {   TextView tv = new TextView(this);
                tv.setText(getTMSFromFileName(fileName));
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                //tv.setWidth(WIDTH_DATE);
                tv.setTag(TAG_PLAYBACK+fileName);
                tv.setOnClickListener(LoadGame.this);
                tbrow.addView(tv);
            }
            //
            {   Button bt = new Button(this);
                bt.setText("Delete");
                bt.setTextColor(Color.BLACK);
                bt.setGravity(Gravity.CENTER);
                bt.setHeight(10);
                bt.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.boardDark), PorterDuff.Mode.MULTIPLY);
                bt.setWidth(WIDTH_DELETE);
                bt.setTag(TAG_DATE);
                bt.setTag(TAG_DELETE+fileName);
                bt.setOnClickListener(LoadGame.this);
                tbrow.addView(bt);
            }
            //
            table.addView(tbrow);
        }
    }

    public static boolean deleteFile(File parentDir, String fileName) {
        String filePath = parentDir.getPath() + File.separator + fileName;
        //
        File file = new File(filePath);
        //
        return file.delete();
    }


    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString();
        //
        if (tag.equalsIgnoreCase(TAG_TITLE)) {
            Comparator<String> comp = title_sort_norm ? title_norm : title_rev;
            title_sort_norm = !title_sort_norm;
            //
            Collections.sort(filenameList, comp);
            //
            int childCount = table.getChildCount();
            // Remove all rows except the first one
            if (childCount > 1) {
                table.removeViews(1, childCount - 1);
            }
            //
            insertRows();


        }
        else if (tag.equalsIgnoreCase(TAG_DATE)) {
            Comparator<String> comp = date_sort_norm ? date_norm : date_rev;
            date_sort_norm = !date_sort_norm;
            //
            Collections.sort(filenameList, comp);
            //
            int childCount = table.getChildCount();
            // Remove all rows except the first one
            if (childCount > 1) {
                table.removeViews(1, childCount - 1);
            }
            //
            insertRows();
        }
        else if (tag.startsWith(TAG_DELETE)) {
            for (int i = 0; i < filenameList.size(); i++) {
                fileName = filenameList.get(i);
                //
                if ((TAG_DELETE + fileName).equals(tag)) {
                    boolean ret = deleteFile(getFilesDir(), fileName);
                    table.removeView(table.getChildAt(i + 1));
                    filenameList.remove(i);
                }
            }
        }
        else if (tag.startsWith(TAG_PLAYBACK)) {
            fileName = tag.substring(TAG_PLAYBACK.length());
            //
            intent = new Intent(LoadGame.this, PlaybackGame.class);
            intent.putExtra(INTENT_DATA_KEY_FILENAME, fileName);

            startActivity(intent);
        }

    }
}
