package com.example.glory_hunter.tnettraining;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.glory_hunter.tnettraining.map_direction.DirectionHandler;
import com.example.glory_hunter.tnettraining.map_direction.DirectionResponse;
import com.example.glory_hunter.tnettraining.map_direction.RetrofitInstance;
import com.example.glory_hunter.tnettraining.map_direction.RetrofitService;
import com.example.glory_hunter.tnettraining.map_direction.RouteModel;
import com.example.glory_hunter.tnettraining.network.GetAddressService;
import com.example.glory_hunter.tnettraining.network.MainObjectJSON;
import com.example.glory_hunter.tnettraining.network.RetrofitFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_PERMISSION = 0;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LatLng currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView tvAddress;
    private Button btGo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tvAddress = findViewById(R.id.tv_location);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (Build.VERSION.SDK_INT >= 23) {
            setupPermission();
        } else {
            setupMap();
        }

        getCurrentLocation();

        btGo = findViewById(R.id.bt_go);

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    mMap.clear();
                    RetrofitService retrofitService = RetrofitInstance.getInstance().create(RetrofitService.class);
                    retrofitService.getDirection(
                            latlngString(currentLocation),
                            latlngString(mMap.getCameraPosition().target)
                    ).enqueue(new Callback<DirectionResponse>() {
                        @Override
                        public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                            RouteModel routeModel = DirectionHandler.getListRoute(response.body()).get(0);
                            mMap.addMarker(new MarkerOptions().position(routeModel.endLocation));


                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .color(Color.CYAN)
                                    .width(8);

                            for (int i = 0; i < routeModel.points.size(); i++) {
                                polylineOptions.add(routeModel.points.get(i));
                            }

                            mMap.addPolyline(polylineOptions);
                            DirectionHandler.zoomRoute(mMap, routeModel.points);
                        }

                        @Override
                        public void onFailure(Call<DirectionResponse> call, Throwable t) {

                        }
                    });
                }
        });

    }

    private void getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 13);
                            mMap.animateCamera(cameraUpdate);

                        }
                    }
                });
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = new LatLng(location.getLongitude(), location.getLatitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLocation, 13);
                mMap.animateCamera(cameraUpdate);
            }
        };

    }

    private void setupPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        } else {
            setupMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                GetAddressService getAddressService = RetrofitFactory.getInstance().create(GetAddressService.class);
                getAddressService.getAddress(latlngString(mMap.getCameraPosition().target)
                        ,false).enqueue(new Callback<MainObjectJSON>() {
                    @Override
                    public void onResponse(Call<MainObjectJSON> call, Response<MainObjectJSON> response) {
                        Log.d("asdasd", "onResponse: " + response.body().getResults().size());
                                if (response.isSuccessful()) {
                                    if (response.body().getResults().size() != 0) {
                                        String string = response.body().getResults().get(0).getFormatted_address();
                                        tvAddress.setText(string);

                                    }
                                }

                    }

                    @Override
                    public void onFailure(Call<MainObjectJSON> call, Throwable t) {

                    }
                });
            }
        });
    }

    public String latlngString(LatLng latLng){
        String string = latLng.latitude + "," + latLng.longitude;
        return string;
    }


}
