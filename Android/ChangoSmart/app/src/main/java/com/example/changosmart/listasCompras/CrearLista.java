package com.example.changosmart.listasCompras;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.changosmart.MainActivity;
import com.example.changosmart.R;

import BD.MisListasCompraTabla;

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
                    Toast.makeText(view.getContext(), "Ingrese un nombre de Lista valido", Toast.LENGTH_SHORT).show();
                    etNombreLista.requestFocus();
                }else {
                    // Creo el objeto que se guardara en la base ........
                    MisListasCompraTabla listaCompra = new MisListasCompraTabla(nombreLista, supermercado);

                    // Limpio los editText
                    etNombreLista.setText("");
                    etSupermercado.setText("");

                    try {
                        // Inserto en la base el objeto creado
                        MainActivity.myAppDatabase.myDao().addLista(listaCompra);

                        // Muestro mensaje de Lista Creada
                        Toast.makeText(view.getContext(), "Lista Creada", Toast.LENGTH_SHORT).show();

                        Intent intentMisListas = new Intent(view.getContext(), MisListas.class);
                        intentMisListas.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intentMisListas);

                        // Eliminamos la actividad actual para que no quede viva
                        setResult(RESULT_OK, null);
                        finish();

                    }catch(SQLiteException e){
                        Toast.makeText(view.getContext(),"Esta lista ya se encuentra creada para este supermecado.",Toast.LENGTH_SHORT).show();
                    };
                }
            }
          }
        );
    }
}
