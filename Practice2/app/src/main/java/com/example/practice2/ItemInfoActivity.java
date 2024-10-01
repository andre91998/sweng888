package com.example.practice2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.practice2.utils.Item;

public class ItemInfoActivity extends AppCompatActivity {
    private static String LOG_TAG = "ItemInfoActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteminfo);
        Item item;
        try {
            item = getIntent().getExtras().getSerializable("item", Item.class);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Failed to find Item to display");
           item = new Item("N/A", "N/A");
        }
        TextView title = findViewById(R.id.titleView);
        title.setText(item.getTitle());
        TextView subtitle = findViewById(R.id.subtitleView);
        subtitle.setText((item.getSubtitle()));
    }
}
