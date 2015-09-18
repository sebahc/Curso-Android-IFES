package com.example.clothingweb;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.clothingweb.Utils.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditarCamisas extends Activity {

    // defino variables/prop que voy a usar
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private EditText txtStock;
    private RadioButton radioButton;

    private String descripcion="";
    private String precio="";
    private String stock="";
    private char colorCode='N';
    String image_url="";

    private String idShirt;

     // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single product url
    private static final String url_shirt_details = "http://api.latamlabs.com/back_clothing/get_shirt_details.php";

    // url to update product
    private static final String url_update_shirt = "http://api.latamlabs.com/back_clothing/update_shirt.php";

    // url to delete product
    private static final String url_delete_shirt = "http://api.latamlabs.com/back_clothing/delete_shirt.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    private static final String TAG_SHIRT = "shirt";
    private static final String TAG_ID = "id";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_IMAGE = "image_url";
    private static final String TAG_PRICE = "price";
    private static final String TAG_COLOR = "color";
    private static final String TAG_STOCK = "qtystock";




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


        Intent mIntent = getIntent();
        idShirt= mIntent.getStringExtra("idShirt");

        // Getting complete product details in background thread
        new GetProductDetails().execute();


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

        descripcion= txtDescripcion.getText().toString();
        precio= txtPrecio.getText().toString();
        stock= txtStock.getText().toString();

        // colorCode lo obtuve con el otro metodo


        // starting background task to update product
        new SaveProductDetails().execute();


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

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Importante");
        dialogo.setMessage("Esta seguro que desea eliminar esta camisa?");
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // usamos un metodo del datasource para borrar un registro pasando el ID

                // deleting product in background thread
                new DeleteProduct().execute();


            }
        });

        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cuando cancelamos no hace nada en particular, solo cerramos la conex


            }
        });

        dialogo.show();

    }

    public void volverEditar(View view){
        finish();
    }

    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditarCamisas.this);
            pDialog.setMessage("Cargando camisa. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(TAG_ID, idShirt));

                        Log.d("Single Shirt Details", idShirt);

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_shirt_details, "GET", params);

                        // check your log for json response
                        Log.d("Single Shirt Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray shirtObj = json
                                    .getJSONArray(TAG_SHIRT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject shirt = shirtObj.getJSONObject(0);


                            // Asocio los view en XML con las variables que creamos anteriormente
                            txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
                            txtPrecio=(EditText) findViewById(R.id.txtPrecio);
                            txtStock=(EditText) findViewById(R.id.txtStock);

                            // seteamos los datos de Shirt en los editText
                            txtDescripcion.setText(shirt.getString(TAG_DESCRIPTION));
                            txtPrecio.setText(shirt.getString(TAG_PRICE));
                            txtStock.setText(shirt.getString(TAG_STOCK));

                            //busco el RadioButton que corresponde al color por ID
                            colorCode=shirt.getString(TAG_COLOR).charAt(0);

                            radioButton=(RadioButton) findViewById(colorAViewId(colorCode));

                            // ponemos q quede selecionado el radiobutton
                            radioButton.setChecked(true);


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditarCamisas.this);
            pDialog.setMessage("Guardando camisa...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ID, idShirt));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, descripcion));
            params.add(new BasicNameValuePair(TAG_IMAGE, image_url));
            params.add(new BasicNameValuePair(TAG_PRICE, precio));
            params.add(new BasicNameValuePair(TAG_COLOR, Character.toString(colorCode)));
            params.add(new BasicNameValuePair(TAG_STOCK, stock));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_shirt,
                    "POST", params);



            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated

                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();


                } else {
                    // failed to update product
                    Log.d("EditarCamisas","hubo un eror!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();


        }
    }

    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditarCamisas.this);
            pDialog.setMessage("Borrando Camisa...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_ID,idShirt));

                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_shirt, "POST", params);

                // check your log for json response
                Log.d("Delete Product", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // product successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about product deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();

        }

    }


}