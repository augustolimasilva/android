package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Item;
import com.example.asilva.bookbuy.callbacks.ItemListener;
import com.example.asilva.bookbuy.callbacks.ItensPedidoListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAOItem {

    private static final String URL = "http://52.32.87.153:8080/WSbookbuy/services/PedidoProdutoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";

    public static final String BUSCAR_ITENS_PEDIDO = "buscarTodos";
    private static final String INSERIR_ITEM = "inserirPedidoProduto";

    boolean retorno = false;
    List<Item> itens = new ArrayList<>();

    public boolean inserirItem(Item item, ItemListener listener){
        new InserirItemTask(listener).execute(item);
        return retorno;
    }

    public void buscarItensPedido(int idPedido, int idRestaurante, ItensPedidoListener listener){
        new BuscarItensPedidoTask(listener).execute(idPedido, idRestaurante);
    }

    class BuscarItensPedidoTask extends AsyncTask<Integer, Void, List<Item>>{

        private final ItensPedidoListener listener;

        public BuscarItensPedidoTask (final ItensPedidoListener listener){
            this.listener = listener;
        }

        @Override
        protected List<Item> doInBackground(Integer... params) {
            itens = new ArrayList<Item>();

            SoapObject buscarItens = new SoapObject(NAMESPACE, BUSCAR_ITENS_PEDIDO);
            buscarItens.addProperty("idPedido", params[0]);
            buscarItens.addProperty("idRestaurante", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarItens);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_ITENS_PEDIDO, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Item item = new Item();

                    item.setNomeProduto(resposta.getProperty("descricao").toString());
                    item.setIdItem(Integer.parseInt(resposta.getProperty("idItem").toString()));
                    item.setIdPedido(Integer.parseInt(resposta.getProperty("idPedido").toString()));
                    item.setIdProduto(Integer.parseInt(resposta.getProperty("idProduto").toString()));
                    item.setQuantidade(Integer.parseInt(resposta.getProperty("quantidade").toString()));
                    item.setValorItem(Float.parseFloat(resposta.getProperty("valorItem").toString()));

                    itens.add(item);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Item item = new Item();

                        item.setNomeProduto(resposta.getProperty("descricao").toString());
                        item.setIdItem(Integer.parseInt(resposta.getProperty("idItem").toString()));
                        item.setIdPedido(Integer.parseInt(resposta.getProperty("idPedido").toString()));
                        item.setIdProduto(Integer.parseInt(resposta.getProperty("idProduto").toString()));
                        item.setQuantidade(Integer.parseInt(resposta.getProperty("quantidade").toString()));
                        item.setValorItem(Float.parseFloat(resposta.getProperty("valorItem").toString()));

                        itens.add(item);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return itens;
        }

        @Override
        protected void onPostExecute(final List<Item> itens) {
            listener.onItens(itens);
        }
    }

    class InserirItemTask extends AsyncTask<Item, Void, Boolean>{

        private final ItemListener listener;

        public InserirItemTask(final ItemListener listener){
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Item... params) {
            SoapObject it = new SoapObject(NAMESPACE, "pedidoProduto");

            Item item = params[0];

            it.addProperty("idItem", item.getIdItem());
            it.addProperty("quantidade", item.getQuantidade());
            it.addProperty("valorItem", item.getValorTeste());
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
