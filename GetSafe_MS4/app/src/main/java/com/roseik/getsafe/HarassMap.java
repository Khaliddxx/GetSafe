package com.roseik.getsafe;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class HarassMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harass_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        List<LatLng> point = new ArrayList<LatLng>();
        point.add(new LatLng(12.862810, 30.217640));
        point.add(new LatLng(37.993150, 23.763420));
        point.add(new LatLng(30.112460, 31.355940));
        point.add(new LatLng(29.956680, 30.958020));
        point.add(new LatLng(30.082510, 31.638650));
        point.add(new LatLng(61.218060, -149.900280));
        point.add(new LatLng(12.862810, 23.740600));
        point.add(new LatLng(30.127960, 31.362630));
        point.add(new LatLng(24.723560, 46.828590));
        point.add(new LatLng(21.535870, 39.165260));




        // Add a marker in Sydney and move the camera
        LatLng case1 = new LatLng(30.074340, 31.486650);

        for (int i=0; i<10; i++) {
            mMap.addMarker(new MarkerOptions().position(point.get(i)).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(case1));
        }
    }
}