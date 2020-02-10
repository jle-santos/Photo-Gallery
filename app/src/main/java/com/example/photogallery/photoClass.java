package com.example.photogallery;

import android.location.Location;
import android.support.media.ExifInterface;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class photoClass {
    String filePath;
    Date dateTime;

    String latitude;
    String longitude;

    public photoClass(String photoPath) {
        try {
            ExifInterface exif = new ExifInterface(photoPath);

            //Convert date string to date object
            String tempDate = exif.getAttribute(ExifInterface.TAG_DATETIME);

            try {
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

    public void setCoordinates(Location location) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.
            //exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLatitude() {
        String lat = "";

        try {
            ExifInterface exif = new ExifInterface(filePath);
            lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lat;
    }

    public String getLongitude() {
        String lat = "";

        try {
            ExifInterface exif = new ExifInterface(filePath);
            lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lat;
    }
}