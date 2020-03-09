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
import com.example.photogallery.photoPackage.photoClass;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.sql.*;

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
        final Button btnDatabase = findViewById(R.id.btnDatabase);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(MainActivity.Picture_Location);

        ImageView sharePreview = findViewById(R.id.ivShare);
        sharePreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Log.i("Dev:: Sharing:",filePath);

        btnDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoClass file = new photoClass(filePath);
                Connection con = null;
                InputStream image = null;
                Log.i("btnDatabase", "has been pressed");

//                String caption = file.getCaption();
//                String date = file.dateTime;
//                String longitude = file.getLongitude();
//                String latitude = file.getLatitude();


                String caption = "y";
                String date = "y";
                String longitude ="y";
                String latitude = "y";
                File im = new File(filePath);
                try {
                    image = new FileInputStream(im);
                }
                catch (FileNotFoundException ex)
                {
                    // insert code to run when exception occurs
                    Log.i("Image file:", "no file found");

                }



                try {
                    // load the driver class
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Log.i("Driver connection:", "drivers loaded");
                }
                catch (Exception ex) {
                    Log.i("Driver connection:", "drivers not loaded");
                }


                try {
                    // establish connection
                    con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.42.49:1521:XE", "system", "riseup");
                    con.setAutoCommit(false);
                    // create table ImageTable(CAPTION char(400), TAKEN char(400), LONGITUDE char(400), LATITUDE char(400), PHOTO BLOB);
                    // Get a result set containing all data from test_table
                    PreparedStatement ps = con.prepareStatement("insert into ImageTable(CAPTION, TAKEN, LONGITUDE, LATITUDE, PHOTO) values(?,?,?,?,?)");
                    ps.setString(1, caption);
                    ps.setString(2, date);
                    ps.setString(3, longitude);
                    ps.setString(4, latitude);
                    ps.setBinaryStream(5, image, (int) im.length());
                    int i = ps.executeUpdate();
                    con.commit();
                    con.setAutoCommit(true);
                    con.close();
                    Toast.makeText(getApplicationContext(), "upload sucessful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch(SQLException ex) {
                    Toast.makeText(getApplicationContext(), "Could not connect to database", Toast.LENGTH_SHORT).show();
                    finish();

                }


            }
        });


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
