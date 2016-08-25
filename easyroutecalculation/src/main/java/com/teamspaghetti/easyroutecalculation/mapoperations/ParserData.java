package com.teamspaghetti.easyroutecalculation.mapoperations;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamspaghetti.easyroutecalculation.listeners.RouteCalculationFinishedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Salih on 23.08.2016.
 */
public class ParserData extends AsyncTask<GoogleMap,String,PolylineOptions> {

    GoogleMap googleMap;
    String route;
    JSONObject routeObject;
    Context context;
    PolylineOptions polylineOptions;
    Boolean isLast;
    RouteCalculationFinishedListener listener;

    public ParserData(String route,Context context,PolylineOptions polylineOptions,Boolean isLast,RouteCalculationFinishedListener listener){
        this.route = route;
        this.context = context;
        this.polylineOptions = polylineOptions;
        this.isLast = isLast;
        this.listener = listener;
    }

    @Override
    protected PolylineOptions doInBackground(GoogleMap... params) {

        googleMap = params[0];
        try {
            routeObject = new JSONObject(route);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DirectionsJSONParser directionsJSONParser  = new DirectionsJSONParser();
        List<List<HashMap<String, String>>> result = directionsJSONParser.parse(routeObject);
        ArrayList<LatLng> points = null;

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            polylineOptions.addAll(points);
        }

        return polylineOptions;
    }

    @Override
    protected void onPostExecute(PolylineOptions polylineOptions) {
        super.onPostExecute(polylineOptions);
        googleMap.addPolyline(polylineOptions);
        if(isLast)
            listener.calculationFinished();
    }

}
