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

    private static final long INTERVAL = 1000;
    private static final long FASTESET_INTERVAL = 5000;
    private static final int  PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final long DEFAULT_ZOOM = 10;

    private static final LocationRequest LOCATION_REQUEST = new LocationRequest().setInterval(INTERVAL)
            .setFastestInterval(FASTESET_INTERVAL)
            .setPriority(PRIORITY);

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private AccountHeader.Result headerNavigationLeft;
    private Drawer.Result navigationDrawerLeft;
    Marker marker;
    private MarkerOptions markerOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
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
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            stopLocationUpdate();
        }
    }

    private synchronized void callConnection() {
        Log.i("LOG", "callConnection()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void startLocationUpdate() {
        initLocationRequest();
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapaActivity.this);
    }


    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapaActivity.this);
    }

    @Override
    public void onConnected(Bundle bundle) {

        final Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                marker = mMap.addMarker(loadMarkerOption().position(latLng));
            }

            @Override
            public void onCancel() {

            }
        });

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LOCATION_REQUEST, this);
    }

    private MarkerOptions loadMarkerOption() {

        if (markerOption == null) {
            markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
        }

        return markerOption;
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOG", "onConnectionSuspended(" + i + ")");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("LOG", "onConnectionFailed(" + connectionResult + ")");
    }

    @Override
    public void onLocationChanged(Location location) {
        marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
    }
}
