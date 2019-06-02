package com.example.changosmart.listasCompras;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class MisListas extends AppCompatActivity {
    private ArrayList<ListaCompra> misListasCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        misListasCompras = new ArrayList<ListaCompra>();

        // Inicio las listas para caso de prueba
        // Devuelve interface LIST, por eso lo casteo a ArrayList
        misListasCompras = (ArrayList) MainActivity.myAppDatabase.myDao().getListaCompras();

        ListView listaComprasView = (ListView) findViewById(R.id.listViewMisListas);

        //Creamos un Adaptador para mostrarlo por la vista
        listaComprasView.setAdapter(new MiAdaptadorListaCompras(this, misListasCompras));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button buttonCrearLista = (Button) findViewById(R.id.buttonCrearLista);
        buttonCrearLista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent newActivity = new Intent(view.getContext(),CrearLista.class);
                        startActivity(newActivity);
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

}
