package com.example.sanje.i_weather;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class CurrentMapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    LatLng patna;
    double latitude;
    double longitude;
    ProgressDialog progressDialog;
    LocationManager locationManager;
    MarkerOptions markerOptions=new MarkerOptions();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_maps);
        initViews();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    void initViews() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Location...");
        progressDialog.setCancelable(false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        patna = new LatLng(25.6081756, 85.0730019);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //Toast.makeText(MapsActivity.this,"HELLO",Toast.LENGTH_LONG).show();

                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 10, this);
                if (ActivityCompat.checkSelfPermission(CurrentMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CurrentMapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Toast.makeText(CurrentMapsActivity.this,"Please grant permission in the manifest",Toast.LENGTH_LONG).show();
                    return false;
                }else {
                    progressDialog.show();
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 10, CurrentMapsActivity.this);
                    return false;
                }
            }
        });
        mMap.addMarker(new MarkerOptions().position(patna).title("Marker in Patna").snippet("PATNA is the proud of bihar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(patna,15));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(CurrentMapsActivity.this,"This is Patna",Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        LatLng newLocation = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,15));


        mMap.addMarker(markerOptions.position(newLocation).title("Your Current Location").snippet("Welcome"));
        //REVERSE geocoding takes place here
        try {
            Geocoder geocoder=new Geocoder(this);
            List<Address> adrsList=geocoder.getFromLocation(latitude,longitude,2);
            if(adrsList!=null && adrsList.size()>0){
                Address addrs=adrsList.get(0);
                StringBuilder stringBuilder=new StringBuilder();
                for(int i=0;i<=addrs.getMaxAddressLineIndex();i++){
                    stringBuilder.append(addrs.getAddressLine(i)).append("\n");
                }
                addrs=adrsList.get(1);
                for(int i=0;i<=addrs.getMaxAddressLineIndex();i++){
                    stringBuilder.append(addrs.getAddressLine(i)).append("\n");
                }

                //txtView.setText(stringBuilder.toString());
                Toast.makeText(CurrentMapsActivity.this,stringBuilder.toString(),Toast.LENGTH_LONG).show();

            }
            else{
                //txtView.setText("No nearby address found");
                Toast.makeText(CurrentMapsActivity.this,"No nearby address found",Toast.LENGTH_LONG).show();
            }} catch (Exception e) {
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please grant permission in the manifest",Toast.LENGTH_LONG).show();
            return;
        }else {
            locationManager.removeUpdates(CurrentMapsActivity.this);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
