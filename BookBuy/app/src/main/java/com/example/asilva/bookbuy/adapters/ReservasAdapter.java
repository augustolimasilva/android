package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Reserva;

import java.util.List;

public class ReservasAdapter extends BaseAdapter {

    List<Reserva> mReservas;

    public static class ViewHolder{
        public final TextView txtTitulo;

        ViewHolder (View v){
            txtTitulo = (TextView)v.findViewById(R.id.txtTitulo);
        }
    }

    public ReservasAdapter(List<Reserva> mReservas) {
        this.mReservas = mReservas;
    }

    @Override
    public int getCount() {
        return mReservas.size();
    }

    @Override
    public Object getItem(int i) {
        return mReservas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Reserva reserva = mReservas.get(i);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item, null);
        }

        ViewHolder holder = new ViewHolder(view);

        holder.txtTitulo.setText(reserva.getDataHora().substring(0, 16));

        return view;
    }
}
