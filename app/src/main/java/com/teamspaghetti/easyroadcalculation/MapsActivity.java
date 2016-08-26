package com.teamspaghetti.easyroadcalculation;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        easyRouteCalculation = new EasyRouteCalculation(this,mMap);
        LatLng ankara = new LatLng(39.55,32.51);
        LatLng izmir = new LatLng(38.25,27.7);
        final LatLng istanbul = new LatLng(41.0,28.58);
        LatLng antalya = new LatLng(36.52,30.45);
        LatLng mugla = new LatLng(37.15,28.22);
        LatLng samsun = new LatLng(41.15,36.22);
        LatLng sinop = new LatLng(42.,35.11);
        easyRouteCalculation.gotoMyLocation(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
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
