package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaListAdapter;
import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private ReadAndWriteSnippets persistencia;
    private ListaListAdapter mListaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = getIntent().getExtras().get("email").toString();
        String nick = getIntent().getStringExtra("nick");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_lista_super_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fabAñadirLista = findViewById(R.id.fabAniadir_lista);
        persistencia = new ReadAndWriteSnippets();
        List<String> listas = persistencia.obtenerListasUsuario(nick);
        // mListaAdapter=new ListaListAdapter(this.getApplicationContext(),listas);

        fabAñadirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,
                        CrearLista.class);
                intent.putExtra("nick", nick);
                intent.putExtra("email", email);
                startActivity(intent);
                Intent replyIntent = new Intent();
                Lista l = (Lista) intent.getSerializableExtra("lista");
                //hacer insert en el usuario internamente
            }
        });

        Usuario u = new Usuario(nick, email);

        TextView t = findViewById(R.id.sustituir);
        t.setText(nick);

        this.obtenerListasUsuario(u.getNick());

    }

    public void obtenerListasUsuario(String nick) {
        List<String> llistas = new ArrayList<>();
        mDatabaseReference.child("users").child(nick).child("listas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getValue().toString();
                        llistas.add(nombre);
                    }
                    mListaAdapter= new ListaListAdapter(R.layout.activity_home,llistas);
                    recyclerView.setAdapter(mListaAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }
        );

    }
}