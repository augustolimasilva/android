package com.example.asilva.bookbuy.dao;

import android.os.AsyncTask;

import com.example.asilva.bookbuy.basicas.Rate;
import com.example.asilva.bookbuy.callbacks.AdicionarRateListener;
import com.example.asilva.bookbuy.callbacks.RateListener;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DAORate {

    private static final String URL = "http://52.32.87.153:8080/WSbookbuy/services/RateDAO?wsdl";
    private static final String NAMESPACE = "http://DAO";

    private static final String BUSCAR_RATE = "buscarRateUsuarioRestaurante";
    public static final String INSERIR_RATE = "inserirRate";

    boolean retorno = false;
    float rateTotal = 0f;

    public void pesquisarRateUsuarioRestaurante(int idRestaurante, int idCliente, RateListener listener){
        new PesquisarRateUsuarioRestauranteTask(listener).execute(idRestaurante, idCliente);
    }

    public void adicionarRate(Rate rate, AdicionarRateListener listener){
        new AdicionarRateTask(listener).execute(rate);
    }

    class AdicionarRateTask extends AsyncTask<Rate, Void, Float>{

        private final AdicionarRateListener listener;

        private AdicionarRateTask(final AdicionarRateListener  listener) {
            this.listener = listener;
        }

        @Override
        protected Float doInBackground(Rate... params) {
            SoapObject rat = new SoapObject(NAMESPACE, "rate");

            Rate rate= params[0];

            rat.addProperty("idRate", rate.getIdRate());
            rat.addProperty("rate", rate.getRate());
            rat.addProperty("idCliente", rate.getIdCliente());
            rat.addProperty("idRestaurante", rate.getIdRestaurante());

            SoapObject inserirRate = new SoapObject(NAMESPACE, INSERIR_RATE);

            inserirRate.addSoapObject(rat);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(inserirRate);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);
            try {
                http.call("urn:" + INSERIR_RATE, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return rateTotal =  Float.parseFloat(resposta.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return 0f;
            }
        }

        @Override
        protected void onPostExecute(final Float rateTotal) {
            listener.onAdicionar(rateTotal);
        }
    }

    class PesquisarRateUsuarioRestauranteTask extends AsyncTask<Integer, Void, Boolean>{

        private final RateListener listener;

        private PesquisarRateUsuarioRestauranteTask(final RateListener  listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            SoapObject buscarRate = new SoapObject(NAMESPACE, BUSCAR_RATE);
            buscarRate.addProperty("idRestaurante", params[0]);
            buscarRate.addProperty("idCliente", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

            envelope.setOutputSoapObject(buscarRate);

            envelope.implicitTypes = true;

            HttpTransportSE http = new HttpTransportSE(URL);

            try {

                http.call("urn:" + BUSCAR_RATE, envelope);

                SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

                return retorno = Boolean.parseBoolean(resposta.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return retorno;
            }
        }

        @Override
        protected void onPostExecute(final Boolean retorno) {
            listener.onRate(retorno);
        }
    }
}
