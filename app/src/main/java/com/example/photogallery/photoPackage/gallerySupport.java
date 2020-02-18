package com.example.photogallery.photoPackage;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class gallerySupport {
    /**
     * Desc:
     *  Generates array of photos in folder
     *
     * Bugs:
     *  None atm
     */
    public ArrayList<photoClass> generatePhotos() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        ArrayList<photoClass> generatePhotos = new ArrayList<photoClass>();

        if(path != null) {
            File folder = new File(path, "/Android/data/com.example.photogallery/files/Pictures");
            File[] currentFiles = folder.listFiles();

            Date minDateQuery = new Date(Long.MIN_VALUE);
            Date maxDateQuery = new Date(Long.MAX_VALUE);

            if (currentFiles != null) {
                for (File file : currentFiles) {
                    if (!file.isDirectory()) {
                        //If found photos, add to gallery
                        photoClass tempPhoto = new photoClass(file.getPath());

                        if (tempPhoto.dateTime.after(minDateQuery) && tempPhoto.dateTime.before(maxDateQuery))
                            generatePhotos.add(tempPhoto);
                    }
                }
            }
        }
        return generatePhotos;
    }

    public ArrayList<photoClass> filterPhotoDates(ArrayList<photoClass> photoGallery, Date minDateQuery, Date maxDateQuery) {

        for (int index = photoGallery.size() - 1; index >= 0; index--) {

            photoClass tempPhoto = photoGallery.get(index);

            if(minDateQuery == null)
                minDateQuery = new Date(Long.MAX_VALUE);

            if(maxDateQuery == null)
                maxDateQuery = new Date(Long.MAX_VALUE);

                if (tempPhoto.dateTime.after(minDateQuery) && tempPhoto.dateTime.before(maxDateQuery)) {
                    //Do not remove photo
                }
                else {
                    //Photo not within filter
                    photoGallery.remove(index);
                }
            }

        return photoGallery;
    }

    /**
     * Desc:
     *  Corrects the list of photos in the array to only contain those
     *  specified by the user
     *
     * Bugs:
     *  None atm
     */
    public ArrayList<photoClass> filterPhotoLocations(ArrayList<photoClass> photoGallery, String lat, String lon, String rad) {
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


}
