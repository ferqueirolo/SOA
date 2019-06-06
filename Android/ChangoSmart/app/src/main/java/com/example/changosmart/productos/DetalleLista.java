package com.example.changosmart.productos;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.listasCompras.LineaCompra;

import java.util.ArrayList;

public class DetalleLista extends AppCompatActivity {
    private ArrayList<LineaCompra> detalleLista;

    public static MiAdaptadorDetalleLista miAdaptadorProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        String nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");

        setContentView(R.layout.activity_detalle_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        detalleLista = (ArrayList) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);


        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductos);

        //Seteo el adaptador y le paso la lista de los productos
        miAdaptadorProductos = new MiAdaptadorDetalleLista(this, detalleLista);
        listaProductosView.setAdapter(miAdaptadorProductos);

        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //AlertDialog.Builder builder = new AlertDialog(view.getContext());
            }
        });

    }

}
