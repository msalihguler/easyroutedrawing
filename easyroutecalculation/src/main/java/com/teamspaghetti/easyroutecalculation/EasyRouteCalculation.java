package com.teamspaghetti.easyroutecalculation;

import android.content.Context;
import android.graphics.Color;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.teamspaghetti.easyroutecalculation.listeners.LocationReadyCallback;
import com.teamspaghetti.easyroutecalculation.locationoperations.CurrentLocationProvider;
import com.teamspaghetti.easyroutecalculation.mapoperations.CalculateRouteBetweenPoints;
import java.io.IOException;

/**
 * Created by Salih on 22.08.2016.
 */
public class EasyRouteCalculation implements LocationReadyCallback {

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
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
            this.map = map;
        }
        else
            createDialogForOpeningGPS(context);

    }

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,Color.RED,5,TravelMode.WALKING);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,String travelMode){
        calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,Color.RED,5,travelMode);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor,String travelMode){
        calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,5,travelMode);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor){
        calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,5,TravelMode.WALKING);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor,int lineWidth){
        calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,lineWidth,TravelMode.WALKING);
    }

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,Color.RED,5,TravelMode.WALKING);
    }

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation,String travelMode){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,Color.RED,5,travelMode);
    }

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation,int lineColor,String travelMode){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,lineColor,5,travelMode);
    }

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation,int lineColor){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,lineColor,5,TravelMode.WALKING);
    }

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation,int lineColor,int lineWidth){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,lineColor,lineWidth,TravelMode.WALKING);
    }

    private void calculateRouteBetweenTwoLocations(LatLng startLocation, LatLng targetLocation,int lineColor,int lineWidth,String travelMode){
        try {
            new CalculateRouteBetweenPoints(map,startLocation,targetLocation,_context,lineColor,lineWidth,travelMode).getDirectionsUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean isGPSEnabled(Context context){
        return Utils.isGPSEnabled(context);
    }

    public void createDialogForOpeningGPS(Context context) {
        Utils.createDialogForOpeningGPS(context);
    }

    public LatLng getCurrentLocation(){
        if(isLocationReady)
            return provider.getLocation();
        else
            return null;
    }

    @Override
    public void locationReadyCallback(Boolean isReady) {
        isLocationReady=isReady;
    }

}
