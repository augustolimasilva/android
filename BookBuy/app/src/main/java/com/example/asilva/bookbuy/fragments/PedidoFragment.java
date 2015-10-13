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
import com.example.asilva.bookbuy.adapters.ProdutosRestauranteAdapter;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.dao.DAOProduto;
import com.example.asilva.bookbuy.util.Util;

import java.util.ArrayList;
import java.util.List;

public class PedidoFragment extends Fragment {

    List<Produto> listaProdutos = new ArrayList<>();
    ListView listProdutos;
    ProgressBar progressBar;
    int idRestaurante;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("dados_restaurante", 0);
        idRestaurante = prefs.getInt("idRestaurante", 1);

        listProdutos = (ListView)view.findViewById(R.id.listProdutos);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        listaProdutos();
        atualizarLista();

        return view;
    }

    public void listaProdutos(){
        progressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            new DAOProduto().buscarTodosProdutos(idRestaurante, new ProdutosListener() {
                @Override
                public void onProduto(List<Produto> produtos) {
                    if (produtos != null) {
                        listaProdutos = produtos;
                        atualizarLista();
                        progressBar.setVisibility(View.GONE);
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Nenhum produto foi encontrado para esse restaurante.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getContext(), "Verifique a conexão de sua Internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizarLista(){
        ProdutosRestauranteAdapter produtosRestauranteAdapter = new ProdutosRestauranteAdapter(listaProdutos);
        listProdutos.setAdapter(produtosRestauranteAdapter);
    }
}
