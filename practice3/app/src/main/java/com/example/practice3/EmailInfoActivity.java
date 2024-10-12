package com.example.practice3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.utils.Product;
import com.example.practice3.utils.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmailInfoActivity extends AppCompatActivity {

    private static String LOG_TAG = "emailInfoActivity";
    private RecyclerView.Adapter mAdapter;
    private List<Product> mProductList;
    private RecyclerView mListView;
    private Button mEmailButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_info);

        mListView = (RecyclerView) findViewById(R.id.recyclerView);
        mProductList = getIntent()
                .getParcelableArrayListExtra("products");
        Log.d(LOG_TAG, "Received Size: " + mProductList.size());
        if (mProductList != null && !mProductList.isEmpty()) {
            mAdapter = new RecyclerViewAdapter(mProductList);
        } else {
            // Failed to get list, so set to empty list view
            mAdapter = new RecyclerViewAdapter(new ArrayList<Product>());
        }

        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        mEmailButton = findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProductList.isEmpty() || mProductList == null) {
                    Snackbar snackbar = Snackbar.make(view, "ERROR: no products to send!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"amsoccercrazy@mail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Practice3_Products");

                StringBuilder sb = new StringBuilder();
                for (Product p : mProductList) {
                    sb.append(p.getProductString());
                    sb.append("\n");
                }

                emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );

//                Uri attachment = FileProvider.getUriForFile(this, "my_fileprovider", myFile);
//                emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);

                for (Product p: mProductList) {
                    File image = new File(getExternalCacheDir(), p.getName() + ".jpg");
                    try (FileOutputStream fos = new FileOutputStream(image)){
                        byte[] picture = p.getPicture();
                        if (picture != null) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(p.getPicture(), 0,
                                    p.getPicture().length);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    startActivity(emailIntent);
                    Toast completionToast = new Toast(getApplicationContext());
                    completionToast.setText("Product Info Successfully sent over Email!");
                    completionToast.show();
                    mAdapter = new RecyclerViewAdapter(new ArrayList<Product>());
                } catch (ActivityNotFoundException e) {
                    //Handle no email client case
                    Snackbar snackbar = Snackbar.make(view, "No email client available!!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
