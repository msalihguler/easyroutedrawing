package com.teamspaghetti.easyroutecalculation;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamspaghetti.easyroutecalculation.listeners.LocationReadyCallback;
import com.teamspaghetti.easyroutecalculation.listeners.RouteCalculationFinishedListener;
import com.teamspaghetti.easyroutecalculation.locationoperations.CurrentLocationProvider;
import com.teamspaghetti.easyroutecalculation.mapoperations.CalculateRouteBetweenPoints;
import com.teamspaghetti.easyroutecalculation.mapoperations.DirectionsJSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Salih on 22.08.2016.
 */

public class EasyRouteCalculation implements LocationReadyCallback,RouteCalculationFinishedListener {

    /*
    *       Easy Route Calculation library is a library that i aimed to ease people's effort on maps
    *       in operations like, calculating the route, getting distance between two points, getting
    *       the duration between two points. It solves this issue in one line of code.
    *
    *       We have two listeners LocationReadyCallback,RouteCalculationFinishedListener which is there for
    *       to notify us on this occations.
    *
    * */

    CurrentLocationProvider provider;
    Boolean isLocationReady = false;
    GoogleMap map;
    Context _context;
    Boolean gotoMyLocationEnabled = false;
    List<LatLng> locations;
    ProgressDialog progressDialog;
    int lineColor,lineWidth;
    String travelMode;


    /*
    *       We do not need map instance for calculating distance and duration.
    *       Therefore only context value will be enough for this calculations.
    * */

