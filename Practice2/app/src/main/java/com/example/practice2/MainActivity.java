package com.example.practice2;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.practice2.utils.Item;
import com.example.practice2.utils.ItemsAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    View currentView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button showButton = findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement button click logic
            }
        });

        // create a arraylist of the type NumbersView
        final ArrayList<Item> arrayList = new ArrayList<Item>();

        // add all the values from 1 to 15 to the arrayList
        // the items are of the type NumbersView
        arrayList.add(new Item("1", "One"));
        arrayList.add(new Item("2", "Two"));
        arrayList.add(new Item("3", "Three"));
        arrayList.add(new Item("4", "Four"));
        arrayList.add(new Item("5", "Five"));
        arrayList.add(new Item("6", "Six"));
        arrayList.add(new Item("7", "Seven"));
        arrayList.add(new Item("8", "Eight"));
        arrayList.add(new Item("9", "Nine"));
        arrayList.add(new Item("10", "Ten"));
        arrayList.add(new Item("11", "Eleven"));
        arrayList.add(new Item("12", "Twelve"));
        arrayList.add(new Item("13", "Thirteen"));
        arrayList.add(new Item("14", "Fourteen"));
        arrayList.add(new Item("15", "Fifteen"));

        ItemsAdapter itemsAdapter = new ItemsAdapter(this, arrayList);
        ListView itemsList = findViewById(R.id.listView);
        itemsList.setAdapter(itemsAdapter);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentView = view;
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.practice2",
                        "com.example.practice2.ItemInfoActivity"));
                intent.putExtra("item", (Serializable) itemsList.getAdapter().getItem(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentView != null) {
            Snackbar snackbar = Snackbar.make(currentView, "Succesfully Returned to Home Screen", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}