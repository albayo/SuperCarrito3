package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Adapters.ListaListAdapter;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Esta clase define la actividad (llamada "activity_perfil_otros") que dispondrá en pantalla el perfil
 * de un usuario que no es el actual
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 03/06/2021
 */
public class Perfil_otros extends AppCompatActivity {
    //Representa una la base de datos
    private FirebaseDatabase database;
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabaseReference;
    //Representa un adapter de tipo lista para poder mostrar las listas del usuario
    private ListaListAdapter mListaAdapter;
    //Representa el layout del menu
    private DrawerLayout drawerLayout;
    //Representa el menú de la aplicación
    private NavigationView navigationView;
    //Representa la barra de la actvidad
    private Toolbar toolbar;
    //Representa una referencia para poder subir imagenes
    private StorageReference mStorage;
    //Representa el email del usuario
    private static String email;
    //Representa el nick del usuario
    private String nick;
    //Reprsenta la foto del perfil del usuario que se quiere ver
    private ImageView fotoperfil;
    //Sirve para que te muestre la galeria del dispositivo para poder subir una foto
    private static final int GALLERY_INTENT = 1;
    //Representa un dialogo
    private ProgressDialog progressDialog;
    //Representa el numero de amigos
    private int numeroAmigos;
    //Representa el botón para ver amigos
    private Button btnAmigos;
    //Representa el botón que permite subir imagenes para ponerselas como foto de perfil
    private ImageButton btnSubir;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_solicitudes" y poder mostrar las listas de los usuarios
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_otros);

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        nick = getIntent().getStringExtra("nick");
        mStorage = FirebaseStorage.getInstance().getReference();
        numeroAmigos = 0;
        Usuario u = new Usuario(nick, email);

        drawerLayout = findViewById(R.id.drawer_layout_perfilot);
        navigationView = findViewById(R.id.nav_viewot);
        toolbar = findViewById(R.id.perftoolbarot);
        Activity activity = this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout, navigationView, toolbar, nick, email, activity, getApplicationContext());

        TextView nickt = findViewById(R.id.text_usernameot);
        TextView emailt = findViewById(R.id.text_emailot);
        fotoperfil = findViewById(R.id.imageview_fotoperfilot);


        toolbar.setTitle("Perfil");
        progressDialog = new ProgressDialog(this);
        obtenerEmail(emailt);
        nickt.setText(nick);

        obtenerFoto(nick);

    }

    /**
     * Método que obtiene el email
     * @param emailt
     */
    private void obtenerEmail(TextView emailt) {
        mDatabaseReference.child("users").child(nick).child("email").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {

            /**
             * Método que se lanza cuando una tarea tiene exitos
             * @param dataSnapshot
             */
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                emailt.setText(dataSnapshot.getValue().toString());
            }
        });

    }

    /**
     * Método que obtiene la foto
     * @param name
     */
    public void obtenerFoto(String name) {

        mDatabaseReference.child("users").child(name).child("fotoperfil").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                Glide.with(com.example.accesoconcorreo.Perfil_otros.this).load(url).fitCenter().centerCrop().override(600, 600).transition(withCrossFade()).into(fotoperfil);
            }
        });
    }
}