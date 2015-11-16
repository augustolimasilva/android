package com.example.asilva.bookbuy.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Rate;
import com.example.asilva.bookbuy.callbacks.AdicionarRateListener;
import com.example.asilva.bookbuy.callbacks.RateListener;
import com.example.asilva.bookbuy.dao.DAORate;
import com.example.asilva.bookbuy.fragments.MenuRestauranteFragment;
import com.example.asilva.bookbuy.fragments.PedidoFragment;
import com.example.asilva.bookbuy.fragments.ReservaFragment;

import io.karim.MaterialTabs;

public class MenuRestauranteActivity extends ActionBarActivity {

    ViewPager mViewPager;
    float latRes, longRes;
    String nomeRestaurante;
    int idCliente, idRestaurante;
    RatingBar rttVotar;
    Button bttSalvar, bttCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurante);

        MenuRestaurantePageAdapter adapter = new MenuRestaurantePageAdapter(
                getSupportFragmentManager());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        SharedPreferences prefs = getSharedPreferences("dados_restaurante", 0);
        nomeRestaurante = prefs.getString("nome", "bookbuy@email.com");
        latRes = prefs.getFloat("latitudeRes", 1);
        longRes = prefs.getFloat("longitudeRes", 1);
        idRestaurante = prefs.getInt("idRestaurante", 1);

        SharedPreferences prefsCliente = getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);

        setTitle(nomeRestaurante);

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        verificarRate();

        getSupportActionBar().setElevation(0);
    }

    public class MenuRestaurantePageAdapter extends FragmentPagerAdapter {

        public MenuRestaurantePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.title_informacoes);
            }else if(position == 1){
                return getString(R.string.title_produto);
            }else{
                return getString(R.string.title_reserva);
            }
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return new MenuRestauranteFragment();
            }else if(i == 1){
                return new PedidoFragment();
            }else{
                return new ReservaFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_restaurante, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icMapa) {
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + latRes + "," + longRes));
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor prefs = getSharedPreferences("dados_restaurante", 0).edit();
        prefs.clear();
        prefs.commit();

        SharedPreferences.Editor prefs2 = getSharedPreferences("dados_rota", 0).edit();
        prefs2.clear();
        prefs2.commit();

        Intent it = new Intent(this, MapaActivity.class);
        startActivity(it);
    }

    public void verificarRate(){
        if(idCliente != 0 && idRestaurante != 0) {
            new DAORate().pesquisarRateUsuarioRestaurante(idRestaurante, idCliente, new RateListener() {
                @Override
                public void onRate(Boolean retorno) {
                    if (retorno) {
                        final Dialog dial = new Dialog(MenuRestauranteActivity.this);
                        dial.setContentView(R.layout.dialog_rate);

                        rttVotar = (RatingBar)dial.findViewById(R.id.rttVotar);
                        bttSalvar = (Button)dial.findViewById(R.id.bttSalvar);
                        bttCancelar = (Button)dial.findViewById(R.id.bttCancelar);

                        Drawable progress = rttVotar.getProgressDrawable();
                        DrawableCompat.setTint(progress, Color.YELLOW);

                        bttSalvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                float nota = rttVotar.getRating();

                                Rate rate = new Rate();
                                rate.setIdCliente(idCliente);
                                rate.setIdRestaurante(idRestaurante);
                                rate.setRate(String.valueOf(nota));

                                new DAORate().adicionarRate(rate, new AdicionarRateListener() {
                                    @Override
                                    public void onAdicionar(float rateTotal) {
                                        if (rateTotal > 0) {
                                            dial.dismiss();
                                            Toast.makeText(getApplication(), R.string.obrigado, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                        bttCancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dial.dismiss();
                            }
                        });

                        dial.setTitle(R.string.avaliacao);
                        dial.show();
                    }
                }
            });
        }
    }
}
