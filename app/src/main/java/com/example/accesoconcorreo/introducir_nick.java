package com.example.accesoconcorreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class introducir_nick extends DialogFragment {

    //Representa el EditText en el cual habrá que introducir el nombre que se le querra dar a la lista
    private EditText etNick;

    //Representa el botón de Aceptar del Fragment
    private Button btnAceptar;
    //Representa el botón de Cancelar del Fragment
    private Button btnCancelar;

    //Representa una cadena que indica el email del usuario
    private String email;
    private String pwd;
    private String nick = "";
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabase;
    //Representa la clase de Lógica de Negocio la cuál será necesaria para comprobar información con la BD
    private ReadAndWriteSnippets persistencia;


    //Representa el nombre del último botón en el cual se ha hecho click
    private String ultBoton = "";

    /**
     * Constructor base
     *
     * @param email
     */
    public introducir_nick(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    /**
     * Devuelve el nombre del último botón que se a clickado o la cadena vacía en caso de que no se haya clickado ninguno
     *
     * @return ultBoton
     */
    public String getUltBoton() {
        return this.ultBoton;
    }

    public String getNick() {
        return this.nick;
    }

    /**
     * Método que inicializa las componentes del dialogfragment
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        persistencia = new ReadAndWriteSnippets();
        etNick = (EditText) view.findViewById(R.id.etNick);
        btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña corresponden a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                ultBoton = "Aceptar";

                nick = etNick.getText().toString();
                if (nick != null && nick.trim().length() > 0) {

                    Task<AuthResult> authResultTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        persistencia.insertarUsuario(nick, email);
                                        showHome(nick, email, ProviderType.Basic); //MEJOR CON ?: ""
                                    } else {
                                        showAlert();
                                    }
                                }

                            });

                    //añadir nick al usuario

                    cerrarFragment();

                    Intent intent = new Intent(getContext(), Home.class);
                    intent.putExtra("nick", nick);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Error, se debe introducir un nick para poder registrarse correctamente", Toast.LENGTH_SHORT).show();
                }
            }


        });
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña corresponden a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                cerrarFragment();//si se le da te hecha de la aplicación
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
        return inflater.inflate(R.layout.fragment_introducir_nick, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * Método que sirve para cerrar el Fragment
     */
    private void cerrarFragment() {

        getFragmentManager().beginTransaction().remove(this).commit();
    }

    private void showHome(String nick, String email, ProviderType provider) {
        Intent homeIntent = new Intent(getContext(), Home.class);
        homeIntent.putExtra("email", email);
        //GUARDAR DATOS

        SharedPreferences pref = getContext().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        pref.edit().putString("email", email);
        pref.edit().putString("nick", nick);
        pref.edit().putString("provider", provider.toString());
        pref.edit().apply();
        homeIntent.putExtra("nick", nick);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
    }

    private void showAlert() {
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("Error");
        b.setMessage("Se ha producido un error autenticando el usuario");
        b.setPositiveButton("Aceptar", null);
        AlertDialog alert = b.create();
        alert.show();
    }
}