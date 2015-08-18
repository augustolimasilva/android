package com.example.asilva.bookbuy;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;

public class MapaActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    double mLat;
    double mLong;
    Location l;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    LatLng latLng, lclAtual;
    private AccountHeader.Result headerNavigationLeft;
    private Drawer.Result navigationDrawerLeft;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        //setUpMapIfNeeded();
        callConnection();
        //NAVIGATION DRAWER

        SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_maps));
        mMap = fragment.getMap();

        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .withHeaderBackground(R.drawable.ic_perfil)
                .addProfiles(
                        new ProfileDrawerItem().withName("Person One").withEmail("person1@gmail.com").withIcon(getResources().getDrawable(R.drawable.ic_perfil))
                )
                .build();

        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withDisplayBelowToolbar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        switch (i) {
                            case 0:
                                Intent it = new Intent(MapaActivity.this, MinhaContaActivity.class);
                                startActivity(it);
                                break;
                            case 1:
                                Intent ite = new Intent(MapaActivity.this, MinhasComprasActivity.class);
                                startActivity(ite);
                                break;
                            case 2:
                                Intent sharing = new Intent(Intent.ACTION_SEND);
                                sharing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                sharing.setType("text/plain");
                                sharing.putExtra(Intent.EXTRA_TEXT, "Book Buy - Faça já sua reserva em um de nossos parceiros.");
                                startActivity(Intent.createChooser(sharing, "Convide os amigos"));
                                break;
                        }
                    }
                })
                .build();


        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Perfil").withIcon(R.drawable.ic_minha_conta));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Minhas compras").withIcon(R.drawable.ic_minhas_compras));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Compartilhar").withIcon(R.drawable.ic_compartilhar));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificações").withChecked(true));
    }
    @Override
    public void onResume(){
        super.onResume();

        if(mGoogleApiClient !=null && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        if(mGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    private synchronized void callConnection(){
        Log.i("LOG", "callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void startLocationUpdate(){
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapaActivity.this);
    }


    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapaActivity.this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i("LOG", "onConnected(" + bundle + ")");

        l = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if(l != null){
            mLat = l.getLatitude();
            mLong = l.getLongitude();

            latLng = new LatLng(mLat, mLong);
            //atualizarMapa();
        }

        startLocationUpdate();
    }

    public void customAddMarker(LatLng latLng, String title){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(title).draggable(false);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));

        marker = mMap.addMarker(options);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed(" + connectionResult + ")");
    }
/*
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_maps))
                    .getMap();
            if (mMap != null) {
                atualizarMapa();
            }
        }
    } */
/*
    private void atualizarMapa(){
       // mMap.clear();
        if(latLng != null){
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).tilt(0).build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(update);

            customAddMarker(latLng, "Marcador 1");
        }
    } */

    @Override
    public void onLocationChanged(Location location) {
        lclAtual = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.clear();
        if(latLng != null && latLng == lclAtual){
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).tilt(0).build();
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mMap.moveCamera(update);

            customAddMarker(latLng, "Marcador 1");
        }else
            if(latLng != null && latLng != lclAtual){
                CameraPosition cameraPosition = new CameraPosition.Builder().target(lclAtual).zoom(13).tilt(0).build();
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.moveCamera(update);

                customAddMarker(lclAtual, "Marcador 1");
            }
    }
}
