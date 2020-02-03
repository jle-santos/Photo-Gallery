package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class shareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        String filePath = intent.getStringExtra(MainActivity.Picture_Location);

        ImageView sharePreview = findViewById(R.id.ivShare);
        sharePreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        Log.i("Dev:: Sharing:",filePath);

    }
}
