package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.SolicitudesAdapter;
import ModeloDominio.Solicitud;

public class Solicitudes extends AppCompatActivity {
    private List<Solicitud> mSolicitudes;
   // private List<Solicitud> mSolicitudesListas;
    private DatabaseReference mDatabase;
    private SolicitudesAdapter solicitudesAdapterAmigos;
    private SolicitudesAdapter solicitudesAdapterListas;

    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewSolicitudesAmigos;
    private RecyclerView recyclerViewSolicitudesListas;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        toolbar = (Toolbar) findViewById(R.id.solicitudesToolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSolicitudes =new ArrayList<>();
        //mSolicitudesListas=new ArrayList<>();

        recyclerViewSolicitudesAmigos=findViewById(R.id.solicitudes_recycler);
        recyclerViewSolicitudesAmigos.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewSolicitudesListas=findViewById(R.id.solicitudes_listas_recycler);
        recyclerViewSolicitudesListas.setLayoutManager(new LinearLayoutManager(this));

        nick=getIntent().getStringExtra("nick");

        String title="Supercarrito - "+nick;

        toolbar.setTitle(title);
        obtenerSolicitudes(nick);


    }

    public void obtenerSolicitudes(String nick){
        mSolicitudes.clear();
        mDatabase.child("users").child(nick).child("solicitudes").child("amistad").addValueEventListener(new ValueEventListener() {
            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
             *
             * @param snapshot Representa el nodo de la base de datos a actualizar.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //mSolicitudes.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String remitente = ds.getKey();
                        Solicitud s=new Solicitud(remitente);
                        s.setSolicitudAmistad();
                        mSolicitudes.add(s);
                        Log.d("Solicitudes", mSolicitudes.size()+"");

                    }
                    Log.d("Solicitudes123", mSolicitudes.size()+"");
                    solicitudesAdapterAmigos = new SolicitudesAdapter(Solicitudes.this, R.layout.item_solicitud, mSolicitudes, nick);
                    recyclerViewSolicitudesAmigos.setAdapter(solicitudesAdapterAmigos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }
        );

        mDatabase.child("users").child(nick).child("solicitudes").child("listas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //mSolicitudes.clear();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        String remitente=ds.getValue().toString();
                        for (DataSnapshot ds2:ds.getChildren()){
                            String idLista=ds2.getKey();
                            String nombreLista=ds2.getValue().toString();
                            Solicitud s=new Solicitud(remitente);
                            s.setSolicitudLista(idLista,nombreLista);
                            mSolicitudes.add(s);
                        }
                    }
                    solicitudesAdapterListas = new SolicitudesAdapter(Solicitudes.this, R.layout.item_solicitud, mSolicitudes, nick);
                    recyclerViewSolicitudesListas.setAdapter(solicitudesAdapterListas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}