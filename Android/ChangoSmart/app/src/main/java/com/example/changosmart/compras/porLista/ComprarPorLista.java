package com.example.changosmart.compras.porLista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.changosmart.ProductoDialog;
import com.example.changosmart.QR.QR;
import com.example.changosmart.R;
import com.example.changosmart.chango.Chango;
import com.example.changosmart.listasCompras.detalleListas.DetalleLista;
import com.example.changosmart.listasCompras.detalleListas.LineaCompra;
import com.example.changosmart.listasCompras.detalleListas.MiAdaptadorDetalleLista;
import com.example.changosmart.compras.express.MiAdaptadorListaProductosExpress;
import com.example.changosmart.productos.Producto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class ComprarPorLista extends AppCompatActivity{
    public static final int REQUEST_CODE_QR = 1010;
    private String nombreListaRecibido;
    private ArrayList<LineaCompra> listaProductosAcomprar;
    private ArrayList<Producto> listaProductosComprados;
    private ListView listViewProductosAComprar,
                     listViewProductosComprados;

    private MiAdaptadorDetalleLista miAdaptadorProdAComprar;
    private MiAdaptadorListaProductosExpress miAdaptadorProdComprados;
    private TextView montoParcial;
    private int cantidadComprar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleRecibido = this.getIntent().getExtras();
        // SE DEBE RECIBIR EN EL INTENT EL NOMBRE DE LA LISTA
        nombreListaRecibido = (String) bundleRecibido.get("NOMBRE_LISTA");
        setContentView(R.layout.activity_comprar_por_lista);

        montoParcial = (TextView) findViewById(R.id.textViewCompraListaTotalParcial);
        montoParcial.setText("0");

        // Seteo los LISTVIEW
        listViewProductosAComprar   = (ListView) findViewById(R.id.listViewAComprarCompraLista);
        listViewProductosComprados  = (ListView) findViewById(R.id.listViewCompradosCompraLista);

        // Completo con los productos de la lista ya creada
        listaProductosAcomprar = (ArrayList) MainActivity.myAppDatabase.myDao().getDetalleLista(nombreListaRecibido);
        // Inicializo la lista vacia
        listaProductosComprados = new ArrayList<Producto>();

        // Adaptador Lista Productos a Comprar
        miAdaptadorProdAComprar = new MiAdaptadorDetalleLista(this, listaProductosAcomprar);
        // Adaptador Lista Productos Comprados
        miAdaptadorProdComprados = new MiAdaptadorListaProductosExpress(this, listaProductosComprados);

        // Seteo el adaptador de la ListView
        listViewProductosAComprar.setAdapter(miAdaptadorProdAComprar);
        // Seteo el adaptador de la otra lista
        listViewProductosComprados.setAdapter(miAdaptadorProdComprados);

        listViewProductosAComprar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ComprarPorLista.this);
                View miAlertDialog = LayoutInflater.from(view.getContext()).inflate(R.layout.alertdialog_linea_compra, null);
                final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextDetalleCantidadProducto);
                builder.setView(miAlertDialog);
                final AlertDialog dialog = builder.create();

                Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogDetalleAceptar);

                buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                            Toast.makeText(ComprarPorLista.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                        } else {
                            MainActivity.myAppDatabase.myDao().setNuevaCantidadAComprar(nombreListaRecibido, listaProductosAcomprar.get(position).getNombreProducto(), Integer.valueOf(etCantidad.getText().toString()));
                            listaProductosAcomprar.get(position).setCantidadAComprar(Integer.valueOf(etCantidad.getText().toString()));
                            miAdaptadorProdAComprar.notifyDataSetChanged();
                            dialog.dismiss();
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
                        MainActivity.myAppDatabase.myDao().eliminarProductoEnLista(nombreListaRecibido, listaProductosAcomprar.get(position).getNombreProducto());
                        listaProductosAcomprar.remove(position);
                        miAdaptadorProdAComprar.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton qrFab = findViewById(R.id.qr);
        qrFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openQr = new Intent(ComprarPorLista.this, QR.class);
                // se abre la vista de la camara para escanear el código qr y agregar el producto.
                startActivityForResult(openQr, REQUEST_CODE_QR);
            }
        });

        FloatingActionButton changoFab = findViewById(R.id.moveChartCompraExpress);
        changoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openChango = new Intent(ComprarPorLista.this, Chango.class);
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
                    if ( nuevoProductoAIngresar(data) ) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ComprarPorLista.this);

                        builder.setTitle("Ingrese el producto al chango")
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    }


                    break;
                default:
                    Intent refreshActivity = new Intent(this, ComprarPorLista.class);
                    startActivity(refreshActivity);
                    this.finish();
            }
        }
    }

    private void anadirProducto(Producto producto){
        int montoTotal = Integer.valueOf(montoParcial.getText().toString());
        montoTotal += producto.getTotalPorProducto();
        montoParcial.setText(String.valueOf(montoTotal));
        eliminarEnListaAComprar(producto);
        listaProductosComprados.add(producto);
        miAdaptadorProdComprados.notifyDataSetChanged();
    }

    private void eliminarEnListaAComprar(Producto producto) {
        Iterator<LineaCompra> iteratorProducto = listaProductosAcomprar.iterator();
        LineaCompra productoActual = iteratorProducto.next();
        int i = 0;
        while (iteratorProducto.hasNext() && ! productoActual.getNombreProducto().equals(producto.getNombre())) {
            productoActual = iteratorProducto.next();
            i ++;
        }
        if(productoActual.getNombreProducto().equals(producto.getNombre()))
            listaProductosAcomprar.remove(i);

        miAdaptadorProdAComprar.notifyDataSetChanged();
    }

    private boolean nuevoProductoAIngresar(Intent data) {
        boolean nuevoProductoIngresado = false;
        final Producto productoNuevo = MainActivity.myAppDatabase.myDao().getProducto(data.getStringExtra("nombreProducto"));
        // SE VA A PODER EDITAR POR POPUP LA CANTIDAD
        if (productoNuevo != null) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(ComprarPorLista.this);
            final View miAlertDialog = LayoutInflater.from(ComprarPorLista.this).inflate(R.layout.alertdialog_producto, null);
            final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
            builder.setView(miAlertDialog);
            final AlertDialog dialog = builder.create();
            Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

            buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etCantidad.getText().toString().isEmpty() || Integer.valueOf(etCantidad.getText().toString()) <= 0) {
                        Toast.makeText(ComprarPorLista.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                    } else {
                        int cantidadNueva = Integer.valueOf(etCantidad.getText().toString());
                        productoNuevo.setCantidad(cantidadNueva);
                        int cantidadEnLista = getCantidadComprar(productoNuevo.getNombre());
                        if(!listaProductosComprados.contains(productoNuevo)){
                            if (cantidadEnLista >= 1) {
                                if (cantidadEnLista != cantidadNueva) {
                                    AlertDialog.Builder builderCantidad = new AlertDialog.Builder(ComprarPorLista.this)
                                            .setTitle("Cantidades a comprar diferentes")
                                            .setMessage("Si acepta se cargara a comprados la cantidad ingresada aunque sea distinta a la que busca.")
                                            .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    anadirProducto(productoNuevo);
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    builderCantidad.show();
                                } else {
                                    anadirProducto(productoNuevo);
                                }
                            }else {
                                anadirProducto(productoNuevo);
                            }
                        } else {
                            int i = 0;
                            Toast.makeText(ComprarPorLista.this, "Ese producto ya está agregado a la lista.", Toast.LENGTH_SHORT).show();
                            Iterator<Producto> iteratorProducto = listaProductosComprados.iterator();
                            Producto productoActual = iteratorProducto.next();
                            while (iteratorProducto.hasNext() && !productoNuevo.getNombre().equals(productoActual.getNombre())) {
                                productoActual = iteratorProducto.next();
                                i++;
                            }
                            int montoTotal = Integer.parseInt(montoParcial.getText().toString()) - listaProductosComprados.get(i).getTotalPorProducto();
                            listaProductosComprados.get(i).sumarCantidadActual(cantidadNueva);
                            montoParcial.setText(String.valueOf(listaProductosComprados.get(i).getTotalPorProducto() + montoTotal));
                            miAdaptadorProdComprados.notifyDataSetChanged();
                        }
                        dialog.dismiss();
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
            Toast.makeText(ComprarPorLista.this, "El producto scaneado no se encuentra cargado en el sistema.", Toast.LENGTH_LONG).show();
        }
        return nuevoProductoIngresado;
    }

    private int getCantidadComprar(String nombreProducto){
        Iterator<LineaCompra> iteratorProducto = listaProductosAcomprar.iterator();
        LineaCompra productoActual = iteratorProducto.next();
        while (iteratorProducto.hasNext() && ! productoActual.getNombreProducto().equals(nombreProducto)) {
            productoActual = iteratorProducto.next();
        }
        if(productoActual.getNombreProducto().equals(nombreProducto))
            return productoActual.getCantidadAComprar();
        else
            return -1;
    }

    /*
    private int openDialog(){

        ProductoDialog productoDialog = new ProductoDialog();
        productoDialog.show(getSupportFragmentManager(), "cantidadProdDialog");
        return cantidadComprar;
    }*/

    /*
    @Override
    public void aplicarCantidad(int cantidad) {
        cantidadComprar = cantidad;
        Log.e("COMPRAR_POR_LISTA","CANTIDAD " + cantidadComprar);
    }



    private int alertDialogNuevaCantidad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ComprarPorLista.this);
        View miAlertDialog = LayoutInflater.from(ComprarPorLista.this).inflate(R.layout.alertdialog_producto,null);
        final EditText etCantidad = (EditText) miAlertDialog.findViewById(R.id.editTextCantidadProducto);
        builder.setView(miAlertDialog);
        final AlertDialog dialog = builder.create();



        Button buttonAceptarAlertProducto = (Button) miAlertDialog.findViewById(R.id.buttonAlertDialogProductoAceptar);

        buttonAceptarAlertProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCantidad.getText().toString().isEmpty()){
                    Toast.makeText(ComprarPorLista.this, "Ingrese una cantidad valida", Toast.LENGTH_SHORT).show();
                }else{

                    final int auxiliar = 2;
                    dialog.dismiss();
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
    }
    */
}
