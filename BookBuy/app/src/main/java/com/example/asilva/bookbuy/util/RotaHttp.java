package com.example.asilva.bookbuy.util;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.asilva.bookbuy.basicas.Rota;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RotaHttp {

    public static Rota downloadRota(float latitude, float longitude, float latRes, float longRes) {

        Rota rota = null;

        try {
            URL url = new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "%20" + longitude + "&destinations=" + latRes + "%20" + longRes + "&mode=driving&language=pt-BR&sensor=false");
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

            conexao.setReadTimeout(10 * 1000); // 10 segundos
            conexao.setConnectTimeout(15 * 1000); // 15 segundos
            conexao.setRequestMethod("GET");
            conexao.setDoInput(true);
            conexao.connect();

            if (conexao.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String jsonString = toString(conexao.getInputStream());
                return rota = parseJson(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rota;
    }

    public static Rota parseJson(String jsonString) {
        Rota rota = new Rota();

        try {
            JSONObject jObj = new JSONObject(jsonString);
            JSONArray rowsArray = jObj.getJSONArray("rows");
            JSONObject rows = rowsArray.getJSONObject(0);

            JSONArray elementsArray = rows.getJSONArray("elements");
            JSONObject newDisTimeOb = elementsArray.getJSONObject(0);

            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            String distancia = distOb.getString("text");
            Log.d("Distance text&value :", distOb.getString("text") + distancia);

            JSONObject distOb2 = newDisTimeOb.getJSONObject("duration");
            String tempo = distOb2.getString("text");
            Log.d("Distance text&value :", distOb2.getString("text") + tempo);

            rota.setDistancia(distancia);
            rota.setTempo(tempo);

        } catch (JSONException e) {
            e.printStackTrace();
            rota = null;
        }
        return rota;
    }

    private static String toString(InputStream is) throws IOException {

        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }
}
