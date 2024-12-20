package com.example.practice3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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

        //configure database and insert our product entries
        initDB();

        mListView = (RecyclerView) findViewById(R.id.recyclerView);
        mProductList = dbHandler.queryAllCourses();

        mAdapter = new RecyclerViewAdapter(mProductList);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        //Configure Proceed Button
        proceedButton = findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enoughItemsSelected()) {
                    //Proceed to next activity
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.example.practice3",
                            "com.example.practice3.EmailInfoActivity"));

                    mProductList.forEach(p -> p.setPicture(null));
                    intent.putParcelableArrayListExtra("products", new ArrayList<Product>(mProductList.stream()
                            .filter(e -> ((RecyclerViewAdapter) mAdapter)
                                    .getProductSelectedMap().get(e.getId()))
                            .collect(Collectors.toList())));
                    startActivity(intent);
                } else {
                    //Display Error Snackbar
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
        dbHandler.addNewProduct("Mustang", "Ford Muscle Car",
                "Ford", 50000f,
                bitmapToByteArray(drawableToBitmap(AppCompatResources.getDrawable(this,
                        R.drawable.mustang))));
        dbHandler.addNewProduct("Camaro", "Chevrolet Muscle Car",
                "Chevrolet", 50500f,
                bitmapToByteArray(drawableToBitmap(AppCompatResources.getDrawable(this,
                        R.drawable.camaro))));
        dbHandler.addNewProduct("Challenger", "Dodge Muscle Car",
                "Dodge", 55000f,
                bitmapToByteArray(drawableToBitmap(AppCompatResources.getDrawable(this,
                        R.drawable.challenger))));
        dbHandler.addNewProduct("Corvette", "Chevrolet Sports Car",
                "Chevrolet", 90000f,
                bitmapToByteArray(drawableToBitmap(AppCompatResources.getDrawable(this,
                        R.drawable.corvette))));    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}