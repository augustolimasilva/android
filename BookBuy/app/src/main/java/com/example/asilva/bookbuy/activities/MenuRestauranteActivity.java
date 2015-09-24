package com.example.asilva.bookbuy.activities;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.fragments.MenuRestauranteFragment;
import com.example.asilva.bookbuy.fragments.PedidoFragment;
import com.example.asilva.bookbuy.fragments.ReservaFragment;

import io.karim.MaterialTabs;

public class MenuRestauranteActivity extends ActionBarActivity {

    ViewPager mViewPager;
    String nomeRestaurante, telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurante);

        MenuRestaurantePageAdapter adapter = new MenuRestaurantePageAdapter(
                getSupportFragmentManager());

        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ea9533")));

        Intent it = getIntent();
        nomeRestaurante = it.getExtras().getString("nomeRestaurante").toString();

        setTitle(nomeRestaurante);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);

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
                return getString(R.string.title_produto);
            }else if(position == 1){
                return getString(R.string.title_reserva);
            }else{
                return getString(R.string.title_pedido);
            }
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return new MenuRestauranteFragment();
            }else if(i == 1){
                return new ReservaFragment();
            }else{
                return new PedidoFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
