package com.example.accesoconcorreo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ModeloDominio.ReadAndWriteSnippets;

/**
 * Esta clase define el fragmento llamado "fragment_introducir_amigo" que sirve para introducir un nombre para mandar una solicitud de amigo a un usuario.
 *  * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 *  * @version: 18/05/2021
 */
public class introducir_amigo extends DialogFragment {

    //Representa el EditText en el cual habrá que introducir el nombre que se le querra dar a la lista
    private EditText etNombre;
    private DatabaseReference mDatabase;
    //Representa el botón de Aceptar del Fragment
    private Button btnAceptar;

    //Representa el botón de Cancelar del Fragment
    private Button btnCancelar;

    //Representa una cadena que indica el nick del usuario actual
    private String nickUser;

    public introducir_amigo(String nickUser) {
        this.nickUser = nickUser;
    }

    /**
     * Método que inicializa las componentes del dialogfragment
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNombre = (EditText) view.findViewById(R.id.etNombre);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que se haya introducido algo en el campo de nick
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            /*
            ANTES BACKUP
            @Override
            public void onClick(View v) {
                mDatabase= FirebaseDatabase.getInstance().getReference();
                String nickAmigo = etNombre.getText().toString();
                if(nickAmigo != null && nickAmigo.trim().length() > 0) {
                    if(nickAmigo==nickUser){
                        mDatabase.child("users").child(nickAmigo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().getValue()!=null){

                                        ReadAndWriteSnippets.solicitudAmistad(nickUser,nickAmigo);
                                        Toast.makeText(getActivity(), "La solicitud ha sido añadida con éxito", Toast.LENGTH_LONG).show();

                                    } else{
                                        Toast toast = Toast.makeText(getContext(), "Usuario no existente", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(),"Error, no puedes enviarte una solicitud a ti mismo",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"Error, se debe introducir un nick para poder mandarle una solicitud",Toast.LENGTH_SHORT).show();
                }
            }
            */
            @Override
            public void onClick(View v) {
                mDatabase= FirebaseDatabase.getInstance().getReference();
                String nickAmigo = etNombre.getText().toString();
                if(nickAmigo != null && nickAmigo.trim().length() > 0) {
                    if(nickAmigo!=nickUser){
                        mDatabase.child("users").child(nickAmigo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult().getValue()!=null){
                                        mDatabase.child("users").child(nickUser).child("amigos").child(nickAmigo).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    if(task.getResult().getValue()==null) {
                                                        ReadAndWriteSnippets.solicitudAmistad(nickUser,nickAmigo);
                                                        Toast.makeText(getActivity(), "La solicitud ha sido añadida con éxito", Toast.LENGTH_LONG).show();

                                                    }
                                                    else{
                                                        Toast toast = Toast.makeText(getContext(), "Usuario ya es tu amigo", Toast.LENGTH_LONG);
                                                        toast.show();
                                                    }
                                                }
                                            }
                                        });


                                    } else{
                                        Toast toast = Toast.makeText(getContext(), "Usuario no existente", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(),"Error, no puedes enviarte una solicitud a ti mismo",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"Error, se debe introducir un nick para poder mandarle una solicitud",Toast.LENGTH_SHORT).show();
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
        return inflater.inflate(R.layout.fragment_introducir_amigo, container, false);
    }

    /**
     * Método que sirve para cerrar el Fragment
     */
    private void cerrarFragment() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }
}