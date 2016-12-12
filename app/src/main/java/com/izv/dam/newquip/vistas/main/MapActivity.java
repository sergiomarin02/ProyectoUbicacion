package com.izv.dam.newquip.vistas.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Ubicacion;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng nuevaUbicacionEncontrada;
    double longitud[];
    double latitud[];
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle b = getIntent().getExtras();
        if(getIntent().hasExtra("longitudes")) {
            longitud = b.getDoubleArray("longitudes");
        }
        if(getIntent().hasExtra("latitudes")) {
            latitud = b.getDoubleArray("latitudes");
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);

        } else {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(lastLocation!=null) {
                nuevaUbicacionEncontrada = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(nuevaUbicacionEncontrada).title("Ubicacion"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaUbicacionEncontrada));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if(lastLocation!=null) {
                    nuevaUbicacionEncontrada = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(nuevaUbicacionEncontrada).title("Ubicacion"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(nuevaUbicacionEncontrada));
                }

            } else {
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
        }
        if(longitud != null){
            for (int x=0; x<longitud.length; x++){
                LatLng punto = new LatLng(latitud[x], longitud[x]);
                mMap.addMarker(new MarkerOptions().position(punto));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        if (nuevaUbicacionEncontrada != null) {
            setResult(RESULT_OK, new Intent().putExtra("latitude", nuevaUbicacionEncontrada.latitude).putExtra("longitud", nuevaUbicacionEncontrada.longitude));
        }
        finish();
    }
}
