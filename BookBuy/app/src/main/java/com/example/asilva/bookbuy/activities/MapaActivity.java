package com.example.asilva.bookbuy.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Restaurante;
import com.example.asilva.bookbuy.dao.DAORestaurante;
import com.mikepenz.materialdrawer.Drawer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long INTERVAL = 1000;
    private static final long FASTESET_INTERVAL = 5000;
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final long DEFAULT_ZOOM = 10;

    private static final LocationRequest LOCATION_REQUEST = new LocationRequest().setInterval(INTERVAL)
            .setFastestInterval(FASTESET_INTERVAL)
            .setPriority(PRIORITY);

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private AccountHeader.Result headerNavigationLeft;
    private Drawer.Result navigationDrawerLeft;
    Marker marker, mkRestaurante;
    private MarkerOptions markerOption, mkoRestaurante;
    List<Restaurante> res;
    Restaurante rs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        callConnection();
        //NAVIGATION DRAWER

        SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_maps));
        mMap = fragment.getMap();

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapaActivity.this, MenuRestauranteActivity.class);

                intent.putExtra("nomeRestaurante", marker.getTitle());
                intent.putExtra("telefone", marker.getSnippet());
                startActivity(intent);
            }
        });

        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(true)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .withHeaderBackground(R.drawable.fb9622)
                .addProfiles(
                        new ProfileDrawerItem().withName(nome).withEmail(email).withIcon(getResources().getDrawable(R.drawable.ic_perfil))
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
                            case 3:
                                        SharedPreferences.Editor prefs = getSharedPreferences("meus_dados", 0).edit();
                                        prefs.clear();
                                        prefs.commit();

                                        Intent itent = new Intent(MapaActivity.this, LoginActivity.class);
                                        startActivity(itent);
                                        finish();


                        }
                    }
                })
                .build();


        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Perfil").withIcon(R.drawable.ic_minha_conta));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Minhas compras").withIcon(R.drawable.ic_minhas_compras));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Compartilhar").withIcon(R.drawable.ic_compartilhar));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sair").withIcon(R.drawable.ic_action_sair));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Notificações").withChecked(true));

        listarRestaurantes();
    }

    public void listarRestaurantes(){
        new DAORestaurante().BuscarTodosRestaurantes(new RestauranteListener() {
            @Override
            public void onRestaurante(List<Restaurante> restaurantes) {

                for (int i = 0; i < restaurantes.size(); i++) {

                    rs = new Restaurante();
                    rs = restaurantes.get(i);

                    mkRestaurante = mMap.addMarker(loadMarkerOptions().position(new LatLng(rs.getLatitude(), rs.getLongitude())));
                    mkRestaurante.setTitle(rs.getNome());
                    mkRestaurante.setSnippet(rs.getTelefone());
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LOCATION_REQUEST, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private synchronized void callConnection() {

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {

            Log.i("LOG", "callConnection()");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();
        }else {
            new MaterialDialog.Builder(this)
                    .title("Teste")
                    .content("Ative sua localização!")
                    .positiveText("ATIVAR")
                    .negativeText("SAIR")
                    .show();
        }
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

    private MarkerOptions loadMarkerOptions(){
        if (mkoRestaurante == null) {
            mkoRestaurante = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurante));
        }

        return mkoRestaurante;
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
        Toast.makeText(getApplicationContext(), "Ative sua localização.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (marker != null) {
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }
}