package com.example.asilva.bookbuy.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ReservasAdapter;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.callbacks.EfetuarReservaListener;
import com.example.asilva.bookbuy.callbacks.ReservaListener;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ReservaFragment extends Fragment {

    EditText edtNomeRestaurante, edtNomeCliente;
    String nomeRestaurante, nomeCliente, dataHoraSelecionada;
    SharedPreferences prefsRestaurante, prefsCliente;
    Spinner spnQtdPessoas, spnReservas;
    int idRestaurante, idCliente, qtdPessoas;
    List<Reserva> listaReservas = new ArrayList<>();
    Button bttReservar;
    boolean reto;
    Reserva reserva, reservaSelecionada;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

        prefsRestaurante = this.getActivity().getSharedPreferences("dados_restaurante", 0);
        nomeRestaurante = prefsRestaurante.getString("nome", "Book buy");
        idRestaurante = prefsRestaurante.getInt("idRestaurante", 1);

        prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        nomeCliente = prefsCliente.getString("nome", "Book buy");
        idCliente = prefsCliente.getInt("id", 1);

        edtNomeRestaurante = (EditText) view.findViewById(R.id.edtNomeRestaurante);
        edtNomeCliente = (EditText) view.findViewById(R.id.edtNomeCliente);
        spnQtdPessoas = (Spinner) view.findViewById(R.id.spnQtdPessoas);
        spnReservas = (Spinner) view.findViewById(R.id.spnReservas);
        bttReservar = (Button) view.findViewById(R.id.bttReservar);

        spnReservas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reservaSelecionada = (Reserva) spnReservas.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnQtdPessoas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qtdPessoas = (int) spnQtdPessoas.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bttReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                efetuarReserva();
            }
        });

        buscarDataHoraDisponiveis();
        listaQtdPessoas();
        //listaDataHoraDisponiveis();

        edtNomeRestaurante.setText(nomeRestaurante);
        edtNomeCliente.setText(nomeCliente);

        return view;
    }

    public void efetuarReserva() {

        reserva = new Reserva();

        reserva.setIdRestaurante(idRestaurante);
        reserva.setIdCliente(idCliente);
        reserva.setStatus("INDISPONIVEL");
        reserva.setSituacao("ATIVO");
        reserva.setDataHora(reservaSelecionada.getDataHora());
        reserva.setIdReserva(reservaSelecionada.getIdReserva());
        reserva.setQtdPessoas(qtdPessoas);

        new DAOReserva().atualizarReserva(reserva, new EfetuarReservaListener() {
            @Override
            public void atualizarReserva(boolean retorno) {
                if (retorno == true) {
                    buscarDataHoraDisponiveis();
                    new MaterialDialog.Builder(getContext())
                            .title("Sucesso")
                            .content("Reserva efetuada com Sucesso!")
                            .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                    }).build().show();
                } else {
                    new MaterialDialog.Builder(getContext())
                            .title("Erro")
                            .content("Não foi possível efetuar sua reserva, tente novamente!")
                            .positiveText("Ok").callback(new MaterialDialog.ButtonCallback() {

                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                    }).build().show();
                }
            }
        });
    }

    public void buscarDataHoraDisponiveis() {
        if (Util.isNetworkConnected(getContext())) {
            new DAOReserva().buscarTodasReservas(idRestaurante, new ReservaListener() {
                @Override
                public void onReserva(List<Reserva> reservas) {
                    listaReservas = reservas;
                    if (listaReservas != null) {
                        listaDataHoraDisponiveis();
                    } else if (listaReservas == null) {
                        Toast.makeText(getContext(), "Nenhum reserva disponível foi encontrado para esse restaurante.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void listaQtdPessoas() {

        List<Integer> list = new ArrayList<Integer>();
        list.add(2);
        list.add(4);
        list.add(6);
        list.add(8);
        list.add(12);
        list.add(14);
        list.add(16);
        list.add(18);
        list.add(20);

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(getContext(),
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnQtdPessoas.setAdapter(dataAdapter);
    }

    public void listaDataHoraDisponiveis() {
        if (listaReservas != null && listaReservas.size() > 0) {
            ReservasAdapter reservasAdapter = new ReservasAdapter(listaReservas);
            spnReservas.setAdapter(reservasAdapter);
        }
    }
}
