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
 * Esta clase define la actividad llamada "activity_solicitudes_lista" que sirve para mostrar todas las solcitudes para listas de un usuario.
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 03/06/2021
 */
public class SolicitudesLista extends AppCompatActivity {
    //Representa las solicitudes de listas de un usuario
    private List<Solicitud> mSolicitudesListas;
    //Representa la referencia a la BD
    private DatabaseReference mDatabase;
    //Representa el adapter con el cual se representarán los datos de las correspondientes listas por pantalla
    private SolicitudesAdapter solicitudesAdapterListas;
    //Representa el tollbar de la actividad
    private Toolbar toolbar;
    //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private RecyclerView recyclerViewSolicitudesListas;
    //Representa el nick del usuario
    private String nick;
    //Representa el email del usuario
    private String email;
    //Representa el DrawerLayout donde se encuentra el menú
    private DrawerLayout drawerLayout;
    //Representa el menú de la aplicación
    private NavigationView navigationView;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_solicitudes_lista" y poder mostrar las listas de los usuarios
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
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
        email=getIntent().getStringExtra("email");

        String title="Solicitudes listas";

        drawerLayout=findViewById(R.id.drawer_layout_solicitudesLista);
        navigationView=findViewById(R.id.nav_view_solicitudesLista);
        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,SolicitudesLista.this,getApplicationContext());

        toolbar.setTitle(title);
        obtenerSolicitudes(nick);
    }

    /**
     * Método que obtiene todas las solicitudes de un usuario y las guarda en el atributo mSolicitudesListas
     * @param nick representa el nick del usuario
     */
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