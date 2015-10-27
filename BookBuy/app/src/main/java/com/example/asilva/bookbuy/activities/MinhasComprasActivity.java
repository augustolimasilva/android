package com.example.asilva.bookbuy.activities;

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
import android.view.Menu;
import android.view.MenuItem;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.fragments.MeusPedidosFragment;
import com.example.asilva.bookbuy.fragments.MinhasReservasFragment;

import io.karim.MaterialTabs;

public class MinhasComprasActivity extends ActionBarActivity {

    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_compras);

        MinhasComprasPageAdapter adapter = new MinhasComprasPageAdapter(
                getSupportFragmentManager());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);

        MaterialTabs tabs = (MaterialTabs) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        getSupportActionBar().setElevation(0);
    }

    public class MinhasComprasPageAdapter extends FragmentPagerAdapter {

        public MinhasComprasPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.title_meus_pedidos);
            }else{
                return getString(R.string.title_minhas_reservas);
            }
        }

        @Override
        public Fragment getItem(int i) {
            if(i == 0){
                return new MeusPedidosFragment();
            }else{
                return new MinhasReservasFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
