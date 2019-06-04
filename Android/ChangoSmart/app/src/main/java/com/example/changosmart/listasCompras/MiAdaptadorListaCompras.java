package com.example.changosmart.listasCompras;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class MiAdaptadorListaCompras extends BaseAdapter {
    private Context contexto;
    private ArrayList<ListaCompra> listaListaCompra;

    // Sirve para instanciar el xml
    private static LayoutInflater inflater = null;

    /** CONSTRUCTOR       ***************************************************/
    public MiAdaptadorListaCompras(Context contexto, ArrayList<ListaCompra> listaListaCompra) {
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

    public ArrayList<ListaCompra> getListaProductos() {
        return listaListaCompra;
    }

    public void setListaProductos(ArrayList<ListaCompra> listaProductos) {
        this.listaListaCompra = listaProductos;
    }
    /************************************************************************/


    @Override
    public View getView(final int i, final View convertView, ViewGroup parent) {
        // Vista que voy a retornar
        final View myView = inflater.inflate(R.layout.item_lista_lista_compra, null);

        // Tomo los campos del xml donde voy a mostrar los datos
        TextView nombreLista        = (TextView) myView.findViewById(R.id.textViewListaCompra);
        TextView supermercado       = (TextView) myView.findViewById(R.id.textViewSupermercado);
        TextView fechaActualizacion = (TextView) myView.findViewById(R.id.textViewFechaActualizacion);

        // Elemento de la lista

        // Populo los datos de los campos del xml
        nombreLista.setText(listaListaCompra.get(i).getNombre_lista());
        supermercado.setText(listaListaCompra.get(i).getSupermercado());
        fechaActualizacion.setText(listaListaCompra.get(i).getFecha_actualizacion());


        // Una accion si tocan en el nombre de la lista
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View vistaFila) {
                // Muestro mensaje
                final AlertDialog.Builder opcionesListaCompra = new AlertDialog.Builder(vistaFila.getContext());

                opcionesListaCompra.setTitle(R.string.title_alert_dialog_lista)
                                    .setItems(R.array.tresOpcionesAlertDialog, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Opcion 1 -> Iniciar Compra
                                            // Opcion 2 -> Editar Lista
                                            // Opcion 3 -> Eliminar Lista
                                            switch (which){
                                            case 2: // Eliminar Lista
                                                eliminarListaCompra(vistaFila, i);
                                                Log.d("V",listaListaCompra.get(i).toString());
                                                notifyDataSetChanged();
                                            break;
                                            }
                                        }

                                    });
                opcionesListaCompra.show();
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

    private void eliminarListaCompra(final View view, final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle("Confirmar Accion")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.myAppDatabase.myDao().deleteLista(
                                listaListaCompra.get(pos).getNombre_lista(),
                                listaListaCompra.get(pos).getSupermercado()
                        );
                        listaListaCompra.remove(pos);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();

    }
}