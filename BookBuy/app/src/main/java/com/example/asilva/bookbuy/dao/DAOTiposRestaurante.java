package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.RestauranteTipo;
import com.example.asilva.bookbuy.basicas.TipoRestaurante;
import com.example.asilva.bookbuy.callbacks.RestaurantesPorTipoListener;
import com.example.asilva.bookbuy.callbacks.TiposRestauranteListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAOTiposRestaurante {

    private static final String URL = "http://52.32.87.153:8080/WSbookbuy/services/TipoRestauranteDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";
    List<TipoRestaurante> listaTiposRes = new ArrayList<TipoRestaurante>();
    List<RestauranteTipo> listaResPorTipo = new ArrayList<RestauranteTipo>();

    private static final String BUSCAR_TODOS = "buscarTodosTiposRestaurante";
    private static final String BUSCAR_TODOS_RESTAURANTES_POR_TIPO = "buscarTodosRestaurantesPorTipo";

    public void buscarTodosTiposRestaurante(TiposRestauranteListener listener) {
        new TiposRestauranteTask(listener).execute();
    }

    public void buscarTodosRestaurantesPorTipo(RestaurantesPorTipoListener listener) {
        new RestaurantesPorTipoTask(listener).execute();
    }

    class TiposRestauranteTask extends AsyncTask<Void, Void, List<TipoRestaurante>>{

        private final TiposRestauranteListener listener;

        public TiposRestauranteTask(final TiposRestauranteListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<TipoRestaurante> doInBackground(Void... params) {
            listaTiposRes = new ArrayList<TipoRestaurante>();

            SoapObject buscarTiposRestaurante = new SoapObject(NAMESPACE, BUSCAR_TODOS);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarTiposRestaurante);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    TipoRestaurante tiposRes = new TipoRestaurante();

                    tiposRes.setIdTipo(Integer.parseInt(resposta.getProperty("idTipo").toString()));
                    tiposRes.setDescricao(resposta.getProperty("descricao").toString());

                    listaTiposRes.add(tiposRes);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        TipoRestaurante tiposRes = new TipoRestaurante();

                        tiposRes.setIdTipo(Integer.parseInt(resposta.getProperty("idTipo").toString()));
                        tiposRes.setDescricao(resposta.getProperty("descricao").toString());

                        listaTiposRes.add(tiposRes);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaTiposRes;
        }
        @Override
        protected void onPostExecute(final List<TipoRestaurante> tiposRestaurantes) {
            listener.onTiposRestaurante(tiposRestaurantes);
        }
    }

    class RestaurantesPorTipoTask extends AsyncTask<Void, Void, List<RestauranteTipo>>{

        private final RestaurantesPorTipoListener listener;

        public RestaurantesPorTipoTask(final RestaurantesPorTipoListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<RestauranteTipo> doInBackground(Void... params) {
            listaResPorTipo = new ArrayList<RestauranteTipo>();

            SoapObject buscarRestaurantesPorTipo = new SoapObject(NAMESPACE, BUSCAR_TODOS_RESTAURANTES_POR_TIPO);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarRestaurantesPorTipo);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {
                http.call("urn:" + BUSCAR_TODOS_RESTAURANTES_POR_TIPO, envelope);

                if (envelope.getResponse() instanceof SoapObject) {
                    SoapObject resposta = (SoapObject) envelope.getResponse();

                    RestauranteTipo resTipo = new RestauranteTipo();

                    resTipo.setIdTipo(Integer.parseInt(resposta.getProperty("idTipo").toString()));
                    resTipo.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                    listaResPorTipo.add(resTipo);

                } else {
                    Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                    for (SoapObject resposta : retorno) {

                        RestauranteTipo resTipo = new RestauranteTipo();

                        resTipo.setIdTipo(Integer.parseInt(resposta.getProperty("idTipo").toString()));
                        resTipo.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));

                        listaResPorTipo.add(resTipo);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return listaResPorTipo;
        }
        @Override
        protected void onPostExecute(final List<RestauranteTipo> restaurantesPorTipo) {
            listener.onRestaurantesPorTipo(restaurantesPorTipo);
        }
    }
}

