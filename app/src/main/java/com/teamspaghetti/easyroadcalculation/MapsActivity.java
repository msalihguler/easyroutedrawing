package com.teamspaghetti.easyroadcalculation;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamspaghetti.easyroutecalculation.EasyRouteCalculation;
import com.teamspaghetti.easyroutecalculation.TravelMode;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EasyRouteCalculation easyRouteCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        easyRouteCalculation = new EasyRouteCalculation(this,mMap);
        easyRouteCalculation.gotoMyLocation(true);
        easyRouteCalculation.getDistanceBetweenPoints(new LatLng(39.99105,32.74136),new LatLng(39.96142,32.73447));

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(easyRouteCalculation!=null)
        if(!easyRouteCalculation.isGPSEnabled(this))
            easyRouteCalculation.createDialogForOpeningGPS(this);
    }
}
