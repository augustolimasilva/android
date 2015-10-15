package com.example.asilva.bookbuy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.activities.LoginActivity;
import com.example.asilva.bookbuy.adapters.ReservaClienteAdapter;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.basicas.Restaurante;
import com.example.asilva.bookbuy.callbacks.EfetuarReservaListener;
import com.example.asilva.bookbuy.callbacks.ReservasClienteListener;
import com.example.asilva.bookbuy.callbacks.RestaurantesListener;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.dao.DAORestaurante;
import com.example.asilva.bookbuy.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MinhasReservasFragment extends Fragment {

    List<Reserva> listaReservas = new ArrayList<>();
    ListView listReservas;
    ProgressBar progressBar;
    int idCliente;
    Reserva reserva;
    SharedPreferences prefsCliente;
    ReservaClienteAdapter reservaClienteAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minhas_reservas, container, false);

        prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        listReservas = (ListView) view.findViewById(R.id.listMinhasReservas);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listReservas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long j) {
                reserva = (Reserva) listReservas.getAdapter().getItem(i);
                new MaterialDialog.Builder(getContext())
                        .title(R.string.opcao)
                        .items(R.array.items2)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 1) {
                                    new MaterialDialog.Builder(getContext())
                                            .title("Alerta")
                                            .content("Deseja realmente cancelar a sua reserva?")
                                            .negativeText("Não").positiveText("Sim").callback(new MaterialDialog.ButtonCallback() {

                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            reserva.setStatus("DISPONIVEL");
                                            new DAOReserva().atualizarReserva(reserva, new EfetuarReservaListener() {
                                                @Override
                                                public void atualizarReserva(boolean retorno) {
                                                    if (retorno == true) {
                                                        listarReservasCliente();
                                                        reservaClienteAdapter.notifyDataSetChanged();
                                                        Toast.makeText(getContext(), "Reserva cancelada!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Não foi possível cancelar sua reserva. Tente Novamente!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onNegative(MaterialDialog dialog) {
                                            dialog.dismiss();
                                        }

                                    }).build().show();
                                }
                            }
                        })
                        .show();
            }
        });

        if (listaReservas == null || listaReservas.size() == 0) {
            listarReservasCliente();
        } else {
            atualizarLista();
        }

        return view;
    }

    public void listarReservasCliente() {
        progressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            new DAOReserva().buscarReservasDoCliente(idCliente, new ReservasClienteListener() {
                @Override
                public void onReserva(List<Reserva> reservas) {
                    if (reservas != null) {
                        listaReservas = reservas;
                        atualizarLista();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Nenhum reserva foi encontrada.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void atualizarLista() {
        reservaClienteAdapter = new ReservaClienteAdapter(listaReservas);
        listReservas.setAdapter(reservaClienteAdapter);
    }
}
