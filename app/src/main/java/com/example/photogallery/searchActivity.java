package com.example.photogallery;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class searchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get intent from main activity inorder to get the location of the picture taken
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(MainActivity.Picture_Location);

        //Removed photo preview for space
        //ImageView imPreview = findViewById(R.id.preview);
        //imPreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        // Create various object on the xml file
        final Button btnCancel = findViewById(R.id.btnCancel);
        final Button btnSearch = findViewById(R.id.btnSearch);
        final EditText captionSearch = findViewById(R.id.searchCaption);

        //Date search
        final EditText minDate = findViewById(R.id.minDateSearch);
        final EditText maxDate = findViewById(R.id.maxDateSearch);


        // close activity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Cancel:", "button has been pressed... Termniating");
                finish();
            }
        });


        // On search find the file
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Search Button:", "Button has been pressed");
                // Create intent to send data back to mainactivity
                Intent path = new Intent();

                String captionQuery = captionSearch.getText().toString();
                String dateMinQuery = minDate.getText().toString();
                String dateMaxQuery = maxDate.getText().toString();


                // Check something is entered in the search boxes
                if (captionQuery.isEmpty()) { // if empty box
                    //Toast.makeText(getApplicationContext(), "Please finish query", Toast.LENGTH_SHORT).show();

                    if(dateMinQuery.isEmpty() && dateMaxQuery.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please finish query either CAPTION or DATE", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        path.putExtra("minDate", dateMinQuery);
                        path.putExtra("maxDate", dateMaxQuery);
                        setResult(RESULT_OK, path);
                        finish();
                    }
                }
                else { // if box not empty




                    /*
                    File dir = new File("/storage/emulated/0/Android/data/com.example.photogallery/files/Pictures/");
                    File[] directoryListing = dir.listFiles();
                    if (directoryListing != null) {
                        for (File child : directoryListing) {
                            // check for date in file name
                            if (child.getName().contains(captionQuery)) {
                                path.putExtra("Path", child.getAbsolutePath());
                                path.putExtra("Filename", child.getName());
                                setResult(RESULT_OK, path);
                                finish(); //EXIT when picture is found
                            }
                        }
                    } else {
                        Log.i("Search Button:", "File path is incorrect");
                        Toast.makeText(getApplicationContext(), "Directory is empty", Toast.LENGTH_SHORT).show();
                    }
                    */
                }
            }
        });

    }
}
