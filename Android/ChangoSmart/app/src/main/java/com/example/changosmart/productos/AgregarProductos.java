package com.example.changosmart.productos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import java.util.ArrayList;

public class AgregarProductos extends AppCompatActivity {
    private ArrayList<Producto> listaProductos;

    public static MiAdaptadorListaProductos miAdaptadorListaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaProductos = new ArrayList<Producto>();

        // Cargo la lista de los productos
        //Se castea porque retorna una clase List
        listaProductos = (ArrayList) MainActivity.myAppDatabase.myDao().getProductos();

        Toast.makeText(this,listaProductos.size(),Toast.LENGTH_SHORT);

        ListView listaProductosView = (ListView) findViewById(R.id.listViewProductos);

        //Seteo el adaptador y le paso la lista de los productos
        miAdaptadorListaProductos = new MiAdaptadorListaProductos(this, listaProductos);
        listaProductosView.setAdapter(miAdaptadorListaProductos);

        listaProductosView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Title");
                        final EditText input = new EditText(view.getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int cantidadAComprar = Integer.valueOf(input.getText().toString());

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }
        );

    }

}
