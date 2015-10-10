package com.example.asilva.bookbuy.callbacks;

import com.example.asilva.bookbuy.basicas.RestauranteTipo;
import com.example.asilva.bookbuy.basicas.TipoRestaurante;

import java.util.List;

/**
 * Created by augusto on 09/10/2015.
 */
public interface RestaurantesPorTipoListener {

    void onRestaurantesPorTipo(List<RestauranteTipo> restaurantesPorTipo);
}
