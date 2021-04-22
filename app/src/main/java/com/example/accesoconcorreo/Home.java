package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
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
       String email=getIntent().getExtras().get("email").toString();
       String nick=getIntent().getStringExtra("nick");
        Usuario u=new Usuario(nick,email);
        ReadAndWriteSnippets persistencia=new ReadAndWriteSnippets();
       /*List<String> listasUsuario=persistencia.obtenerListasbyUserID();
        for(String l: listasUsuario){
            TextView t=new TextView(this);
            t.setText(l);
        }*/
        TextView t=findViewById(R.id.sustituir);
        t.setText(nick);

    }
}