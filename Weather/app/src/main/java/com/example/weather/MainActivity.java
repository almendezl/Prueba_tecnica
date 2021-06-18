package com.example.weather;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /**
     * Conexion logica con la interfaz
     * Codigo: ANGIE MENDEZ
     */

    EditText editText_ciudad;
    ListView listView_ciudades;
    ArrayList<String> ciudades = new ArrayList<String>();
    ArrayAdapter adaptador_ciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enlazar con los elementos
        editText_ciudad = findViewById(R.id.edit_textCiudad);
        listView_ciudades = findViewById(R.id.listViewCiudades);

        //cargar las ciudades predeterminadas
        ciudades.add("MIAMI");
        ciudades.add("LONDRES");
        ciudades.add("CARACAS");
        ciudades.add("BOGOTA");

        adaptador_ciudades = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ciudades);
        listView_ciudades = findViewById(R.id.listViewCiudades);
        listView_ciudades.setAdapter(adaptador_ciudades);

        listView_ciudades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                     @Override
                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                         Intent i = new Intent(MainActivity.this, DetalleClima.class);
                                                         i.putExtra("ciudad", ciudades.get(position));
                                                         startActivity(i);

                                                     }
                                                 }

        );
        listView_ciudades.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Importante");
                alerta.setMessage("¿ Desea eliminar la ciudad ?");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        ciudades.remove(position);
                        adaptador_ciudades.notifyDataSetChanged();
                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface alerta, int id) {
                        alerta.dismiss();
                    }
                });
                alerta.show();

                return true;
            }
        });
    }

    /**
     * Revisar tildes en el tecto que ingresan, espacios
     */
    //Metodo para el boton de agregar ciudad
    public void agregar_ciudad(View view) {
        String ciudad = editText_ciudad.getText().toString();
        //ciudad = StringUtils.stripAccents(ciudad);
        if (!ciudad.isEmpty()) {
            //verifica si ya existe la ciudad
            if (ciudades.contains(ciudad)) {
                Toast.makeText(this, "La ciudad ya se encuentra agregada", Toast.LENGTH_SHORT).show();
            } else {
                //agrega la ciudad
                ciudades.add(ciudad.toUpperCase());
                adaptador_ciudades.notifyDataSetChanged();
                //limpia el campo
                editText_ciudad.setText("");
            }


        } else {
            //si el campo esta vacio
            Toast.makeText(this, "Debe ingresar una ciudad!!", Toast.LENGTH_SHORT).show();
        }

    }
}