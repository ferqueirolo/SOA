package com.example.changosmart.compras.express;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.QR.QR;
import com.example.changosmart.R;
import com.example.changosmart.chango.Chango;
import com.example.changosmart.productos.Producto;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.Iterator;

import BD.ShakeDetector;

public class AnadirProductoExpress extends AppCompatActivity {
    public static final int REQUEST_CODE_QR = 1010;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private MiAdaptadorListaProductosExpress adaptator;
    private static ArrayList<Producto> listaProductos = new ArrayList<Producto>();
    private TextView precioParcial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto_express);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listaProductosView = (ListView) findViewById(R.id.listViewProductosExpress);
        Producto prod = null;
        String valor;
        precioParcial = (TextView) findViewById(R.id.textViewExpressTotalParcial);

        //Si lo obtenido es diferente de null lo voy a buscar a la base de productos para obtener el producto
        if( getIntent().getExtras() !=  null){
            valor = getIntent().getExtras().getString("nombreProducto");
            //Toast.makeText(this,"CODIGO LEIDO : " + valor, Toast.LENGTH_SHORT);
            prod = MainActivity.myAppDatabase.myDao().findByProductoExpress( valor );
            //Verifico que el producto no esté en la lista
            int flag = 0;
            Iterator<Producto> iteratorProducto = listaProductos.iterator();
            while(iteratorProducto.hasNext()) {
                Producto productoActual = iteratorProducto.next();
                if ( prod.getNombre().equals(productoActual.getNombre())){
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


        //Le seteo el total al label de la vista.
        precioParcial.setText(String.valueOf(0));

        //Seteo el adaptador y le paso la lista de los productos
        adaptator = new MiAdaptadorListaProductosExpress(this, listaProductos);
        listaProductosView.setAdapter(adaptator);

        //Si se clickea un elemento de la lista se propone eliminarlo, de ser así se elimina de la lista.
        listaProductosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
                    View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_linea_compra, null);
                    final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextDetalleCantidadProducto);
                    builder.setView(miAlertDialog);
                    final AlertDialog dialog = builder.create();

                    Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleAceptar);

                    buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                                Toast.makeText(AnadirProductoExpress.this, "Error - Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                            } else {
                                int montoActual = Integer.valueOf(precioParcial.getText().toString()) - listaProductos.get(position).getTotalPorProducto();
                                int nuevoMonto;

                                listaProductos.get(position).setCantidad(Integer.valueOf(etCantidad.getText().toString()));
                                adaptator.notifyDataSetChanged();

                                nuevoMonto = listaProductos.get(position).getTotalPorProducto() + montoActual;

                                dialog.dismiss();

                                precioParcial.setText(String.valueOf(nuevoMonto));
                            }
                        }
                    });

                    Button buttonCancelarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleCancelar);

                    buttonCancelarAlertProducto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    Button buttonEliminarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleEliminar);

                    buttonEliminarAlertProducto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // HAY QUE AGREGAR EL ALERT DIALOG PARA RECIBIR LA BARRERA
                            int montoActual = Integer.valueOf(precioParcial.getText().toString());
                            int nuevoMonto = montoActual - listaProductos.get(position).getTotalPorProducto();

                            listaProductos.remove(position);
                            adaptator.notifyDataSetChanged();
                            dialog.dismiss();

                            precioParcial.setText(String.valueOf(nuevoMonto));
                        }
                    });

                    dialog.show();
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
                startActivityForResult(openQr, 1);
                finish();
            }
        });

        FloatingActionButton qrFab = findViewById(R.id.qr);
        qrFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openQr = new Intent(AnadirProductoExpress.this, QR.class);
                // se abre la vista de la camara para escanear el código qr y agregar el producto.
                startActivityForResult(openQr, REQUEST_CODE_QR);
            }
        });

        FloatingActionButton changoFab = findViewById(R.id.moveChartCompraExpress);
        changoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openChango = new Intent(AnadirProductoExpress.this, Chango.class);
                // se abre la vista de la camara para escanear el código qr y agregar el producto.
                startActivity(openChango);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode) {
                case REQUEST_CODE_QR:
                    // A REALIZAR
                    // CREAR UN ASYNCTASK QUE SI RECIBE UN 1 DE LA BARRERA DEL ARDUINO HAGA UN DISMISS DEL DIALOG Y TE AGREGUE EL PRODUCTO

                    // Si pudo ingresar el nuevo producto tengo que esperar a las barreras
                    final Producto productoNuevo = MainActivity.myAppDatabase.myDao().getProducto(data.getStringExtra("nombreProducto"));
                    // SE VA A PODER EDITAR POR POPUP LA CANTIDAD
                    if (productoNuevo != null){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);
                        final View miAlertDialog = LayoutInflater.from(AnadirProductoExpress.this).inflate(R.layout.alertdialog_producto, null);
                        final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
                        builder.setView(miAlertDialog);
                        final AlertDialog dialog = builder.create();
                        Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

                        buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                                    Toast.makeText(AnadirProductoExpress.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                                } else {
                                    int cantidadNueva = Integer.valueOf(etCantidad.getText().toString());
                                    productoNuevo.setCantidad(cantidadNueva);
                                    if(!listaProductos.contains(productoNuevo)){
                                        anadirProducto(productoNuevo);
                                    } else {
                                        Toast.makeText(AnadirProductoExpress.this, "Ese producto ya está agregado a la lista.", Toast.LENGTH_SHORT).show();
                                        int i = 0;
                                        Iterator<Producto> iteratorProducto = listaProductos.iterator();
                                        Producto productoActual = iteratorProducto.next();
                                        while(iteratorProducto.hasNext() && ! productoNuevo.getNombre().equals(productoActual.getNombre())) {
                                            productoActual = iteratorProducto.next();
                                            i++;
                                        }
                                        int montoTotal = Integer.parseInt(precioParcial.getText().toString()) - listaProductos.get(i).getTotalPorProducto();
                                        listaProductos.get(i).sumarCantidadActual(cantidadNueva);
                                        precioParcial.setText(String.valueOf(listaProductos.get(i).getTotalPorProducto() + montoTotal));
                                        adaptator.notifyDataSetChanged();
                                    }
                                    dialog.dismiss();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AnadirProductoExpress.this);

                                    builder.setTitle("Ingrese el producto al chango");
                                    AlertDialog dialogIngresarProd = builder.create();
                                    dialogIngresarProd.setCancelable(false);
                                    dialogIngresarProd.setCanceledOnTouchOutside(false);
                                    dialogIngresarProd.show();
                                }
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
                    }else{
                        Toast.makeText(this, "Este producto no se encuentra registrado en el sistema.", Toast.LENGTH_SHORT).show();
                    }

                break;
                default:
                    Intent refreshActivity = new Intent(this, AnadirProductoExpress.class);
                    startActivity(refreshActivity);
                    this.finish();
            }
        }
    }

    private void anadirProducto(Producto producto){
        int montoTotal = Integer.parseInt(precioParcial.getText().toString());
        montoTotal += producto.getTotalPorProducto();
        precioParcial.setText(String.valueOf(montoTotal));
        listaProductos.add(producto);
        adaptator.notifyDataSetChanged();

    }

    private boolean nuevoProductoAIngresar(Intent data){
        boolean nuevoProductoIngresado = false;

        return  nuevoProductoIngresado;
    }
}
