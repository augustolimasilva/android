package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;


import com.example.asilva.bookbuy.callbacks.RestaurantesListener;
import com.example.asilva.bookbuy.basicas.Restaurante;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAORestaurante {

    private static final String URL = "http://52.25.165.254:8080/WSbookbuy/services/RestauranteDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    List<Restaurante> listaRes = new ArrayList<Restaurante>();

    private static final String BUSCAR_TODOS = "buscarTodosRestaurantes";

    public void buscarTodosRestaurantes(RestaurantesListener listener) {
        new RestaurantesTask(listener).execute();
    }

    class RestaurantesTask extends AsyncTask<Void, Void, List<Restaurante>>{

        private final RestaurantesListener listener;

        public RestaurantesTask(final RestaurantesListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Restaurante> doInBackground(Void... params) {
            listaRes = new ArrayList<Restaurante>();

            SoapObject buscarRestaurantes = new SoapObject(NAMESPACE, BUSCAR_TODOS);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarRestaurantes);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    Restaurante res = new Restaurante();

                    res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                    res.setNome(resposta.getProperty("nome").toString());
                    res.setTelefone(resposta.getProperty("telefone").toString());
                    res.setEndereco(resposta.getProperty("rua").toString());
                    res.setBairro(resposta.getProperty("bairro").toString());
                    res.setLatitude((Float.parseFloat(resposta.getProperty("latitude").toString())));
                    res.setLongitude((Float.parseFloat(resposta.getProperty("longitude").toString())));

                    listaRes.add(res);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        Restaurante res = new Restaurante();

                        res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                        res.setNome(resposta.getProperty("nome").toString());
                        res.setTelefone(resposta.getProperty("telefone").toString());
                        res.setEndereco(resposta.getProperty("rua").toString());
                        res.setBairro(resposta.getProperty("bairro").toString());
                        res.setLatitude((Float.parseFloat(resposta.getProperty("latitude").toString())));
                        res.setLongitude((Float.parseFloat(resposta.getProperty("longitude").toString())));

                        listaRes.add(res);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaRes;
        }
        @Override
        protected void onPostExecute(final List<Restaurante> restaurantes) {
            listener.onRestaurante(restaurantes);
        }
    }
}
