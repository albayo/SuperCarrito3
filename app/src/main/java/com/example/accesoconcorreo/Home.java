package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ModeloDominio.Lista;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    //private FireBase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.recycler_lista_super_prod);
        FloatingActionButton fabAñadirLista=findViewById(R.id.fabAniadir_lista);
        fabAñadirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,
                        CrearLista.class);
                startActivity(intent);
                Intent replyIntent = new Intent();
                Lista l = (Lista) intent.getSerializableExtra("lista");
               //hacer insert en el usuario internamente
            }
        });

    }
}