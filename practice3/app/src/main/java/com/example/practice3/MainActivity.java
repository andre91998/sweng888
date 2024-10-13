package com.example.practice3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.database.DBHandler;
import com.example.practice3.utils.Product;
import com.example.practice3.utils.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = "Practice3Main";
    private DBHandler dbHandler;
    private Button proceedButton;
    private RecyclerView mListView;
    private List<Product> mProductList;
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

        initDB();

        mListView = (RecyclerView) findViewById(R.id.recyclerView);
        mProductList = dbHandler.queryAllCourses();

        mAdapter = new RecyclerViewAdapter(mProductList);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        proceedButton = findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enoughItemsSelected()) {
                    //Proceed to next activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.practice3",
                            "com.example.practice3.EmailInfoActivity"));

                    intent.putParcelableArrayListExtra("products", new ArrayList<Product>(mProductList.stream()
                            .filter(e -> ((RecyclerViewAdapter) mAdapter)
                                    .getProductSelectedMap().get(e.getId()))
                            .collect(Collectors.toList())));
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

    private boolean enoughItemsSelected() {
        return ((RecyclerViewAdapter) mAdapter).getSelectedItemCount() >= 3;
    }

    private void initDB() {
        getApplicationContext().deleteDatabase("practice3DB"); //to have fresh database every demo run
        dbHandler = new DBHandler(getApplicationContext());
        dbHandler.addNewProduct("Mustang", "Ford Muscle Car", "Ford", 50000f, getImage(getApplicationContext(), R.drawable.mustang));
        dbHandler.addNewProduct("Camaro", "Chevrolet Muscle Car", "Chevrolet", 50500f, getImage(getApplicationContext(), R.drawable.camaro));
        dbHandler.addNewProduct("Challenger", "Dodge Muscle Car", "Dodge", 55000f, getImage(getApplicationContext(), R.drawable.challenger));
        dbHandler.addNewProduct("Corvette", "Chevrolet Sports Car", "Chevrolet", 90000f, getImage(getApplicationContext(), R.drawable.corvette));
    }

    private static byte[] getImage(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0, canvas.getWidth(), canvas.getHeight());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}