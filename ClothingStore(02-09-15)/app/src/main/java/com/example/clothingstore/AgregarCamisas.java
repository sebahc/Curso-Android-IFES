package com.example.clothingstore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.clothingstore.model.Shirt;
import com.example.clothingstore.model.ShirtDataSource;

import java.sql.SQLException;

public class AgregarCamisas extends Activity {

	private EditText txtDescripcion;
	private EditText txtPrecio;
	private EditText txtStock;
	private RadioButton radioColor;

	private String descripcion;
	private double precio;
	private int stock;
	private char colorCode;
	private ShirtDataSource dataSource;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // llamo el metodo original de la Clase Activity (Superclase)
		setContentView(R.layout.activity_agregar_camisas);
		
	}

	public void guardarAgregar(View view){
		txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
		txtPrecio = (EditText)findViewById(R.id.txtPrecio);
		txtStock = (EditText) findViewById(R.id.txtStock);
		// el codigo de color lo seteo con otro metodo

		descripcion=txtDescripcion.getText().toString();
		precio=Double.parseDouble(txtPrecio.getText().toString());
		stock=Integer.parseInt(txtStock.getText().toString());

		// TODO: validar los datos

		Shirt mShirt = new Shirt();
		mShirt.addShirt(descripcion,colorCode,precio,stock);

		dataSource= new ShirtDataSource(this);
		try {
			dataSource.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dataSource.addShirt(mShirt);

		dataSource.close();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Nueva Camisa Guardada");
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				txtDescripcion.setText("");
				txtPrecio.setText("");
				txtStock.setText("");

			}
		});

		builder.show();

	}

	public void clickRadioButton(View view){
		radioColor =(RadioButton) view;
		boolean seleccionado= radioColor.isChecked();

		switch (view.getId()){
			case R.id.radioNoSeteado:
				if (seleccionado) colorCode='N';
				break;
			case R.id.radioRojo:
				if(seleccionado) colorCode='R';
				break;
			case R.id.radioAzul:
				if(seleccionado) colorCode='A';
				break;
			case R.id.radioVerde:
				if(seleccionado) colorCode='V';
				break;
		}

	}

	
	public void volverAgregar(View view){
		// regreso a la activity anterior
		finish();
	}	
	


}
