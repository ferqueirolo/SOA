package BD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.Producto;
import com.example.changosmart.R;

import java.util.ArrayList;

public class MyAdaptador extends BaseAdapter {
    private Context contexto;
    private ArrayList<ListaCompraE> listaListaCompra;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MyAdaptador(Context contexto, ArrayList<ListaCompraE> listaListaCompra) {
        this.contexto = contexto;
        this.listaListaCompra = listaListaCompra;

        inflater = (LayoutInflater)contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }
    /************************************************************************/

    /** GETTER & SETTERS  *************************************************/
    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public ArrayList<ListaCompraE> getListaProductos() {
        return listaListaCompra;
    }

    public void setListaProductos(ArrayList<ListaCompraE> listaProductos) {
        this.listaListaCompra = listaProductos;
    }
    /************************************************************************/


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_lista_compra, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView nombreLista        = (TextView) myView.findViewById(R.id.textViewListaCompra);
        TextView supermercado       = (TextView) myView.findViewById(R.id.textViewSupermercado);
        TextView fechaActualizacion = (TextView) myView.findViewById(R.id.textViewFechaActualizacion);

        // Populo los datos de los campos del xml
        nombreLista.setText(listaListaCompra.get(i).getNombre_Lista());
        supermercado.setText(listaListaCompra.get(i).getSupermercado());
        fechaActualizacion.setText(listaListaCompra.get(i).getFecha_Actualizacion());

        // Una accion si tocan en el nombre de la lista
        nombreLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestro mensaje
                Toast.makeText(v.getContext(),"QUE ME TOCAS GATOO ?",Toast.LENGTH_SHORT).show();
            }
        });

        // Retorno la vista generada
        return myView;
    }
    @Override
    public int getCount() {
        return listaListaCompra.size();
    }

    @Override
    public Object getItem(int position) {
        return listaListaCompra.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
