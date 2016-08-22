package com.teamspaghetti.easyroutecalculation;

import android.content.Context;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Salih on 22.08.2016.
 */
public class EasyRouteCalculation {

    LatLng myLocation;

    public EasyRouteCalculation(Context context){
        if(Utils.isGPSEnabled(context))
            myLocation = new CurrentLocationProvider(context).getCurrentLocation();
        else
            Utils.createDialogForOpeningGPS(context);
    }

    public void calculateRouteFromMyLocation(LatLng targetLocation){
        calculateRouteFromMyLocation(myLocation,targetLocation);
    }

    public void calculateRouteFromMyLocation(LatLng myLocation, LatLng targetLocation){

    }

}
