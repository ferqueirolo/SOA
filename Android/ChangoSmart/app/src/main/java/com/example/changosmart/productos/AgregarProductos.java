package com.example.changosmart.productos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class AgregarProductos extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;

    public static MiAdaptadorListaProductos miAdaptadorListaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();


        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductos);

        //Seteo el adaptador y le paso la lista de los productos
        miAdaptadorListaProductos = new MiAdaptadorListaProductos(this, listaProductos);
        listaProductosView.setAdapter(miAdaptadorListaProductos);

        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog(view.getContext());
            }
        });

    }

}
