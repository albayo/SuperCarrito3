package com.example.accesoconcorreo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;


/**
 * Esta clase define la actividad (llamada "activity_login") que servirá para el logueo del Usuario
 * en la aplicación
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
//      USUARIO: 16alvaro1bac@gmail.com
//      Contraseña: alvarobayo

public class Login extends AppCompatActivity {
    private static final int GOOGLE_SIGN_IN = 100;
    //Representa una instancia de la BD
    private FirebaseAuth mAuth;
    //Representa el TAG que sirve para distinguir la actividad
    private static final String LOG_TAG = Login.class.getSimpleName();

    //Representa la clase de Lógica de Negocio la cuál será necesaria para comprobar información con la BD
    private ReadAndWriteSnippets persistencia;

    //Representa el cuadro de texto (EditText) en el cual se escribirá el nombre de Usuario
    private EditText usuarioET;

    //Representa el cuadro de texto (EditText) en el cual se escribirá la contraseña del Usuario
    private EditText contraseniaET;


    /**
     * Método que sirve para borrar los campos de usuario y contraseña cuando se vuelva a recuperar
     * el foco después de haber ido a otra activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        usuarioET.setText("");
        contraseniaET.setText("");
    }

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
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.fichaProdToolbar);
        myToolbar.setTitle("SuperCarrito");
        persistencia = new ReadAndWriteSnippets();
        usuarioET = findViewById(R.id.editText_email);
        contraseniaET = findViewById(R.id.editText_contrasenia);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para comprobar que lo introducido en los campos de usuario y
             *  contraseña no corresponden a un usuario existente para registrar un usuario
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                if (usuarioET.getText().toString().trim().length() > 0 && contraseniaET.getText().toString().trim().length() > 0) {
                    introducir_nick nickDF = new introducir_nick(usuarioET.getText().toString(),contraseniaET.getText().toString());
                    nickDF.show(getSupportFragmentManager(),"tag");
                   /* Task<AuthResult> authResultTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(usuarioET.getText().toString(), contraseniaET.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String[] nick = usuarioET.getText().toString().split("@");
                                        persistencia.insertarUsuario(nick[0], usuarioET.getText().toString());
                                        showHome(nick[0], usuarioET.getText().toString(), ProviderType.Basic); //MEJOR CON ?: ""
                                    } else {
                                        showAlert();
                                    }
                                }

                            });*/

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Error, debe rellenar los campos", Toast.LENGTH_LONG);
                    toast.show();
                }

            }


        });

        Button btngoogle = findViewById(R.id.login_google);

        btngoogle.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para mostrar/ocultar la contraseña (cambiar su modo de visualización)
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                GoogleSignInClient googleclient = GoogleSignIn.getClient(Login.this, gso);
                startActivityForResult(googleclient.getSignInIntent(), GOOGLE_SIGN_IN);
                // googleclient.signOut();

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
                Log.d("Mostrar contraseña", "Click");
                if (contraseniaET.getTransformationMethod() == null) {
                    Log.d("Mostrar contraseña", "Habilitar mostrar contraseña");
                    contraseniaET.setTransformationMethod(new PasswordTransformationMethod());
                    ;
                } else {
                    contraseniaET.setTransformationMethod(null);
                }
            }
        });

        Button btnAcceder = findViewById(R.id.btnAcceder);
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
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioET.getText().toString(), contraseniaET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String nick = persistencia.getNick(usuarioET.getText().toString());
                                showHome(nick, usuarioET.getText().toString(), ProviderType.Basic); //MEJOR CON ?: ""
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
    }
    /**
     * Método que sirve para mostrar un dialogo de alerta
     */
    private void showAlert() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage("Se ha producido un error autenticando el usuario");
        b.setPositiveButton("Aceptar", null);
        AlertDialog alert = b.create();
        alert.show();
    }

    /**
     * Método que sirve para mostrar/ocultar la contraseña (cambiar su modo de visualización)
     * @param nick Representa el nick que tiene el usuario logueado
     * @param email Representa el email que tiene el email logueado
     * @param provider Representa la forma en la que se ha logueado el usuario
     */
    private void showHome(String nick, String email, ProviderType provider) {
        Intent homeIntent = new Intent(this, Home.class);
        homeIntent.putExtra("email", email);
        //GUARDAR DATOS

        SharedPreferences pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        pref.edit().putString("email", usuarioET.toString());
        pref.edit().putString("nick", nick);
        pref.edit().putString("provider", provider.toString());
        pref.edit().apply();
        homeIntent.putExtra("nick", nick);
        homeIntent.putExtra("provider", provider.name());
        startActivity(homeIntent);
    }

    /*
    private void session() {
        SharedPreferences pref = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = pref.getString("email", null);
        String provider = pref.getString("provider", null);
        String nick = pref.getString("nick", null);
        if (email != null && provider != null) {
            //PUEDES PONER EL LAYOUT INVISIBLE
            showHome(nick, email, ProviderType.valueOf(provider));
        }
    }*/

    /**
     *  Método que registra al usuario con Google
     * @param requestCode código que es solicitado
     * @param resultCode código que ha sido resultado del evento
     * @param data el intent del que sacamos los datos
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        /**
                         * Método que se ejecuta si la tarea a ejecutar se ejecuta de manera satisfactoria
                         * @param task Representa la tarea que se ha ejecutado
                         */
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                persistencia.insertarUsuario(account.getDisplayName(), account.getEmail());
                                showHome(account.getDisplayName(), account.getEmail(), ProviderType.google);
                            } else {
                                showAlert();
                            }
                        }
                    });
                }
                // firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }


}




