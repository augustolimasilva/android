package com.example.asilva.bookbuy.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ItemAdapter;
import com.example.asilva.bookbuy.adapters.ProdutosRestauranteAdapter;
import com.example.asilva.bookbuy.adapters.ReservasAdapter;
import com.example.asilva.bookbuy.basicas.Item;
import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.basicas.Reserva;
import com.example.asilva.bookbuy.basicas.Rota;
import com.example.asilva.bookbuy.callbacks.ItemListener;
import com.example.asilva.bookbuy.callbacks.PedidoListener;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.callbacks.ReservasClienteListener;
import com.example.asilva.bookbuy.callbacks.RotaListener;
import com.example.asilva.bookbuy.dao.DAOItem;
import com.example.asilva.bookbuy.dao.DAOPedido;
import com.example.asilva.bookbuy.dao.DAOProduto;
import com.example.asilva.bookbuy.dao.DAOReserva;
import com.example.asilva.bookbuy.util.BaixarRota;
import com.example.asilva.bookbuy.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoFragment extends Fragment {

    List<Produto> listaProdutos = new ArrayList<>();
    List<Item> listaProdutosPedido = new ArrayList<>();
    ListView listProdutos;
    ProgressBar progressBar;
    int idRestaurante, quantidade, idCliente, idPed;
    TextView txtValor, txtValorTotal, txtValorFinal, txtData;
    List<Reserva> listaReservas = new ArrayList<>();
    ReservasAdapter reservasAdapter;
    ListView list;
    ItemAdapter itemAdapter;
    int posicao;

    EditText edtQuantidade;

    Button bttAdicionarItem, bttConcluir;
    Produto produto;
    float latitude, longitude, latRes, longRes;
    String valorProduto, tempoEstimado, data;
    float valorTotal;
    Item item;

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
        latitude = prefs.getFloat("latitude", 1);
        longitude = prefs.getFloat("longitude", 1);
        latRes = prefs.getFloat("latitudeRes", 1);
        longRes = prefs.getFloat("longitudeRes", 1);

        SharedPreferences prefs2 = this.getActivity().getSharedPreferences("dados_rota", 0);
        tempoEstimado = prefs2.getString("tempo", "22 minutos");


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
                ;

                bttAdicionarItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edtQuantidade.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getContext(), "Preencha corretamente o campo quantidade!", Toast.LENGTH_SHORT).show();
                        }else{
                            quantidade = Integer.parseInt(String.valueOf(edtQuantidade.getText().toString().trim()));
                        }

                        if (quantidade <=0 ) {
                            Toast.makeText(getContext(), "Preencha corretamente o campo quantidade!", Toast.LENGTH_SHORT).show();
                        } else {
                            item = new Item();

                            item.setIdProduto(produto.getIdProduto());
                            item.setQuantidade(quantidade);
                            item.setNomeProduto(produto.getDescricao());
                            item.setValorItem(produto.getValorProduto() * quantidade);

                            listaProdutosPedido.add(item);

                            dialog.dismiss();
                        }
                    }
                });

                txtValor.setText("R$: " + valorProduto + "0");

                dialog.show();
            }
        });

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
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icFinalizarPedido) {

            if (listaProdutosPedido != null) {
                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title(R.string.dialog_meu_pedido)
                        .items(R.array.items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                posicao = which;
                                new MaterialDialog.Builder(getContext())
                                        .title("Excluir")
                                        .content("Deseja excluir esse produto do seu pedido?")
                                        .negativeText("Não").positiveText("Sim").callback(new MaterialDialog.ButtonCallback() {

                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        listaProdutosPedido.remove(posicao);
                                    }

                                    @Override
                                    public void onNegative(MaterialDialog dialog) {
                                        dialog.dismiss();
                                    }

                                }).build().show();
                            }
                        }).positiveText("Finalizar Pedido").callback(new MaterialDialog.ButtonCallback() {
                                                                         @Override
                                                                         public void onPositive(MaterialDialog dialog) {

                                                                             reservasAdapter = new ReservasAdapter(listaReservas);
                                                                             calcularValorDoPedido();
                                                                             final Dialog dial = new Dialog(getContext());
                                                                             dial.setContentView(R.layout.dialog_pedido_concluir);

                                                                             dial.setTitle("Escolha um horário: ");

                                                                             txtData = (TextView) dial.findViewById(R.id.txtData);
                                                                             txtValorFinal = (TextView) dial.findViewById(R.id.txtValorFinal);
                                                                             bttConcluir = (Button) dial.findViewById(R.id.bttConcluir);
                                                                             txtValorFinal = (TextView) dial.findViewById(R.id.txtValorFinal);

                                                                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                                             Date date = new Date();
                                                                             data = dateFormat.format(date);

                                                                             txtData.setText(data.substring(8, 10) + "-" + data.substring(5, 7) + "-" + data.substring(0, 4) +
                                                                                     " " + data.substring(11, 13) + ":" + data.substring(14, 16));
                                                                             ;
                                                                             txtValorFinal.setText("R$: " + Float.toString(valorTotal) + "0");

                                                                             bttConcluir.setOnClickListener(new View.OnClickListener() {

                                                                                 @Override
                                                                                 public void onClick(View v) {

                                                                                     final Pedido pedido = new Pedido();
                                                                                     pedido.setDataHora(data);
                                                                                     pedido.setSituacao("ATIVO");
                                                                                     pedido.setStatus("ABERTO");
                                                                                     pedido.setTempoEstimado(tempoEstimado);
                                                                                     pedido.setIdCliente(idCliente);
                                                                                     pedido.setIdRestaurante(idRestaurante);
                                                                                     pedido.setIdMesa(1);

                                                                                     new DAOPedido().inserirPedido(pedido, new PedidoListener() {
                                                                                         @Override
                                                                                         public void onPedido(Integer idPedido) {
                                                                                             if (idPedido > 0) {
                                                                                                 idPed = idPedido;

                                                                                                 for (int i = 0; i < listaProdutosPedido.size(); i++) {

                                                                                                     Item it = new Item();
                                                                                                     it.setIdPedido(idPed);
                                                                                                     it.setIdPromocao(1);
                                                                                                     it.setIdProduto(listaProdutosPedido.get(i).getIdProduto());
                                                                                                     it.setQuantidade(listaProdutosPedido.get(i).getQuantidade());
                                                                                                     it.setValorTeste(String.valueOf(listaProdutosPedido.get(i).getValorItem()));

                                                                                                     new DAOItem().inserirItem(it, new ItemListener() {
                                                                                                         @Override
                                                                                                         public void onItem(boolean retorno) {
                                                                                                             if (retorno == true) {
                                                                                                                 Toast.makeText(getContext(), "Pedido efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                                                                                                                 listaProdutosPedido.clear();
                                                                                                                 dial.dismiss();
                                                                                                             } else {
                                                                                                                 Toast.makeText(getContext(), "Não foi possível Concluir seu Pedido. Tente Novamente!", Toast.LENGTH_SHORT).show();
                                                                                                             }
                                                                                                         }
                                                                                                     });
                                                                                                 }
                                                                                             } else {
                                                                                                 Toast.makeText(getContext(), "Não foi possível realizar seu Pedido. Tente Novamente!", Toast.LENGTH_SHORT).show();
                                                                                             }
                                                                                         }
                                                                                     });
                                                                                 }
                                                                             });

                                                                             dial.show();
                                                                         }
                                                                     }
                        ).show();

                list = dialog.getListView();
                itemAdapter = new ItemAdapter(listaProdutosPedido);
                list.setAdapter(itemAdapter);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void calcularValorDoPedido() {
        valorTotal = 0;

        for (int i = 0; i < listaProdutosPedido.size(); i++) {
            valorTotal = valorTotal + listaProdutosPedido.get(i).getValorItem();
        }
    }
}