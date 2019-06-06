package com.example.changosmart.listasCompras;

import com.example.changosmart.productos.Producto;

public class LineaCompra {
    private Producto producto;
    private int cantidadAComprar,
                cantidadComprada = 0;

    public LineaCompra(Producto producto, int cantidadAComprar) {
        this.producto = producto;
        this.cantidadAComprar = cantidadAComprar;
        this.cantidadComprada = 0;
    }


    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidadAComprar() {
        return cantidadAComprar;
    }

    public void setCantidadAComprar(int cantidadAComprar) {
        this.cantidadAComprar = cantidadAComprar;
    }

    public int getCantidadComprada() {
        return cantidadComprada;
    }

    public void setCantidadComprada(int cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }
}
