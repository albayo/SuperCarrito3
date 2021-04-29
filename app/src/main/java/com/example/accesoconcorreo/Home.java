package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
       // user = FirebaseAuth.getInstance().getCurrentUser();
        String email = getIntent().getExtras().get("email").toString();
        String nick = getIntent().getStringExtra("nick");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.homeToolbar);
        myToolbar.setTitle("SuperCarrito-" + nick);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_lista_super_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fabAñadirLista = findViewById(R.id.fabAniadir_lista);
        persistencia = new ReadAndWriteSnippets();
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder b= new AlertDialog.Builder(this);
        b.setTitle("Confirmación");
        b.setMessage("¿Está seguro/a de que desea cerrar sesión?");
        b.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {

            /**
             * Método que sirve para volver a activity_home
             * @param dialog representa el Dialog en el que se ha hecho click
             * @param which representa el botón que ha sido clickado o la posición del item clickado
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Home.super.onBackPressed();
            }
        });

        b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            /**
             * Método que sirve para volver a activity_home
             * @param dialog representa el Dialog en el que se ha hecho click
             * @param which representa el botón que ha sido clickado o la posición del item clickado
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        AlertDialog alert=b.create();
        alert.show();
        //super.onBackPressed();
    }

    /*@Override
    protected void onPause() {
        OnBackPressedDispatcher backPressedDispatcher = getOnBackPressedDispatcher();
        backPressedDispatcher.
        super.onPause();

    }*/

    public void obtenerListasUsuario(String nick) {
        List<String> llistas = new ArrayList<>();
        List<String> lListaid=new ArrayList<>();
        mDatabaseReference.child("users").child(nick).child("listas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    llistas.clear();
                    lListaid.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id=ds.getKey();
                        String nombre = ds.getValue().toString();
                        llistas.add(nombre);
                        lListaid.add(id);
                    }
                    mListaAdapter = new ListaListAdapter(Home.this,R.layout.pantalla_listas_list, llistas,lListaid);
                    recyclerView.setAdapter(mListaAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}