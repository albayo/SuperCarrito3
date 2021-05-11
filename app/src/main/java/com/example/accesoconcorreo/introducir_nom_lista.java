package com.example.accesoconcorreo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * Esta clase define el fragmento llamado "fragment_introducir_nom_lista2" que sirve para introducit un nombre para una nueva lista.
 *  * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 *  * @version: 30/04/2021
 */
public class introducir_nom_lista extends DialogFragment {

    //Representa el EditText en el cual habrá que introducir el nombre que se le querra dar a la lista
    private EditText etNombre;

    //Representa el botón de Aceptar del Fragment
    private Button btnAceptar;

    //Representa el botón de Cancelar del Fragment
    private Button btnCancelar;

    //Representa una cadena que indica el tipo de lista que se tendrá que crear
    private String tipoLista;
    private String email;
    private String nick;
    private String nombreLista;


    //Representa el nombre del último botón en el cual se ha hecho click
    private String ultBoton="";

    /**
     * Constructor
     *
     * @param tipoLista Define si la lsita es personal o grupal
     * @param email Representa el correo del usuaria al que está creando la lista
     * @param nick Representa el nick del usuaria al que está creando la lista
     */
    public introducir_nom_lista(String tipoLista,String email,String nick){
        this.email=email;
        this.nick=nick;
        this.tipoLista = tipoLista;
    }

    /**
     * Devuelve el nombre del último botón que se a clickado o la cadena vacía en caso de que no se haya clickado ninguno
     * @return ultBoton
     */
    public String getUltBoton(){
        return this.ultBoton;
    }

    /**
     * Devuelve el nombre de la lista
     * @return nombreLista
     */
    public String getNombreLista(){
        return this.nombreLista;
    }

    /**
     * Método que inicializa las componentes del dialogfragment
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReadAndWriteSnippets.actualizaContadorListas();
        etNombre = (EditText) view.findViewById(R.id.etNombre);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        Log.d("FIREBASE","HOLAAA");
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña corresponden a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                ultBoton = "Aceptar";

                Log.d("Cont", "Onclick:" + Lista.getContLista());
                String nombreLista = etNombre.getText().toString();
                if(nombreLista != null && nombreLista.trim().length() > 0) {
                    if (tipoLista.equals("grupal")) {

                        fragment_crear_compartida fragment=new fragment_crear_compartida(email,nick,etNombre.getText().toString());

                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        /*
                        fragment.show(getActivity().getSupportFragmentManager(),"tag");
                        fragment.getLifecycle().getCurrentState();
                        */

                    } else {

                        Log.d("Cont", "CrearLista:" +Lista.getContLista());
                        List<Usuario> lista=new ArrayList<>();
                        ReadAndWriteSnippets.insertarLista(nombreLista,nick);
                        cerrarFragment();

                        Intent intent = new Intent(getContext(), ListaProductos.class);
                        intent.putExtra("nick",nick);
                        intent.putExtra("email",email);
                        intent.putExtra("nombreLista",nombreLista);
                        intent.putExtra("idLista",String.valueOf(Lista.getContLista()));
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(),"Error, se debe introducir un nombre para la lista",Toast.LENGTH_SHORT).show();
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
     * Método que devuelve el inflater del fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introducir_nom_lista2, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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