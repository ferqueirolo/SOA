package com.example.changosmart.productos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;
import java.util.HashMap;

import BD.DetalleListaCompraTabla;

public class TodosProductos extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;
    private MiAdaptadorProductos miAdaptadorProductos;
    private HashMap<String, Integer> hashMapCantidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        final String nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_todos_productos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();

        if(listaProductos == null)
            listaProductos = new ArrayList<Producto>();

        final ListView listaProductosView = (ListView) findViewById(R.id.listViewTodosProductos);

        miAdaptadorProductos = new MiAdaptadorProductos(this, listaProductos);

        listaProductosView.setAdapter(miAdaptadorProductos);

        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodosProductos.this);
                View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_producto,null);
                final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
                builder.setView(miAlertDialog);
                final AlertDialog dialog = builder.create();

                Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

                buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etCantidad.getText().toString().isEmpty()){
                            Toast.makeText(TodosProductos.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                        }else{
                            if( MainActivity.myAppDatabase.myDao().existeProductoEnLista(nombreListaRecibido,listaProductos.get(position).getNombre()) != 1) {
                                MainActivity.myAppDatabase.myDao().insertarLineaCompra(
                                        new DetalleListaCompraTabla(
                                                nombreListaRecibido,
                                                listaProductos.get(position).getNombre(),
                                                Integer.valueOf(etCantidad.getText().toString())
                                        ));
                                Toast.makeText(TodosProductos.this, "Producto añadido a la lista", Toast.LENGTH_SHORT).show();
                            }else{
                                MainActivity.myAppDatabase.myDao().actualizarCantidadAComprar(nombreListaRecibido, listaProductos.get(position).getNombre(),  Integer.valueOf(etCantidad.getText().toString()));
                                Toast.makeText(TodosProductos.this, "Producto modificado en la lista", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });

                Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoCancelar);

                buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

}
