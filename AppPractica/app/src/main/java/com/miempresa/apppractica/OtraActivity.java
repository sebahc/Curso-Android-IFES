package com.miempresa.apppractica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OtraActivity extends AppCompatActivity {

    // Tag de prueba para ver en el logcat
    public static final String TAG_RECIBE="Recibe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // asocia xml layout con el la Activity
        setContentView(R.layout.activity_otra);

        // recupero el objeto intent de la otra clase

        Intent otroIntent = getIntent();

        String nombre = otroIntent.getStringExtra(MainActivity.EXTRA_NOMBRE);
        String apellido = otroIntent.getStringExtra(MainActivity.EXTRA_NOMBRE);
        String edad = otroIntent.getStringExtra(MainActivity.EXTRA_EDAD);
        String email = otroIntent.getStringExtra(MainActivity.EXTRA_EMAIL);

        // verificamos con un log si estamos enviando bien los datos
        Log.d(TAG_RECIBE, "Nombre: " + nombre + " Apellido: " + apellido+" Edad: "+edad+" Email: "+email);

        // asocio los textview con variables en esta clase en java

        TextView lblNombre = (TextView) findViewById(R.id.lblNombre);
        TextView lblApellido = (TextView) findViewById(R.id.lblApellido);
        TextView lblEdad = (TextView) findViewById(R.id.lblEdad);
        TextView lblEmail = (TextView) findViewById(R.id.lblEmail);

        // seteo cada dato recibido por el intent en las variables que cree anteriormente
        lblNombre.setText(nombre);
        lblApellido.setText(apellido);
        lblEdad.setText(edad);
        lblEmail.setText(email);

    }

}
