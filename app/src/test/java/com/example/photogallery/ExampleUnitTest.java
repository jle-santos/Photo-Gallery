package com.example.photogallery;

import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.example.photogallery.photoPackage.gallerySupport;
import com.example.photogallery.photoPackage.photoClass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testNoPhotos_Date() {
        String testPath = null;
        ArrayList<photoClass> tempGallery = new ArrayList();

        gallerySupport gallery = new gallerySupport();

        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        tempGallery = gallery.filterPhotoDates(tempGallery, minDate, maxDate);

        assertEquals(0 ,tempGallery.size());
    }

    @Test
    public void testPhotos_Date() {
        ArrayList<photoClass> tempGallery = new ArrayList();
        //Add photo
        tempGallery.add(new photoClass("FILEPATH"));

        Date tempDate = null;
        try {
            tempDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse("2018:02:03 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tempGallery.get(tempGallery.size()-1).dateTime = tempDate;

        gallerySupport gallery = new gallerySupport();

        Date minDate = null;
        Date maxDate = null;
        try {
            minDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse("2017:02:03 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            maxDate = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse("2020:02:03 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tempGallery = gallery.filterPhotoDates(tempGallery, minDate, maxDate);

        assertEquals(tempDate,tempGallery.get(tempGallery.size()-1).dateTime);
    }

    @Test
    public void testNoPhotos_Location() {
        ArrayList<photoClass> tempGallery = new ArrayList();

        gallerySupport gallery = new gallerySupport();

        String testLat = "49";
        String testLon = "-122";
        String testRad = "5";

        tempGallery = gallery.filterPhotoLocations(tempGallery, testLat, testLat, testRad);

        assertEquals(0 ,tempGallery.size());
    }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testPhoto_Location() {
        ArrayList<photoClass> tempGallery = new ArrayList();

        File tempFile = null;
        try {
            tempFile = folder.newFile("testPhoto.jpg");
            //Uri photoURI = FileProvider.getUriForFile(this, "com.example.photogallery.fileprovider", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Add photo
        tempGallery.add(new photoClass(tempFile.getPath()));

        //tempGallery.get(tempGallery.size()-1).setCoordinates("49", "-122");

        String test = tempGallery.get(tempGallery.size()-1).getLatitude();
        //Set coordinates
        gallerySupport gallery = new gallerySupport();

        String testLat = "49";
        String testLon = "-122";
        String testRad = "5";

        tempGallery = gallery.filterPhotoLocations(tempGallery, testLat, testLon, testRad);

        assertEquals(1 ,tempGallery.size());
    }
}