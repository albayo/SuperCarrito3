package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.ListaListAdapter;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class Perfil extends AppCompatActivity {
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

    private String email;
    private String nick;
    private ImageView fotoperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        email = getIntent().getExtras().get("email").toString();
        //cambiar esto al user normal
        nick = getIntent().getStringExtra("nick");

        Usuario u = new Usuario(nick, email);

        drawerLayout= findViewById(R.id.drawer_layout_perfil);
        navigationView= findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.perftoolbar);
        Activity activity=this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,activity,getApplicationContext());

        TextView nickt=findViewById(R.id.text_username);
        TextView emailt=findViewById(R.id.text_username2);
         fotoperfil=findViewById(R.id.imageview_fotoperfil);

        toolbar.setTitle("Perfil");


        emailt.setText(email);
        nickt.setText(nick);
        obtenerFoto(nick);



        }
    public void obtenerFoto(String name){
        mDatabaseReference.child("users").child(name).child("fotoperfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}