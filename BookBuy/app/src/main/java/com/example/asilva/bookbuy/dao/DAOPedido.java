package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.callbacks.PedidoListener;

public class DAOPedido {

    private static final String URL = "http://52.25.165.254:8080/WSbookbuy/services/PedidoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";

    int idPedido = 0;

    private static final String INSERIR = "inserirPedido";

    public int inserirPedido(Pedido pedido, PedidoListener listener) {
        new InserirPedidoTask(listener).execute(pedido);
        return idPedido;
    }

    class InserirPedidoTask extends AsyncTask<Pedido, Void, Integer>{

        private final PedidoListener listener;

        public InserirPedidoTask(final PedidoListener listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(Pedido... params) {
            return null;
        }
    }
}
