package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAOProduto {

    private static final String URL = "http://52.25.38.52:8080/WSbookbuy/services/ProdutoDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    List<Produto> listaProdutos = new ArrayList<Produto>();

    private static final String BUSCAR_TODOS = "buscarTodosProdutos";

    public void buscarTodosProdutos(int idRestaurante, ProdutosListener listener) {
        new ProdutosTask(listener).execute(idRestaurante);
    }

    class ProdutosTask extends AsyncTask<Integer, Void, List<Produto>>{

        private final ProdutosListener listener;

        private ProdutosTask(final ProdutosListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Produto> doInBackground(Integer... params) {
            listaProdutos = new ArrayList<Produto>();

            SoapObject buscarProdutos = new SoapObject(NAMESPACE, BUSCAR_TODOS);
            buscarProdutos.addProperty("idRestaurante", params[0]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarProdutos);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Produto pro = new Produto();

                    pro.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                    pro.setDescricao(resposta.getProperty("descricao").toString());
                    pro.setIdProduto(Integer.parseInt(resposta.getProperty("idProduto").toString()));
                    pro.setSituacao(resposta.getProperty("situacao").toString());
                    pro.setValorProduto(Float.parseFloat(resposta.getProperty("valorProduto").toString()));

                    listaProdutos.add(pro);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Produto pro = new Produto();

                        pro.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                        pro.setDescricao(resposta.getProperty("descricao").toString());
                        pro.setIdProduto(Integer.parseInt(resposta.getProperty("idProduto").toString()));
                        pro.setSituacao(resposta.getProperty("situacao").toString());
                        pro.setValorProduto(Float.parseFloat(resposta.getProperty("valorProduto").toString()));

                        listaProdutos.add(pro);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaProdutos;
        }

        @Override
        protected void onPostExecute(final List<Produto> produtos) {
            listener.onProduto(produtos);
        }
    }
}
