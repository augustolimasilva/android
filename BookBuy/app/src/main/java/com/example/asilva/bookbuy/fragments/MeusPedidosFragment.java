package com.example.asilva.bookbuy.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.activities.EfetuarPagamentoActivity;
import com.example.asilva.bookbuy.adapters.PedidosClienteAdapter;
import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.callbacks.AtualizarPedidoListener;
import com.example.asilva.bookbuy.callbacks.PedidoClienteListener;
import com.example.asilva.bookbuy.dao.DAOPedido;
import com.example.asilva.bookbuy.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MeusPedidosFragment extends Fragment {

    ListView listMeusPedidos;
    ProgressBar progressBar;
    SharedPreferences prefsCliente;
    int idCliente;
    List<Pedido> listaPedidos = new ArrayList<>();
    Pedido pedido;
    PedidosClienteAdapter pedidosClienteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_pedidos, container, false);

        listMeusPedidos = (ListView) view.findViewById(R.id.listMeusPedidos);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        if (listaPedidos == null || listaPedidos.size() == 0) {
            buscarPedidosDoCliente();
        } else {
            listarPedidos();
        }
        //buscarPedidosDoCliente();

        listMeusPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long j) {
                pedido = (Pedido) listMeusPedidos.getAdapter().getItem(i);

                new MaterialDialog.Builder(getContext())
                        .title(R.string.opcao)
                        .items(R.array.items3)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {
                                    new MaterialDialog.Builder(getContext())
                                            .title("Alerta")
                                            .content("Deseja realmente cancelar o seu Pedido?")
                                            .negativeText("Não").positiveText("Sim").callback(new MaterialDialog.ButtonCallback() {

                                        @Override
                                        public void onPositive(MaterialDialog dialog) {

                                            pedido.setSituacao("INATIVO");

                                            new DAOPedido().atualizarPedido(pedido, new AtualizarPedidoListener() {
                                                @Override
                                                public void onPedido(boolean retorno) {
                                                    if (retorno) {
                                                        listaPedidos.remove(pedido);
                                                        pedidosClienteAdapter.notifyDataSetChanged();
                                                        Toast.makeText(getContext(), "Pedido cancelado com sucesso!", Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(getContext(), "Não foi possível cancelar seu pedido. Tente Novamente!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onNegative(MaterialDialog dialog) {
                                            dialog.dismiss();
                                        }

                                    }).build().show();
                                } else if (which == 1) {
                                    Intent it = new Intent(getActivity(), EfetuarPagamentoActivity.class);
                                    it.putExtra("idPedido", pedido.getIdPedido());
                                    it.putExtra("idRestaurante", pedido.getIdRestaurante());
                                    it.putExtra("nomeRestaurante", pedido.getNomeRestaurante());
                                    it.putExtra("data", pedido.getDataHora());
                                    it.putExtra("status", pedido.getStatus());
                                    startActivity(it);
                                }
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    public void buscarPedidosDoCliente() {
        progressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            new DAOPedido().buscarTodosPedidosDoCliente(idCliente, new PedidoClienteListener() {
                @Override
                public void onPedidoCliente(List<Pedido> pedidos) {
                    if (pedidos != null) {
                        listaPedidos = pedidos;
                        listarPedidos();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Nenhum pedido foi encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getContext(), "Verifique a conexão de sua Internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void listarPedidos() {
        pedidosClienteAdapter = new PedidosClienteAdapter(listaPedidos);
        listMeusPedidos.setAdapter(pedidosClienteAdapter);
        pedidosClienteAdapter.notifyDataSetChanged();
    }
}
