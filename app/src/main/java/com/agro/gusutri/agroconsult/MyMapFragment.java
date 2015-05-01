package com.agro.gusutri.agroconsult;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

/**
 * Created by dragos on 4/20/15.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback{

    private View view;
    private GoogleMap mMap;
    private FragmentManager fragmentManager;
    private Double latitude, longitude;
    private ArrayList<Marker> points = new ArrayList<>();
    private ArrayList<Polygon> fields = new ArrayList<>();
    private boolean didMyLocationAppear = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentManager = getChildFragmentManager();
        if (container == null) {
            return null;
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.map_fragment, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        latitude = 0.0;
        longitude = 0.0;

        // Try to instantiate a map and obtain it from the SupportMapFragment.
        if (mMap == null) {
            ((SupportMapFragment) fragmentManager.findFragmentById(R.id.location_map)).getMapAsync(this);
        }

        final Button btnAddLocation = (Button) view.findViewById(R.id.map_button_add_marker);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddLocation.setText(latitude + "-" + longitude);
                LatLng newPoint = new LatLng(latitude, longitude);
                Marker marker = mMap.addMarker(new MarkerOptions().position(newPoint).title(points.size() + ""));
                marker.setDraggable(true);
                points.add(marker);
            }
        });

        Button btnSend = (Button) view.findViewById(R.id.map_button_send_data);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (points.size() > 2) {
                    PolygonOptions polygonOptions = new PolygonOptions().strokeColor(Color.BLUE).fillColor(Color.argb(50, 162, 23, 23));
                    for(Marker m: points){
                        polygonOptions.add(m.getPosition());
                    }
                    Polygon fieldMap = mMap.addPolygon(polygonOptions);
                    fields.add(fieldMap);
                } else {
                    // show alert
                }
            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set location values
        GoogleMap.OnMyLocationChangeListener locationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if (didMyLocationAppear == false) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                            longitude), 18.0f));
                    didMyLocationAppear = true;
                }
            }
        };
        mMap.setOnMyLocationChangeListener(locationChangeListener);

        // For showing a move to my location button
        mMap.setMyLocationEnabled(true);

        //center on Romania
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.91,
                25.80), 5.0f));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        didMyLocationAppear = false;
    }
}
