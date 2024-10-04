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

        // create a arraylist of the type NumbersView
        final ArrayList<Item> arrayList = new ArrayList<Item>();

        // add all the values from 1 to 15 to the arrayList
        // the items are of the type NumbersView
        arrayList.add(new Item("SWENG888", "Mobile Computing", getString(R.string.description_888)));
        arrayList.add(new Item("AI879", "Machine Vision", getString(R.string.description_879)));
        arrayList.add(new Item("SWENG587", "Software System Architecture", getString(R.string.description_587)));
        arrayList.add(new Item("SWENG837", "Software System Design", getString(R.string.description_837)));
        arrayList.add(new Item("SWENG505", "Software Project Management", getString(R.string.description_505)));
        arrayList.add(new Item("SWENG581", "Software Testing", getString(R.string.description_581)));
        arrayList.add(new Item("SWENG861", "Software Construction", getString(R.string.description_861)));
        arrayList.add(new Item("SWENG586", "Requirements Engineering", getString(R.string.description_586)));
        arrayList.add(new Item("INSC561", "Web Security", getString(R.string.description_561)));

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