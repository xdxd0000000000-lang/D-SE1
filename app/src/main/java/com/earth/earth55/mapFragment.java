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
import android.widget.Toast;

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

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new
                            String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(callbackactive);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapFragment != null) {
                    mapFragment.getMapAsync(callbackactive);
                }
                Toast.makeText(getActivity(), "Get Your Location",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
            String strtitle = "Kittisak Location";
            String strsnippet = "กูอยู่นี่ " + clat[0] + ", " + clng[0];
            LatLng cposition = new LatLng(clat[0], clng[0]);
            MarkerOptions options = new MarkerOptions()
                    .position(cposition)
                    .title(strtitle)
                    .snippet(strsnippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            LatLng cposition2 = new LatLng(15.246228413380893, 104.84522771608624);
            MarkerOptions options2 = new MarkerOptions()
                    .position(cposition2)
                    .title("Maker 1")
                    .snippet("คอมพิวเตอร์อยู่นี้")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            googleMap.addMarker(options);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cposition, 15));
        }
    };
}