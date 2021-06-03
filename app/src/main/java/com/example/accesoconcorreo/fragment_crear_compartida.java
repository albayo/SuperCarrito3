package com.example.accesoconcorreo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * Esta clase define el DialogFragment (llamada "fragment_crear_compartida") que servirá para que el usuario pueda crear una lista compartida
 * en la aplicación
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
public class fragment_crear_compartida extends DialogFragment {
    //Representa el EditText donde se introducirá el nick de un usuario para poder compartir con el la lista
    private EditText nickCompartir;
    //Representa un TextView
    private TextView aniadidos;
    //Representa el botón de aceptar
    private Button aceptar;
    //Representa el botón de cancelar
    private Button cancelar;
    private Button seguir;
    //Representa el nick y email del usuario y el nombre de la lista
    private String nick,email,nombreLista;
    //Representa la lista de usuarios
    private List<String> usuarios=new ArrayList<>();
    //Representa una referencia a la BD
    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    /**
     * Constructor de la clase
     * @param email representa el email del usuario
     * @param nick representa el nick del usuario
     * @param nombreLista representa el nombre de la lista que se quiere crear
     */
    public fragment_crear_compartida(String email, String nick,String nombreLista) {
        this.email=email;
        this.nick=nick;
        this.nombreLista=nombreLista;
    }

    /**
     * Método que inicializa las componentes del dialogfragment
     * @param view reprenta la vista sobre la que se ejecutará el Fragment
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ReadAndWriteSnippets.actualizaContadorListas();
        nickCompartir=(EditText)view.findViewById(R.id.ET_nickCompartir);
        aceptar=(Button)view.findViewById(R.id.btAceptar);
        cancelar=(Button)view.findViewById(R.id.btCancelar);
        seguir=(Button)view.findViewById(R.id.btSeguir);
        aniadidos=(TextView)view.findViewById(R.id.et_aniadidos);


        aceptar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en el corresponde a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                String usuario = nickCompartir.getText().toString();

                mDatabase.child("users").child(usuario).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    /**
                     * Método que tiene lugar cuando una tarea se ha acabado (tanto de forma satisfactoria como de forma errónea)
                     * @param task tarea que se ha completado
                     */
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getValue()!=null){
                                usuarios.add(usuario);
                                nickCompartir.setText("");
                                Toast toast = Toast.makeText(getContext(), "Usuario añadido", Toast.LENGTH_LONG);
                                toast.show();

                                ReadAndWriteSnippets.insertarLista(nombreLista,nick,true);
                                String idLista=String.valueOf(Lista.getContLista());
                                for(String s: usuarios){
                                    //ReadAndWriteSnippets.aniadirUsuarioaList(nombreLista,s,String.valueOf(Lista.getContLista()));
                                    ReadAndWriteSnippets.solicitudLista(nick,s,idLista,nombreLista);
                                }
                                cerrarFragment();

                                Intent intent = new Intent(getContext(), ListaProductos.class);
                                intent.putExtra("nick",nick);
                                intent.putExtra("email",email);
                                intent.putExtra("nombreLista",nombreLista);
                                intent.putExtra("idLista",String.valueOf(Lista.getContLista()));
                                startActivity(intent);
                            } else{
                                Toast toast = Toast.makeText(getContext(), "Usuario no existente", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                });
                }
        });
        seguir.setOnClickListener(new View.OnClickListener() {

            /**
             * Método que sirve para comprobar que lo introducido en el corresponde a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                String usuario=nickCompartir.getText().toString();
                mDatabase.child("users").child(usuario).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

                    /**
                     * Método que tiene lugar cuando una tarea se ha acabado (tanto de forma satisfactoria como de forma errónea)
                     * @param task tarea que se ha completado
                     */
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getValue()!=null){
                                usuarios.add(usuario);
                                nickCompartir.setText("");
                                Toast toast = Toast.makeText(getContext(), "Usuario añadido", Toast.LENGTH_LONG);
                                toast.show();

                            } else{
                                Toast toast = Toast.makeText(getContext(), "Usuario no existente", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve cerrar el fragment cuando se haga click en el botón
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                cerrarFragment();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_compartida, container, false);
    }

    /**
     * Método que sirve para cerrar el fragment actual
     */
    private void cerrarFragment() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.nom_lista)).commit();
        getFragmentManager().beginTransaction().remove(fragment_crear_compartida.this).commit();
    }
}