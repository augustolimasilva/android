package com.example.asilva.bookbuy.util;


import android.os.AsyncTask;
import android.util.Log;

import com.example.asilva.bookbuy.basicas.Rota;
import com.example.asilva.bookbuy.callbacks.RotaListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BaixarRota {

    public void baixarRota(float latitude, float longitude, float latRes, float longRes, RotaListener listener) {
        new BaixarRotaTask(listener).execute(latitude, longitude, latRes, longRes);
    }

    class BaixarRotaTask extends AsyncTask<Float, Void, Rota> {

        private final RotaListener listener;

        public BaixarRotaTask(final RotaListener listener) {
            this.listener = listener;
        }

        @Override
        protected Rota doInBackground(Float... params) {

            Rota rota = null;

            try {

                rota = new Rota();

                float latitude = params[0];
                float longitude = params[1];
                float latRes = params[2];
                float longRes = params[3];

                URL url = new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "%20" + longitude + "&destinations=" + latRes + "%20" + longRes + "&mode=driving&language=pt-BR&sensor=false");
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                conexao.setReadTimeout(10 * 1000); // 10 segundos
                conexao.setConnectTimeout(15 * 1000); // 15 segundos
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

              //  if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    byte[] bytes = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int lidos;

                    while ((lidos = conexao.getInputStream().read(bytes)) > 0) {
                        baos.write(bytes, 0, lidos);
                    }

                    String teste = new String(baos.toByteArray());

                    JSONObject jObj = new JSONObject(teste);
                    JSONArray rowsArray = jObj.getJSONArray("rows");
                    JSONObject rows = rowsArray.getJSONObject(0);

                    JSONArray elementsArray = rows.getJSONArray("elements");
                    JSONObject newDisTimeOb = elementsArray.getJSONObject(0);

                    JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                    String distancia = distOb.getString("text");
                    JSONObject distOb2 = newDisTimeOb.getJSONObject("duration");
                    String tempo = distOb2.getString("text");

                    rota.setDistancia(distancia);
                    rota.setTempo(tempo);
            //    }

                return rota;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final Rota rota) {
            listener.onRota(rota);
        }
    }
}
