package com.example.changosmart.chango;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.changosmart.Bluetooth.Bluetooth;
import com.example.changosmart.R;

//Clase para manejar el movimiento del chango
public class Chango extends AppCompatActivity {

    private Button buttonUp;

    private Button buttonLeft;

    private Button buttonDown;

    private Button buttonRight;

    private char IdentificadorMovimiento;

    //********BLUETOOTH*************
    private SeekBar elevation;

    private TextView debug;

    private TextView status;

    private Bluetooth bt;

    public final String TAG = "Main";
    //********BLUETOOTH*************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover_chango);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        debug = (TextView) findViewById(R.id.textDebug);
        status = (TextView) findViewById(R.id.textStatus);




        //Inicializo los botones con los correspondientes controles
        buttonUp = findViewById(R.id.buttonUp);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);
        buttonRight = findViewById(R.id.buttonRight);


        //Si se presiona el boton avanzar
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectService();
                //Asigno la variable que va a recibir el embebido según el movimiento que quiero hacer.
                IdentificadorMovimiento = 'A';
                bt.sendMessage("A");
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
                bt.sendMessage("I");
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
                    bt.sendMessage("S");
                } else {
                    IdentificadorMovimiento = 'B';
                    bt.sendMessage("B");
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
                bt.sendMessage("D");
                // Pregunto por la conexión bluetooth
                // Le paso el caracter al arduino.

                //Si no estoy conectado al bluetooth o se pierde la señal
                // Pido reconectarse
            }
        });

        //********BLUETOOTH*************
        bt = new Bluetooth(this, mHandler);
        connectService();
        //********BLUETOOTH*************
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

    //********BLUETOOTH*************
    public void connectService(){
        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("ChangoSmart");
                Log.d(TAG, "SE CONECTÓ !!");
                status.setText("Connected");
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;
                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ ");
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };
    //********BLUETOOTH*************
}
