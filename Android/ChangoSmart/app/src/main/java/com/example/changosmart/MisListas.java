package com.example.changosmart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import BD.ListaCompraE;
import BD.MyAdaptador;

public class MisListas extends AppCompatActivity {
    private ListView listaComprasView;
    private ArrayList<ListaCompraE> misListasCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_listas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        misListasCompras = new ArrayList<ListaCompraE>();
        // Inicio las listas para caso de prueba
        iniciarLista();

        listaComprasView = (ListView) findViewById(R.id.listViewMisListas);

        listaComprasView.setAdapter(new MyAdaptador(this, misListasCompras));

        //Creamos un Adaptador para mostrarlo por la vista
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

    private void iniciarLista(){
        this.misListasCompras.add(new ListaCompraE("Pepe 1", "Dia"));
        this.misListasCompras.add(new ListaCompraE("Salado", ""));
        this.misListasCompras.add(new ListaCompraE("Fiesta Loca", "Coto"));
    }

}
