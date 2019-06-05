package com.example.changosmart.chango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.changosmart.R;

//Clase para manejar el movimiento del chango
public class Chango extends AppCompatActivity {

    private Button buttonUp;

    private Button buttonLeft;

    private Button buttonDown;

    private Button buttonRight;

    private char IdentificadorMovimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_chango);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicializo los botones con los correspondientes controles
        buttonUp = findViewById(R.id.buttonUp);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);
        buttonRight = findViewById(R.id.buttonRight);

        //Si se presiona el boton superior
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'u';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });

        //Si se presiona el boton izquierdo
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'l';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });

        //Si se presiona el boton inferior
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'd';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });

        //Si se presiona el boton derecho
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'r';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
