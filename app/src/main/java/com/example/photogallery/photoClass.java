package com.example.photogallery;

import android.media.ExifInterface;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class photoClass {
    String filePath;
    Date dateTime;

    public photoClass(String photoPath) {
        try {
            ExifInterface exif = new ExifInterface(photoPath);

            //Convert date string to date object
            try {
                String tempDate = exif.getAttribute(ExifInterface.TAG_DATETIME);
                dateTime = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //Check if no caption
            if (exif.getAttribute(ExifInterface.TAG_USER_COMMENT) == null)
                exif.setAttribute(ExifInterface.TAG_USER_COMMENT, "No Caption");

            exif.saveAttributes();
            filePath = photoPath;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCaption(String caption) {

        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, caption);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCaption() {
        String caption = "";

        try {
            ExifInterface exif = new ExifInterface(filePath);
            caption = exif.getAttribute(ExifInterface.TAG_USER_COMMENT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return caption;
    }

    public String getLatitude() {
        String coord = "N/A";

        try {
            ExifInterface exif = new ExifInterface(filePath);
            String coordLine = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
            if(coordLine != null)
                coord = coordLine.substring(0, coordLine.indexOf("_"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return coord;
    }

    public String getLongitude() {
        String coord = "N/A";

        try {
            ExifInterface exif = new ExifInterface(filePath);
            String coordLine = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

            if(coordLine != null)
                coord = coordLine.substring(coordLine.indexOf("_")+1, coordLine.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return coord;
    }

    public void setCoordinates(String latitude, String longitude) {
        String coordinates = latitude + "_" + longitude;

        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, coordinates);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}