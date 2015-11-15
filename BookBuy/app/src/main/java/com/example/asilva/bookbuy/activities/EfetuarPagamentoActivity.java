package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ItemPedidoAdapter;
import com.example.asilva.bookbuy.basicas.Item;
import com.example.asilva.bookbuy.callbacks.ItensPedidoListener;
import com.example.asilva.bookbuy.dao.DAOItem;
import com.example.asilva.bookbuy.pagseguro.AppUtil;
import com.example.asilva.bookbuy.pagseguro.PagSeguroAddress;
import com.example.asilva.bookbuy.pagseguro.PagSeguroAreaCode;
import com.example.asilva.bookbuy.pagseguro.PagSeguroBrazilianStates;
import com.example.asilva.bookbuy.pagseguro.PagSeguroBuyer;
import com.example.asilva.bookbuy.pagseguro.PagSeguroCheckout;
import com.example.asilva.bookbuy.pagseguro.PagSeguroFactory;
import com.example.asilva.bookbuy.pagseguro.PagSeguroItem;
import com.example.asilva.bookbuy.pagseguro.PagSeguroPayment;
import com.example.asilva.bookbuy.pagseguro.PagSeguroPhone;
import com.example.asilva.bookbuy.pagseguro.PagSeguroShipping;
import com.example.asilva.bookbuy.pagseguro.PagSeguroShippingType;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.List;

public class EfetuarPagamentoActivity extends AppCompatActivity {

    Button button, bttAdicionarItem;
    int idPedido, idRestaurante;
    List<PagSeguroItem> shoppingCart = new ArrayList<>();
    List<Item> itensPedido = new ArrayList<Item>();
    float valorItem;
    String data, nomeRestaurante, status;
    TextView txtData, txtStatus, txtNomeRestaurante, txtCodigo;
    ListView lstMeusItens;
    ItemPedidoAdapter itemPedidoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        button = (Button) findViewById(R.id.button);
        bttAdicionarItem = (Button) findViewById(R.id.bttAdicionarItem);
        txtData = (TextView)findViewById(R.id.txtData);
        txtStatus = (TextView)findViewById(R.id.txtStatus);
        txtNomeRestaurante = (TextView)findViewById(R.id.txtNomeRestaurante);
        txtCodigo = (TextView)findViewById(R.id.txtCodigo);
        lstMeusItens = (ListView)findViewById(R.id.lstMeusItens);

        idPedido = getIntent().getIntExtra("idPedido", 1);
        idRestaurante = getIntent().getIntExtra("idRestaurante", 1);
        data = getIntent().getStringExtra("data");
        status = getIntent().getStringExtra("status");
        nomeRestaurante = getIntent().getStringExtra("nomeRestaurante");

        if(status.equals("FECHADO")){
            button.setVisibility(View.INVISIBLE);
            bttAdicionarItem.setVisibility(View.INVISIBLE);
        }

        String dataFormatada = data.substring(8, 10) + "-" + data.substring(5, 7) + "-" + data.substring(0, 4) +
                " " + data.substring(11,13) + ":" + data.substring(14,16);

        txtData.setText("Data: " + dataFormatada);
        txtNomeRestaurante.setText(nomeRestaurante);
        txtStatus.setText("Pagamento: " + status);
        txtCodigo.setText("Cód. Pedido: " + idPedido);

        baixarItens();

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                incluirItensPagseguro();
                final PagSeguroFactory pagseguro = PagSeguroFactory.instance();
                PagSeguroPhone buyerPhone = pagseguro.phone(PagSeguroAreaCode.DDD81, "998187427");
                PagSeguroBuyer buyer = pagseguro.buyer("Ricardo Ferreira", "14/02/1978", "15061112000", "test@email.com.br", buyerPhone);
                PagSeguroAddress buyerAddress = pagseguro.address("Av. Boa Viagem", "51", "Apt201", "Boa Viagem", "51030330", "Recife", PagSeguroBrazilianStates.PERNAMBUCO);
                PagSeguroShipping buyerShippingOption = pagseguro.shipping(PagSeguroShippingType.PAC, buyerAddress);
                PagSeguroCheckout checkout = pagseguro.checkout("Ref0001", shoppingCart, buyer, buyerShippingOption);
                new PagSeguroPayment(EfetuarPagamentoActivity.this).pay(checkout.buildCheckoutXml());
            }
        });

        bttAdicionarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent it = new Intent(EfetuarPagamentoActivity.this, AdicionarItemActivity.class);
                it.putExtra("idPedido", idPedido);
                it.putExtra("idRestaurante", idRestaurante);
                startActivity(it);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // se foi uma tentativa de pagamento
            if (requestCode == PagSeguroPayment.PAG_SEGURO_REQUEST_CODE) {
                // exibir confirmação de cancelamento
                final String msg = getString(R.string.transaction_cancelled);
                AppUtil.showConfirmDialog(this, msg, null);
            }
        } else if (resultCode == RESULT_OK) {
            // se foi uma tentativa de pagamento
            if (requestCode == PagSeguroPayment.PAG_SEGURO_REQUEST_CODE) {
                // exibir confirmação de sucesso
                final String msg = getString(R.string.transaction_succeded);
                AppUtil.showConfirmDialog(this, msg, null);
            }
        } else if (resultCode == PagSeguroPayment.PAG_SEGURO_REQUEST_CODE) {
            switch (data.getIntExtra(PagSeguroPayment.PAG_SEGURO_EXTRA, 0)) {
                case PagSeguroPayment.PAG_SEGURO_REQUEST_SUCCESS_CODE: {
                    final String msg = getString(R.string.transaction_succeded);
                    AppUtil.showConfirmDialog(this, msg, null);
                    break;
                }
                case PagSeguroPayment.PAG_SEGURO_REQUEST_FAILURE_CODE: {
                    final String msg = getString(R.string.transaction_error);
                    AppUtil.showConfirmDialog(this, msg, null);
                    break;
                }
                case PagSeguroPayment.PAG_SEGURO_REQUEST_CANCELLED_CODE: {
                    final String msg = getString(R.string.transaction_cancelled);
                    AppUtil.showConfirmDialog(this, msg, null);
                    break;
                }
            }
        }
    }

    public void baixarItens() {
        new DAOItem().buscarItensPedido(idPedido, idRestaurante, new ItensPedidoListener() {
            @Override
            public void onItens(List<Item> itens) {
                if (itens != null) {
                    itensPedido = itens;
                    listarItens();
                }
            }
        });
    }

    public void incluirItensPagseguro(){
        for (int i = 0; i < itensPedido.size(); i++) {

            final PagSeguroFactory pagseguro = PagSeguroFactory.instance();

            String id = String.valueOf(i + 1);

            valorItem = itensPedido.get(i).getValorItem() / itensPedido.get(i).getQuantidade();

            shoppingCart.add(pagseguro.item(id, itensPedido.get(i).getNomeProduto(), BigDecimal.valueOf(valorItem),
                    itensPedido.get(i).getQuantidade(), 300));
        }
    }

    public void listarItens(){
        itemPedidoAdapter = new ItemPedidoAdapter(itensPedido);
        lstMeusItens.setAdapter(itemPedidoAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MinhasComprasActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}