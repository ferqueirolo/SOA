package com.example.changosmart.listasCompras;

import com.example.changosmart.productos.Producto;

public class LineaCompra {
    private Producto producto;
    private int cantAComprar,
                cantComprada;

    public LineaCompra(Producto producto, int cantAComprar, int cantComprada) {
        this.producto = producto;
        this.cantAComprar = cantAComprar;
        this.cantComprada = cantComprada;
    }
}
