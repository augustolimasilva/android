package com.example.asilva.bookbuy.callbacks;

import com.example.asilva.bookbuy.basicas.Pedido;

import java.util.List;

public interface PedidoClienteListener {

    void onPedidoCliente(List<Pedido> pedidos);
}
