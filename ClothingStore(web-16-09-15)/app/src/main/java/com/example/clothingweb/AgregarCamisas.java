package com.example.clothingweb;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.clothingweb.Utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AgregarCamisas extends Activity {

	private EditText txtDescripcion;
	private EditText txtPrecio;
	private EditText txtStock;
	private RadioButton radioColor;

	private String descripcion="";
	private String precio="";
	private String stock="";
	private char colorCode='N';
	String image_url="";


	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();

	// url to create new product
	private static String url_create_product = "http://api.latamlabs.com/back_clothing/create_shirt.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";




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
		precio=txtPrecio.getText().toString();
		stock=txtStock.getText().toString();

		// TODO: validar los datos


		new CreateNewProduct().execute();




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

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		int success=0;



		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AgregarCamisas.this);
			pDialog.setMessage("Creando Camisa..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("description", descripcion));
			params.add(new BasicNameValuePair("image_url", image_url));
			params.add(new BasicNameValuePair("price", precio));
			params.add(new BasicNameValuePair("color", Character.toString(colorCode)));
			params.add(new BasicNameValuePair("qtystock", stock));


			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				success = json.getInt(TAG_SUCCESS);


			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarCamisas.this);

			if (success == 1) {
				// successfully created product

					builder.setMessage("Nueva Camisa Guardada");
					builder.setCancelable(false);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							txtDescripcion.setText("");
							txtPrecio.setText("");
							txtStock.setText("");

							radioColor= (RadioButton) findViewById(R.id.radioNoSeteado);
							radioColor.setChecked(true);


						}
					});


			} else {

				// failed to create product

					builder.setMessage("Hubo un error, intente de nuevo..");
					builder.setCancelable(false);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {


						}
					});

			}

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					builder.show();
				}
			});
		}

	}

	


}
