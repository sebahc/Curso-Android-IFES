package com.example.clothingstore;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.clothingstore.Utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListadoActivity extends ListActivity implements AdapterView.OnItemClickListener{

    public static String TAG="Listado";
    private ListView listView;

    // Progress Dialog
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> shirtsList;

    // url to get all products list
    private static String url_all_shirts = "http://api.latamlabs.com/back_clothing/get_all_shirts.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SHIRTS = "shirts";
    private static final String TAG_DESCRIPCION = "description";
    private static final String TAG_PRECIO = "price";

    // products JSONArray
    JSONArray shirts = null;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);


        // Hashmap for ListView
        shirtsList = new ArrayList<HashMap<String, String>>();

        if (isOnline()) {

            // Loading products in Background Thread
            new LoadAllProducts().execute();

        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Red Desconectada...");
            alertDialog.setMessage("Conectese a una Red e intente nuevamente");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    // here you can add functions
                }
            });
            //alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.show();
        }


        listView=getListView();

        // asociamos el listener

        listView.setOnItemClickListener(this);

        // agregamos el header que creamos antes

        View header= getLayoutInflater().inflate(R.layout.header,null);
        listView.addHeaderView(header);


    }

    public void volverListado(View view){
        finish();

    }

    public void agregarCamisa(View view){
        Intent mIntent = new Intent(this, AgregarCamisas.class);
        startActivity(mIntent);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent mIntent= new Intent(getApplicationContext(),EditarCamisas.class);
            mIntent.putExtra("idShirt",id);
            startActivity(mIntent);
    }

    @Override
    protected void onRestart(){
        super.onRestart();

    }


    /*
    Check Connectivity
     */
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting()
                || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting())
            return true;
        else
            return false;
    }


    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListadoActivity.this);
            pDialog.setMessage("Cargando Items. Por Favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL

            JSONObject json = jParser.makeHttpRequest(url_all_shirts, "GET", params);


            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

              try {


                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // products found
                        // Getting Array of Products
                        shirts = json.getJSONArray(TAG_SHIRTS);

                        // looping through All Products
                        for (int i = 0; i < shirts.length(); i++) {
                            JSONObject c = shirts.getJSONObject(i);

                            // Storing each json item in variable
                            String descripcion = c.getString(TAG_DESCRIPCION);
                            String precio = c.getString(TAG_PRECIO);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_DESCRIPCION, descripcion);
                            map.put(TAG_PRECIO, precio);

                            // adding HashList to ArrayList
                            shirtsList.add(map);
                        }
                    } else {
                        Log.d(TAG,"no shirts found");
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ListadoActivity.this,
                            shirtsList,
                            R.layout.fila,
                            new String[] { TAG_DESCRIPCION, TAG_PRECIO},
                            new int[] { R.id.lblDescripcion,R.id.lblPrecio});
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }


}
