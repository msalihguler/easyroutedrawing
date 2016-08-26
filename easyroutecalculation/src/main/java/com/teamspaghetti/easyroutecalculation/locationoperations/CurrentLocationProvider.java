package com.teamspaghetti.easyroutecalculation.locationoperations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.teamspaghetti.easyroutecalculation.R;
import com.teamspaghetti.easyroutecalculation.listeners.LocationReadyCallback;

/**
 * Created by Salih on 22.08.2016.
 */
public class CurrentLocationProvider  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    /*
    *       With we try to retrieve current location we are using GoogleApiClient to start a location
    *       service to retrieve current location and change it when it changes. This method triggers
    *       the interface and send data.
    *
    * */

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
    private Context _context;
    private LatLng location;
    private LocationReadyCallback locationReadyCallback;
    private int interval, fastestInterval;

    public CurrentLocationProvider(Context context,LocationReadyCallback locationReadyCallback){
        _context = context;
        buildApiClient();
        interval = 10000;
        fastestInterval = 5000;
        this.locationReadyCallback = locationReadyCallback;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mGoogleApiClient.connect();
    }

    public CurrentLocationProvider(Context context,LocationReadyCallback locationReadyCallback,int interval){
        _context = context;
        buildApiClient();
        this.interval=interval;
        this.fastestInterval = 5000;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationReadyCallback = locationReadyCallback;
        mGoogleApiClient.connect();
    }

    public CurrentLocationProvider(Context context,LocationReadyCallback locationReadyCallback,int interval,int fastestInterval){
        _context = context;
        buildApiClient();
        this.interval=interval;
        this.fastestInterval = fastestInterval;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationReadyCallback = locationReadyCallback;
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }

        if (mLocation != null) {

            setLocation(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
            locationReadyCallback.locationReadyCallback(true);

        } else {

            Toast.makeText(_context, _context.getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        setLocation(new LatLng(location.getLatitude(),location.getLongitude()));
    }
    protected void startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(interval)
                .setFastestInterval(fastestInterval);

        if (ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    private void buildApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(_context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
