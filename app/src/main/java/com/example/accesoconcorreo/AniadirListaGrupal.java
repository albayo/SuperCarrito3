package com.example.accesoconcorreo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * Esta clase define el DialogFragment (llamada "introducir_usuarios_lista_grupal") que servirá para que el usuario pueda añadir una lista grupal
 * en la aplicación
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
public class AniadirListaGrupal extends DialogFragment {

    //Representa el EditText en el cual habrá que introducir el nombre que se le querra dar a la lista
    private EditText etNombre;
    //Representa el botón de Aceptar del Fragment
    private Button btnAceptar;
    //Representa el botón de Cancelar del Fragment
    private Button btnCancelar;
    //Representa el nick del usuario a añadir a la lista grupal
    private String nick;
    //Represent el email del usuario a añadir a la lista grupal
    private String email;
    //Representa el nombre de la lista a la que se quiere añadir el usuario
    private String nombreLista;
    //Representa el id de la lista a la que se quiere añadir el usuario
    private String idLista;
    //Representa el nombre del último botón en el cual se ha hecho click
    private String ultBoton = "";
    //Representa una referencia a la BD
    private DatabaseReference mDatabase;

    /**
     * Constructor base
     * @param
     */
    public AniadirListaGrupal(String nombreLista, String idLista) {
        this.nombreLista = nombreLista;
        this.idLista = idLista;
    }

    /**
     * Devuelve el nombre del último botón que se a clickado o la cadena vacía en caso de que no se haya clickado ninguno
     * @return ultBoton
     */
    public String getUltBoton() {
        return this.ultBoton;
    }

    /**
     * Devuelve el nick del usuario que se quiere añadir a la lista
     * @return
     */
    public String getNick() {
        return this.nick;
    }

    /**
     * Método que inicializa las componentes del dialogfragment
     * @param view reprenta la vista sobre la que se ejecutará el Fragment
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
        etNombre = view.findViewById(R.id.usuario_aniadir);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar_grupal);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar_grupal);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en el campo corresponde a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                ultBoton = "Aceptar";
                String nick = etNombre.getText().toString();
                if (nick != null && nick.trim().length() > 0) {
                    email = mDatabase.child("users").child(nick).child("email").get().getResult().getValue().toString();
                    //ReadAndWriteSnippets.aniadirUsuarioaList(nombreLista,nick,idLista,email);
                    cerrarFragment();

                    Intent intent = new Intent(getContext(), ListaProductos.class);
                    intent.putExtra("nombreLista", nombreLista);
                    intent.putExtra("idLista", Lista.getContLista());
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Error, se debe introducir un nick para el usuario", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña corresponden a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                ultBoton = "Cerrar";
                cerrarFragment();
            }
        });
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.introducir_usuarios_lista_grupal, container, false);
    }

    /**
     * Método que sirve para cerrar el Fragment
     */
    private void cerrarFragment() {

        getActivity().onBackPressed();
        //getFragmentManager().beginTransaction().remove(this).commit();
            /*Intent intent = new Intent(CrearLista.class, ListaProductos.class);
            startActivity(intent);*/
        //getFragmentManager().beginTransaction().remove(this).commit(); //no funciona, mirar con logger
    }
}
