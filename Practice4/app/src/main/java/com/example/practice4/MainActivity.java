
package com.example.practice4;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice4.util.Scope;
import com.example.practice4.util.ScopeRecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends NavigationActivity {

    private RecyclerView mScopeListView; // List of scopes
    private RecyclerView.Adapter mAdapter; // Adapter for scopes
    private List<Scope> mScopeList = new ArrayList<>(); // Scope list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Set up the navigation drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawer(toolbar, drawerLayout, navigationView); // Call the method from BaseActivity

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mScopeListView = findViewById(R.id.scopeListView);
        mScopeList.add(new Scope(0, "RAZOR HD GEN III", "Vortex",
                36f, true));
        mScopeList.add(new Scope(1, "GOLDEN EAGLE HD", "Vortex",
                60f, true));
        mScopeList.add(new Scope(2, "HWS EXPS2", "EOTECH",
                1f, false));
        mAdapter = new ScopeRecyclerViewAdapter(mScopeList);
        mScopeListView.setAdapter(mAdapter);
        mScopeListView.setLayoutManager(new LinearLayoutManager(this));
    }

}
