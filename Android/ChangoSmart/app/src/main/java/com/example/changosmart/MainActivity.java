package com.example.changosmart;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.changosmart.Bluetooth.Bluetooth;
import com.example.changosmart.listasCompras.MisListas;
import com.example.changosmart.chango.Chango;
import com.example.changosmart.productos.AnadirProducto;
import com.example.changosmart.productos.AnadirProductoExpress;

import BD.MyAppDatabase;

public class MainActivity extends AppCompatActivity {
    public static MyAppDatabase myAppDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Botón para mover el chango
        FloatingActionButton moveChart = findViewById(R.id.moveChart);
        moveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveChango = new Intent( view.getContext(), Chango.class);
                startActivity(moveChango);
            }
        });


        // INICIALIZO LA BASE
        // HAY QUE CREAR UN HILO PARA QUE NO TRABAJE EN EL HILO PRINCIPAL
        myAppDatabase = Room.databaseBuilder(this.getApplicationContext(), MyAppDatabase.class, "db").allowMainThreadQueries().build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage (View view){
        Intent openMisListas = new Intent(this, MisListas.class);
        startActivity(openMisListas);
    }

    public void openListExpress (View view){
        Intent openListExpress = new Intent(this, AnadirProductoExpress.class);
        startActivity(openListExpress);
    }
}
