package com.example.asilva.bookbuy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asilva.bookbuy.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MinhaContaActivityFragment extends Fragment {

    public MinhaContaActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_minha_conta, container, false);
    }
}
