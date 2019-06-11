package com.example.changosmart.productos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

import com.example.changosmart.QR.QR;

public class AnadirProductoExpress extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listaProductos = new ArrayList<Producto>();
        String valor ;
        if( getIntent().getExtras() !=  null){
            valor = getIntent().getExtras().getString("idProducto");
            listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().findByProductoExpress(Integer.parseInt(valor));
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto_express);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        listaProductos.add(new Producto("Mayonesa","Hellman",2,2));

        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductosExpress);

        //Seteo el adaptador y le paso la lista de los productos
        listaProductosView.setAdapter(new MiAdaptadorListaProductosExpress(this, listaProductos));

            FloatingActionButton fab = findViewById(R.id.qr);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    startActivity(openQr);
                }
            });
    }

}
