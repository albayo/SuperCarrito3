package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaAmigosAdapter;
import ModeloDominio.ReadAndWriteSnippets;

public class MostrarMiebrosLista extends AppCompatActivity {
    private List<String> mAmigos;
    private List<String> mCorreos;
    private DatabaseReference mDatabase;
    private ListaAmigosAdapter listaAmigosAdapter;
    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewAmigos;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String nick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_miebros_lista);


        toolbar = (Toolbar) findViewById(R.id.miembroslista_toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAmigos=new ArrayList<>();
        mCorreos=new ArrayList<>();
        recyclerViewAmigos=findViewById(R.id.amigos_recycler);
        recyclerViewAmigos.setLayoutManager(new LinearLayoutManager(this));
        nick=getIntent().getStringExtra("nick");

        String title="Amigos";

        toolbar.setTitle(title);
        FloatingActionButton fab=findViewById(R.id.fab_Aniadir_Miembro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introducir_amigo amigoDF = new introducir_amigo(nick);
                amigoDF.show(getSupportFragmentManager(),"tag");
            }
        });

        drawerLayout= findViewById(R.id.drawer_layout_amigos);
        navigationView= findViewById(R.id.nav_View);
        Activity activity=this;
        String idLista=" ",nombreLista=" ";

        idLista=getIntent().getExtras().getString("idLista");
        nombreLista=getIntent().getStringExtra("nombreLista");

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,getIntent().getStringExtra("email"),activity,getApplicationContext());

        obtenerMiembrosLista(nick,idLista,nombreLista);

    }


    public void obtenerMiembrosLista(String nick,String idLista,String nombreLista) {

        /*//NECESARIO PARA BORRAR EL ÚLTIMO
        mDatabase.child("listas").child(idLista).child("miembros").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()==null){
                        mAmigos.clear();
                        listaAmigosAdapter= new ListaAmigosAdapter(MostrarMiebrosLista.this,R.layout.amigos_recycler, mAmigos,mCorreos,nick,modo,idLista,nombreLista);
                        recyclerViewAmigos.setAdapter(listaAmigosAdapter);
                    }
                }
            }
        });*/
        mDatabase.child("listas").child(idLista).child("miembros").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar las listas de manera actualizada
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mCorreos.clear();
                    mAmigos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getKey();
                        String correo = ds.getValue().toString();
                        mAmigos.add(nombre);
                        mCorreos.add(correo);
                    }

                    //listaAmigosAdapter= new ListaAmigosAdapter(ListaAmigos.this,R.layout.amigos_recycler, mAmigos,mCorreos,nick,modo,idLista,nombreLista);
                    recyclerViewAmigos.setAdapter(listaAmigosAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}