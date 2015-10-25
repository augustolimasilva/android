package com.example.asilva.bookbuy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.adapters.ProdutosRestauranteAdapter;
import com.example.asilva.bookbuy.basicas.Produto;
import com.example.asilva.bookbuy.basicas.Restaurante;
import com.example.asilva.bookbuy.basicas.Rota;
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.callbacks.RotaListener;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.example.asilva.bookbuy.dao.DAOProduto;
import com.example.asilva.bookbuy.util.BaixarRota;
import com.example.asilva.bookbuy.util.RotaHttp;
import com.example.asilva.bookbuy.util.Util;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuRestauranteFragment extends Fragment {

    int idRestaurante;
    String telefone, email, rua, numero;
    TextView txtRua, txtEmail, txtTelefone, txtDistancia;
    double dist;
    float latitude, longitude, latRes, longRes;
    Rota rot;
   // RotaTask rotaTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_restaurante, container, false);

        //rotaTask = new RotaTask();
        //rotaTask.execute();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("dados_restaurante", 0);
        telefone = prefs.getString("telefone", "81999999999");
        email = prefs.getString("email", "erro@gmail.com");
        rua = prefs.getString("rua", "Erro");
        numero = prefs.getString("numero", "19");
        idRestaurante = prefs.getInt("idRestaurante", 1);
        latitude = prefs.getFloat("latitude", 1);
        longitude = prefs.getFloat("longitude", 1);
        latRes = prefs.getFloat("latitudeRes", 1);
        longRes = prefs.getFloat("longitudeRes", 1);

        buscarDistancia();

        txtRua = (TextView) view.findViewById(R.id.txtRua);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtTelefone = (TextView) view.findViewById(R.id.txtTelefone);

        txtTelefone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + telefone));
                startActivity(intent);
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.setData(Uri.parse("mailto:" + email));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        txtRua.setText("Endereço: " + rua + "," + " " + numero);
        txtTelefone.setText("Telefone: " + telefone);
       // txtEmail.setText("Email: " + email);
        txtEmail.setText(rot.getDistancia());

        return view;
    }

    public void buscarDistancia(){
        new BaixarRota().baixarRota(latitude, longitude, latRes, longRes, new RotaListener() {
            @Override
            public void onRota(Rota rota) {
                if(rota != null){
                    rot = rota;
                }
            }
        });
    }
/*
    class RotaTask extends AsyncTask<Void, Void, Rota> {

        @Override
        protected Rota doInBackground(Void... params) {
            return rot = RotaHttp.downloadRota(latitude, longitude, latRes, longRes);
        }

        @Override
        protected void onPostExecute(Rota rota) {
            super.onPostExecute(rota);

            if(rota != null){
                rot = rota;
            }
        }
    }*/

}
