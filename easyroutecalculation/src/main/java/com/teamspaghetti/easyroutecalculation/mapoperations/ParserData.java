package com.teamspaghetti.easyroutecalculation.mapoperations;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
    ProgressDialog progressDialog;

    public ParserData(String route,Context context){
        this.route = route;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading URL");
        progressDialog.show();
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
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

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
            lineOptions.addAll(points);
            lineOptions.width(5);
            lineOptions.color(Color.RED);
        }

        return lineOptions;
    }

    @Override
    protected void onPostExecute(PolylineOptions polylineOptions) {
        super.onPostExecute(polylineOptions);
        googleMap.addPolyline(polylineOptions);
        progressDialog.dismiss();
    }

}
