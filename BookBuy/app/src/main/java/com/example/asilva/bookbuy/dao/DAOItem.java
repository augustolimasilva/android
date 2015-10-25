package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Item;
import com.example.asilva.bookbuy.callbacks.ItemListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DAOItem {

    private static final String URL = "http://52.25.38.52:8080/WSbookbuy/services/PedidoProdutoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    private static final String INSERIR_ITEM = "inserirPedidoProduto";

    boolean retorno = false;

    public boolean inserirItem(Item item, ItemListener listener){
        new InserirItemTask(listener).execute(item);
        return retorno;
    }

    class InserirItemTask extends AsyncTask<Item, Void, Boolean>{

        private final ItemListener listener;

        public InserirItemTask(final ItemListener listener){
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Item... params) {
            SoapObject it = new SoapObject(NAMESPACE, "item");

            Item item = params[0];

            it.addProperty("idItem", item.getIdItem());
            it.addProperty("quantidade", item.getQuantidade());
            it.addProperty("valorItem", item.getValorItem());
            it.addProperty("idPedido", item.getIdPedido());
            it.addProperty("idProduto", item.getIdProduto());
            it.addProperty("idPromocao", item.getIdPromocao());

            SoapObject inserirPedido = new SoapObject(NAMESPACE, INSERIR_ITEM);

            inserirPedido.addSoapObject(it);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirPedido);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR_ITEM, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return retorno;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.onItem(retorno);
        }
    }
}
