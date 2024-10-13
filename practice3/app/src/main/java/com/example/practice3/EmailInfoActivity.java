package com.example.practice3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practice3.utils.Product;
import com.example.practice3.utils.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
    private ActivityResultLauncher<Intent> mLauncher;

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

        mLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(LOG_TAG, "Activity Result Received" + result.getResultCode());
                    if(result.getResultCode() == RESULT_CANCELED) {
                        Toast completionToast = new Toast(getApplicationContext());
                        completionToast.setText("Product Info Successfully sent over Email!");
                        completionToast.show();
                        mProductList.clear();
                        mAdapter = new RecyclerViewAdapter(mProductList);
                        mListView.setAdapter(mAdapter);
                        mListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }
        );

        mEmailButton = findViewById(R.id.emailButton);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProductList.isEmpty() || mProductList == null) {
                    Snackbar snackbar = Snackbar.make(view, "ERROR: no products to send!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return;
                }

                Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
                emailSelectorIntent.setData(Uri.parse("mailto:"));

                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"amsoccercrazy@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Practice3_Products");

                StringBuilder sb = new StringBuilder();
                for (Product p : mProductList) {
                    sb.append(p.getProductString());
                    sb.append("\n");
                    sb.append("\n");
                }

                emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                emailIntent.setSelector( emailSelectorIntent );

                ArrayList<Uri> imageUris = new ArrayList<>();
                for (Product p: mProductList) {
                    byte[] picture = p.getPicture();
                    if (picture != null) {
                        File photo = new File(getFilesDir(), p.getName() + ".jpg");
                        try {
                            FileOutputStream fos = new FileOutputStream(photo);
                            fos.write(picture);
                            fos.close();
                            if (photo.exists() && photo.length()>0) {
                                Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                                        getPackageName() + ".fileprovider", photo);
                                imageUris.add(uri);
                            }
                        } catch(java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

                try {
                    mLauncher.launch(emailIntent);
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
