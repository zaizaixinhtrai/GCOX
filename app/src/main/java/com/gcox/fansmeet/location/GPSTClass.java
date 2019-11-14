package com.gcox.fansmeet.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.tbruyelle.rxpermissions2.RxPermissions;
import timber.log.Timber;

public class GPSTClass implements LocationListener {
    private static GPSTClass mInstance;

    // flag for GPS status
    private boolean isGPSEnabled = false;

    // flag for network status
    private boolean isNetworkEnabled = false;

    // flag for GPS status
    private boolean canGetLocation = false;

    private Location mLocation; // mLocation
    private double latitude; // latitude
    private double longitude; // longitude

//    public void setMyLocationListener(MyLocationListener myLocationListener) {
//        this.myLocationListener = myLocationListener;
//    }
//
//    private MyLocationListener myLocationListener;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 60000; // 1 minute

    // Declaring a Location Manager
    private LocationManager mLocationManager;
    private RxPermissions mRxPermissions;
    public static GPSTClass getInstance() {
        if (mInstance == null) {
            mInstance = new GPSTClass();
        }
        return mInstance;
    }

    public Location getCurrentLocation() {
        return mLocation;
    }

    public Location getLocation(Context context) {
        if(context==null) return null;
        mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mRxPermissions = new RxPermissions((Activity) context);
        if(mLocationManager == null){
            return null;
        }

        // getting GPS status
        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if ((isGPSEnabled || isNetworkEnabled) && mRxPermissions!=null) {
            this.canGetLocation = true;
        }

        if(mRxPermissions!=null && mRxPermissions.isGranted(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mLocation = getLocationViaNetwork();
        }

        if (mLocation == null && mRxPermissions!=null){
            if(mRxPermissions.isGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                mLocation = getLocationViaGps();
            }
        }
        return mLocation;
    }

    private Location getLocationViaNetwork(){
        Location location = null;
        if (isNetworkEnabled) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Timber.d("get mLocation via Network");
            if (mLocationManager != null) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }
            }
        }
        stopUsingGPS();
        return location;
    }

    /**
     * if GPS Enabled get lat/long using GPS Services
     */
    private Location getLocationViaGps(){
        Location location = null;
        if (isGPSEnabled) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Timber.d("get mLocation via GPS");
            if (mLocationManager != null) {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (mLocation != null) {
                    latitude = mLocation.getLatitude();
                    longitude = mLocation.getLongitude();
                }
            }
        }
        stopUsingGPS();
        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    private void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }
        mRxPermissions =null;
    }


//    /**
//     * Function to get country code
//     */
//    public String getCountryCode() {
//        if (mLocation != null) {
//            latitude = mLocation.get();
//        }
//
//        // return country code
//        return latitude;
//    }
    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (mLocation != null) {
            latitude = mLocation.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (mLocation != null) {
            longitude = mLocation.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {

//        if (myLocationListener != null) {
//            myLocationListener.onLocationFound();
//        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
