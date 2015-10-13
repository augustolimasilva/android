package com.example.asilva.bookbuy.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ReservaClienteAdapter;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.callbacks.ReservasClienteListener;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MinhasReservasFragment extends Fragment {

    List<Reserva> listaReservas = new ArrayList<>();
    ListView listReservas;
    ProgressBar progressBar;
    int idCliente;
    SharedPreferences prefsCliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minhas_reservas, container, false);

        prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        listReservas = (ListView)view.findViewById(R.id.listMinhasReservas);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        listarReservasCliente();
        atualizarLista();

        return view;
    }

    public void listarReservasCliente(){
        progressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            new DAOReserva().buscarReservasDoCliente(idCliente, new ReservasClienteListener() {
                @Override
                public void onReserva(List<Reserva> reservas) {
                    if(reservas != null){
                        listaReservas = reservas;
                        atualizarLista();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Nenhum reserva foi encontrada.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void atualizarLista(){
        ReservaClienteAdapter reservaClienteAdapter = new ReservaClienteAdapter(listaReservas);
        listReservas.setAdapter(reservaClienteAdapter);
    }
}
