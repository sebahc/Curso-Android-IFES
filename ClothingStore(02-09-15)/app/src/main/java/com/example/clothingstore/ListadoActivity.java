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

import com.example.clothingstore.model.ClothingContract;
import com.example.clothingstore.model.ShirtDataSource;

import java.sql.SQLException;

public class ListadoActivity extends ListActivity implements AdapterView.OnItemClickListener{

    private ShirtDataSource dataSource;
    private SimpleCursorAdapter adapter;
    private Cursor mCursor;
    private ListView listView;

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
                        R.layout.fila,mCursor,
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
