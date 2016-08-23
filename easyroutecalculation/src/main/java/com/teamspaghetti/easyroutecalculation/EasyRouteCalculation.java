package com.teamspaghetti.easyroutecalculation;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamspaghetti.easyroutecalculation.listeners.LocationReadyCallback;
import com.teamspaghetti.easyroutecalculation.locationoperations.CurrentLocationProvider;
import com.teamspaghetti.easyroutecalculation.mapoperations.CalculateRouteBetweenPoints;

import java.io.IOException;

/**
 * Created by Salih on 22.08.2016.
 */
public class EasyRouteCalculation implements LocationReadyCallback {

    LatLng myLocation;
    CurrentLocationProvider provider;
    Boolean isLocationReady = false;
    GoogleMap map;
    Context _context;
    public EasyRouteCalculation(Context context){
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
        }
        else
            Utils.createDialogForOpeningGPS(context);
    }
    public EasyRouteCalculation(Context context, GoogleMap map){
        if(Utils.isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
            this.map = map;
        }
        else
            createDialogForOpeningGPS(context);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        myLocation = getCurrentLocation();
        calculateRouteFromMyLocation(myLocation,targetLocation);
    }

    private void calculateRouteFromMyLocation(LatLng myLocation, LatLng targetLocation){
        try {
            new CalculateRouteBetweenPoints(map,myLocation,targetLocation,_context).getDirectionsUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
