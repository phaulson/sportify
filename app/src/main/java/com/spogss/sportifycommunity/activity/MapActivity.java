package com.spogss.sportifycommunity.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.spogss.sportifycommunity.R;
import com.spogss.sportifycommunity.data.Coordinate;
import com.spogss.sportifycommunity.data.LocationType;
import com.spogss.sportifycommunity.data.User;
import com.spogss.sportifycommunity.data.connection.QueryType;
import com.spogss.sportifycommunity.data.connection.SportifyClient;
import com.spogss.sportifycommunity.data.connection.asynctasks.ClientQueryListener;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapActivity extends AppCompatActivity implements ClientQueryListener, GoogleMap.OnInfoWindowClickListener {

    private static final String FINE_LOCATION = ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final int radius = 1000;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15;
    private SportifyClient client;
    private Collection<LocationType> types = Arrays.asList(LocationType.values());
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        user = (User) getIntent().getSerializableExtra("user");
        if(user == null){
            setTitle("Map");
        }
        else{
            setTitle("Locations by "+user.getUsername());
        }

        client = SportifyClient.newInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLocationPermission();
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.i("MapActivity", "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            if (user == null) {
                                client.getNearbyLocationsAsync(new Coordinate(currentLocation.getLatitude(), currentLocation.getLongitude()), radius, types, MapActivity.this);
                            } else {
                                client.getLocationsAsync(user.getId(), MapActivity.this);
                            }
                        } else {
                            Log.i("MapActivity", "onComplete: current location is null");

                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.i("MapActivity", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (mLocationPermissionGranted) {
                    getDeviceLocation();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnInfoWindowClickListener(MapActivity.this);
                }
            }
        });
    }

    private void getLocationPermission() {
        String[] permissions = {ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }

                    }
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onSuccess(Object... results) {
        QueryType t = (QueryType) results[0];
        Collection<com.spogss.sportifycommunity.data.Location> locations;

        locations = (Collection<com.spogss.sportifycommunity.data.Location>) results[1];
        for (com.spogss.sportifycommunity.data.Location l : locations) {
            MarkerOptions m = new MarkerOptions()
                    .position(new LatLng(l.getCoordinates().getLat(), l.getCoordinates().getLng()))
                    .title(l.getName());
            m.zIndex(l.getPageId());

            int res = 0;
            switch (l.getType()){
                case GYM:
                    res = R.drawable.sp_darkgreen_g;
                    break;
                case EVENT:
                    res = R.drawable.sp_red_e;
                    break;
                case PUBLIC_PLACE:
                    res = R.drawable.sp_blue_p;
                    break;
                case OTHER:
                    res = R.drawable.sp_yellow_o;
                    break;
            }
            BitmapDescriptor ic = BitmapDescriptorFactory.fromResource(res);

            m.icon(ic);
            mMap.addMarker(m);

        }

    }

    @Override
    public void onFail(Object... errors) {
        Toast.makeText(this, "Error while loading nearby Locations", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("profile", (int) marker.getZIndex());
        startActivity(intent);
    }
}
