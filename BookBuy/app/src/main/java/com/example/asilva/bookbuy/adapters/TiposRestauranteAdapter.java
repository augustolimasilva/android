package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.TipoRestaurante;

import java.util.List;

/**
 * Created by augusto on 09/10/2015.
 */
public class TiposRestauranteAdapter extends BaseAdapter {

    List<TipoRestaurante> mTipoRestaurante;

    public static class ViewHolder{
        public final TextView txtTitulo;

        ViewHolder (View v){
            txtTitulo = (TextView)v.findViewById(R.id.txtTitulo);
        }
    }

    public TiposRestauranteAdapter(List<TipoRestaurante> mTipoRestaurante) {
        this.mTipoRestaurante = mTipoRestaurante;
    }

    @Override
    public int getCount() {
        return mTipoRestaurante.size();
    }

    @Override
    public Object getItem(int i) {
        return mTipoRestaurante.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TipoRestaurante tipoRestaurante = mTipoRestaurante.get(i);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item, null);
        }

        ViewHolder holder = new ViewHolder(view);

        holder.txtTitulo.setText(tipoRestaurante.descricao);

        return view;
    }
}
