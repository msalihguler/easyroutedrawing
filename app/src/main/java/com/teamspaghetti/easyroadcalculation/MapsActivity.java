package com.teamspaghetti.easyroadcalculation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamspaghetti.easyroutecalculation.EasyRouteCalculation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    EasyRouteCalculation easyRouteCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        easyRouteCalculation = new EasyRouteCalculation(this,mMap);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("POSSS"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12.0f));
                easyRouteCalculation.calculateRouteFromMyLocation(latLng);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(easyRouteCalculation!=null)
        if(!easyRouteCalculation.isGPSEnabled(this))
            easyRouteCalculation.createDialogForOpeningGPS(this);
    }
}
