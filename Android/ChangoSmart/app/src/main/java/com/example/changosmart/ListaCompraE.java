package com.example.changosmart;



import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;


/**
 * Clase para crear la tabla ListaCompra
 * Se emplea usando ROOM library
 */

@Entity(tableName = "Lista_Compras")
public class ListaCompraE {
    // Si quiero cambiarle el nombre a la tabla @ ColumnInfo(name = "nombre_lista")
    //Tengo que tener al menos una clave primaria
    @PrimaryKey(autoGenerate=true)
    private int ID;

    private String nombre_Lista;
    private String supermercado;
    //private Date fecha_Actualizacion;

    public ListaCompraE (String nombre_Lista, String supermercado){
        this.nombre_Lista = nombre_Lista;
        this.supermercado = supermercado;
        //this.fecha_Actualizacion = Calendar.getInstance().getTime();
    }

    // SETTERS
    public void setNombre_Lista(String nombre_Lista) {
        nombre_Lista = nombre_Lista;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    /*
    public void setFecha_Actualizacion(Date fecha_Actualizacion) {
        this.fecha_Actualizacion = fecha_Actualizacion;
    }
    */
    // GETTERS
    public String getNombre_Lista() {
        return nombre_Lista;
    }

    /*
    public Date getFecha_Actualizacion() {
        return fecha_Actualizacion;
    }
    */
    public String getSupermercado() {
        return supermercado;
    }

    public int getID() {
        return ID;
    }


}