package com.example.asilva.bookbuy.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.PedidoProdutoAdapter;
import com.example.asilva.bookbuy.adapters.ProdutosRestauranteAdapter;
import com.example.asilva.bookbuy.adapters.ReservasAdapter;
import com.example.asilva.bookbuy.adapters.TiposRestauranteAdapter;
import com.example.asilva.bookbuy.basicas.PedidoProduto;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.callbacks.ReservaListener;
import com.example.asilva.bookbuy.callbacks.ReservasClienteListener;
import com.example.asilva.bookbuy.dao.DAOProduto;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoFragment extends Fragment {

    List<Produto> listaProdutos = new ArrayList<>();
    List<PedidoProduto> listaProdutosPedido = new ArrayList<>();
    ListView listProdutos;
    ProgressBar progressBar;
    int idRestaurante, quantidade, idCliente;
    TextView txtValor, txtValorTotal, txtValorFinal;
    Spinner spnDatas;
    List<Reserva> listaReservas = new ArrayList<>();
    ReservasAdapter reservasAdapter;

    EditText edtQuantidade;

    Button bttAdicionarItem, bttConcluir;
    Produto produto;
    float latitude, longitude;
    String valorProduto;
    float valorTotal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);

        quantidade = 0;

        SharedPreferences prefs = this.getActivity().getSharedPreferences("dados_restaurante", 0);
        idRestaurante = prefs.getInt("idRestaurante", 1);
        latitude = prefs.getFloat("latitudeRes", (float) 9.1);
        longitude = prefs.getFloat("longitudeRes", (float) 9.2);

        SharedPreferences prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        listProdutos = (ListView) view.findViewById(R.id.listProdutos);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        listProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long j) {

                produto = (Produto) listProdutos.getAdapter().getItem(i);

                final Dialog dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.dialog_pedido);

                dialog.setTitle(produto.getDescricao());

                valorProduto = Float.toString(produto.valorProduto);

                txtValor = (TextView) dialog.findViewById(R.id.txtValor);
                txtValorTotal = (TextView) dialog.findViewById(R.id.txtValorTotal);
                edtQuantidade = (EditText) dialog.findViewById(R.id.edtQuantidade);
                bttAdicionarItem = (Button) dialog.findViewById(R.id.bttAdicionarItem);

                bttAdicionarItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantidade = Integer.parseInt(String.valueOf(edtQuantidade.getText().toString()));

                        if(quantidade != 0) {

                            PedidoProduto pedidoProduto = new PedidoProduto();

                            pedidoProduto.setIdProduto(produto.getIdProduto());
                            pedidoProduto.setQuantidade(quantidade);
                            pedidoProduto.setSituacao("ATIVO");
                            pedidoProduto.setNomeProduto(produto.getDescricao());
                            pedidoProduto.setValorItem(produto.getValorProduto() * quantidade);

                            listaProdutosPedido.add(pedidoProduto);

                            dialog.dismiss();
                        }else{
                            Toast.makeText(getContext(), "Preencha corretamente o campo quantidade!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                txtValor.setText("R$: " + valorProduto + "0");

                dialog.show();
            }
        });

        buscarDataHoraDisponiveis();
        listaProdutos();
        atualizarLista();

        return view;
    }

    public void listaProdutos() {
        progressBar.setVisibility(View.VISIBLE);
        if (Util.isNetworkConnected(getContext())) {
            new DAOProduto().buscarTodosProdutos(idRestaurante, new ProdutosListener() {
                @Override
                public void onProduto(List<Produto> produtos) {
                    if (produtos != null) {
                        listaProdutos = produtos;
                        atualizarLista();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Nenhum produto foi encontrado para esse restaurante.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Verifique a conexão de sua Internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizarLista() {
        ProdutosRestauranteAdapter produtosRestauranteAdapter = new ProdutosRestauranteAdapter(listaProdutos);
        listProdutos.setAdapter(produtosRestauranteAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icFinalizarPedido) {
            if (listaProdutosPedido != null) {
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_meu_pedido)
                        .items(R.array.items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            }
                        }).positiveText("Finalizar Pedido").callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {

                                reservasAdapter = new ReservasAdapter(listaReservas);
                                calcularValorDoPedido();

                                final Dialog dial = new Dialog(getContext());

                                dial.setContentView(R.layout.dialog_pedido_concluir);

                                dial.setTitle("Escolha um horário: ");

                                spnDatas = (Spinner)dial.findViewById(R.id.spnDatas);
                                txtValorFinal = (TextView)dial.findViewById(R.id.txtValorFinal);
                                bttConcluir = (Button)dial.findViewById(R.id.bttConcluir);
                                txtValorFinal = (TextView)dial.findViewById(R.id.txtValorFinal);

                                txtValorFinal.setText("R$: " + Float.toString(valorTotal) + "0");
                                spnDatas.setAdapter(reservasAdapter);

                                dial.show();
                            }
                         }
                        ).show();

                ListView list = dialog.getListView();
                PedidoProdutoAdapter pedidoProdutoAdapter = new PedidoProdutoAdapter(listaProdutosPedido);
                list.setAdapter(pedidoProdutoAdapter);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarDataHoraDisponiveis() {
        if (Util.isNetworkConnected(getContext())) {
            new DAOReserva().buscarReservasDoClienteRestaurante(idRestaurante,idCliente, new ReservasClienteListener() {
                @Override
                public void onReserva(List<Reserva> reservas) {
                    if(reservas != null) {
                        listaReservas = reservas;
                    }else{
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String data = dateFormat.format(date);

                        Reserva res = new Reserva();
                        res.setDataHora(data);
                        listaReservas.add(res);
                    }
                }
            });
        }
    }

    public void calcularValorDoPedido(){
        valorTotal = 0;

        for(int i = 0; i < listaProdutosPedido.size(); i++){
            valorTotal = valorTotal + listaProdutosPedido.get(i).getValorItem();
        }
    }
}
