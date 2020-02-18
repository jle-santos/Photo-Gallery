// File:         shareActivity.java
// Created:      [2020/01/14 creation date]
// Author:       Lemuel, Karen, Ryan
//
// Desc:
//  Share activity allows the user to share to:
//      1. Messenger
//      2. Discord
//
package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

//*******************************************************************
//  ShareActivity
//
//  Shares the current photo selected by the main activity
//*******************************************************************
public class shareActivity extends AppCompatActivity {

    String filePath;

    // Initilizes activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        final Button btnTwitter = findViewById(R.id.btnTwitter);
        final Button btnDiscord = findViewById(R.id.btnDiscord);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(MainActivity.Picture_Location);

        ImageView sharePreview = findViewById(R.id.ivShare);
        sharePreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Log.i("Dev:: Sharing:",filePath);

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Dev::", "Sharing to Messenger");
                shareImage("com.facebook.orca");
                /*
                File file = new File(filePath);

                String packageName = "com.facebook.orca";

                Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                                            "com.example.photogallery.fileprovider",
                                                    file);

                Intent sendIntent = new Intent();
                sendIntent.setType("image/*");
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                //sendIntent.putExtra(Intent.EXTRA_SUBJECT, "SENT FROM MY ANDROID");
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "TEST FROM ANDROID");
                //sendIntent.setType("text/plain");

                sendIntent.setPackage(packageName);

                try {
                    startActivity(sendIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "No Application Found", Toast.LENGTH_SHORT).show();
                }*/
            }



        });

        btnDiscord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Dev::", "Sharing to Discord");
                shareImage("com.discord");

            }



        });
    }

    /**
     * Desc:
     *  pushes the current image on the main activity as an intent
     *  to the desired app [Discord or messenger]
     *
     * Bugs:
     *  None atm
     */
    private void shareImage(String namePackage) {
        File file = new File(filePath);

        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(),
                "com.example.photogallery.fileprovider",
                file);

        Intent sendIntent = new Intent();
        sendIntent.setType("image/*");
        sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        //sendIntent.putExtra(Intent.EXTRA_SUBJECT, "SENT FROM MY ANDROID");
        //sendIntent.putExtra(Intent.EXTRA_TEXT, "TEST FROM ANDROID");
        //sendIntent.setType("text/plain");

        sendIntent.setPackage(namePackage);

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No Application Found", Toast.LENGTH_SHORT).show();
            }
        }
}
