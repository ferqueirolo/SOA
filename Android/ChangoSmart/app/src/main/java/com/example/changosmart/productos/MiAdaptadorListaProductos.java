package com.example.changosmart.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.R;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;

public class MiAdaptadorListaProductos extends BaseAdapter {
    private Context contexto;
    private ArrayList<Producto> listaProductos;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MiAdaptadorListaProductos(Context contexto, ArrayList<Producto> listaProductos) {
        this.contexto = contexto;
        this.listaProductos = listaProductos;

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

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    /************************************************************************/


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_lista_compra, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView tv_nombreProducto      = (TextView) myView.findViewById(R.id.textViewNombreProducto);
        TextView tv_categoriaProducto   = (TextView) myView.findViewById(R.id.textViewCategoriaProducto);
        TextView tv_precioProducto      = (TextView) myView.findViewById(R.id.textViewPrecioProducto);


        // Populo los datos de los campos del xml
        tv_nombreProducto.setText(listaProductos.get(i).getNombre());
        tv_categoriaProducto.setText(listaProductos.get(i).getCategoria());
        tv_precioProducto.setText(listaProductos.get(i).getPrecio());

        // Una accion si tocan en el nombre de la lista
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestro mensaje
                Toast.makeText(v.getContext(),"A - A - A",Toast.LENGTH_SHORT).show();
            }
        });

        // Retorno la vista generada
        return myView;
    }
    @Override
    public int getCount() {
        return listaProductos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaProductos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
