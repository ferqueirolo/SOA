package com.example.changosmart.productos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.listasCompras.LineaCompra;

import java.util.ArrayList;

public class DetalleLista extends AppCompatActivity {
    private ArrayList<LineaCompra> detalleLista;
    private String nombreListaRecibido;

    public static MiAdaptadorDetalleLista miAdaptadorDetalleLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_detalle_lista);
        Button buttonAgregarProductos = (Button) findViewById(R.id.buttonAgregarProductos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargo la lista de los productos
        //Se castea porque retorna una clase List

        detalleLista = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);

        if (detalleLista == null)
            detalleLista = new ArrayList<LineaCompra>();

        ListView listaProductosView = (ListView) findViewById(R.id.listViewDetalleLista);

        //Seteo el adaptador y le paso la lista de los productos
        miAdaptadorDetalleLista = new MiAdaptadorDetalleLista(this, detalleLista);
        listaProductosView.setAdapter(miAdaptadorDetalleLista);

        buttonAgregarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProductos = new Intent(v.getContext(), TodosProductos.class);
                intentProductos.putExtra("NOMBRE_LISTA", nombreListaRecibido);
                startActivity(intentProductos);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refreshActivity = new Intent(this, DetalleLista.class);
            startActivity(refreshActivity);
            this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        detalleLista = (ArrayList<LineaCompra>) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
    }
}
