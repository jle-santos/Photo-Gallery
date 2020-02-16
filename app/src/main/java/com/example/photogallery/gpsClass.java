package com.example.photogallery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;
//*******************************************************************
//  gpsClass
//
//  Object meant for managing all location based services
//*******************************************************************
public class gpsClass extends Service {
    // Acquire a reference to the system Location Manager

    //Location objects
    protected LocationManager locationManager;
    private boolean gpsStatus = false;
    private boolean networkStatus = false;

    Context mContext;
    /**
     * Desc:
     *  Constructor for the object creating the context
     *
     * Bugs:
     *  None atm
     */
    public gpsClass(Context mContext) {
        this.mContext = mContext;
        //getLocation();
    }

    /**
     * Desc:
     *  returns the GPS coordinates
     *
     * Bugs:
     *  None atm
     */
    public Location getLocation() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        //Get status of location providers
        // Getting GPS status
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Getting network status
        networkStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.i("Dev::", "GPS: " + gpsStatus + "NET: " + networkStatus);


        Log.i("Dev::", "Defining listener");
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates

        int finePerm = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePerm = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION);


       if (finePerm == PackageManager.PERMISSION_GRANTED
               || coarsePerm == PackageManager.PERMISSION_GRANTED) {

            if(gpsStatus) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.i("Dev::", "GPS PRovider");
                return location;
            }
            else if(networkStatus) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("Dev::", "NETWORK Provider");
                return location;
            }
            else {
                //No location provider enabled
                Log.i("Dev::", "No Location provider enabled");
                return null;
            }
        }
        else
            return null;

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
