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


public class Perfil_otros extends AppCompatActivity {

    //Representa una la base de datos
    private FirebaseDatabase database;
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabaseReference;
    //Representa un adapter de tipo lista para poder mostrar las listas del usuario
    private ListaListAdapter mListaAdapter;
    //Representa el layout del menu
    private DrawerLayout drawerLayout;
    //Representa un navigation
    private NavigationView navigationView;
    private Toolbar toolbar;
    private StorageReference mStorage;

    private static String email;
    private String nick;
    private ImageView fotoperfil;
    private static final int GALLERY_INTENT = 1;

    private ProgressDialog progressDialog;

    private int numeroAmigos;

    private Button btnAmigos;
    private ImageButton btnSubir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_otros);

        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        nick = getIntent().getStringExtra("nick");
        obtenerEmail(nick);
        mStorage = FirebaseStorage.getInstance().getReference();
        numeroAmigos = 0;
        Usuario u = new Usuario(nick, email);

        drawerLayout = findViewById(R.id.drawer_layout_perfilot);
        navigationView = findViewById(R.id.nav_viewot);
        toolbar = findViewById(R.id.perftoolbarot);
        Activity activity = this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout, navigationView, toolbar, nick, email, activity, getApplicationContext());

        TextView nickt = findViewById(R.id.text_usernameot);
        TextView numAmigos = findViewById(R.id.text_numeroamot);
        TextView emailt = findViewById(R.id.text_emailot);
        fotoperfil = findViewById(R.id.imageview_fotoperfilot);


        toolbar.setTitle("Perfil");
        progressDialog = new ProgressDialog(this);
        numeroAmigos(nick);
        emailt.setText(email);
        nickt.setText(nick);
        numAmigos.setText(String.valueOf(numeroAmigos));
        obtenerFoto(nick);

    }

    private void obtenerEmail(String nick) {
        mDatabaseReference.child("users").child(nick).child("email").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String em = dataSnapshot.getValue().toString();
                Perfil_otros.email=em;
            }
        });

    }

    private void numeroAmigos(String name) {
        mDatabaseReference.child("users").child(name).child("amigos").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
        });
    }

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