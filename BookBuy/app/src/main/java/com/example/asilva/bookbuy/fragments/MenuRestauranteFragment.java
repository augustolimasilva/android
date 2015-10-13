package com.example.asilva.bookbuy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.example.asilva.bookbuy.callbacks.ProdutosListener;
import com.example.asilva.bookbuy.dao.DAOCliente;
import com.example.asilva.bookbuy.dao.DAOProduto;
import com.example.asilva.bookbuy.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuRestauranteFragment extends Fragment {

    private Restaurante restaurante;
    int idRestaurante;
    String telefone, email, rua, numero;
    TextView txtRua, txtEmail, txtTelefone;

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
        txtEmail.setText("Email: " + email);

        return view;
    }
}
