package com.google.ar.core.examples.java.helloar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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
        LatLng location_A = new LatLng(25.03684432930838, 121.5692829249226);
//        LatLng location_A=new LatLng(25.036592455672526, 121.56937719083899);
//        LatLng location_B=new LatLng(25.037059933088432, 121.56934728017326);
//        LatLng location_C=new LatLng(25.03627064047405, 121.5686107300296);
//        LatLng location_D=new LatLng(25.03725852998541, 121.5687283046128);
//        LatLng location_E=new LatLng(25.03575948667267, 121.57228766350666);
//        LatLng location_F=new LatLng(25.034526707060134, 121.5733489402254);
//        LatLng location_G=new LatLng(25.03335986869663, 121.5677492477858);
//        LatLng location_H=new LatLng(25.03290028750567, 121.5700587319932);
//        LatLng location_I=new LatLng(25.02747186568306, 121.56428120691108);
//        LatLng location_J=new LatLng(25.033112010458133, 121.5577363304537);
//        LatLng location_K=new LatLng(25.037505093500496, 121.55365520632878);
//        LatLng location_L=new LatLng(25.027784050993414, 121.57446914749835);


        commerce_marker=mMap.addMarker(new MarkerOptions().position(location_A).title("政大商院"));
//        mMap.addMarker(new MarkerOptions().position(location_A).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_B).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_C).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_D).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_E).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_F).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_G).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_H).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_I).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_J).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_K).title(""));
//        mMap.addMarker(new MarkerOptions().position(location_L).title(""));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location_A));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(commerce_marker))
        {
            //handle click here
            Log.d("你選擇的是",marker.getTitle()+"");

            //把intent 放這裡 直接傳入
        }
        return false;
    }
}