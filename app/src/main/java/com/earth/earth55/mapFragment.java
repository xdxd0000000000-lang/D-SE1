package com.earth.earth55;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Objects;


public class mapFragment extends Fragment {

    private int locationRequestCode = 1000;
    private Button button;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    private double[] clat = {0};
    private double[] clng = {0};


    public mapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        button = (Button) view.findViewById(R.id.button);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        return view;
    }
    private void Get_Current_location() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            clat[0] = location.getLatitude();
                            clng[0] = location.getLongitude();
                        }
                    }
                });
    }
    private OnMapReadyCallback callbackactive = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Get_Current_location();
            googleMap.clear();
            String strtitle = "Your Location";
            String strsnippet = "I am Here at " + clat[0] + ", " + clng[0];
            LatLng cposition = new LatLng(clat[0], clng[0]);

            MarkerOptions options = new MarkerOptions()
                    .position(new LatLng(clat[0], clng[0]))
                    .title(strtitle)
                    .snippet(strsnippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(options);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cposition, 12));
        }
    };
}