// File:         MainActivity.java
// Created:      [2020/01/14 creation date]
// Author:       Lemuel, Karen, Ryan
//
// Desc:
//  1. Allows user to take photos and stores these on a file system folder
//  2. Automatically tags each captured photo with the current timestamp
//  3. Allows user to add and/or edit a caption to the photo, and supports time as well as
//     caption based search of the photos and ability to view these photos.
//  4. Photo location tagging
//  5. Location based search
//  6. Uploading of photos to social media
//  7. Automation of location based search

package com.example.photogallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.photogallery.photoPackage.gpsClass;
import com.example.photogallery.photoPackage.photoClass;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// import com.example.photogallery.R;

//*******************************************************************
//  MainActivity
//
// Main activity that connects to the search and share activity
//*******************************************************************
public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String Picture_Location = "com.example.photogallery.Picture_Location"; // Send picture location to search activity
    String mCurrentPhotoPath = "";
    private String captionText = "";
    private ArrayList<photoClass> photoGallery;
    private int currentPhotoIndex = 0;

    // Create GPS class that will return location
    private gpsClass gps = new gpsClass(this);

    // Initialize activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Object Creation
        final Button btnCaption = findViewById(R.id.btnCaption);
        final Button btnSearch = findViewById(R.id.btnSearch);
        final Button btnShare = findViewById(R.id.btnShare);
        final TextView caption = findViewById(R.id.captionText);
        final Button btnNext = findViewById(R.id.btnNext);
        final Button btnPrev = findViewById(R.id.btnPrev);

        //Generate gallery
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        //Location location = gps.getLocation(this);
        photoGallery = generatePhotos(minDate, maxDate);
        Log.i("Dev::", "generating gallery");

        if (photoGallery.isEmpty()) {
            Log.i("Dev::", "No photos found");
            caption.setText("Empty Gallery");
        }
        else
        {
            Log.i("Dev::", "photos found");
            //Print to screen how many photos found

            Log.d("Dev::/onCreate, number of photos:", Integer.toString(photoGallery.size()));
            //caption.setText("Photos in Gallery:" + Integer.toString(photoGallery.size()));
            mCurrentPhotoPath = photoGallery.get(currentPhotoIndex).filePath;
            displayPhoto(mCurrentPhotoPath);
        }

        // if caption button is clicked add caption text and save it
        btnCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // print to terminal when button is pressed
                Log.i("btnCaption", "has been pressed");

                if (mCurrentPhotoPath.isEmpty() || mCurrentPhotoPath == null) {
                    Toast.makeText(getApplicationContext(), "Take a photo!", Toast.LENGTH_SHORT).show();
                } else {
                    // Create pop dialogue box
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Please Enter Caption");

                    // Set up the input
                    final EditText input = new EditText(MainActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            captionText = input.getText().toString(); //get value from user
                            caption.setText(captionText); // write to textview
                            //Set caption
                            photoGallery.get(currentPhotoIndex).setCaption(captionText);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });

        // if search button is clicked start new activity where search will be done
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // print to terminal when button is pressed
                Log.i("btnSearch", "has been pressed");
                openSearchActivity();
            }
        });

        // Create new share activity when button is pressed
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // print to terminal when button is pressed
                Log.i("btnShare", "has been pressed");
                openShareActivity();
            }
        });

        // Cycles which photo is selected and displayed on screen
        // chooses next photo
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++currentPhotoIndex;

                if (currentPhotoIndex < 0)
                    currentPhotoIndex = 0;
                if (currentPhotoIndex >= photoGallery.size())
                    currentPhotoIndex = photoGallery.size() - 1;

                if(!photoGallery.isEmpty()) {
                    Log.d("Dev:: btnNext,", Integer.toString(currentPhotoIndex));
                    displayPhoto(photoGallery.get(currentPhotoIndex).filePath);
                    mCurrentPhotoPath = photoGallery.get(currentPhotoIndex).filePath;
                }
            }
        });
        // Cycles which photo is selected and displayed on screen
        // chooses previous photo
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --currentPhotoIndex;

                if (currentPhotoIndex < 0)
                    currentPhotoIndex = 0;
                if (currentPhotoIndex >= photoGallery.size())
                    currentPhotoIndex = photoGallery.size() - 1;

                if(!photoGallery.isEmpty()) {
                    Log.d("Dev:: btnPrev,", Integer.toString(currentPhotoIndex));
                    displayPhoto(photoGallery.get(currentPhotoIndex).filePath);
                    mCurrentPhotoPath = photoGallery.get(currentPhotoIndex).filePath;
                }
            }
        });
    }


    /**
     * Desc:
     *  Corrects the list of photos in the array to only contain those
     *  specified by the user
     *
     * Bugs:
     *  None atm
     */
    private ArrayList<photoClass> filterPhotoLocations(ArrayList<photoClass> photoGallery, String lat, String lon, String rad) {
        Double latitude = Double.parseDouble(lat);
        Double longitude = Double.parseDouble(lon);
        Double radius = Double.parseDouble(rad);

        Double latMin = latitude - radius;
        Double latMax = latitude + radius;

        Double lonMin = longitude - radius;
        Double lonMax = longitude + radius;

        for (int index = photoGallery.size() - 1; index >= 0; index--) {

            photoClass photo = photoGallery.get(index);

            if(photo.getLatitude().equals("N/A") && photo.getLongitude().equals("N/A")) {
                //Invalid coordinates
                //Remove photo
                photoGallery.remove(index);
            }
            else {
                Double latPhoto = Double.parseDouble(photo.getLatitude());
                Double lonPhoto = Double.parseDouble(photo.getLongitude());

                if(latPhoto > latMin && latPhoto < latMax &&
                        lonPhoto > lonMin && lonPhoto < lonMax) {
                    //Photo within filter
                }
                else {
                    //Photo not within filter
                    photoGallery.remove(index);
                }
            }
        }

        return photoGallery;
    }

    /**
     * Desc:
     *  Adds photos from search query to the current photo gallery
     *
     * Bugs:
     *  None atm
     */
    private ArrayList<photoClass> generatePhotos(Date minDateQuery, Date maxDateQuery) {
        File folder = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photogallery/files/Pictures");

        ArrayList<photoClass> generatePhotos = new ArrayList<photoClass>();
        File[] currentFiles = folder.listFiles();

        if(minDateQuery == null)
            minDateQuery = new Date(Long.MIN_VALUE);

        if(maxDateQuery == null)
            maxDateQuery = new Date(Long.MAX_VALUE);

        if(currentFiles != null) {
            for (File file : currentFiles) {
                if (!file.isDirectory()) {
                    //If found photos, add to gallery
                    photoClass tempPhoto = new photoClass(file.getPath());

                    if(tempPhoto.dateTime.after(minDateQuery) && tempPhoto.dateTime.before(maxDateQuery))
                        generatePhotos.add(tempPhoto);
                }
            }
        }
        return generatePhotos;
    }


    /**
     * Desc:
     *  Sets the main activity imageview to display the photo passed to this function
     *
     * Bugs:
     *  None atm
     */
    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.ivGallery);
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        writeCaption(path);
    }

    /**
     * Desc:
     *  Extracts the metadata from the image and updates all textview
     *  related fields on display
     *
     * Bugs:
     *  None atm
     */
    private void writeCaption(String path) {
        TextView captiontextrefresh = findViewById(R.id.captionText);
        TextView dateTextRefresh = findViewById(R.id.dateView);
        TextView latRefresh = findViewById(R.id.latView);
        TextView lonRefresh = findViewById(R.id.longView);

        latRefresh.setText(photoGallery.get(currentPhotoIndex).getLatitude());
        lonRefresh.setText(photoGallery.get(currentPhotoIndex).getLongitude());
        //Get image metadata
        captiontextrefresh.setText(photoGallery.get(currentPhotoIndex).getCaption());
        dateTextRefresh.setText(photoGallery.get(currentPhotoIndex).dateTime.toString());
    }



    /**
     * Desc:
     *  creates a new instance of the share activity with a request code of 404
     *
     * Bugs:
     *  None atm
     */
    public void openShareActivity() {
        Intent intent = new Intent(this, shareActivity.class);
        intent.putExtra(Picture_Location, mCurrentPhotoPath); //pass file location to new activity
        startActivityForResult(intent, 404);
    }

    /**
     * Desc:
     *  creates a new instance of the search activity with a request code of 999
     *
     * Bugs:
     *  None atm
     */
    public void openSearchActivity() {
        Intent intent = new Intent(this, searchActivity.class);
        //intent.putExtra(Picture_Location, mCurrentPhotoPath); //pass file location to new activity
        startActivityForResult(intent, 999);
    }

    /**
     * Desc:
     *  Opens the camera and lets the user take a picture
     *
     * Bugs:
     *  None atm
     */
    public void takePicture(View v) {
        //Request location to tag photo with
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogallery.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    /**
     * Desc:
     *  Takes the photo taken by the user and generates a file name
     *  at the location: android/data/com.example.photogallery/files/pictures
     *
     * Bugs:
     *  None atm
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, "_.jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    /**
     * Desc:
     *  Receives results from the other activities and processes the data
     *  code: 404 - share activity [NOT IN USE]
     *  code: 999 - search activity
     *  code: REQUEST_IMAGE_CAPTURE - camera activity
     *
     * Bugs:
     *  None atm
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // takes photo from camera activity
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Append to photo gallery
            photoGallery.add(new photoClass(mCurrentPhotoPath));
            //Set index to the most recent photo
            currentPhotoIndex = photoGallery.size() - 1;
            Location location = gps.getLocation();
            if(location != null) {
                String longitude = (Location.convert(location.getLongitude(), Location.FORMAT_DEGREES));
                String latitude = (Location.convert(location.getLatitude(), Location.FORMAT_DEGREES));

                photoGallery.get(currentPhotoIndex).setCoordinates(latitude, longitude);

                Log.i("Dev:: ", "Lat: " + photoGallery.get(currentPhotoIndex).getLatitude() +
                        " Lon: " + photoGallery.get(currentPhotoIndex).getLongitude());
            }
            else
                Log.i("Dev::", "No GPS signal");
          
            displayPhoto(mCurrentPhotoPath);
        // takes search query data from search activity
        } else if (requestCode == 999 && resultCode == RESULT_OK) {

            String searchType = data.getStringExtra("Type");

            if(searchType.equals("Date")) {
                String minDate = data.getStringExtra("minDate") + " 00:00:00";
                String maxDate = data.getStringExtra("maxDate") + " 24:59:59";

                Date startDate = null;
                try {
                    startDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(minDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date endDate = null;
                try {
                    endDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(maxDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                photoGallery = generatePhotos(startDate, endDate);
            }
            else if(searchType.equals("Location")) {
                String latitude = data.getStringExtra("latitude");
                String longitude = data.getStringExtra("longitude");
                String radius = data.getStringExtra("radius");

                photoGallery = filterPhotoLocations(photoGallery, latitude, longitude, radius);

            }

            if (photoGallery.isEmpty()) {
                Log.i("Dev:: Search yielded", "No photos found");
                Toast.makeText(getApplicationContext(), "No Photos Found", Toast.LENGTH_SHORT).show();
                photoGallery = generatePhotos(null, null);
            }
            else
            {
                Log.i("Dev::", "photos found");
                //Print to screen how many photos found
                Toast.makeText(getApplicationContext(), "Found " + photoGallery.size() + " photos!", Toast.LENGTH_SHORT).show();
                Log.d("Dev::: Search yielded:", Integer.toString(photoGallery.size()));
                currentPhotoIndex = photoGallery.size() - 1 ;
                mCurrentPhotoPath = photoGallery.get(currentPhotoIndex).filePath;
                displayPhoto(mCurrentPhotoPath);
            }
        }
    }
}
