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

public class SolicitudesAmigos extends AppCompatActivity {
    private List<Solicitud> mSolicitudesAmigos;

    private DatabaseReference mDatabase;
    private SolicitudesAdapter solicitudesAdapterAmigos;


    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewSolicitudesAmigos;


    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        toolbar = (Toolbar) findViewById(R.id.solicitudesAmigosToolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSolicitudesAmigos =new ArrayList<>();


        recyclerViewSolicitudesAmigos=findViewById(R.id.solicitudes_recyclerAmigos);
        recyclerViewSolicitudesAmigos.setLayoutManager(new LinearLayoutManager(this));


        nick=getIntent().getStringExtra("nick");

        String title="Supercarrito - "+nick;

        toolbar.setTitle(title);
        obtenerSolicitudes(nick);



    }

    public void obtenerSolicitudes(String nick){
        mDatabase.child("users").child(nick).child("solicitudes").child("amistad").addValueEventListener(new ValueEventListener() {
            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
             *
             * @param snapshot Representa el nodo de la base de datos a actualizar.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    mSolicitudesAmigos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String remitente = ds.getKey();
                        Solicitud s=new Solicitud(remitente);
                        s.setSolicitudAmistad();
                        mSolicitudesAmigos.add(s);
                        Log.d("SolicitudesAmigos", mSolicitudesAmigos.size()+"");

                    }
                    solicitudesAdapterAmigos = new SolicitudesAdapter(SolicitudesAmigos.this, R.layout.item_solicitud, mSolicitudesAmigos, nick);
                    recyclerViewSolicitudesAmigos.setAdapter(solicitudesAdapterAmigos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }
        );

    }

}