package com.example.accesoconcorreo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import ModeloDominio.Usuario;


public class pantalla_listas extends AppCompatActivity {
    //Representa el RecyclerView en el cual se dispondrán las Listas del Usuario
    private RecyclerView recyclerView;

    //Representa al Usuario que se ha logueado (para sacar las listas del Usuario entre otras cosas)
    private Usuario usuario = null;

    /**
     * Método que sirve para guardar información obtenida de la BD una vez que se pierde el foco de la actividad
     *  y así ahorrarse accesos posteriores innecesarios
     * @param outState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onSaveInstanceState(outState);



    }

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_pantalla_listas"
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(savedInstanceState != null){
            usuario = (Usuario) savedInstanceState.getSerializable("usuario");


        }else {
            Intent intent = getIntent();
            usuario = (Usuario) intent.getSerializableExtra("usuario");

            //sacar la imagen del usuario y las listas de la BD
        }

        FloatingActionButton fabAniadirLista = findViewById(R.id.fabAniadir_lista);

        recyclerView=this.findViewById(R.id.recycler_lista_super_prod);
        //listaListAdapter=new ListaListAdapter(this.getApplicationContext(),usuario,superVM.getlListas().getValue());
    }
}