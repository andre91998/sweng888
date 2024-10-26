package com.example.practice4;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends NavigationActivity {

    // UI components
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about); // Set the layout for MainActivity

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set toolbar as ActionBar

        // Initialize DrawerLayout and NavigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setupDrawer(toolbar, drawer, navigationView, currentUser);
    }

}
