package com.teamspaghetti.easyroutecalculation;

import android.content.Context;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Salih on 22.08.2016.
 */
public class EasyRouteCalculation implements LocationReadyCallback {

    LatLng myLocation;
    CurrentLocationProvider provider;
    Boolean isLocationReady = false;
    GoogleMap map;

    public EasyRouteCalculation(Context context){
        if(isGPSEnabled(context)) {
            provider = new CurrentLocationProvider(context,this);
        }
        else
            Utils.createDialogForOpeningGPS(context);
    }
    public EasyRouteCalculation(Context context, GoogleMap map){
        if(Utils.isGPSEnabled(context)) {
            provider = new CurrentLocationProvider(context,this);
            this.map = map;
        }
        else
            createDialogForOpeningGPS(context);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        calculateRouteFromMyLocation(myLocation,targetLocation);
    }

    private void calculateRouteFromMyLocation(LatLng myLocation, LatLng targetLocation){

    }

    public LatLng getCurrentLocation(){
        if(isLocationReady)
            return provider.getLocation();
        else
            return null;
    }

    public boolean isGPSEnabled(Context context){
        return Utils.isGPSEnabled(context);
    }

    public void createDialogForOpeningGPS(Context context) {
        Utils.createDialogForOpeningGPS(context);
    }

    @Override
    public void locationReadyCallback(Boolean isReady) {
        isLocationReady=isReady;
        if(isReady){
            map.addMarker(new MarkerOptions().position(getCurrentLocation()));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLocation(),12.0f));
        }
    }

}
