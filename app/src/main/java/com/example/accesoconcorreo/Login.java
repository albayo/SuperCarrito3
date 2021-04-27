package com.example.accesoconcorreo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;


/**
 * Esta clase define la actividad (llamada "activity_login") que servirá para el logueo del Usuario
 * en la aplicación
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */
//      USUARIO: 16alvaro1bac@gmail.com
//      Contraseña: alvarobayo

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    //Representa el TAG que sirve para distinguir la actividad
    private static final String LOG_TAG = Login.class.getSimpleName();

    //Representa la clase de Lógica de Negocio la cuál será necesaria para comprobar información con la BD
    //private SuperViewModel superViewModel;

    //Representa el cuadro de texto (EditText) en el cual se escribirá el nombre de Usuario
    private EditText usuarioET;

    //Representa el cuadro de texto (EditText) en el cual se escribirá la contraseña del Usuario
    private EditText contraseniaET;

    private ReadAndWriteSnippets persistencia;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_pantalla_listas"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
       // superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
        setContentView(R.layout.activity_login);
        usuarioET = findViewById(R.id.editText_email);
        contraseniaET = findViewById(R.id.editText_contrasenia);//
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        persistencia=new ReadAndWriteSnippets();
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña no corresponden a un usuario existente para registrar un usuario
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                if (usuarioET.getText().toString().trim().length() > 0 && contraseniaET.getText().toString().trim().length() > 0) {
                    Task<AuthResult> authResultTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuarioET.getText().toString(), contraseniaET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String[] nick=usuarioET.getText().toString().split("@");
                                persistencia.insertarUsuario(nick[0],usuarioET.getText().toString());

                                showHome(usuarioET.getText().toString(),ProviderType.Basic); //MEJOR CON ?: ""
                            } else {

                                showAlert();
                            }
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error, debe rellenar los campos", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

        });
        ImageButton mostrarContrasena = findViewById(R.id.imageButton_mostrarC);
        mostrarContrasena.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para mostrar/ocultar la contraseña (cambiar su modo de visualización)
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {

                if( contraseniaET.getTransformationMethod() == null){
                    contraseniaET.setTransformationMethod(new PasswordTransformationMethod());;
                }else{
                    contraseniaET.setTransformationMethod(null);
                }
            }
        });

        Button btnAcceder =findViewById(R.id.btnAcceder);
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña corresponden a un usuario existente
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                usuarioET = findViewById(R.id.editText_email);
                contraseniaET = findViewById(R.id.editText_contrasenia);
                if (usuarioET.getText().toString().trim().length() > 0 && contraseniaET.getText().toString().trim().length() > 0) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioET.getText().toString(),contraseniaET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                showHome(usuarioET.getText().toString(),ProviderType.Basic); //MEJOR CON ?: ""
                            }
                            else{
                                showAlert();
                            }
                        }
                    });


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error, debe rellenar los campos", Toast.LENGTH_LONG);
                    toast.show();
                }

            }

        });
    }
    private void showAlert(){
        AlertDialog.Builder b= new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage("Se ha producido un error autenticando el usuario");
        b.setPositiveButton("Aceptar",null);
        AlertDialog alert=b.create();
        alert.show();
    }
    private void showHome(String email, ProviderType provider){
        Intent homeIntent=new Intent(this,Home.class);
        homeIntent.putExtra("email",email);
        String[] nick=usuarioET.getText().toString().split("@");
        homeIntent.putExtra("nick", nick[0]);
        homeIntent.putExtra("provider",provider.name());
        startActivity(homeIntent);
    }


    /**
     * Método que sirve para lanzar la actividad de carga de datos en la BD de la API
     *
     * @param view Representa al objeto View sobre el cual se ha hecho click
     * @param u    Representa al Usuario que se ha logueado en la aplicación
     */
    /*protected void launchPantalla_listas(View view, Usuario u) {

        //CAMBIAR DEBE LLEVAR A LA ACTIVIDAD DE CARGA, NO A LA DE LAS LISTAS
        //DEPENDE SI FIREBASE CARGA LA API O NO
        Log.d(LOG_TAG, "btnAcceder clicado!");
        Intent intent = new Intent(this,AccesoCorreo.class);
        intent.putExtra("usuario", u);
        startActivity(intent);

    }*/
}




