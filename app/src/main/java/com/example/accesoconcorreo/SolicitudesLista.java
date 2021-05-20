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

public class SolicitudesLista extends AppCompatActivity {
    private List<Solicitud> mSolicitudesListas;
    private DatabaseReference mDatabase;
    private SolicitudesAdapter solicitudesAdapterListas;

    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewSolicitudesListas;

    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes_lista);

        toolbar = (Toolbar) findViewById(R.id.solicitudesListasToolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSolicitudesListas =new ArrayList<>();

        recyclerViewSolicitudesListas=findViewById(R.id.solicitudes_recyclerListas);
        recyclerViewSolicitudesListas.setLayoutManager(new LinearLayoutManager(this));

        nick=getIntent().getStringExtra("nick");

        String title="Supercarrito - "+nick;

        toolbar.setTitle(title);
        obtenerSolicitudes(nick);
    }

    public void obtenerSolicitudes(String nick){
        mSolicitudesListas.clear();
        mDatabase.child("users").child(nick).child("solicitudes").child("listas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    mSolicitudesListas.clear();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        String remitente=ds.getKey().toString();
                        for (DataSnapshot ds2:ds.getChildren()){
                            String idLista=ds2.getKey();
                            String nombreLista=ds2.getValue().toString();
                            Solicitud s=new Solicitud(remitente);
                            s.setSolicitudLista(idLista,nombreLista);
                            mSolicitudesListas.add(s);
                            Log.d("SolicitudesListas", mSolicitudesListas.size()+"");
                        }
                    }
                    solicitudesAdapterListas = new SolicitudesAdapter(SolicitudesLista.this, R.layout.item_solicitud, mSolicitudesListas, nick);
                    recyclerViewSolicitudesListas.setAdapter(solicitudesAdapterListas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}