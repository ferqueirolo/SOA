package com.example.changosmart.listasCompras;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class MisListas extends AppCompatActivity {
    private ArrayList<ListaCompra> misListasCompras;

    private MiAdaptadorListaCompras adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Inicio las listas para caso de prueba
        // Devuelve interface LIST, por eso lo casteo a ArrayList
        final ArrayList<ListaCompra> misListasCompras = (ArrayList) MainActivity.myAppDatabase.myDao().getListaCompras();

        ListView listaComprasView = (ListView) findViewById(R.id.listViewMisListas);

        //Creamos un Adaptador para mostrarlo por la vista
        adaptador = new MiAdaptadorListaCompras(this, misListasCompras);

        listaComprasView.setAdapter(adaptador);


        /**
        // Una accion si tocan en el nombre de la lista
        listaComprasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MisListas.this, " A A A A ", Toast.LENGTH_LONG);

                AlertDialog.Builder opcionesListaCompra = new AlertDialog.Builder(view.getContext());
                /*
                opcionesListaCompra.setTitle(R.string.title_alert_dialog_lista)
                        .setItems(R.array.tresOpcionesAlertDialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Opcion 1 -> Iniciar Compra
                                // Opcion 2 -> Editar Lista
                                // Opcion 3 -> Eliminar Lista
                                switch (which) {
                                    case 2: // Eliminar Lista
                                        eliminarListaCompra(view, position);
                                        Toast.makeText(view.getContext(), "Lista eliminada", Toast.LENGTH_LONG);
                                        adaptador.notifyDataSetChanged();
                                        break;
                                }
                            }
                        });
                opcionesListaCompra.show();
            }
        });*/

        Button buttonCrearLista = (Button) findViewById(R.id.buttonCrearLista);
        buttonCrearLista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newActivity = new Intent(view.getContext(),CrearLista.class);
                        startActivityForResult(newActivity, 1);
                    }
                }
        );
    }


    @Override
    public void onStart() {
        super.onStart();

        // Devuelve interface LIST, por eso lo casteo a ArrayList
        misListasCompras = (ArrayList) MainActivity.myAppDatabase.myDao().getListaCompras();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refreshActivity = new Intent(this, MisListas.class);
            startActivity(refreshActivity);
            this.finish();
        }
    }

    /**
     * Elimina lista de compra de la base de datos
     * @param view
     * @param pos
     */

    private void eliminarListaCompra(final View view, final int pos ){
        // Genero mensaje de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Confirmar Accion")
                //BOTON PARA CONFIRMAR ELIMINACION
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Elimino de la base de datos el registro
                        MainActivity.myAppDatabase.myDao().deleteLista(
                                misListasCompras.get(pos).getNombre_lista(),
                                misListasCompras.get(pos).getSupermercado()
                        );

                        // Lo saco de la lista local
                        misListasCompras.remove(pos);
                    }
                })
                // BOTON PARA CANCELAR LA ELIMINACION
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

}
