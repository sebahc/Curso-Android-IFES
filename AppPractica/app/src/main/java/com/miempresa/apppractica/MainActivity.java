package com.miempresa.apppractica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.miempresa.apppractica.utils.StringUtils;

public class MainActivity extends AppCompatActivity {

    // Tag de prueba para ver en el logcat
    public static final String TAG_ENVIA="Envio";

    // Cadenas Extra para usar de Key para pasar el mensaje x el intent
    public static final String EXTRA_NOMBRE="Nombre";
    public static final String EXTRA_APELLIDO="Apellido";
    public static final String EXTRA_EDAD="Edad";
    public static final String EXTRA_EMAIL="Email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
    }


    public void EnviarDatos(View view){
        // creo variables del tipo EditText y la asocio con el view en xml
        EditText txtNombre = (EditText) findViewById(R.id.txtNombre);
        EditText txtApellido = (EditText) findViewById(R.id.txtApellido);
        EditText txtEdad = (EditText) findViewById(R.id.txtEdad);
        EditText txtEmail = (EditText) findViewById(R.id.txtEmail);

        // creo variables del tipo String para guardar lo que el usuario ingreso
        String nombre= txtNombre.getText().toString();
        String apellido= txtApellido.getText().toString();
        String edad= txtEdad.getText().toString();
        String email= txtEmail.getText().toString();

        boolean esValido=true;

        esValido &= !StringUtils.isNullOrEmpty(nombre);
        esValido &= !StringUtils.isNullOrEmpty(apellido);
        esValido &= !StringUtils.isNullOrEmpty(edad);
        esValido &= !StringUtils.isNullOrEmpty(email);

        if(!esValido){
            Toast.makeText(this,"Debe completar todos los campos!",Toast.LENGTH_SHORT).show();
            return;

        }

        // creo un objeto del tipo intent

        Intent miIntent = new Intent(this,OtraActivity.class);

        // seteamos los parametros que vamos a enviar en el intent
        miIntent.putExtra(EXTRA_NOMBRE,nombre);
        miIntent.putExtra(EXTRA_APELLIDO,apellido);
        miIntent.putExtra(EXTRA_EDAD,edad);
        miIntent.putExtra(EXTRA_EMAIL,email);

        // verificamos con un log si estamos enviando bien los datos
        Log.d(TAG_ENVIA, "Nombre: "+nombre+" Apellido: "+apellido+" Edad: "+edad+" Email: "+email);

        // ahora llamamos a la otra Activity

        startActivity(miIntent);

    }


}
