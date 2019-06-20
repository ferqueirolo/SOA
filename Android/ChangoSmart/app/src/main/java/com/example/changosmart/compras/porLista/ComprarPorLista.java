package com.example.changosmart.compras.porLista;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.listasCompras.detalleListas.LineaCompra;
import com.example.changosmart.listasCompras.detalleListas.MiAdaptadorDetalleLista;
import com.example.changosmart.compras.express.MiAdaptadorListaProductosExpress;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;

public class ComprarPorLista extends AppCompatActivity {
    private String nombreListaRecibido;
    private ArrayList<LineaCompra> listaProductosAcomprar;
    private ArrayList<Producto> listaProductosComprados;
    private ListView listViewProductosAComprar,
                     listViewProductosComprados;

    private MiAdaptadorDetalleLista miAdaptadorDetalleLista;
    private MiAdaptadorListaProductosExpress miAdaptadorListaProductosExpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_comprar_por_lista);

        // Seteo los LISTVIEW
        listViewProductosAComprar   = (ListView) findViewById(R.id.listViewAComprarCompraLista);
        listViewProductosComprados  = (ListView) findViewById(R.id.listViewCompradosCompraLista);

        // Completo con los productos de la lista ya creada
        listaProductosAcomprar = (ArrayList) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
        // Inicializo la lista vacia
        listaProductosComprados = new ArrayList<Producto>();

        // Adaptador Lista Productos a Comprar
        miAdaptadorDetalleLista = new MiAdaptadorDetalleLista(this, listaProductosAcomprar);
        // Adaptador Lista Productos Comprados
        miAdaptadorListaProductosExpress = new MiAdaptadorListaProductosExpress(this, listaProductosComprados);

        // Seteo el adaptador de la ListView
        listViewProductosAComprar.setAdapter(miAdaptadorDetalleLista);
        // Seteo el adaptador de la otra lista
        listViewProductosComprados.setAdapter(miAdaptadorListaProductosExpress);





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lo dejo comentado porque lo vamos a usar para manejar el changoMas
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

}
