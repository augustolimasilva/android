package com.example.asilva.bookbuy.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ItemAdapter;
import com.example.asilva.bookbuy.adapters.ProdutosRestauranteAdapter;
import com.example.asilva.bookbuy.basicas.Item;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.callbacks.ItemListener;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.dao.DAOItem;
import com.example.asilva.bookbuy.dao.DAOProduto;

import java.util.ArrayList;
import java.util.List;

public class AdicionarItemActivity extends AppCompatActivity {

    int idPedido, idRestaurante, quantidade, posicao;
    List<Produto> listaProdutos = new ArrayList<Produto>();
    ProdutosRestauranteAdapter produtosRestauranteAdapter;
    ListView lstProdutos;
    List<Item> listaProdutosPedido = new ArrayList<>();
    ProgressBar progressBar;
    Produto produto;
    TextView txtValor, txtValorTotal, txtValorFinal;
    Button bttAdicionarItem, bttConcluir;
    String valorProduto;
    EditText edtQuantidade;
    ListView list;
    ItemAdapter itemAdapter;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_item);

        lstProdutos = (ListView) findViewById(R.id.lstProdutos);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        idPedido = getIntent().getIntExtra("idPedido", 1);
        idRestaurante = getIntent().getIntExtra("idRestaurante", 1);

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);

        lstProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long j) {

                produto = (Produto) lstProdutos.getAdapter().getItem(i);

                final Dialog dialog = new Dialog(AdicionarItemActivity.this);

                dialog.setContentView(R.layout.dialog_pedido);

                dialog.setTitle(produto.getNome());

                valorProduto = Float.toString(produto.valorProduto);

                txtValor = (TextView) dialog.findViewById(R.id.txtValor);
                txtValorTotal = (TextView) dialog.findViewById(R.id.txtValorTotal);
                edtQuantidade = (EditText) dialog.findViewById(R.id.edtQuantidade);
                bttAdicionarItem = (Button) dialog.findViewById(R.id.bttAdicionarItem);


                bttAdicionarItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edtQuantidade.getText().toString().trim().isEmpty()) {
                            Toast.makeText(AdicionarItemActivity.this, "Preencha corretamente o campo quantidade!", Toast.LENGTH_SHORT).show();
                        } else {
                            quantidade = Integer.parseInt(String.valueOf(edtQuantidade.getText().toString().trim()));
                        }

                        if (quantidade <= 0 || quantidade > 99) {
                            Toast.makeText(AdicionarItemActivity.this, "Preencha corretamente o campo quantidade!", Toast.LENGTH_SHORT).show();
                        } else {
                            item = new Item();

                            item.setIdProduto(produto.getIdProduto());
                            item.setQuantidade(quantidade);
                            item.setIdPromocao(1);
                            item.setIdPedido(idPedido);
                            item.setNomeProduto(produto.getNome());
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

        baixarProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.icFinalizarPedido) {
            if (listaProdutosPedido.size() > 0) {
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title(R.string.dialog_adicionar_item)
                        .items(R.array.itens)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                posicao = i;
                                new MaterialDialog.Builder(AdicionarItemActivity.this)
                                        .title(R.string.excluir)
                                        .content(R.string.pergunta)
                                        .negativeText(R.string.nao).positiveText(R.string.sim).callback(new MaterialDialog.ButtonCallback() {

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
                        }).positiveText(R.string.adicionar).callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                for (int i = 0; i < listaProdutosPedido.size(); i++) {

                                    Item it = new Item();
                                    it.setIdPedido(idPedido);
                                    it.setIdPromocao(1);
                                    it.setIdProduto(listaProdutosPedido.get(i).getIdProduto());
                                    it.setQuantidade(listaProdutosPedido.get(i).getQuantidade());
                                    it.setValorTeste(String.valueOf(listaProdutosPedido.get(i).getValorItem()));

                                    new DAOItem().inserirItem(it, new ItemListener() {
                                        @Override
                                        public void onItem(boolean retorno) {
                                            if (retorno == true) {
                                                Toast.makeText(AdicionarItemActivity.this, "Produtos Adicionado!", Toast.LENGTH_SHORT).show();
                                                listaProdutosPedido.clear();
                                                //dial.dismiss();
                                            } else {
                                                Toast.makeText(AdicionarItemActivity.this, "Não foi possível concluir a alteração. Tente Novamente!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }).show();

                list = dialog.getListView();
                itemAdapter = new ItemAdapter(listaProdutosPedido);
                list.setAdapter(itemAdapter);

            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void baixarProdutos() {
        progressBar.setVisibility(View.VISIBLE);
        new DAOProduto().buscarTodosProdutos(idRestaurante, new ProdutosListener() {
            @Override
            public void onProduto(List<Produto> produtos) {
                if (produtos != null || produtos.size() > 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    listaProdutos = produtos;
                    atualizarLista();
                }
            }
        });
    }

    public void atualizarLista() {
        produtosRestauranteAdapter = new ProdutosRestauranteAdapter(listaProdutos);
        lstProdutos.setAdapter(produtosRestauranteAdapter);
    }
}
