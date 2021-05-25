package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import Adapters.MiembrosListaAdapter;
import ModeloDominio.ReadAndWriteSnippets;

public class MostrarMiebrosLista extends AppCompatActivity {
    private List<String> mMiembros;
    private DatabaseReference mDatabase;
    private MiembrosListaAdapter miembrosAdapter;
    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewMiembros;
    private boolean propietario;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String nick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_miebros_lista);
        mMiembros=new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.miembroslista_toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMiembros=new ArrayList<>();
        String title="Miembros";
        toolbar.setTitle(title);
        propietario=false;

        recyclerViewMiembros=findViewById(R.id.miembros_recycler);
        recyclerViewMiembros.setLayoutManager(new LinearLayoutManager(this));

        nick=getIntent().getStringExtra("nick");
        String idLista=" ",nombreLista=" ";
        idLista=getIntent().getExtras().getString("idLista");
        nombreLista=getIntent().getStringExtra("nombreLista");
        FloatingActionButton fab=findViewById(R.id.fab_Aniadir_Miembro);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introducir_amigo amigoDF = new introducir_amigo(nick);
                amigoDF.show(getSupportFragmentManager(),"tag");
            }
        });
        fab.setVisibility(View.INVISIBLE);
        mDatabase.child("listas").child(idLista).child("propietario").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()!=null){
                        String propie=task.getResult().getValue().toString();
                        Log.d("Prop",propie);
                        Log.d("Nick",nick);
                        if(propie.equals(nick)){
                           propietario=true;
                           //miembrosAdapter.setPropietario(propietario);
                           fab.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });


        drawerLayout= findViewById(R.id.drawer_layout_amigos);
        navigationView= findViewById(R.id.nav_View);
        Activity activity=this;




        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,getIntent().getStringExtra("email"),activity,getApplicationContext());

        obtenerMiembrosLista(nick,idLista,nombreLista);

    }


    public void obtenerMiembrosLista(String nick,String idLista,String nombreLista) {
        mDatabase.child("listas").child(idLista).child("miembros").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar las listas de manera actualizada
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    mMiembros.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getKey();
                        mMiembros.add(nombre);
                        Log.d("ADDD",nombre);

                    }
                    Log.d("PROP",""+propietario);
                    miembrosAdapter= new MiembrosListaAdapter(MostrarMiebrosLista.this,R.layout.item_miembros, mMiembros,nick,idLista,nombreLista,propietario);
                    recyclerViewMiembros.setAdapter(miembrosAdapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}