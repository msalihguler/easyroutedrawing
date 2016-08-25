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

    CurrentLocationProvider provider;
    Boolean isLocationReady = false;
    GoogleMap map;
    Context _context;
    Boolean gotoMyLocationEnabled = false;
    List<LatLng> locations;
    ProgressDialog progressDialog;

    public EasyRouteCalculation(Context context){
        if(isGPSEnabled(context)) {
            _context = context;
            provider = new CurrentLocationProvider(context,this);
            locations = new ArrayList<>();
            progressDialog = new ProgressDialog(context);
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
            locations = new ArrayList<>();
            progressDialog = new ProgressDialog(context);
        }
        else
            createDialogForOpeningGPS(context);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        if(getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,Color.RED,5,TravelMode.WALKING);
        else
            Toast.makeText(_context,"Location is not ready yet",Toast.LENGTH_SHORT).show();
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,String travelMode){
        if(getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,Color.RED,5,travelMode);
        else
            Toast.makeText(_context,"Location is not ready yet",Toast.LENGTH_SHORT).show();
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor,String travelMode){
        if (getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,5,travelMode);
        else
            Toast.makeText(_context,"Location is not ready yet",Toast.LENGTH_SHORT).show();
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor){
        if (getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,5,TravelMode.WALKING);
        else
            Toast.makeText(_context,"Location is not ready yet",Toast.LENGTH_SHORT).show();
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation,int lineColor,int lineWidth){
        if(getCurrentLocation()!=null)
            calculateRouteBetweenTwoLocations(getCurrentLocation(),targetLocation,lineColor,lineWidth,TravelMode.WALKING);
        else
            Toast.makeText(_context,"Location is not ready yet",Toast.LENGTH_SHORT).show();
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
            progressDialog.setMessage("Creating route!");
            progressDialog.show();
            new CalculateRouteBetweenPoints(map,startLocation,targetLocation,_context,lineColor,lineWidth,travelMode,true,this).getDirectionsUrl();
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
        if(isLocationReady) {
            return provider.getLocation();
        }else {
            return null;
        }
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

    public String getDistanceBetweenPoints(LatLng startLocation,LatLng targetLocation,String mode) throws ExecutionException, InterruptedException {
        final CalculateRouteBetweenPoints crbp = new CalculateRouteBetweenPoints(startLocation,targetLocation,_context,mode,true,this);
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

        return future.get();
    }

    public String getDistanceBetweenPoints(LatLng startLocation,LatLng targetLocation){
        try {
            return getDistanceBetweenPoints(startLocation,targetLocation,TravelMode.WALKING);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDurationBetweenTwoPoints(LatLng startLocation,LatLng targetLocation,String mode) {
        final CalculateRouteBetweenPoints crbp = new CalculateRouteBetweenPoints(startLocation,targetLocation,_context,mode,true,this);
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

    public String getDurationBetweenTwoPoints(LatLng startLocation,LatLng targetLocation){
            return getDurationBetweenTwoPoints(startLocation,targetLocation,TravelMode.WALKING);
    }

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
        calculateRouteForMultiplePositions(TravelMode.WALKING);
    }

    public void calculateRouteForMultiplePositions(String travelMode){
        if (locations.size() > 0) {
            if (locations.size() == 1) {
            Toast.makeText(_context, "Location size must be greater than zero", Toast.LENGTH_SHORT).show();
            } else if (locations.size() == 2) {
            calculateRouteBetweenTwoPoints(locations.get(0), locations.get(1));
            } else {
                progressDialog.setMessage("Creating route!");
                progressDialog.show();
                for(int i = 0;i<locations.size()-1;i++){
                    if(i==locations.size()-2)
                        calculateRouteBetweenMultiplePoints(locations.get(i),locations.get(i+1),travelMode,true);
                    else
                        calculateRouteBetweenMultiplePoints(locations.get(i),locations.get(i+1),travelMode,false);
                }
            }
        }
    }
    private void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,boolean isLast){
        calculateRouteBetweenMultiplePoints(startLocation,targetLocation,Color.RED,5,TravelMode.DRIVING,isLast);
    }
    private void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,String travelMode,boolean isLast){
        calculateRouteBetweenMultiplePoints(startLocation,targetLocation,Color.RED,5,travelMode,isLast);
    }
    private void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,int color,boolean isLast){
        calculateRouteBetweenMultiplePoints(startLocation,targetLocation,color,5,TravelMode.DRIVING,isLast);
    }
    private void calculateRouteBetweenMultiplePoints(LatLng startLocation, LatLng targetLocation,int lineColor,int lineWidth,String travelMode,boolean isLast){
        try {
            progressDialog.setMessage("Creating route!");
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
}
