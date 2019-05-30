package com.example.changosmart;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

/**
 * Clase para conectarse a la BD ROOM
 */
@Dao
public interface MyDao {
    // Firma de metodo para insertar Listas de Compras en la base ROOM
    @Insert
    public void  addLista(ListaCompraE lista);
}
