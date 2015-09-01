package com.example.clothingstore;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.clothingstore.model.ShirtDataSource;

public class ListadoActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private ShirtDataSource dataSource;
    private SimpleCursorAdapter adapter;
    private Cursor mCursor;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
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

    }
}
