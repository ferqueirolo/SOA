package com.example.changosmart.productos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;
import com.example.changosmart.listasCompras.detalleListas.DetalleLista;

import java.util.ArrayList;
import java.util.HashMap;

import BD.DetalleListaCompraTabla;
import BD.ShakeDetector;
import com.example.changosmart.ShakeService;

public class TodosProductos extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;
    private ArrayList<Producto> listaProductosFilt;
    private MiAdaptadorProductos miAdaptadorProductos;
    private HashMap<String, Integer> hashMapCantidades;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private static final String TAG = "MainActivity";
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        final String nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_todos_productos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();
        //DEFINICION DEL BUSCADOR
        EditText filter2 = (EditText) findViewById(R.id.searchFilter2);
        //Activo sensor shake
        Intent intent = new Intent(this, ShakeService.class);
        //Start Service
        startService(intent);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager              .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
              @Override
              public void onShake(int count) {
                  /*
                   * The following method, "handleShakeEvent(count):" is a stub //
                   * method you would use to setup whatever you want done once the
                   * device has been shook.
                   */
                  Toast.makeText(TodosProductos.this, "Shaked!!!", Toast.LENGTH_SHORT).show();
              }
        });

        if(listaProductos == null)
            listaProductos = new ArrayList<Producto>();

        final ListView listaProductosView = (ListView) findViewById(R.id.listViewTodosProductos);

        miAdaptadorProductos = new MiAdaptadorProductos(this, listaProductos);

        listaProductosView.setAdapter(miAdaptadorProductos);

        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodosProductos.this);
                View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_producto,null);
                final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
                builder.setView(miAlertDialog);
                final AlertDialog dialog = builder.create();

                Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

                buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etCantidad.getText().toString().isEmpty()){
                            Toast.makeText(TodosProductos.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                        }else{
                            if( MainActivity.myAppDatabase.myDao().existeProductoEnLista(nombreListaRecibido,listaProductos.get(position).getNombre()) != 1) {
                                MainActivity.myAppDatabase.myDao().insertarLineaCompra(
                                        new DetalleListaCompraTabla(
                                                nombreListaRecibido,
                                                listaProductos.get(position).getNombre(),
                                                Integer.valueOf(etCantidad.getText().toString())
                                        ));
                                Toast.makeText(TodosProductos.this, "Producto añadido a la lista", Toast.LENGTH_SHORT).show();
                            }else{
                                MainActivity.myAppDatabase.myDao().actualizarCantidadAComprar(nombreListaRecibido, listaProductos.get(position).getNombre(),  Integer.valueOf(etCantidad.getText().toString()));
                                Toast.makeText(TodosProductos.this, "Producto modificado en la lista", Toast.LENGTH_SHORT).show();
                            }
                        }
                        dialog.dismiss();
                    }
                });

                Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoCancelar);

                buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        // Agregamos el listener en el textView
        filter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Tomamos todos los productos
                listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();
                // Lista que va almacenar las palabras que entren en el filtro
                listaProductosFilt=new ArrayList<Producto>();
                
                if(charSequence.toString().equals("")) {
                    // Reseteamos la lista de los productos
                    listaProductosFilt = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();
                }
                else {
                    // Recorremos la lista para agregar aquellos productos que contentan la frase de busqueda 
                    for (Producto item : listaProductos) {
                        if ((item.getNombre().toLowerCase()).contains(charSequence.toString().toLowerCase())) {
                            listaProductosFilt.add(item);
                        }
                    }
                }
                // Seteamos la nueva lista filtrada en la lista de productos
                listaProductos = listaProductosFilt;
                // Actualizamos el adaptador
                miAdaptadorProductos.setListaProductos(listaProductos);
                // Notificamos los cambios para que los muestre en la listView
                miAdaptadorProductos.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent intentDetalleListas = new Intent(this, DetalleLista.class);
        intentDetalleListas.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intentDetalleListas);

        // Eliminamos la actividad actual para que no quede viva
        setResult(RESULT_OK, null);
        finish();
    }

}
