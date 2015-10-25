package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Item;

import java.util.List;

public class ItemAdapter extends BaseAdapter {

    List<Item> mItems;

    public static class ViewHolder{
        public final TextView txtNomeProduto;
        public final TextView txtQuantidade;
        public final TextView txtValorTotal;

        ViewHolder (View v){
            txtNomeProduto = (TextView)v.findViewById(R.id.txtNomeProduto);
            txtQuantidade = (TextView)v.findViewById(R.id.txtQuantidade);
            txtValorTotal = (TextView)v.findViewById(R.id.txtValorTotal);
        }
    }

    public ItemAdapter(List<Item> mItems) {
        this.mItems = mItems;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Item item = mItems.get(i);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_lista_pedido_produto, null);
        }

        ViewHolder holder = new ViewHolder(view);

        holder.txtNomeProduto.setText(item.nomeProduto);
        holder.txtQuantidade.setText("Quantidade: " + String.valueOf(item.quantidade));
        holder.txtValorTotal.setText("Valor Total: R$:" + Float.toString(item.valorItem) + "0");

        return view;
    }
}
