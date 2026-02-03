package com.earth.earth55;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class mapFragment extends Fragment {

    private static final int LOCATION_REQUEST_CODE = 1000;

    private Button button, button2;
    private EditText editTextname;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private double clat = 0.0;
    private double clng = 0.0;

    public mapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // bind view
        button = view.findViewById(R.id.button);
        button2 = view.findViewById(R.id.button2);
        editTextname = view.findViewById(R.id.editTextText);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        requestPermission();
        getCurrentLocation();

        if (mapFragment != null) {
            mapFragment.getMapAsync(mapReadyCallback);
        }

        // ปุ่มหาตำแหน่งปัจจุบัน
        button.setOnClickListener(v -> {
            getCurrentLocation();
            if (mapFragment != null) {
                mapFragment.getMapAsync(mapReadyCallback);
            }
            Toast.makeText(getActivity(), "ตำแหน่งปัจจุบัน", Toast.LENGTH_SHORT).show();
        });

        // ปุ่มค้นหาสถานที่
        button2.setOnClickListener(v -> searchLocation());

        return view;
    }

    // ================= Permission =================
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_REQUEST_CODE);
        }
    }

    // ================= Current Location =================
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        clat = location.getLatitude();
                        clng = location.getLongitude();
                    }
                });
    }

    // ================= Search Location =================
    private void searchLocation() {
        String location = editTextname.getText().toString().trim();

        if (location.isEmpty()) {
            Toast.makeText(getActivity(), "กรุณาใส่ชื่อสถานที่", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(getActivity());

        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);

            if (addressList == null || addressList.isEmpty()) {
                Toast.makeText(getActivity(), "ไม่พบสถานที่", Toast.LENGTH_SHORT).show();
                return;
            }

            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            if (mMap != null) {
                mMap.clear();

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(location)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ================= Map =================
    private final OnMapReadyCallback mapReadyCallback = googleMap -> {
        mMap = googleMap;
        mMap.clear();

        LatLng current = new LatLng(clat, clng);

        mMap.addMarker(new MarkerOptions()
                .position(current)
                .title("ตำแหน่งของฉัน")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        LatLng fixed = new LatLng(15.246228413380893, 104.84522771608624);
        mMap.addMarker(new MarkerOptions()
                .position(fixed)
                .title("Marker 1")
                .snippet("คอมพิวเตอร์อยู่นี้")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fixed, 15));
    };
}
