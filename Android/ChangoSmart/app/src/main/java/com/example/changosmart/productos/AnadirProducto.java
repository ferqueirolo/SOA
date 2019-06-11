package com.example.changosmart.productos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class AnadirProducto extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaProductos = new ArrayList<Producto>();

        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();

        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductos);

        //Seteo el adaptador y le paso la lista de los productos
        listaProductosView.setAdapter(new MiAdaptadorListaProductos(this, listaProductos));

//            FloatingActionButton qr = findViewById(R.id.qr);
//            qr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });
    }

}
