package com.example.practice3;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.database.DBHandler;
import com.example.practice3.utils.ProductItem;
import com.example.practice3.utils.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private Button proceedButton;
    private RecyclerView mListView;
    private List<ProductItem> mProductItemList;
    private RecyclerView.Adapter mAdapter;

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

        dbHandler = new DBHandler(getApplicationContext());

        mListView = (RecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new RecyclerViewAdapter(getListData());

        proceedButton = findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enoughItemsSelected()) {
                    //Proceed to next activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.practice3",
                            "com.example.practice3.EmailInfoActivity"));
                    intent.putParcelableArrayListExtra("products", );
                    startActivity(intent);
                } else {
                    //Display Snackbar
                    Snackbar snackbar = Snackbar.make(v, "Please select 3 or more products!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private List<ProductItem> getListData() {
        //TODO: Query Database
        return null;
    }

    private boolean enoughItemsSelected() {
        return mProductItemList.parallelStream().filter(ProductItem::isSelected).count() >= 3;
    }
}