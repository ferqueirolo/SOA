package BD;



import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private String fecha_Actualizacion;

    public ListaCompraE (String nombre_Lista, String supermercado){
        this.nombre_Lista = nombre_Lista;
        this.supermercado = supermercado;
        this.fecha_Actualizacion = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
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

    public void setFecha_Actualizacion(String fecha_actualizacion) { this.fecha_Actualizacion = fecha_Actualizacion; }

    // GETTERS
    public String getNombre_Lista() {
        return nombre_Lista;
    }

    public String getFecha_Actualizacion() { return fecha_Actualizacion; }

    public String getSupermercado() { return supermercado; }

    public int getID() { return ID; }


    // METODOS
    public void actualizarFechaActualizacion(){
        this.fecha_Actualizacion = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
    }

}