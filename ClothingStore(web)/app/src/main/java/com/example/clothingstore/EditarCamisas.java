package com.example.clothingstore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.clothingstore.model.Shirt;
import com.example.clothingstore.model.ShirtDataSource;

import java.sql.SQLException;

public class EditarCamisas extends Activity {

    // defino variables/prop que voy a usar
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private EditText txtStock;
    private RadioButton radioButton;

    private char colorCode;
    private ShirtDataSource dataSource;

    private Shirt mShirt;
    private long idShirt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_camisas);
        llenarDatos();
    }

    /*
    Este metodo es para recibir el id de la camisa y llenar los datos de nuestra Activity
     */
    public void llenarDatos(){
        // primero recupero el intent
        Bundle bundle=getIntent().getExtras();
        idShirt = bundle.getLong("idShirt");
        //otra forma de recibir el intent seria..
        //Intent mIntent = getIntent();
        //idShirt= mIntent.getLongExtra("idShirt", 0);

        // creamos la conex BD
        dataSource= new ShirtDataSource(this);

        // abrimos la conex
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // creo un obj tipo Shirt para guardar los datos recuperados
        mShirt= new Shirt();

        // realizo la consulta a BD y lo guardo
        mShirt=dataSource.getShirt(idShirt);

        // Asocio los view en XML con las variables que creamos anteriormente
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtPrecio=(EditText) findViewById(R.id.txtPrecio);
        txtStock=(EditText) findViewById(R.id.txtStock);

        // seteamos los datos de mShirt en los editText
        txtDescripcion.setText(mShirt.getDescription());

        // cuando el dato es numerico lo parseo a formato String
        txtPrecio.setText(String.valueOf(mShirt.getPrice()));
        txtStock.setText(String.valueOf(mShirt.getQuantityInStock()));

        //busco el RadioButton que corresponde al color por ID
        colorCode=mShirt.getColorCode();

        radioButton=(RadioButton) findViewById(colorAViewId(colorCode));

        // ponemos q quede selecionado el radiobutton
        radioButton.setChecked(true);

        //cerramos la conex a la BD
        dataSource.close();

    }
    /*
    Este metodo recibe el el color en formato char y devuelve el id del view correspondiente
     */
    public int colorAViewId(char color){
        int mId=0;

        switch (color){
            case 'N':
                return (mId=R.id.radioNoSeteado);
            case 'A':
                return (mId=R.id.radioAzul);
            case 'R':
                return (mId=R.id.radioRojo);
            case 'V':
                return (mId=R.id.radioVerde);
        }

        return mId;
    }

    public void guardarEditar(View view){
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String descripcion= txtDescripcion.getText().toString();
        Double precio= Double.parseDouble(txtPrecio.getText().toString());
        int stock= Integer.parseInt(txtStock.getText().toString());

        // recuperamos el color con el metodo clickradioButton

        //en realidad mi Objeto Shirt ya tiene cargado el id anteriormente, desde el llenarDatos()
        // pero
        mShirt.setID(idShirt);
        mShirt.setDescription(descripcion);
        mShirt.setPrice(precio);
        mShirt.setQuantityInStock(stock);
        mShirt.setColorCode(colorCode);

        dataSource.updateShirt(mShirt);

        dataSource.close();

        // Creamos un objeto del tipo Dialog para mostrar que los cambios
        // fueron guardados

        AlertDialog.Builder dialogo= new AlertDialog.Builder(this);
        dialogo.setMessage("Cambios Guardados");
        dialogo.setCancelable(false);
        dialogo.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cuando hago click en OK finalizo la Activity
                finish();
            }
        });

        dialogo.show();
    }

    public void clickRadioButton(View view){

        radioButton=(RadioButton) view; // el view q recibo como parametro lo asocio a una var RadioButton
        boolean chequeado = radioButton.isChecked(); // almaceno en una var Booleana el estado del RadioButton

        // De acuerdo al radiobutton que fue clickeado asignamos el color a colorCode

        switch (view.getId()){
            case R.id.radioNoSeteado:
                if (chequeado) colorCode='N';
                break;
            case R.id.radioRojo:
                if (chequeado) colorCode='R';
                break;
            case R.id.radioAzul:
                if(chequeado) colorCode='A';
                break;
            case R.id.radioVerde:
                if (chequeado) colorCode='V';
                break;
        }
    }

    public void eliminarEditar(View view){
        // abrimos la conex
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Importante");
        dialogo.setMessage("Esta seguro que desea eliminar esta camisa?");
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // usamos un metodo del datasource para borrar un registro pasando el ID
                dataSource.deleteShirt(idShirt);
                // cerramos la conex y la Activity
                dataSource.close();
                finish();

            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cuando cancelamos no hace nada en particular, solo cerramos la conex
                dataSource.close();

            }
        });

        dialogo.show();

    }

    public void volverEditar(View view){
        finish();
    }


}