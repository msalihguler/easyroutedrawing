package com.teamspaghetti.easyroadcalculation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
        easyRouteCalculation.gotoMyLocation(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(easyRouteCalculation!=null)
        if(!easyRouteCalculation.isGPSEnabled(this))
            easyRouteCalculation.createDialogForOpeningGPS(this);
    }
}
