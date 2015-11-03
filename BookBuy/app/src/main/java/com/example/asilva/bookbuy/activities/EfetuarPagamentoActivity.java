package com.example.asilva.bookbuy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.asilva.bookbuy.R;
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

    Button button;
    int idPedido, idRestaurante;
    List<PagSeguroItem> shoppingCart = new ArrayList<>();
    float valorItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);

        button = (Button) findViewById(R.id.button);

        idPedido = getIntent().getIntExtra("idPedido", 1);
        idRestaurante = getIntent().getIntExtra("idRestaurante", 1);

        baixarItens();

        final ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(false);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final PagSeguroFactory pagseguro = PagSeguroFactory.instance();
                PagSeguroPhone buyerPhone = pagseguro.phone(PagSeguroAreaCode.DDD81, "998187427");
                PagSeguroBuyer buyer = pagseguro.buyer("Ricardo Ferreira", "14/02/1978", "15061112000", "test@email.com.br", buyerPhone);
                PagSeguroAddress buyerAddress = pagseguro.address("Av. Boa Viagem", "51", "Apt201", "Boa Viagem", "51030330", "Recife", PagSeguroBrazilianStates.PERNAMBUCO);
                PagSeguroShipping buyerShippingOption = pagseguro.shipping(PagSeguroShippingType.PAC, buyerAddress);
                PagSeguroCheckout checkout = pagseguro.checkout("Ref0001", shoppingCart, buyer, buyerShippingOption);
                new PagSeguroPayment(EfetuarPagamentoActivity.this).pay(checkout.buildCheckoutXml());
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
                    for (int i = 0; i < itens.size(); i++) {
                        final PagSeguroFactory pagseguro = PagSeguroFactory.instance();

                        String id = String.valueOf(i + 1);

                        valorItem = itens.get(i).getValorItem() / itens.get(i).getQuantidade();

                        shoppingCart.add(pagseguro.item(id, itens.get(i).getNomeProduto(), BigDecimal.valueOf(valorItem),
                                itens.get(i).getQuantidade(), 300));
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(this, MinhasComprasActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}