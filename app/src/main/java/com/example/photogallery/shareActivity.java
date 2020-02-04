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



public class shareActivity extends AppCompatActivity {

    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        final Button btnTwitter = findViewById(R.id.btnTwitter);

        Intent intent = getIntent();
        filePath = intent.getStringExtra(MainActivity.Picture_Location);

        ImageView sharePreview = findViewById(R.id.ivShare);
        sharePreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Log.i("Dev:: Sharing:",filePath);

        btnTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Dev::", "Sharing to Messenger");

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
                }



            }



        });


    }
}
