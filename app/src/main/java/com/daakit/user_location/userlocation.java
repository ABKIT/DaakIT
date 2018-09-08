package com.daakit.user_location;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.daakit.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userlocation extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mgoogleapiclient;
    private Location mlastlocation;
    private LocationRequest mlocationreq;
    private FirebaseAuth ath;
    private SharedPreferences lastlocation;
    private String lastlat,lastlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlocation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getids();
        getpreference();
    }
    private void getpreference()
    {
        lastlocation=getSharedPreferences("Lservice",MODE_PRIVATE);
        lastlat=lastlocation.getString("Latitude","none");
        lastlong=lastlocation.getString("Longtitude","none");
    }
    private void setprefernce()
    {
        try {
            SharedPreferences.Editor editor = getSharedPreferences("Lservice", MODE_PRIVATE).edit();
            editor.putString("Latitude", String.valueOf(mlastlocation.getLatitude()));
            editor.putString("Longtitude", String.valueOf(mlastlocation.getLongitude()));
            editor.apply();
        }
        catch (Exception e)
        {

        }
    }

    private void getids() {
        ath=FirebaseAuth.getInstance();
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

        buildgoogleapiclient();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        if(!lastlat.equals("none") && !lastlong.equals("none"))
        {
            LatLng latLng=new LatLng(Double.valueOf(lastlat),Double.valueOf(lastlong));

            //move camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }


    }

    private synchronized void buildgoogleapiclient()
    {
        mgoogleapiclient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mgoogleapiclient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationreq = new LocationRequest();
        mlocationreq.setInterval(1000);
        mlocationreq.setFastestInterval(1000);
        mlocationreq.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mgoogleapiclient, mlocationreq, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mlastlocation=location;

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        //move camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

    @Override
    protected void onStop() {
        super.onStop();
        setprefernce();
    }

    //add location when user create order
    private void createuserlocation()
    {
        String uid=ath.getCurrentUser().getUid();
        DatabaseReference dbstore=FirebaseDatabase.getInstance().getReference("Ordercreated");
        GeoFire geofire=new GeoFire(dbstore);
        geofire.setLocation(uid,new GeoLocation(mlastlocation.getLatitude(),mlastlocation.getLongitude()));


    }

    //remove user location after order cancel or completed
    private void removeuserlocation()
    {
        String uid=ath.getCurrentUser().getUid();
        DatabaseReference dbstore=FirebaseDatabase.getInstance().getReference("Ordercreated");
        GeoFire geofire=new GeoFire(dbstore);
        geofire.removeLocation(uid);
    }
}
