package com.example.asilva.bookbuy.activities;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Restaurante;
import com.example.asilva.bookbuy.fragments.MenuRestauranteFragment;
import com.example.asilva.bookbuy.fragments.PedidoFragment;
import com.example.asilva.bookbuy.fragments.ReservaFragment;

import io.karim.MaterialTabs;

public class MenuRestauranteActivity extends ActionBarActivity {

    ViewPager mViewPager;
    float latitude, longitude;
    String nomeRestaurante;

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
        latitude = prefs.getFloat("latitudeRes", (float) 9.1);
        longitude = prefs.getFloat("longitudeRes", (float) 9.2);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);

        setTitle(nomeRestaurante);

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

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
            Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latitude + "," + longitude + "?q="));
            startActivity(it);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        SharedPreferences.Editor prefs = getSharedPreferences("dados_restaurante", 0).edit();
        prefs.clear();
        prefs.commit();

        Intent it = new Intent(this, MapaActivity.class);
        startActivity(it);
    }
}
