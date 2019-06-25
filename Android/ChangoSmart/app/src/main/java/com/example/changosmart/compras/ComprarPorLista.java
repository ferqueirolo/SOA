package com.example.changosmart.compras;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.compras.express.MiAdaptadorListaProductosExpress;
import com.example.changosmart.listasCompras.detalleListas.LineaCompra;
import com.example.changosmart.listasCompras.detalleListas.MiAdaptadorDetalleLista;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;
import java.util.Objects;

import BT.Bluetooth;

public class ComprarPorLista extends AppCompatActivity {
    private String nombreListaRecibido;
    private ArrayList<LineaCompra> listaProductosAcomprar;
    private ArrayList<Producto> listaProductosComprados;
    private ListView listViewProductosAComprar,
                     listViewProductosComprados;

    private Bluetooth bluetoothInstance;

    private MiAdaptadorDetalleLista miAdaptadorDetalleLista;
    private MiAdaptadorListaProductosExpress miAdaptadorListaProductosExpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");
        
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        nombreListaRecibido = (String) Objects.requireNonNull(bundleRecibido).get("NOMBRE_LISTA");

        // Seteo los LISTVIEW
        listViewProductosAComprar   = (ListView) findViewById(R.id.listViewAComprarCompraLista);
        listViewProductosComprados  = (ListView) findViewById(R.id.listViewCompradosCompraLista);

        // Completo con los productos de la lista ya creada
        listaProductosAcomprar = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);

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

        setContentView(R.layout.activity_comprar_por_lista);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

}
