package com.example.asilva.bookbuy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Rota;
import com.example.asilva.bookbuy.callbacks.RotaListener;
import com.example.asilva.bookbuy.util.BaixarRota;

public class MenuRestauranteFragment extends Fragment {

    int idRestaurante, idCliente;
    String telefone, email, rua, numero;
    TextView txtRua, txtEmail, txtTelefone, txtDistancia;
    float latitude, longitude, latRes, longRes, rate;
    RatingBar rttRestaurante;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_restaurante, container, false);

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
        rate = prefs.getFloat("rate", 1);

        SharedPreferences prefsCliente = this.getActivity().getSharedPreferences("meus_dados", 0);
        idCliente = prefsCliente.getInt("id", 1);

        buscarDistancia();

        txtRua = (TextView) view.findViewById(R.id.txtRua);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtTelefone = (TextView) view.findViewById(R.id.txtTelefone);
        txtDistancia  = (TextView)view.findViewById(R.id.txtDistancia);
        rttRestaurante = (RatingBar)view.findViewById(R.id.rttRestaurante);

        final LayerDrawable layerDrawable = (LayerDrawable) rttRestaurante.getProgressDrawable();

        layerDrawable.getDrawable(0).setColorFilter(ContextCompat.getColor(getActivity(), R.color.white_transparent), PorterDuff.Mode.SRC_ATOP);
        layerDrawable.getDrawable(1).setColorFilter(ContextCompat.getColor(getActivity(), R.color.rate_yellow), PorterDuff.Mode.SRC_ATOP);
        layerDrawable.getDrawable(2).setColorFilter(ContextCompat.getColor(getActivity(), R.color.rate_yellow), PorterDuff.Mode.SRC_ATOP);

        rttRestaurante.setRating(rate);

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
        txtEmail.setText("Email: " + email);

        return view;
    }

    public void buscarDistancia(){
        new BaixarRota().baixarRota(latitude, longitude, latRes, longRes, new RotaListener() {
            @Override
            public void onRota(Rota rota) {
                if(rota != null){
                   txtDistancia.setText("Distância: " + rota.getDistancia() + " " +  "-" + " " + rota.getTempo());

                    SharedPreferences prefs = getActivity().getSharedPreferences("dados_rota", 0);
                    SharedPreferences.Editor editor = prefs.edit();

                    editor.putString("distancia", rota.getDistancia());
                    editor.putString("tempo", rota.getTempo());

                    editor.commit();
                }
            }
        });
    }

}
