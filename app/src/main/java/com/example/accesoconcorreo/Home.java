package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private FirebaseDatabase database;
    DatabaseReference myRef;
    private List<String> listasUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        String email=getIntent().getExtras().get("email").toString();
        String nick=getIntent().getStringExtra("nick");
        ReadAndWriteSnippets persistencia=new ReadAndWriteSnippets();
        Usuario u=new Usuario(nick,email);
      //  List<String> listasUsuario=persistencia.obtenerListasUsuario(u.getNick());
        listasUsuario=new ArrayList<>();
        //persistencia.obtenerListasUsuario(listasUsuario,u.getNick());

        myRef.child("users").child(nick).child("listas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> ds= dataSnapshot.getChildren();
                Log.d("ObtenerListas","Success");
                int i=1;
                for (DataSnapshot d:ds) {

                    listasUsuario.add(d.getValue().toString());
                    Log.d("ObtenerListas","Metiendo"+d.getValue());
                    //Log.d("ObtenerListas","Metiendo"+ llistas.get(i));
                    i++;
                }
                //crear adapter para mostrar en recycler
                @SuppressLint("ResourceType") ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),R.id.recycler_lista_super_prod,listasUsuario);

            }
        });
        FloatingActionButton fabAñadirLista=findViewById(R.id.fabAniadir_lista);
        fabAñadirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,
                        CrearLista.class);
                intent.putExtra("nick",nick);
                intent.putExtra("email",email);
                startActivity(intent);
                Intent replyIntent = new Intent();
                Lista l = (Lista) intent.getSerializableExtra("lista");
               //hacer insert en el usuario internamente
            }
        });

        recyclerView=findViewById(R.id.recycler_lista_super_prod);




        TextView textView=findViewById(R.id.tamañolista);
        textView.setText("Tamaño: "+listasUsuario.size());
        TextView nombrelista=findViewById(R.id.nombrelista);

        for(String l: listasUsuario){
            nombrelista.setText(l);
        }
        TextView t=findViewById(R.id.sustituir);
        t.setText(nick);

    }
}