package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class CrearLista extends AppCompatActivity {
    //Representa el CardView que implica la creación de una lista compartida
    private CardView cardViewgrupo;

    //Representa el CardView que implica la creación de una lista personal
    private CardView cardViewsola;
    private String email;
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
        cardViewsola = (CardView)findViewById(R.id.lista_personal_card);
        cardViewgrupo = (CardView)findViewById(R.id.grupo_compra_card);
        email=getIntent().getExtras().get("email").toString();
        nick=getIntent().getStringExtra("nick");
        registerForContextMenu(cardViewsola);
        registerForContextMenu(cardViewgrupo);

        //usuario=firebaseAuth.getCurrentUser();
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
     *
     * @param tipoLista Representa
     */
    public void abrirFragment(String tipoLista){
        introducir_nom_lista listaDialogFragment = new introducir_nom_lista(tipoLista,email,nick);
        listaDialogFragment.show(getSupportFragmentManager(),"tag");

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
                startActivity(intent);
            }
        }
    }


}