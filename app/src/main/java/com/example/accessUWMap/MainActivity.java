package com.example.accessUWMap;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {
    ////////////////////////////////////////////////////////////
    ///     Fields
    ////////////////////////////////////////////////////////////

    // Coords for scrolling in map view
    private float mx, my;
    // Scroll views for moving on the map
    private ScrollView vScroll;
    private HorizontalScrollView hScroll;

    private String[] locations = {"Terry Hall", "Kane Hall", "Odegaard Library", "The HUB", "Yahtzee!!", "Hocus pocus"};


    ////////////////////////////////////////////////////////////
    ///     Methods
    ////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get scroll views that control 2-D scrollability (i.e. user can move freely around map)
        vScroll = findViewById(R.id.vScroll);
        hScroll = findViewById(R.id.hScroll);

        // Get auto-complete start search view
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, locations);
        AutoCompleteTextView startACTV = findViewById(R.id.searchStartView);
        startACTV.setThreshold(1);
        startACTV.setAdapter(startAdapter);

        // Get auto-complete end search view
        ArrayAdapter<String> endAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, locations);
        AutoCompleteTextView endACTV = findViewById(R.id.searchEndView);
        endACTV.setThreshold(1);
        endACTV.setAdapter(endAdapter);

        // Set click listener for lower-right button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Current x,y position on map that the user is looking at
        float curX, curY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                // Get current x,y on screen of where user clicks
                mx = event.getX();
                my = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Scroll on the map based on user's finger movement
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                mx = curX;
                my = curY;
                break;
            case MotionEvent.ACTION_UP:
                // Stop moving the map once the user releases the screen
                curX = event.getX();
                curY = event.getY();
                vScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                hScroll.scrollBy((int) (mx - curX), (int) (my - curY));
                break;
        }
        return true;
    }

    /**
     * Updater method that controls the current start location of the user's selected route
     * @param newStart is the new start location for the user's route
     */
    public void updateStartLocation(String newStart) {
        System.out.println("NEW START: " + newStart);

        //TODO: REMOVE LOCAL startLocation and switch to Presenter version of it
        //TODO: Add catch here in updateStartLocation to tell user to give a valid input in case it's wrong

        //TODO: Make it so user can't hit enter to go down and hiddenly stretch text view
        //TODO: Maybe make it so if user hits enter it counts as hitting "Search"
        //TODO: Make search bars have room on the left side for clear button and switch button?

        //TODO: Can use getListSelection() to get what user selected
        //TODO: Can use isPopupShowing() to see if popup menu is showing

    }
}