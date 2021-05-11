package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import ModeloDominio.Lista;

/**
 * Esta clase define la actividad (llamada "activity_login") que servirá para el logueo del Usuario
 * en la aplicación
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */

public class CrearLista extends AppCompatActivity {
    //Representa el CardView que implica la creación de una lista compartida
    private CardView cardViewgrupo;
    //Representa el CardView que implica la creación de una lista personal
    private CardView cardViewsola;
    //Representa el email del usuario que está logueado en la aplicación
    private String email;
    //Representa el nick que tiene el usuario que está logueado en la aplicación
    private String nick;
    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_pantalla_listas"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lista);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.crearlistaToolbar);
        myToolbar.setTitle("SuperCarrito-Crear Lista");

        cardViewsola = (CardView)findViewById(R.id.lista_personal_card);
        cardViewgrupo = (CardView)findViewById(R.id.grupo_compra_card);
        email=getIntent().getExtras().get("email").toString();
        nick=getIntent().getStringExtra("nick");
        registerForContextMenu(cardViewsola);
        registerForContextMenu(cardViewgrupo);
        cardViewgrupo.setOnClickListener(new View.OnClickListener(){

            /**
             * Método que sirve sacar un menú contextual para indicar el nombre de la lista que se
             *  va a crear
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                abrirFragment("grupal");
            }
        });

        /**
         * Método que sirve sacar un menú contextual para indicar el nombre de la lista que se
         *  va a crear
         * @param v Representa al objeto View sobre el cual se ha hecho click
         */
        cardViewsola.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                abrirFragment("sola");
            }
        });

    }

    /**
     *  Método que abre el fragment introducir_nom_lista y saca la información cuando este se cierra
     * @param tipoLista Representa el tipo de lista del cual desea crear el usuario la lista
     */
    public void abrirFragment(String tipoLista){
        introducir_nom_lista listaDialogFragment = new introducir_nom_lista(tipoLista,email,nick);
        listaDialogFragment.show(getSupportFragmentManager(),"tag");

        /*
        String ultB = listaDialogFragment.getUltBoton();
        listaDialogFragment.getLifecycle().getCurrentState();
        while(!listaDialogFragment.isCancelable()){

        }
        if(ultB.length() > 0){
            if(ultB.equals("Aceptar")){
                Intent intent = new Intent(this, ListaProductos.class);
                intent.putExtra("nick",nick);
                intent.putExtra("email",email);
                intent.putExtra("nombreLista",listaDialogFragment.getNombreLista());
                intent.putExtra("idLista",String.valueOf(Lista.getContLista()));
                startActivity(intent);
            }
        }
        */
    }

    public String getNick(){return nick;}


}