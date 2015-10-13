package com.example.asilva.bookbuy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.asilva.bookbuy.R;
import com.example.asilva.bookbuy.basicas.Produto;

import java.util.List;

public class ProdutosRestauranteAdapter extends BaseAdapter{

    List<Produto> mProdutos;

    public static class ViewHolder{
        public final TextView txtNomeProduto;
        public final TextView txtValorProduto;

        ViewHolder (View v){
            txtNomeProduto = (TextView)v.findViewById(R.id.txtNomeProduto);
            txtValorProduto = (TextView)v.findViewById(R.id.txtValorProduto);
        }
    }

    public ProdutosRestauranteAdapter(List<Produto> mProdutos) {
        this.mProdutos= mProdutos;
    }

    @Override
    public int getCount() {
        return mProdutos.size();
    }

    @Override
    public Object getItem(int i) {
        return mProdutos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Produto produto = mProdutos.get(i);

        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_lista, null);
        }

        ViewHolder holder = new ViewHolder(view);

        String valorProduto = Float.toString(produto.valorProduto);

        holder.txtNomeProduto.setText(produto.descricao);
        holder.txtValorProduto.setText("R$: " + valorProduto + 0);

        return view;
    }
}
