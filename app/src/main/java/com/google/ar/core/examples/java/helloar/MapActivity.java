package com.google.ar.core.examples.java.helloar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.ar.core.examples.java.helloar.databinding.ActivityMapBinding;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private Marker commerce_marker;
    private Marker admin_marker;
    private Marker library_marker;
    private Marker general_marker;
    private Marker information_building_marker;
    private Marker da_yong_building_marker;
    private Marker uni_library_marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(this);
        mMap = googleMap;



        // Add a marker in Sydney and move the camera
        LatLng location_A = new LatLng(24.98669477712358, 121.57654715815653);
        LatLng location_B=new LatLng(24.98637112016357, 121.57541765152799);
        LatLng location_C=new LatLng(24.986070955024015, 121.57679016370417);
        LatLng location_D=new LatLng(24.986331048806218, 121.57342539453673);
        LatLng location_E=new LatLng(24.98718430066657, 121.57514647761452);
        LatLng location_F=new LatLng(24.986839760009055, 121.57391539546231);
        LatLng location_G=new LatLng(24.98834643808405, 121.57886438046648);
//        LatLng location_=new LatLng();

        commerce_marker=mMap.addMarker(new MarkerOptions().position(location_A).title("政大商院"));
        admin_marker=mMap.addMarker(new MarkerOptions().position(location_B).title("行政大樓"));
        library_marker=mMap.addMarker(new MarkerOptions().position(location_C).title("中正圖書館"));
        general_marker=mMap.addMarker(new MarkerOptions().position(location_D).title("綜合院館"));;
        information_building_marker=mMap.addMarker(new MarkerOptions().position(location_E).title("資訊大樓"));;
        da_yong_building_marker=mMap.addMarker(new MarkerOptions().position(location_F).title("大勇樓"));;
        uni_library_marker=mMap.addMarker(new MarkerOptions().position(location_G).title("達賢圖書館"));;

//        mMap.addMarker(new MarkerOptions().position(location_B).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location_A));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent=new Intent(MapActivity.this,BrowsingActivity.class);
        intent.putExtra("CheckPlace",marker.getTitle());
        startActivity(intent);
        return false;
    }
}