package com.example.clothingstore;

import android.os.Bundle;
import android.app.Activity;

import android.view.View;

public class AgregarCamisas extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // llamo el metodo original de la Clase Activity (Superclase)
		setContentView(R.layout.activity_agregar_camisas);
		
	}

	
	public void volverAgregar(View view){
		// regreso a la activity anterior
		finish();
	}	
	


}
