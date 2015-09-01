package com.example.clothingstore;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class EditarCamisas extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_camisas);

    }



    public void volverEditar(View view){
        finish();
    }


}
