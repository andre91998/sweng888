
package com.example.practice4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice4.util.Scope;
import com.example.practice4.util.ScopeRecyclerViewAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
                            scope.setId(document.getId()); // Set the Firestore document ID
                            scope.setName(document.getString("name"));
                            scope.setBrand(document.getString("brand"));
                            scope.setMaxMagnification(document.getString("maxMagnification"));
                            scope.setVariableMagnification(Boolean.TRUE.equals(document
                                    .getBoolean("variableMagnification")));
                            mScopeList.add(scope); // Add the product to the list
                        }
                    }
                    mAdapter = new ScopeRecyclerViewAdapter(mScopeList);
                    mScopeListView.setAdapter(mAdapter);
                    mScopeListView.setLayoutManager(new LinearLayoutManager(this));
                });
    }

    private void addScope() {

        //Show popup to fill in new scope data
        final View layout = View.inflate(this, R.layout.add_scope_dialog, null);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Create New Scope")
                .setMessage("Set the Scope info")
                .setView(layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //TODO: write to firebase
                        EditText edit = (EditText)layout.findViewById(R.id.inputScopeName);
                        String scopeName = edit.getText().toString();

                        edit = (EditText)layout.findViewById(R.id.inputScopeBrand);
                        String scopeBrand = edit.getText().toString();

                        edit = (EditText)layout.findViewById(R.id.inputMaxMagnification);
                        String scopeMaxMagnification = edit.getText().toString();

                        Switch aSwitch = (Switch)layout.findViewById(R.id.inputScopeVariableMagnification);
                        boolean hasVarMag = aSwitch.isChecked();

                        if (scopeName.isEmpty() || scopeBrand.isEmpty() || scopeMaxMagnification.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter all information", Toast.LENGTH_SHORT).show();
                            return; // Exit if inputs are invalid
                        }

                        // Add a new document with a generated id.
                        Map<String, Object> scope = new HashMap<>();
                        scope.put("name", scopeName);
                        scope.put("brand", scopeBrand);
                        scope.put("maxMagnification", scopeMaxMagnification);
                        scope.put("variableMagnification", hasVarMag);
                        db.collection("scopes").add(scope).addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Scope added successfully", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(), "Error adding scope: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        setupRecyclerView();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
    }

    private void clearScopes() {
            for (Scope scope : mScopeList) {
                // Delete the scope from Firestore
                db.collection("scopes").document(scope.getId()).delete()
                        .addOnSuccessListener(aVoid -> {
                            mScopeList.remove(scope); // Remove from the list
                            setupRecyclerView();
                        })
                        .addOnFailureListener(e -> {
                            //do nothing
                        });
            }
        }
}