    public EasyRouteCalculation(Context context){
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
            locations = new ArrayList<>();
            progressDialog = new ProgressDialog(context);
            setLineColor(Color.RED);
            setLineWidth(5);
            setTravelMode(TravelMode.WALKING);
        }
        else {
            _context = context;
            Utils.createDialogForOpeningGPS(context);
        }
    }

    /*
    *       We need our map instance so we can draw route between points provided.
    * */

    public EasyRouteCalculation(Context context, GoogleMap map){
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
            this.map = map;
            locations = new ArrayList<>();
            progressDialog = new ProgressDialog(context);
            setLineColor(Color.RED);
            setLineWidth(5);
            setTravelMode(TravelMode.WALKING);
        }
        else
            createDialogForOpeningGPS(context);
    }

    /*
    *       'calculateRouteFromMyLocation' method is created to help for calculating the route
    *       from current location to destination
    *       In this method you are able to pick travel mode, line color and width on the map
    * */

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        if(getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,getLineColor(),getLineWidth(),getTravelMode());
        else
            Toast.makeText(_context,_context.getResources().getString(R.string.location_not_ready),Toast.LENGTH_SHORT).show();
    }


    /*
    *      'calculateRouteBetweenTwoPoints' is here for calculating the distance between two points
    *      In this method you are able to pick travel mode, line color and width on the map
    *
    * */

    public void calculateRouteBetweenTwoPoints(LatLng startLocation,LatLng targetLocation){
        calculateRouteBetweenTwoLocations(startLocation,targetLocation,getLineColor(),getLineWidth(),getTravelMode());
    }

    private void calculateRouteBetweenTwoLocations(LatLng startLocation, LatLng targetLocation,int lineColor,int lineWidth,String travelMode){
        try {
            progressDialog.setMessage(_context.getResources().getString(R.string.creating_route_message));
            progressDialog.show();
            new CalculateRouteBetweenPoints(map,startLocation,targetLocation,_context,lineColor,lineWidth,travelMode,true,this).getDirectionsUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    *       Method to check whether gps is enabled or not
    *
    * */

    public boolean isGPSEnabled(Context context){
        return Utils.isGPSEnabled(context);
    }

    /*
    *       This method creates a dialog for opening a map if gps is not enabled or somewhere else.
    *
    * */

    public void createDialogForOpeningGPS(Context context) {
        Utils.createDialogForOpeningGPS(context);
    }

    /*
    *       Get current location if the location is ready.
    *
    * */

    public LatLng getCurrentLocation(){
        if(isLocationReady) {
            return provider.getLocation();
        }else {
            return null;
        }
    }

    /*
    *       This method is called when current location is ready.
    * */

    @Override
    public void locationReadyCallback(Boolean isReady) {
        isLocationReady=isReady;
        if(isReady)
            if(gotoMyLocationEnabled){
                map.addMarker(new MarkerOptions().position(getCurrentLocation()));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(getCurrentLocation(),12.0f));
            }

    }

    /*
    *   This method is centering your current location when needed.
    * */

    public void gotoMyLocation(Boolean isEnabled){
        gotoMyLocationEnabled = isEnabled;
    }

    /*
    *       Getting distance between points in km based.
    *
    * */

    public String getDistanceBetweenPoints(LatLng startLocation,LatLng targetLocation)  {
        final CalculateRouteBetweenPoints crbp = new CalculateRouteBetweenPoints(startLocation,targetLocation,_context,getTravelMode(),true,this);
        final String url = crbp.prepareAddress();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
                @Override
                public String call() throws IOException, JSONException {
                    return new DirectionsJSONParser().getDistance(new JSONObject(crbp.downloadUrl(url)));
                }
            };
            Future<String> future = executor.submit(callable);
            executor.shutdown();

        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "0";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "0";
        }
    }


    /*
    *   This method is called when we want to get duration between points
    *
    * */

    public String getDurationBetweenTwoPoints(LatLng startLocation,LatLng targetLocation) {
        final CalculateRouteBetweenPoints crbp = new CalculateRouteBetweenPoints(startLocation,targetLocation,_context,getTravelMode(),true,this);
        final String url = crbp.prepareAddress();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws IOException, JSONException {
                return new DirectionsJSONParser().getDuration(new JSONObject(crbp.downloadUrl(url)));
            }
        };
        Future<String> future = executor.submit(callable);
        executor.shutdown();

        try {
            return future.get();
        }  catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    *       For multiple locations and creating a route we are using 'addLocation' to add one location
    *       and for deleting location we use 'deleteLocation' and 'calculateRouteForMultiplePositions'
    *       for calculating route between them.
    * */

    public void addLocation(LatLng latLng){
        locations.add(latLng);
    }

    public void deleteLocation(LatLng latLng){
        locations.remove(latLng);
    }

    public void deleteLocation(int position){
        locations.remove(position);
    }

    public void deleteAllLocations(){
        locations.clear();
    }

    public void calculateRouteForMultiplePositions() {
        calculateRouteForMultiplePositions(getTravelMode(),getLineColor(),getLineWidth());
    }

    private void calculateRouteForMultiplePositions(String travelMode,int lineColor,int lineWidth){
        if (locations.size() > 0) {
            if (locations.size() == 1) {
                Toast.makeText(_context, _context.getResources().getString(R.string.single_location_error), Toast.LENGTH_SHORT).show();
            } else if (locations.size() == 2) {
                calculateRouteBetweenTwoLocations(locations.get(0), locations.get(1),lineColor,lineWidth,travelMode);
            } else {
                progressDialog.setMessage(_context.getResources().getString(R.string.creating_route_message));
                progressDialog.show();
                for(int i = 0;i<locations.size()-1;i++){
                    if(i==locations.size()-2)
                        calculateRouteBetweenMultiplePoints(locations.get(i),locations.get(i+1),lineColor,lineWidth,travelMode,true);
                    else
                        calculateRouteBetweenMultiplePoints(locations.get(i),locations.get(i+1),lineColor,lineWidth,travelMode,false);
                }
            }
        }
    }
    public void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,boolean isLast){
        calculateRouteBetweenMultiplePoints(startLocation,targetLocation,getLineColor(),getLineWidth(),getTravelMode(),isLast);
    }
    private void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,int lineColor,int lineWidth,String travelMode,boolean isLast){
        try {
            progressDialog.setMessage(_context.getResources().getString(R.string.creating_route_message));
            progressDialog.show();
            new CalculateRouteBetweenPoints(map,startLocation,targetLocation,_context,lineColor,lineWidth,travelMode,isLast,this).getDirectionsUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void calculationFinished() {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }
    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
