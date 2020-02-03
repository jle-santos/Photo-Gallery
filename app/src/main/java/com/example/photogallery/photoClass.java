package com.example.photogallery;

import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class photoClass {
    String filePath;
    String dateTime;

    public photoClass(String photoPath) {
        try {
            ExifInterface exif = new ExifInterface(photoPath);
            dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);

            //Check if no caption
            if(exif.getAttribute(ExifInterface.TAG_USER_COMMENT) == null)
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
}
