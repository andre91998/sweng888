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

import com.example.practice3.database.DBHandler;
import com.example.practice3.utils.Product;
import com.example.practice3.utils.RecyclerViewAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
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
    private DBHandler dbHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_info);

        mListView = (RecyclerView) findViewById(R.id.recyclerView);

        mProductList = getIntent().getParcelableArrayListExtra("products");

        Log.d(LOG_TAG, "Received Size: " + mProductList.size());

        dbHandler = new DBHandler(getApplicationContext());
        mProductList.forEach(p -> p.setPicture(
                dbHandler.queryProductPicture(p.getId())));

        if (mProductList != null && !mProductList.isEmpty()) {
            mAdapter = new RecyclerViewAdapter(mProductList);
        } else {
            // Failed to get list, so set to empty list view
            mAdapter = new RecyclerViewAdapter(new ArrayList<Product>());
        }

        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(this));

        //Setup callback mechanism for the execution of the email sending activity
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

        //setup send email functionality
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

                final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

                //set email recipients and subject
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"sweng888mobileapps@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Practice3_Products");

                //Set Email body
                StringBuilder sb = new StringBuilder();
                for (Product p : mProductList) {
                    sb.append(p.getProductString());
                    sb.append("\n");
                    sb.append("\n");
                }
                ArrayList<CharSequence> sbArray = new ArrayList<>();
                sbArray.add(sb.toString());
                emailIntent.putExtra(Intent.EXTRA_TEXT, sbArray.get(0));
                emailIntent.setSelector( emailSelectorIntent );

                //Set email image attachments
                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                for (Product p: mProductList) {
                    byte[] picture = p.getPicture();
                    if (picture != null) {
                        File photo = new File(getExternalCacheDir(),
                                p.getName() + ".jpg");
                        try {
                            FileOutputStream fos = new FileOutputStream(photo);
                            fos.write(picture);
                            fos.close();
                            if (photo.exists() && photo.length()>0) {
                                Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                                        getPackageName() + ".provider", photo);
                                imageUris.add(uri);

                                //grant permission to gmail to access the uri
                                getApplicationContext().grantUriPermission(
                                        "com.google.android.gm", uri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
                        } catch(java.io.IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(LOG_TAG, "Picture was null");
                    }
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                //launch email intent
                try {
                    mLauncher.launch(emailIntent);
                    //getApplicationContext().star(emailIntent, );
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
