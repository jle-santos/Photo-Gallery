package com.example.photogallery;

import android.media.ExifInterface;

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

    /*
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
        /*
        double lat = location.getLatitude();
        double alat = Math.abs(lat);
        String dms = Location.convert(alat, Location.FORMAT_SECONDS);
        String[] splits = dms.split(":");
        String[] secnds = (splits[2]).split("\\.");
        String seconds;
        if(secnds.length==0)
        {
            seconds = splits[2];
        }
        else
        {
            seconds = secnds[0];
        }

        String latitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";

        double lon = location.getLongitude();
        double alon = Math.abs(lon);

        dms = Location.convert(alon, Location.FORMAT_SECONDS);
        splits = dms.split(":");
        secnds = (splits[2]).split("\\.");

        if(secnds.length==0)
        {
            seconds = splits[2];
        }
        else
        {
            seconds = secnds[0];
        }
        String longitudeStr = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
        */


        String coordinates = latitude + "_" + longitude;

        try {
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, coordinates);
            //exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
            //exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, lat>0?"N":"S");

            //exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            //exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, lon>0?"E":"W");

            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}