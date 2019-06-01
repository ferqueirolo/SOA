package com.example.changosmart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import BD.ListaCompraE;

public class CrearLista extends AppCompatActivity {
    private EditText etNombreLista,
                     etSupermercado;
    private Button buttonCommitLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lista);

        etNombreLista = this.findViewById(R.id.editTextNombreLista);
        etSupermercado = this.findViewById(R.id.editTextSupermercado);

        // Creo el boton para comitear los datos en la base
        buttonCommitLista = (Button) findViewById(R.id.buttonCommitLista);
        // Seteo el listener y lo creo adentro
        buttonCommitLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Variables para crear la Lista
                String nombreLista = etNombreLista.getText().toString();
                String supermercado= etSupermercado.getText().toString();

                if( nombreLista.isEmpty()) {
                    // Muestro mensaje
                    Toast.makeText(view.getContext(), "Ingrese un nombre de Lista", Toast.LENGTH_SHORT).show();
                    etNombreLista.requestFocus();
                }else {
                    // Creo el objeto que se guardara en la base ........
                    ListaCompraE listaCompra = new ListaCompraE(nombreLista, supermercado);
                    // Limpio los editText
                    etNombreLista.setText("");
                    etSupermercado.setText("");
                    // Inserto en la base el objeto creado
                    MainActivity.myAppDatabase.myDao().addLista(listaCompra);
                    // Muestro mensaje
                    Toast.makeText(view.getContext(),"Lista Creada",Toast.LENGTH_SHORT).show();
                }
            }
          }
        );
    }
}
