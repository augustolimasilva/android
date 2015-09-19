package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;


import com.example.asilva.bookbuy.basicas.Restaurante;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DAORestaurante {

    private static final String URL = "http://54.149.96.214:8080/bookbuyWS/services/RestauranteDAO?wsdl";
    private static final String NAMESPACE = "http://bookbuyWS";

    private static final String BUSCAR_TODOS = "buscarTodosRestaurantes";

    public List<Restaurante> BuscarTodosRestaurantes() {

        List<Restaurante> listaRes = new ArrayList<Restaurante>();

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

            } else {
                Vector<SoapObject> retorno = (Vector<SoapObject>) envelope.getResponse();

                for (SoapObject resposta : retorno) {

                    Restaurante res = new Restaurante();

                    res.setIdRestaurante(Integer.parseInt(resposta.getProperty("idRestaurante").toString()));
                    res.setNome(resposta.getProperty("nome").toString());
                    res.setTelefone(resposta.getProperty("telefone").toString());

                    listaRes.add(res);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return listaRes;
    }
}
