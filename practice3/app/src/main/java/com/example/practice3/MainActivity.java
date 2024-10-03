package com.example.practice3;

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

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private DBHandler dbHandler;
    private Button proceedButton;
    private RecyclerView mListView;
    private List<ProductItem> mProductList;
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
                //TODO: Button Logic
                if (enoughItemsSelected()) {
                    //TODO: proceed to next activity
                } else {
                    //TODO:Display Snackbar
                }
            }
        });
    }

    private List<ProductItem> getListData() {
        return null;
    }

    private boolean enoughItemsSelected() {
        return mProductList.parallelStream().filter(ProductItem::isSelected).count() >= 3;
    }
}