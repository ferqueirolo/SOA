package com.example.changosmart.productos;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.QR.QR;
import com.example.changosmart.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import BT.Bluetooth;
import BT.BluetoothConnectionService;

import static java.lang.StrictMath.abs;

public class AnadirProductoExpress extends AppCompatActivity {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private MiAdaptadorListaProductosExpress adaptator;
    private static ArrayList<Producto> listaProductos = new ArrayList<Producto>();

    private Bluetooth bluetoothInstance;

    private BluetoothConnectionService bluetoothConnection;

    private byte[] commandInBytes;

    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private float prevX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto_express);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SensorManager mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor myAccelerometerSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        prevX = 0;

        //Se instancia el bluetooth en base a la conexión actual
        bluetoothInstance = Objects.requireNonNull(getIntent().getExtras()).getParcelable("btInstance");

        //Se verifica que se tenga una conexión activa de bluetooth.
        if (bluetoothInstance != null){
            //Se inicia el socket del bt para escuchar mensajes del arduino.
            bluetoothConnection = new BluetoothConnectionService(getApplicationContext());
            if (bluetoothInstance.getPairDevice() != null){
                bluetoothConnection.startClient( bluetoothInstance.getPairDevice(), bluetoothConnection.getDeviceUUID());
                //Registro el evento del broadcast para detectar el provider que genera la lectura de un dato enviado por el arduino (BluetoothServiceConnection)
                LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter("IncomingMessage"));
            }else {
                //Se informa al usuario que debe emparejarse con un dispositivo.
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor..." , Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();
            }
        }

        //Solo si se detecta sensor.
        if (myAccelerometerSensor != null) {
           mySensorManager.registerListener(accelerometerSensorEventListener,myAccelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductosExpress);
        Producto prod = null;
        String valor;
        TextView precioParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);

        //Si lo obtenido es diferente de null lo voy a buscar a la base de productos para obtener el producto
        if( getIntent().getExtras() !=  null){
            valor = getIntent().getExtras().getString("nombreProducto");
            //Toast.makeText(this,"CODIGO LEIDO : " + valor, Toast.LENGTH_SHORT);
            prod = MainActivity.myAppDatabase.myDao().findByProductoExpress( valor );
            //Verifico que el producto no esté en la lista
            int flag = 0;
            for (Producto productoActual : listaProductos) {
                if (prod.getNombre().equals(productoActual.getNombre())) {
                    flag = 1;
                }
            }

            if ( flag == 1 ) {
                //Muestro mensaje de que el producto ya existe
                Toast.makeText(this,"Ese producto ya está agregado a la lista.",Toast.LENGTH_SHORT).show();
            } else {
                //Agrego el producto a la lista
                listaProductos.add( prod );
            }
        }

        //Obtengo el valor total parcial hasta el momento
        Iterator<Producto> iterator = listaProductos.iterator();
        int totalParcial = 0;
        while(iterator.hasNext()) {
            Producto productoActual = iterator.next();
            totalParcial += productoActual.getPrecio();
        }

        //Le seteo el total al label de la vista.
        precioParcial.setText(String.valueOf(totalParcial));

        //Seteo el adaptador y le paso la lista de los productos
        adaptator = new MiAdaptadorListaProductosExpress(this, listaProductos);
        listaProductosView.setAdapter(adaptator);

        //Si se clickea un elemento de la lista se propone eliminarlo, de ser así se elimina de la lista.
        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("¿Desea eliminar el producto de la lista?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView totalParcialView = (TextView) findViewById(R.id.textViewExpressTotalParcial);
                                int totalParcial = listaProductos.get(position).getPrecio() /* * listaProductos.get(position).getCantidad()*/;
                                listaProductos.remove(position);
                                //Seteo nuevamente la vista del total con el cálculo del total anterior menos el precio del eliminado.
                                totalParcialView.setText( String.valueOf( Integer.parseInt(totalParcialView.getText().toString()) - totalParcial));
                                adaptator.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });

        Button limpiarLista = (Button) findViewById(R.id.ClearListButton);

        limpiarLista.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        builder.setTitle("¿Desea vaciar la lista de productos?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TextView totalParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);
                                        totalParcial.setText(String.valueOf(0));
                                        listaProductos.clear();
                                        adaptator.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    }
                }
        );

        FloatingActionButton fab = findViewById(R.id.qr);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bluetoothInstance.getPairDevice() != null){
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    // se abre la vista de la camara para escanear el código qr y agregar el producto.
                    startActivityForResult(openQr, 1);
                    finish();
                }else {
                    //Se informa al usuario que debe emparejarse con un dispositivo.
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(), "No estás conectado a ningún dispositivo. Conectate vía bluetooth por favor..." , Toast.LENGTH_SHORT);

                    toast1.setGravity(Gravity.CENTER,0,0);

                    toast1.show();
                }
        }});
    }

    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Acá se recibe el mensaje del arduino y se evalua si es In o Out
            String text = intent.getStringExtra("theMessage");

            if (text.equals("IN")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "Ingresó un producto al chango." , Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();
            }else if (text.equals("OUT")){
                Toast toast1 =
                        Toast.makeText(getApplicationContext(), "Salió un producto del chango." , Toast.LENGTH_SHORT);

                toast1.setGravity(Gravity.CENTER,0,0);

                toast1.show();
            }
        }
    };

    SensorEventListener accelerometerSensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta; // perform low-cut filter

                if (mAccel > 15 && abs(abs(x)-abs(prevX))>=20 ) {
                    prevX=x;
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    startActivityForResult(openQr, 1);
                    finish();
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refreshActivity = new Intent(this, AnadirProductoExpress.class);
            startActivity(refreshActivity);
            this.finish();
        }
    }
}