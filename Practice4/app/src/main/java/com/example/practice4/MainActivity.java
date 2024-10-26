
package com.example.practice4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice4.util.Scope;
import com.example.practice4.util.ScopeRecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends NavigationActivity {

    // UI components
    private Toolbar toolbar;
    private Button addButton, clearButton;
    private RecyclerView mScopeListView; // List of scopes
    private RecyclerView.Adapter mAdapter; // Adapter for scopes
    private List<Scope> mScopeList = new ArrayList<>(); // Scope list

    private FirebaseFirestore db; // Firestore database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for MainActivity

        //Initialize DB
        db = FirebaseFirestore.getInstance();

        addButton = findViewById(R.id.addScopeButton);
        addButton.setOnClickListener(f -> addScope());

        clearButton = findViewById(R.id.clearScopesButton);
        clearButton.setOnClickListener(f -> clearScopes());

        // Setup Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set toolbar as ActionBar

        // Initialize DrawerLayout and NavigationView
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setupDrawer(toolbar, drawer, navigationView, currentUser);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mScopeListView = findViewById(R.id.scopeListView);
        db.collection("scopes")
                .addSnapshotListener((EventListener<QuerySnapshot>) (value, error) -> {
                    if (error != null) {
                        Toast.makeText(getApplicationContext(), "Failed to load products.", Toast.LENGTH_SHORT).show();
                        return; // Exit if there is an error
                    }

                    // Clear the current list and add all products from Firestore
                    mScopeList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot document : value) {
                            Scope scope = document.toObject(Scope.class); // Convert document to Product object
                            scope.setId(Integer.parseInt(document.getId())); // Set the Firestore document ID
                            mScopeList.add(scope); // Add the product to the list
                        }
                    }
                    mAdapter = new ScopeRecyclerViewAdapter(mScopeList);
                    mScopeListView.setAdapter(mAdapter);
                    mScopeListView.setLayoutManager(new LinearLayoutManager(this));
                });
    }

    private void addScope() {
        //TODO: show popup to fill in new scope data
        //TODO: write to firebase
    }

    private void clearScopes() {
        //TODO: clear firebase scope database
    }

}
