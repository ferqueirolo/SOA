package com.example.changosmart.chango;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.changosmart.R;

import java.nio.charset.Charset;

import BT.Bluetooth;
import BT.BluetoothConnectionService;

//Clase para manejar el movimiento del chango
public class Chango extends AppCompatActivity {

    private final String TAG = "Chango";

    private Button buttonUp;

    private Button buttonLeft;

    private Button buttonDown;

    private Button buttonRight;

    private char establecerComandoMovimiento;

    private Bluetooth bluetoothInstance;

    private BluetoothConnectionService bluetoothConnection;

    private byte[] commandInBytes;

    private float prevLuz=15;

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

        //Instancio el bt actual en el activity
        bluetoothInstance = getIntent().getExtras().getParcelable("btInstance");

        if (bluetoothInstance.getPairDevice() != null){
            bluetoothConnection = new BluetoothConnectionService(getApplicationContext());
            //Si tengo un disposito conectado comienzo la conexión.
            bluetoothConnection.startClient(bluetoothInstance.getPairDevice(), bluetoothConnection.getDeviceUUID());
        }

        //Si se presiona el boton avanzar
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                if (establecerComandoMovimiento == 'S' || establecerComandoMovimiento == 'I' || establecerComandoMovimiento == 'D') {
                    establecerComandoMovimiento = 'A';
                } else {
                    establecerComandoMovimiento = 'B';
                }

                enviarInformacionMovimiento(establecerComandoMovimiento);
            }
        });

        //Si se presiona el boton izquierdo
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                establecerComandoMovimiento = 'I';

                enviarInformacionMovimiento(establecerComandoMovimiento);
            }
        });

        //Si se presiona el boton retroceder
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                //Si el estado anterior era una A, significa que debe detenerse ( S ), sino, retrocedo (b)
                if (establecerComandoMovimiento == 'A' || establecerComandoMovimiento == 'I' || establecerComandoMovimiento == 'D') {
                    establecerComandoMovimiento = 'S';
                } else {
                    establecerComandoMovimiento = 'B';
                }

                enviarInformacionMovimiento(establecerComandoMovimiento);
            }
        });

        //Si se presiona el boton derecho
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                establecerComandoMovimiento = 'D';

                enviarInformacionMovimiento(establecerComandoMovimiento);
            }
        });
    }

    //Método para envíar información al arduino.
    public void enviarInformacionMovimiento(char direccionMovimiento){
        if (bluetoothInstance.getPairDevice() == null){
            //Si no estoy conectado al bluetooth o se pierde la señal
            // Pido reconectarse
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor... ", Toast.LENGTH_SHORT);

            toast1.setGravity(Gravity.CENTER,0,0);

            toast1.show();
        } else {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(), "Caracter a enviar: " + direccionMovimiento, Toast.LENGTH_SHORT);

            toast1.setGravity(Gravity.CENTER,0,0);

            toast1.show();

            commandInBytes = String.valueOf(direccionMovimiento).getBytes(Charset.defaultCharset());

            bluetoothConnection.write(commandInBytes);
        }
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
                    //Envía un mensaje al arduino para que frene.
                    commandInBytes = String.valueOf('S').getBytes(Charset.defaultCharset());
                    bluetoothConnection.write(commandInBytes);
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
                if (event.values[0] < 10 && prevLuz > 10) {
                    //El sensor no detecta luz y estaba apagada la luz del carrito
                    prevLuz=event.values[0];
                    //Encender luz carrito
                    commandInBytes = String.valueOf('L').getBytes(Charset.defaultCharset());
                    bluetoothConnection.write(commandInBytes);
                } else {
                    if (event.values[0] >= 10 && prevLuz < 10) {
                        //El sensor detecta luz y estaba encedida la luz del carrito
                        prevLuz=event.values[0];
                        //Apagar luz carrito
                        commandInBytes = String.valueOf('L').getBytes(Charset.defaultCharset());
                        bluetoothConnection.write(commandInBytes);
                    }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 18) {
            if(resultCode == RESULT_OK) {
                bluetoothInstance = data.getExtras().getParcelable("btInstanceBack");
            }
        }
    }
}
