package com.example.changosmart.chango;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.changosmart.R;

import java.util.Random;

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

        SensorManager mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        Sensor myLightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (myProximitySensor == null) {
            //Si no se detecta sensor de proximidad;
        } else {
            mySensorManager.registerListener(proximitySensorEventListener,myProximitySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (myLightSensor == null) {
            //Si no se detecta sensor de proximidad;
        } else {
            mySensorManager.registerListener(lightSensorEventListener,myLightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        //Inicializo los botones con los correspondientes controles
        buttonUp = findViewById(R.id.buttonUp);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);
        buttonRight = findViewById(R.id.buttonRight);

        //Si se presiona el boton avanzar
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'A';

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
                IdentificadorMovimiento = 'I';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });

        //Si se presiona el boton retroceder
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                //Si el estado anterior era una A, significa que debe detenerse ( S ), sino, retrocedo (b)
                if ( IdentificadorMovimiento == 'A' || IdentificadorMovimiento == 'I' || IdentificadorMovimiento == 'D' ) {
                    IdentificadorMovimiento = 'S';
                } else {
                    IdentificadorMovimiento = 'B';
                }

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
                IdentificadorMovimiento = 'D';

                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });
    }

    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    //El sensor detecta algo próximo
                    buttonUp.setEnabled(false);
                    buttonLeft.setEnabled(false);
                    buttonDown.setEnabled(false);
                    buttonRight.setEnabled(false);
                } else {
                    buttonUp.setEnabled(true);
                    buttonLeft.setEnabled(true);
                    buttonDown.setEnabled(true);
                    buttonRight.setEnabled(true);
                }
            }
        }
    };

    SensorEventListener lightSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                if (event.values[0] < 10) {
                    //El sensor no detecta luz

                    //Encender luz carrito
                } else {
                    //El sensor detecta luz

                    //Apagar luz carrito
                }
            }
        }
    };

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
