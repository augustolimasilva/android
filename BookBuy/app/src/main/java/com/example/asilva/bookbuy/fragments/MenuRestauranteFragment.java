package com.example.asilva.bookbuy.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Restaurante;
public class MenuRestauranteFragment extends Fragment {

    private Restaurante restaurante;

    public static MenuRestauranteFragment newInstance(Restaurante restaurante) {
        MenuRestauranteFragment menuRestauranteFragment = new MenuRestauranteFragment();

        Bundle args = new Bundle();
        args.putSerializable("restaurante", restaurante);
        menuRestauranteFragment.setArguments(args);

        return menuRestauranteFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            restaurante = (Restaurante) getArguments().getSerializable("restaurante");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_restaurante, container, false);

        TextView txtNome = (TextView) view.findViewById(R.id.txtNome);
        TextView txtRua = (TextView) view.findViewById(R.id.txtEndereco);
        TextView txtBairro = (TextView) view.findViewById(R.id.txtBairro);
        TextView txtTelefone = (TextView) view.findViewById(R.id.txtTelefone);

        txtNome.setText(restaurante.nome);
        txtRua.setText(restaurante.endereco);
        txtBairro.setText(restaurante.bairro);
        txtTelefone.setText(restaurante.telefone);

        return view;
    }

}
