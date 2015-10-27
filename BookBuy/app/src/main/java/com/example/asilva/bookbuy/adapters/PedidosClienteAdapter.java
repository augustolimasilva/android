package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Pedido;
import com.example.asilva.bookbuy.basicas.Produto;

import java.util.List;

public class PedidosClienteAdapter extends BaseAdapter {

    List<Pedido> mPedidos;

    public static class ViewHolder{
        public final TextView txtNomeRestaurante;
        public final TextView txtDataHora;
        public final TextView txtStatus;

        ViewHolder (View v){
            txtNomeRestaurante = (TextView)v.findViewById(R.id.txtNomeRestaurante);
            txtDataHora = (TextView)v.findViewById(R.id.txtDataHora);
            txtStatus = (TextView)v.findViewById(R.id.txtStatus);
        }
    }

    public PedidosClienteAdapter(List<Pedido> mPedidos) {
        this.mPedidos = mPedidos;
    }

    @Override
    public int getCount() {
        return mPedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return mPedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Pedido pedido = mPedidos.get(i);
        String data = pedido.getDataHora().substring(8,10) + "-" + pedido.getDataHora().substring(5,7) + "-" + pedido.getDataHora().substring(0,4) +
                " " + pedido.getDataHora().substring(11,13) + ":" + pedido.getDataHora().substring(14,16);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_lista_meus_pedidos, null);
        }

        ViewHolder holder = new ViewHolder(view);

        holder.txtNomeRestaurante.setText(pedido.getNomeRestaurante());
        holder.txtDataHora.setText("Data: " + data);
        holder.txtStatus.setText("Status: " + pedido.getStatus());

        return view;
    }
}
