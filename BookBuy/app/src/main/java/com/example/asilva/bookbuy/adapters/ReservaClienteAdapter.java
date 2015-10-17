package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservaClienteAdapter extends BaseAdapter {

    List<Reserva> mReservas;

    public static class ViewHolder{
        public final TextView txtNomeRestaurante;
        public final TextView txtDataReserva;
        public final TextView txtQtdPessoas;

        ViewHolder (View v){
            txtNomeRestaurante = (TextView)v.findViewById(R.id.txtNomeRestaurante);
            txtDataReserva = (TextView)v.findViewById(R.id.txtDataReserva);
            txtQtdPessoas = (TextView)v.findViewById(R.id.txtQtdPessoas);
        }
    }

    public ReservaClienteAdapter(List<Reserva> mReservas) {
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
        String data = reserva.getDataHora().substring(8,10) + "-" + reserva.getDataHora().substring(5,7) + "-" + reserva.getDataHora().substring(0,4) +
                      " " + reserva.getDataHora().substring(11,13) + ":" + reserva.getDataHora().substring(14,16);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_lista_minhas_reservas, null);
        }

        ViewHolder holder = new ViewHolder(view);

        holder.txtNomeRestaurante.setText(reserva.getStatus());
        holder.txtDataReserva.setText("Data: " + data);
        holder.txtQtdPessoas.setText("Qtd de Pessoas: " + String.valueOf(reserva.getQtdPessoas()));

        return view;
    }
}
