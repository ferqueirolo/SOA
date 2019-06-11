package com.example.changosmart.productos;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.QR.QR;
import com.example.changosmart.R;

import java.util.ArrayList;

import BD.ShakeDetector;

public class AnadirProductoExpress extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listaProductos = new ArrayList<Producto>();
        String valor ;
        if( getIntent().getExtras() !=  null){
            valor = getIntent().getExtras().getString("idProducto");
            Toast.makeText(this,"CODIGO LEIDO : " + valor, Toast.LENGTH_SHORT);
          //  listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().findByProductoExpress(Integer.parseInt(valor));
        }
        listaProductos = new ArrayList<Producto>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto_express);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        listaProductos.add(new Producto("Mayonesa","Hellman",2,2));

        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductosExpress);

        //Seteo el adaptador y le paso la lista de los productos
        listaProductosView.setAdapter(new MiAdaptadorListaProductosExpress(this, listaProductos));

        //Activo sensor shake
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                startActivity(openQr);
            }
        });

            FloatingActionButton fab = findViewById(R.id.qr);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                    startActivity(openQr);
                }
            });
    }

}
