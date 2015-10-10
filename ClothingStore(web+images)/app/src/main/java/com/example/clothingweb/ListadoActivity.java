package com.example.clothingweb;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.clothingweb.Utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ListadoActivity extends ListActivity implements AdapterView.OnItemClickListener{

    public static String TAG="Listado";
    private ListView listView;
    private ListAdapter adapter;

    // Progress Dialog
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> shirtsList = new ArrayList<HashMap<String, String>>();;

    // url to get all products list
    private static String url_all_shirts = "http://api.latamlabs.com/back_clothing/get_all_shirts2.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SHIRTS = "shirts";
    private static final String TAG_ID = "id_shirt";
    private static final String TAG_DESCRIPCION = "description";
    private static final String TAG_PRECIO = "price";
    private static final String TAG_URL = "image_url";

    // products JSONArray
    JSONArray shirts = null;
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);


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

      //  View header= getLayoutInflater().inflate(R.layout.header,null);
       // listView.addHeaderView(header);


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

            // recupero el id desde la fila

            String idShirt = ((TextView) view.findViewById(R.id.idShirt)).getText().toString();
            Log.d("Listado",idShirt);

            Intent mIntent= new Intent(getApplicationContext(),EditarCamisas.class);
            mIntent.putExtra("idShirt",idShirt);

            // starting new activity and expecting some response back
            startActivityForResult(mIntent, 100);
            //startActivity(mIntent);
    }

    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100) {
            // if result code 100
            if (resultCode == 100) {
                // if result code 100 is received
                // means user edited/deleted product
                // reload this screen again

                Log.d("Listado", "cambio el dataset");
                shirtsList.clear();

                new LoadAllProducts().execute();
            }
        }

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

            Log.d("Listado","doInBack..");


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            // agrego un parametro random para q mi request sea spre diferente
            Random mRandom= new Random();
            int randomInt = mRandom.nextInt(100);

            params.add(new BasicNameValuePair("rnd", String.valueOf(randomInt)));

            // getting JSON string from URL

            JSONObject json = jParser.makeHttpRequest(url_all_shirts, "GET", params);

            // Check your log cat for JSON reponse
            //Log.d("All Products: ", json.toString());


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
                            String id = c.getString(TAG_ID);
                            String descripcion = c.getString(TAG_DESCRIPCION);
                            String precio = c.getString(TAG_PRECIO);
                            String url = c.getString(TAG_URL);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_ID,id);
                            map.put(TAG_DESCRIPCION, descripcion);
                            map.put(TAG_PRECIO, precio);
                            map.put(TAG_URL, url);

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
                    /*adapter = new SimpleAdapter(
                            ListadoActivity.this,
                            shirtsList,
                            R.layout.fila,
                            new String[] { TAG_ID,TAG_DESCRIPCION, TAG_PRECIO},
                            new int[] { R.id.idShirt,R.id.lblDescripcion,R.id.lblPrecio});*/
                    adapter = new ProductsAdapter(ListadoActivity.this,shirtsList);
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }

    class ProductsAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        ArrayList<HashMap<String, String>> mProductList;

        ProductsAdapter(Context context,ArrayList productList ){
            mContext=context;
            mProductList=productList;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mProductList==null ? 0: mProductList.size();
        }

        @Override
        public Object getItem(int position) {
            if(mProductList != null && mProductList.size()>position){
                return mProductList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;


            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.fila, null);

                holder = new ViewHolder();
                holder.mImagen=((ImageView) convertView.findViewById(R.id.imgImagen));
                holder.descripcion=(TextView)convertView.findViewById(R.id.lblDescripcion);
                holder.precio=(TextView) convertView.findViewById(R.id.lblPrecio);
                holder.id=(TextView) convertView.findViewById(R.id.idShirt);

                convertView.setTag(holder);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> product = (HashMap<String, String>)getItem(position);


            holder.descripcion.setText(product.get(TAG_DESCRIPCION));
            holder.precio.setText(product.get(TAG_PRECIO));
            holder.id.setText(product.get(TAG_ID));

            Log.d("Product_Url",product.get(TAG_URL));

            if (!product.get(TAG_URL).isEmpty()) {
                Picasso
                        .with(ListadoActivity.this)
                        .load(product.get(TAG_URL))
                        .into(holder.mImagen);
            }else{
                holder.mImagen.setImageResource(R.drawable.ic_launcher);
            }

            return convertView;


        }

        private class ViewHolder {
            ImageView mImagen;
            TextView id;
            TextView descripcion;
            TextView precio;
        }
    }


}
