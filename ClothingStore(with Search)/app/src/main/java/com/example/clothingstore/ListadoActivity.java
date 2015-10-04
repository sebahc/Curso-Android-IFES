package com.example.clothingstore;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.example.clothingstore.model.ClothingContract;
import com.example.clothingstore.model.ShirtDataSource;

import java.sql.SQLException;

public class ListadoActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private ShirtDataSource dataSource;
    private SimpleCursorAdapter adapter;
    private Cursor mCursor;
    private ListView listView;
    private String mSearchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);


        // instanciamos una var tipo datasource y abrimos la conex

        dataSource = new ShirtDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();

        }

        // creamos 2 array

        // el primero de donde saco los datos para mostrar

        String[] fromColumns={ClothingContract.ShirtTable.COLUMN_DESCRIPTION,
                                ClothingContract.ShirtTable.COLUMN_PRICE};

        // el segundo array en q forma lo voy a mostrar

        int [] toViews={R.id.lblDescripcion,R.id.lblPrecio};

        // traigo los datos a mostrar y los guardo en un Cursor
        mCursor=dataSource.getAllShirt2();

        // instaciamos var tipo adapter

        adapter= new SimpleCursorAdapter(this,
                        R.layout.fila,
                        mCursor,
                        fromColumns,
                        toViews,
                        0);

        //recupero el listview de esta Activity

        listView=getListView();

        // asociamos el listener

        listView.setOnItemClickListener(this);

        // agregamos el header que creamos antes

        View header= getLayoutInflater().inflate(R.layout.header,null);
        listView.addHeaderView(header);

        // asocio el adapter con el listview

        listView.setAdapter(adapter);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


    }

    public void doMySearch(String query){

        dataSource = new ShirtDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        Cursor mCursor = dataSource.find(query);

        // reemplazo curso viejo x nuevo
        adapter.swapCursor(mCursor);

        // avisamos al adapter q cambiaron los datos a mostrar
        adapter.notifyDataSetChanged();


    }

    public void initListview(){
        mCursor=dataSource.getAllShirt2();
        // reemplazo curso viejo x nuevo
        adapter.swapCursor(mCursor);

        // avisamos al adapter q cambiaron los datos a mostrar
        adapter.notifyDataSetChanged();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        // Set listeners for SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryText) {
                // Nothing needs to happen when the user submits the search string
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the action bar search text has changed.  Updates
                // the search filter, and restarts the loader to do a new query
                // using the new search string.
                String newFilter = !TextUtils.isEmpty(newText) ? newText : null;

                // Don't do anything if the filter is empty
                if (mSearchTerm == null && newFilter == null) {
                    return true;
                }

                // Don't do anything if the new filter is the same as the current filter
                if (mSearchTerm != null && mSearchTerm.equals(newFilter)) {
                    return true;
                }

                // Updates current filter to new filter
                mSearchTerm = newFilter;

                doMySearch(mSearchTerm);
                return true;
            }
        });



            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    // Nothing to do when the action item is expanded
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    // When the user collapses the SearchView the current search string is
                    // cleared and the loader restarted.
                    if (!TextUtils.isEmpty(mSearchTerm)) {
                        getListView().clearChoices();
                    }
                    mSearchTerm = null;

                    initListview();

                    return true;
                }
            });


       return true;
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
        // realizo nueva consulta
        mCursor=dataSource.getAllShirt2();

        // reemplazo curso viejo x nuevo
        adapter.swapCursor(mCursor);

        // avisamos al adapter q cambiaron los datos a mostrar
        adapter.notifyDataSetChanged();


    }


}
