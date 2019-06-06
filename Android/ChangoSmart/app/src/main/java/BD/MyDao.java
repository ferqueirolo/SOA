package BD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.changosmart.listasCompras.ListaCompra;
import com.example.changosmart.productos.Producto;

import java.util.List;

/**
 * Clase para conectarse a la BD ROOM
 */
@Dao
public interface MyDao {
    // Firma de metodo para insertar Listas de Compras en la base ROOM
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public void  addLista(MisListasCompraTabla lista);

    @Query("SELECT nombre_lista, supermercado, fecha_actualizacion FROM Mis_Listas_Compras ORDER BY fecha_actualizacion DESC")
    public List<ListaCompra> getListaCompras();

    @Query("SELECT nombre, categoria, precio FROM Productos ORDER BY Nombre")
    public  List<Producto> getProductos();

    @Query("SELECT nombre, categoria, precio FROM Productos WHERE nombre = :nombreProducto")
    public  List<Producto> getProducto(String nombreProducto);

    @Query("DELETE FROM Mis_Listas_Compras WHERE nombre_lista = :nombre_lista AND supermercado = :supermercado")
    public void deleteLista(String nombre_lista, String supermercado);

    @Query("UPDATE Detalle_Lista_Compra SET cantidadAComprar = :cantidadAComprar WHERE supermercado = :supermercado AND nombre_lista = :nombre_lista")
    public void update_cantidad(String nombre_lista, String supermercado, int cantidadAComprar);

    @Insert
    public void cargarProductos(List<ProductoTabla> listaProductoTabla);
}


