package com.example.accesoconcorreo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
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

    //Representa la clase de Lógica de Negocio la cuál será necesaria para sacar la información de la BD
    // private SuperViewModel superViewModel;
    private ReadAndWriteSnippets persistencia;

    //Representa el nombre del último botón en el cual se ha hecho click
    private String ultBoton="";

    /**
     * Constructor base
     * @param tipoLista
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
        //superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
        etNombre = (EditText) view.findViewById(R.id.etNombre);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        persistencia= new ReadAndWriteSnippets();
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
                        //crear la lista GRUPAL con nombre "nombre"
                    } else {

                        Log.d("Cont", "CrearLista:" +Lista.getContLista());
                        List<Usuario> lista=new ArrayList<>();
                        persistencia.insertarLista(nombreLista,nick);
                        cerrarFragment();

                        Intent intent = new Intent(getContext(), ListaProductos.class);
                        intent.putExtra("nick",nick);
                        intent.putExtra("email",email);
                        intent.putExtra("nombreLista",nombreLista);
                        intent.putExtra("idLista",Lista.getContLista());
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
     *
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