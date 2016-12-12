package com.izv.dam.newquip.vistas.notas;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteUbicacion;
import com.izv.dam.newquip.contrato.ContratoNota;

import com.izv.dam.newquip.pojo.Ubicacion;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.pojo.Ubicacion;
import com.izv.dam.newquip.util.UtilFecha;
import com.izv.dam.newquip.vistas.main.MapActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
//    private GoogleApiClient mGoogleApiClient;

    private Button ubicacionBoton;
    private Context c = this;
    private int VUELVE_MAPA = 111;
    private double latitudNueva=20000;
    private double longitudNueva =20000;
    private AyudanteUbicacion ayudanteUbicacion;
    private ArrayList<Ubicacion> listaUbicaciones;
//    RuntimeExceptionDao<Ubicacion,Integer> Dao = a.getSimpleRunTimeDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        presentador = new PresentadorNota(this);

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        ubicacionBoton = (Button) findViewById(R.id.mapa);

        ayudanteUbicacion = OpenHelperManager.getHelper(this, AyudanteUbicacion.class);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            dameUbicaciones();
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
                dameUbicaciones();
            }
        }
        mostrarNota(nota);

//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(LocationServices.API)
//                    .build();
//        }

        ubicacionBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, MapActivity.class);
                if (listaUbicaciones != null && !listaUbicaciones.isEmpty()) {
                    double[] longitudes = new double[listaUbicaciones.size()];
                    double[] latitudes = new double[listaUbicaciones.size()];
                    for (int x = 0; x < listaUbicaciones.size(); x++) {
                        latitudes[x] = listaUbicaciones.get(x).getLatitude();
                        longitudes[x] = listaUbicaciones.get(x).getLongitude();
                    }

                    i.putExtra("longitudes", longitudes);
                    i.putExtra("latitudes", latitudes);
                }
                startActivityForResult(i, VUELVE_MAPA);
            }
        });
    }

    private void dameUbicaciones() {
        Dao dao;


        try {
            dao = ayudanteUbicacion.getSimpleDao();
            listaUbicaciones = (ArrayList<Ubicacion>) dao.queryForEq("idnota", nota.getId());
            System.out.println("blablabla");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
    }

    private void saveNota() {
        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setNota(editTextNota.getText().toString());
        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);
        }

        guardarUbicacion();
    }

    private void guardarUbicacion() {
        if(latitudNueva!=20000 && longitudNueva!=20000) {
            try {
                Dao dao = ayudanteUbicacion.getSimpleDao();
                Ubicacion ubicacion = new Ubicacion();
                ubicacion.setId(System.currentTimeMillis());
                ubicacion.setIdnota(nota.getId());
                ubicacion.setLatitude(latitudNueva);
                ubicacion.setLongitude(longitudNueva);
                dao.create(ubicacion);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onStart() {
//        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
//        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VUELVE_MAPA && data!=null && data.hasExtra("latitude")) {
            latitudNueva = data.getExtras().getDouble("latitude");
            longitudNueva = data.getExtras().getDouble("longitud");
        }
    }

    //    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        System.out.println("On connected");
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "NO FINE LOCATION", Toast.LENGTH_SHORT);
//            System.out.println("no fine location");
//            return;
//        }
//
//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "NO COARSED LOCATION", Toast.LENGTH_SHORT);
//            System.out.println("no coarsed location");
//            return;
//
//        }
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            Toast.makeText(this, mLastLocation.getLatitude() + ", " + mLastLocation.getLongitude(), Toast.LENGTH_SHORT);
//        }
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        Toast.makeText(this,"Conexion suspendida",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Toast.makeText(this,"Conexion fallida",Toast.LENGTH_SHORT).show();
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        saveNota();
//        float latitude = (float) location.getLatitude();
//        float longitude = (float) location.getLongitude();
//        String fecha = UtilFecha.formatDate(new Date());
//        Ubicacion loc = new Ubicacion(nota.getId(),latitude,longitude,fecha);
//        Dao.create(loc);
//    }
}