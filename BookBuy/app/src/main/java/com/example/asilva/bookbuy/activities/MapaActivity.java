package com.example.asilva.bookbuy.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.TiposRestauranteAdapter;
import com.example.asilva.bookbuy.basicas.RestauranteTipo;
import com.example.asilva.bookbuy.basicas.TipoRestaurante;
import com.example.asilva.bookbuy.callbacks.RestaurantesListener;
import com.example.asilva.bookbuy.callbacks.RestaurantesPorTipoListener;
import com.example.asilva.bookbuy.callbacks.TiposRestauranteListener;
import com.example.asilva.bookbuy.dao.DAOTiposRestaurante;
import com.example.asilva.bookbuy.util.Util;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final long INTERVAL = 1000;
    private static final long FASTESET_INTERVAL = 5000;
    private static final int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private static final long DEFAULT_ZOOM = 10;

    private NetworkState networkState;

    private static final LocationRequest LOCATION_REQUEST = new LocationRequest().setInterval(INTERVAL)
            .setFastestInterval(FASTESET_INTERVAL)
            .setPriority(PRIORITY);

    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private AccountHeader.Result headerNavigationLeft;
    private Drawer.Result navigationDrawerLeft;
    Marker marker, mkRestaurante;
    private MarkerOptions markerOption, mkoRestaurante;
    List<Restaurante> res, resFiltrados;
    List<TipoRestaurante> listaTiposRes;
    List<RestauranteTipo> listaResPorTipo;
    Restaurante rs, rest;
    Toolbar mToolbar;
    float latitude, longitude;
    TextView bookbuy;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        //NAVIGATION DRAWER

        SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frag_maps));
        mMap = fragment.getMap();

        mToolbar = (Toolbar) findViewById(R.id.tb_main);

        bookbuy = (TextView) findViewById(R.id.bookbuy);
        bookbuy.setText("Book Buy");

        setSupportActionBar(mToolbar);

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        SharedPreferences prefs = getSharedPreferences("meus_dados", 0);
        String nome = prefs.getString("nome", "BookBuy");
        String email = prefs.getString("email", "bookbuy@email.com");

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapaActivity.this, MenuRestauranteActivity.class);

                for (int i = 0; i < res.size(); i++) {

                    if (marker.getTitle().toString().equals(res.get(i).getNome().toString())) {

                        rest = new Restaurante();
                        rest = res.get(i);

                        SharedPreferences prefs = getSharedPreferences("dados_restaurante", 0);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putInt("idRestaurante", rest.getIdRestaurante());
                        editor.putString("nome", rest.getNome());
                        editor.putString("telefone", rest.getTelefone());
                        editor.putString("rua", rest.getRua());
                        editor.putString("bairro", rest.getBairro());
                        editor.putString("cidade", rest.getCidade());
                        editor.putString("complemento", rest.getComplemento());
                        editor.putString("numero", rest.getNumero());
                        editor.putString("cnpj", rest.getCnpj());
                        editor.putString("email", rest.getEmail());
                        editor.putFloat("latitudeRes", rest.getLatitude());
                        editor.putFloat("longitudeRes", rest.getLongitude());
                        editor.putString("cep", rest.getNome());
                        editor.putFloat("latitude", latitude);
                        editor.putFloat("longitude", longitude);
                        editor.putFloat("rate", rest.getRate());

                        editor.commit();

                        startActivity(intent);
                    }
                }
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapaActivity.this);
        String path = preferences.getString("path", null);

        Drawable drawable = path == null ? getResources().getDrawable(R.drawable.ic_perfil) : Drawable.createFromPath(path);

        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(true)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(true)
                .withHeaderBackground(R.drawable.fb9622)
                .addProfiles(

        new ProfileDrawerItem().withName(nome).withEmail(email).withIcon(drawable)
        )
        .build();

        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withDisplayBelowToolbar(false)
                .withToolbar(mToolbar)
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

                                new MaterialDialog.Builder(MapaActivity.this)
                                        .title("Alerta")
                                        .content("Deseja sair do aplicativo BookBuy?")
                                        .negativeText("Não").positiveText("Sim").callback(new MaterialDialog.ButtonCallback() {

                                    @Override
                                    public void onPositive(MaterialDialog dialog) {

                                        SharedPreferences.Editor prefs = getSharedPreferences("meus_dados", 0).edit();
                                        prefs.clear();
                                        prefs.commit();

                                        Intent itent = new Intent(MapaActivity.this, LoginActivity.class);
                                        startActivity(itent);
                                        finish();
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }

                                }).build().show();
                        }
                    }
                }).build();

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Perfil").withIcon(R.drawable.ic_minha_conta));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Minhas compras").withIcon(R.drawable.ic_minhas_compras));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Compartilhar").withIcon(R.drawable.ic_compartilhar));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sair").withIcon(R.drawable.ic_action_sair));

        networkState = new NetworkState();
        listarRestaurantes();
        listarTiposRestaurante();
        listarRestaurantesPorTipo();
    }

    public void listarRestaurantes() {

        if (Util.isNetworkConnected(this)) {

            new DAORestaurante().buscarTodosRestaurantes(new RestaurantesListener() {
                @Override
                public void onRestaurante(List<Restaurante> restaurantes) {

                    res = restaurantes;

                    if (restaurantes == null) {
                        Toast.makeText(getApplicationContext(), "Problemas de conexão com o servidor.", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < restaurantes.size(); i++) {
                            rs = new Restaurante();
                            rs = restaurantes.get(i);

                            mkRestaurante = mMap.addMarker(loadMarkerOptions().position(new LatLng(rs.getLatitude(), rs.getLongitude())));
                            mkRestaurante.setTitle(rs.getNome());
                            mkRestaurante.setSnippet("Telefone: " + rs.getTelefone());
                        }
                    }
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "Ative sua Internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void listarTiposRestaurante() {

        if (Util.isNetworkConnected(this)) {
            new DAOTiposRestaurante().buscarTodosTiposRestaurante(new TiposRestauranteListener() {
                @Override
                public void onTiposRestaurante(List<TipoRestaurante> listTiposRes) {

                    listaTiposRes = listTiposRes;
                }
            });
        }
    }

    public void listarRestaurantesPorTipo() {

        if (Util.isNetworkConnected(this)) {
            new DAOTiposRestaurante().buscarTodosRestaurantesPorTipo(new RestaurantesPorTipoListener() {
                @Override
                public void onRestaurantesPorTipo(List<RestauranteTipo> restaurantesPorTipo) {
                    listaResPorTipo = restaurantesPorTipo;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            callConnection();

        } else if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, LOCATION_REQUEST, this);
        }

        registerReceiver(networkState, new IntentFilter(NetworkStateReceiver.NETWORK));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        unregisterReceiver(networkState);
    }

    public class NetworkState extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            listarRestaurantes();
            listarRestaurantesPorTipo();
            listarTiposRestaurante();
        }
    }

    private void callConnection() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Log.i("LOG", "callConnection()");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addOnConnectionFailedListener(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();

            mGoogleApiClient.connect();

        } else {

            final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Ative sua localização.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        final Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location != null) {
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
        } else {
            //Toast.makeText(getApplicationContext(), "Problemas no servidor. Por favor, repita a operação.", Toast.LENGTH_SHORT).show();
        }
    }

    private MarkerOptions loadMarkerOptions() {
        if (mkoRestaurante == null) {
            mkoRestaurante = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.mkres));
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
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();

        if (marker != null) {
            marker.setPosition(new LatLng(latitude, longitude));
        }
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawerLeft.isDrawerOpen()) {
            navigationDrawerLeft.closeDrawer();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icFiltro) {
            if (listaTiposRes != null) {
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.dialog_tipo)
                        .items(R.array.itens)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                which++;
                                resFiltrados = new ArrayList<Restaurante>();
                                for (int i = 0; i < listaResPorTipo.size(); i++) {
                                    for (int z = 0; z < res.size(); z++) {
                                        if (which == listaResPorTipo.get(i).getIdTipo() &&
                                                listaResPorTipo.get(i).getIdRestaurante() == res.get(z).getIdRestaurante()) {

                                            //resFiltrados = new ArrayList<Restaurante>();
                                            resFiltrados.add(res.get(z));
                                        }
                                    }
                                }
                                if (resFiltrados != null && resFiltrados.size() > 0) {
                                    mMap.clear();
                                    for (int x = 0; x < resFiltrados.size(); x++) {
                                        rs = new Restaurante();
                                        rs = resFiltrados.get(x);

                                        mkRestaurante = mMap.addMarker(loadMarkerOptions().position(new LatLng(rs.getLatitude(), rs.getLongitude())));
                                        mkRestaurante.setTitle(rs.getNome());
                                        mkRestaurante.setSnippet("Telefone: " + rs.getTelefone());

                                        callConnection();
                                    }
                                } else if (resFiltrados == null || resFiltrados.size() == 0) {
                                    Toast.makeText(getApplicationContext(), "Nenhum restaurante encontrado para o tipo selecionado.", Toast.LENGTH_SHORT).show();
                                }

                                if (resFiltrados != null) {
                                    resFiltrados.clear();
                                }
                            }
                        })
                        .show();

                ListView list = dialog.getListView();
                TiposRestauranteAdapter tiposRestauranteAdapter = new TiposRestauranteAdapter(listaTiposRes);
                list.setAdapter(tiposRestauranteAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "Problemas de conexão com o servidor.", Toast.LENGTH_SHORT).show();
                listarTiposRestaurante();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}