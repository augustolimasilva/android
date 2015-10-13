package com.example.asilva.bookbuy.callbacks;

import com.example.asilva.bookbuy.basicas.Reserva;

import java.util.List;

public interface ReservasClienteListener {
    void onReserva(List<Reserva> reservas);
}
