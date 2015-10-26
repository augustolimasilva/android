package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.callbacks.PedidoListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DAOPedido {

    private static final String URL = "http://52.10.208.222:8080/WSbookbuy/services/PedidoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";

    int idPedido = 0;

    private static final String INSERIR_PEDIDO = "inserirPedido";

    public int inserirPedido(Pedido pedido, PedidoListener listener) {
        new InserirPedidoTask(listener).execute(pedido);
        return idPedido;
    }

    class InserirPedidoTask extends AsyncTask<Pedido, Void, Integer> {

        private final PedidoListener listener;

        public InserirPedidoTask(final PedidoListener listener) {
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(Pedido... params) {
            SoapObject ped = new SoapObject(NAMESPACE, "pedido");

            Pedido pedido = params[0];

            ped.addProperty("idPedido", pedido.getIdPedido());
            ped.addProperty("dataHora", pedido.getDataHora());
            ped.addProperty("situacao", pedido.getSituacao());
            ped.addProperty("status", pedido.getStatus());
            ped.addProperty("tempoEstimado", pedido.getTempoEstimado());
            ped.addProperty("idCliente", pedido.getIdCliente());
            ped.addProperty("idMesa", pedido.getIdMesa());
            ped.addProperty("idRestaurante", pedido.getIdRestaurante());

            SoapObject inserirPedido = new SoapObject(NAMESPACE, INSERIR_PEDIDO);

            inserirPedido.addSoapObject(ped);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirPedido);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR_PEDIDO, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return idPedido = Integer.parseInt(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return idPedido;
            }
        }

        @Override
        protected void onPostExecute(final Integer idPedido) {
            listener.onPedido(idPedido);
        }
    }
}
