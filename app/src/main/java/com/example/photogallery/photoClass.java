package com.example.photogallery;

import android.media.ExifInterface;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//*******************************************************************
//  photoClass
//
//  Object meant for managing all data related to each photo
//*******************************************************************
public class photoClass {
    String filePath;
    Date dateTime;
  
    String latitude;
    String longitude;

    /**
     * Desc:
     *  find all the data you can about the photo selected by the user
     *
     * Bugs:
     *  None atm
     */
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


    /**
     * Desc:
     *  Adds the caption to the metadata of the photo under "comment"
     *
     * Bugs:
     *  None atm
     */
    public void setCaption(String caption) {

        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, caption);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Desc:
     *  retun the caption under the "comment" section in the metadata
     *
     * Bugs:
     *  None atm
     */
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

    /*

    /**
     * Desc:
     *  set the coordinates of where the photo was taken
     *
     * Bugs:
     *  None atm
    public void setCoordinates(String latitude, String longitude) {
        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /**
     * Desc:
     *  Returns the latitude of where the photo was taken
     *
     * Bugs:
     *  None atm
     */
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


    /**
     * Desc:
     *  Returns the longitude of where the photo was taken
     *
     * Bugs:
     *  None atm
     */
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



    /**
     * Desc:
     *  Sets the coordinate values in the metadata
     *
     * Bugs:
     *  None atm
     */
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