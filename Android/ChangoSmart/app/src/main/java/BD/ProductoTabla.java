package BD;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.changosmart.productos.Producto;

import java.util.ArrayList;

/**
 * Clase Tabla para utilizarla desde ROOM
 */

@Entity(tableName = "Productos")
public class ProductoTabla {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String nombre;
    private String categoria;
    private int precio;

    public ProductoTabla(String nombre, String categoria, int precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    /** GETTERS   *****************************/
    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getPrecio() {
        return precio;
    }

    /** SETTERS   *****************************/
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

}
