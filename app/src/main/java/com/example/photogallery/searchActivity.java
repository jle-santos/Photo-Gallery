package com.example.photogallery;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
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

    //Location
    private gpsClass gpsSearch = new gpsClass(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get intent from main activity inorder to get the location of the picture taken
        //Intent intent = getIntent();
        //String filePath = intent.getStringExtra(MainActivity.Picture_Location);

        //Removed photo preview for space
        //ImageView imPreview = findViewById(R.id.preview);
        //imPreview.setImageBitmap(BitmapFactory.decodeFile(filePath));

        // Create various object on the xml file
        final Button btnCancel = findViewById(R.id.btnCancel);
        final Button btnSearch = findViewById(R.id.btnSearch);

        final Button btnLocate = findViewById(R.id.btnLocate);

        final EditText captionSearch = findViewById(R.id.searchCaption);

        //Date search
        final EditText minDate = findViewById(R.id.minDateSearch);
        final EditText maxDate = findViewById(R.id.maxDateSearch);

        final EditText latitudeSearch = findViewById(R.id.latitudeSearch);
        final EditText longitudeSearch = findViewById(R.id.longitudeSearch);
        final EditText radiusSearch = findViewById(R.id.radiusSearch);

        // close activity
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Cancel:", "button has been pressed... Terminating");
                finish();
            }
        });

        //Get location
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Dev::", "Getting location");

                Location location = gpsSearch.getLocation();

                if(location != null) {
                    latitudeSearch.setText(Double.toString(location.getLatitude()));
                    longitudeSearch.setText(Double.toString(location.getLongitude()));
                }
                else
                    Toast.makeText(getApplicationContext(), "No location found", Toast.LENGTH_SHORT).show();
            }
        });

        // On search find the filegit 
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Search Button:", "Button has been pressed");
                // Create intent to send data back to mainactivity
                Intent path = new Intent();

                String captionQuery = captionSearch.getText().toString();
                String dateMinQuery = minDate.getText().toString();
                String dateMaxQuery = maxDate.getText().toString();

                String latitudeQuery = latitudeSearch.getText().toString();
                String longitudeQuery = longitudeSearch.getText().toString();
                String radiusQuery = radiusSearch.getText().toString();

                // Check something is entered in the search boxes
                //Check if no search terms
                if(captionQuery.isEmpty() && dateMinQuery.isEmpty() && dateMaxQuery.isEmpty() &&
                    radiusQuery.isEmpty() && latitudeQuery.isEmpty() && longitudeQuery.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter search queries: CAPTION, DATE, LOCATION", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Check if dates are entered
                    if(!dateMinQuery.isEmpty() || !dateMaxQuery.isEmpty()) {
                        path.putExtra("minDate", dateMinQuery);
                        path.putExtra("maxDate", dateMaxQuery);
                        path.putExtra("Type", "Date");
                        setResult(RESULT_OK, path);
                        finish();
                    }
                    //If dates are empty, check latitude
                    else if(!latitudeQuery.isEmpty() && !longitudeQuery.isEmpty() && !radiusQuery.isEmpty()) {
                        path.putExtra("latitude", latitudeQuery);
                        path.putExtra("longitude", longitudeQuery);
                        path.putExtra("radius", radiusQuery);
                        path.putExtra("Type", "Location");
                        setResult(RESULT_OK, path);
                        finish();
                    }
                }

                }
        });

    }
}
