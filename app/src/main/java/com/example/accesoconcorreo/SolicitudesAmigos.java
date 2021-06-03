package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.SolicitudesAdapter;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Solicitud;

/**
 * Esta clase define la actividad (llamada "activity_solicitudes") que dispondrá en pantalla las
 *  listas que tiene un determinado usuario
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 03/06/2021
 */
public class SolicitudesAmigos extends AppCompatActivity {
    //Representa todas las solicitudes de amigo de un usuario
    private List<Solicitud> mSolicitudesAmigos;
    //Representa una referencia a la BD
    private DatabaseReference mDatabase;
    //Representa el adpater con el cual se dispondrán los datos por pantalla
    private SolicitudesAdapter solicitudesAdapterAmigos;
    //Representa la barra de la actividad
    private Toolbar toolbar;
    //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewSolicitudesAmigos;
    //Representa el DrawerLayout donde se encuentra el menú
    private DrawerLayout drawerLayout;
    //Representa el menú de la aplicación
    private NavigationView navigationView;
    //Representa el nick del usuario
    private String nick;
    //Representa el email del usuario
    private String email;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_solicitudes" y poder mostrar las listas de los usuarios
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);

        toolbar = (Toolbar) findViewById(R.id.solicitudesAmigosToolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSolicitudesAmigos =new ArrayList<>();

        drawerLayout=findViewById(R.id.drawer_layout_solicitudes);
        navigationView=findViewById(R.id.nav_view_solicitudes);

        recyclerViewSolicitudesAmigos=findViewById(R.id.solicitudes_recyclerAmigos);
        recyclerViewSolicitudesAmigos.setLayoutManager(new LinearLayoutManager(this));

        nick=getIntent().getStringExtra("nick");
        email=getIntent().getStringExtra("email");

        String title="Solicitudes Amigos";

        toolbar.setTitle(title);

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,SolicitudesAmigos.this,getApplicationContext());

        obtenerSolicitudes(nick);
    }

    /**
     * Método que obtiene todas las solicitudes de un usuario y las guarda en el atributo mSolicitudesAmigos
     * @param nick
     */
    public void obtenerSolicitudes(String nick){
        mDatabase.child("users").child(nick).child("solicitudes").child("amistad").addValueEventListener(new ValueEventListener() {
            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
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