// Authors: Lemuel, Karen, Ryan
// Desc: allows user to take photos and stores these on a file system folder, automatically tags each captured photo with the current timestamp,
//       allows user to add and/or edit a caption to the photo, and supports time as well as caption based search of the photos and ability to view these photos.

package com.example.photogallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
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

// import com.example.photogallery.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Send picture location to search activity
    public static final String Picture_Location = "com.example.photogallery.Picture_Location";
    String mCurrentPhotoPath = "";

    // Hold user entered caption
    private String captionText = "";

    //Photo List
    private ArrayList<String> photoGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Object Creation
        final Button btnCaption = findViewById(R.id.btnCaption);
        final Button btnSearch = findViewById(R.id.btnSearch);
        final  TextView caption = findViewById(R.id.captionText);

        // Set default text for caption text view
        caption.setText("Take a photo!");

        //Generate gallery
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);
        photoGallery = populateGallery(minDate, maxDate);

        //Print to screen how many photos found
        Log.d("onCreate, number of photos:", Integer.toString(photoGallery.size()));

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

                            String OriginalPhotoPath = mCurrentPhotoPath;

                            // Remove Previous caption if one is found
                            if (mCurrentPhotoPath.contains("_*")){
                                mCurrentPhotoPath = mCurrentPhotoPath.substring(0, mCurrentPhotoPath.indexOf("_*"));
                                mCurrentPhotoPath = mCurrentPhotoPath + ".jpg";
                            }
                            // Overwrite file name with caption
                            //String filelocation = "/storage/emulated/0/Android/data/com.example.photogallery/files/Pictures/";
                            String newName = mCurrentPhotoPath.substring(0, mCurrentPhotoPath.length() - 4) + "_*" + captionText + ".jpg";
                            File currentPicutrename = new File(OriginalPhotoPath);
                            File newPicturename = new File(newName);
                            currentPicutrename.renameTo(newPicturename);
                            mCurrentPhotoPath = newName;

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

        // if search button is clickeed start new activity where search will be done
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // print to terminal when button is pressed
                Log.i("btnSearch", "has been pressed");
                openSearchActivity();
            }
        });
    }

    //Function to generate list of files in folder
    private ArrayList<String> populateGallery(Date minDate, Date maxDate) {
        File folder = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photogallery/files/Pictures");

        ArrayList<String> populateGallery = new ArrayList<String>();

       File[] currentFiles = folder.listFiles();
       for(File file : currentFiles) {
           if (!file.isDirectory()) {
               populateGallery.add(new String(file.getName()));
           }
       }
        return populateGallery;
    }

    // function to open new search activity
    public void openSearchActivity() {
        Intent intent = new Intent(this, searchActivity.class);
        intent.putExtra(Picture_Location, mCurrentPhotoPath); //pass file location to new activity
        startActivityForResult(intent, 999);
    }



    // Activate camera and take the picture
    public void takePicture(View v) {
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

    // Time stamp picture from camera and put in picture directory
    // located in android/data/com.example.photogallery/files/pictures
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // Puts image from camera onto the imageview object
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // exectue this if from camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            TextView captiontextrefresh = findViewById(R.id.captionText);
            captiontextrefresh.setText("Please Enter a Caption");
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
        // execute this if from search activity
        } else if (requestCode == 999 && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
            TextView captiontextrefresh = findViewById(R.id.captionText);

            String filePath = data.getStringExtra("Path");
            String fileName = data.getStringExtra("Filename");
            // Print image to imageview
            mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
            // print to file name to text view
            String fileName_2 = fileName.substring(fileName.indexOf("_*")+2);
            fileName_2 = fileName_2.substring(0, fileName_2.length() - 4);
            captiontextrefresh.setText(fileName_2.trim());

            mCurrentPhotoPath = filePath;



        }
    }



}
