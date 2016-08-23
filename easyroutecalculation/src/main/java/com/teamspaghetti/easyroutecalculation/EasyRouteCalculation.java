package com.teamspaghetti.easyroutecalculation;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamspaghetti.easyroutecalculation.listeners.LocationReadyCallback;
import com.teamspaghetti.easyroutecalculation.locationoperations.CurrentLocationProvider;
import com.teamspaghetti.easyroutecalculation.mapoperations.CalculateRouteBetweenPoints;
import com.teamspaghetti.easyroutecalculation.mapoperations.DirectionsJSONParser;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Salih on 22.08.2016.
 */
public class EasyRouteCalculation implements LocationReadyCallback {

    CurrentLocationProvider provider;
    Boolean isLocationReady = false;
    GoogleMap map;
    Context _context;
    Boolean gotoMyLocationEnabled = false;

    public EasyRouteCalculation(Context context){
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
        }
        else {
            _context = context;
            Utils.createDialogForOpeningGPS(context);
        }
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
        if(isReady)
            if(gotoMyLocationEnabled){
                map.addMarker(new MarkerOptions().position(getCurrentLocation()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLocation(),12.0f));
            }

    }

    public void gotoMyLocation(Boolean isEnabled){
        gotoMyLocationEnabled = isEnabled;
    }

    public void getDistanceBetweenPoints(LatLng startLocation,LatLng targetLocation,String mode){
        final CalculateRouteBetweenPoints crbp = new CalculateRouteBetweenPoints(startLocation,targetLocation,_context,mode);
        final String url = crbp.prepareAddress();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    new DirectionsJSONParser().getDistance(new JSONObject(crbp.downloadUrl(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void getDistanceBetweenPoints(LatLng startLocation,LatLng targetLocation){
        getDistanceBetweenPoints(startLocation,targetLocation,TravelMode.WALKING);
    }
}
