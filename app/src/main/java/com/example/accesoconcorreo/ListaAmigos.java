package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaAmigosAdapter;
import Adapters.ProductListAdapter;
import Adapters.SolicitudesAdapter;
import ModeloDominio.Lista;
import ModeloDominio.Producto;
import ModeloDominio.Solicitud;

public class ListaAmigos extends AppCompatActivity {
    private List<String> mAmigos;
    private List<String> mCorreos;
    private DatabaseReference mDatabase;
    private ListaAmigosAdapter listaAmigosAdapter;
    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewAmigos;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amigos);

        toolbar = (Toolbar) findViewById(R.id.amigos_toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAmigos=new ArrayList<>();
        mCorreos=new ArrayList<>();
        recyclerViewAmigos=findViewById(R.id.amigos_recycler);
        recyclerViewAmigos.setLayoutManager(new LinearLayoutManager(this));
        nick=getIntent().getStringExtra("nick");

        String title="Supercarrito - "+nick;

        toolbar.setTitle(title);
        FloatingActionButton fab=findViewById(R.id.fab_Aniadir_Amigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introducir_amigo amigoDialogFragment = new introducir_amigo(nick);
                amigoDialogFragment.show(getSupportFragmentManager(),"tag");
            }
        });

    }

    public void obtenerAmigosUsuario(String nick) {
        List<String> lAmigos = new ArrayList<>();
        List<String> lCorreos=new ArrayList<>();
        mDatabase.child("users").child(nick).child("amigos").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar las listas de manera actualizada
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lCorreos.clear();
                    lAmigos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getKey();
                        String correo = ds.getValue().toString();
                        lAmigos.add(nombre);
                        lCorreos.add(correo);
                    }
                    listaAmigosAdapter= new ListaAmigosAdapter(ListaAmigos.this,R.layout.amigos_recycler, lAmigos,lCorreos);
                    recyclerViewAmigos.setAdapter(listaAmigosAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}