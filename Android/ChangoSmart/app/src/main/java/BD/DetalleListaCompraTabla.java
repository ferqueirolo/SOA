package BD;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

@Entity(tableName = "Detalle_Lista_Compra", primaryKeys ={"supermercado", "nombre_lista", "id_producto"})
public class DetalleListaCompraTabla {

    @NonNull
    private String supermercado;
    @NonNull
    private String nombre_lista;
    @NonNull
    private String id_producto;
    private int cantidadAComprar;

    public DetalleListaCompraTabla(@NonNull String supermercado, @NonNull String nombre_lista, @NonNull String id_producto) {
        this.supermercado = supermercado;
        this.nombre_lista = nombre_lista;
        this.id_producto = id_producto;
    }

    @NonNull
    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(@NonNull String supermercado) {
        this.supermercado = supermercado;
    }

    @NonNull
    public String getNombre_lista() {
        return nombre_lista;
    }

    public void setNombre_lista(@NonNull String nombre_lista) {
        this.nombre_lista = nombre_lista;
    }

    @NonNull
    public String getId_producto() {
        return id_producto;
    }

    public void setId_producto(@NonNull String id_producto) {
        this.id_producto = id_producto;
    }

    public int getCantidadAComprar() {
        return cantidadAComprar;
    }

    public void setCantidadAComprar(int cantidadAComprar) {
        this.cantidadAComprar = cantidadAComprar;
    }
}
