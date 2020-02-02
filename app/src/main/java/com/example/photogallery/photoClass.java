package com.example.photogallery;

import android.util.Log;

public class photoClass {

    String fileName;

    public photoClass(String name) {
        fileName = name; //Set filename to name
        Log.i("File name set", name);
    }

}
