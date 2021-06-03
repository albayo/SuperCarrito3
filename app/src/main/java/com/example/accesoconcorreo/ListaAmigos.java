package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import Adapters.ProductListAdapter;
import Adapters.SolicitudesAdapter;
import ModeloDominio.Lista;
import ModeloDominio.Producto;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Solicitud;

/**
 * Esta clase define la actividad (llamada "activity_lista_amigos") que dispondrá en pantalla las
 *  listas que tiene un determinado usuario
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
public class ListaAmigos extends AppCompatActivity {
    //Lista con los nicks de los amigos del usuario
    private List<String> mAmigos;
    //Lista con los correos de los amigos del usuario
    private List<String> mCorreos;
    //Referencia a la base de datos
    private DatabaseReference mDatabase;
    //Instancia del adapter para mostrar la información de cada amigo del usuario
    private ListaAmigosAdapter listaAmigosAdapter;
    //Representa el RecyclerView en el cual se dispondrán las solicitudes del usuario
    private Toolbar toolbar;
    //Representa el recycler view donde estarán los ítems
    private RecyclerView recyclerViewAmigos;
    //Representa el drawerLayout en el que se encuentra el menú principal
    private DrawerLayout drawerLayout;
    //Representa el menú de la aplicación
    private NavigationView navigationView;
    //Representa el nick del usuario.
    private String nick;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_lista_amigos" y poder mostrar los amigos del usuario
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_amigos);

        toolbar = (Toolbar) findViewById(R.id.amigos_toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAmigos=new ArrayList<>();
        mCorreos=new ArrayList<>();
        recyclerViewAmigos=findViewById(R.id.amigos_recycler);
        recyclerViewAmigos.setLayoutManager(new LinearLayoutManager(this));
        nick=getIntent().getStringExtra("nick");

        String title="Amigos";

        toolbar.setTitle(title);
        FloatingActionButton fab=findViewById(R.id.fab_Aniadir_Amigos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                introducir_amigo amigoDF = new introducir_amigo(nick);
                amigoDF.show(getSupportFragmentManager(),"tag");
            }
        });

        drawerLayout= findViewById(R.id.drawer_layout_amigos);
        navigationView= findViewById(R.id.nav_View);
        navigationView.setCheckedItem(getIntent().getIntExtra("menuitem", 0));
        Activity activity=this;
        String idLista=" ",nombreLista=" ",modo=" ";
        modo=getIntent().getExtras().getString("modo");
        idLista=getIntent().getExtras().getString("idLista");
        nombreLista=getIntent().getStringExtra("nombreLista");
        if(modo==null){
            modo=" ";
            idLista=" ";nombreLista=" ";

        }
        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,getIntent().getStringExtra("email"),activity,getApplicationContext());
        obtenerAmigosUsuario(nick,modo,idLista,nombreLista);


    }

    /**
     * Método que sirve para leer de la base de datos todos los amigos del usuario. Cada amigo lo va intorduciendo en la lsita de nicks y de emails.
     * @param nick Nick del usuario
     * @param modo Indica desde dónde se ha llamado a esta actividad. Si es para visualizar los amigos o para añadirlos a una lista compartida.
     * @param idLista Id de la lista en el caso de que se haya llamado a la actividad para añadir amigos a una lista
     * @param nombreLista Nombre de la lista en el caso de que se haya llamado a la actividad para añadir amigos a una lista
     */
    public void obtenerAmigosUsuario(String nick, String modo,String idLista,String nombreLista) {

        //NECESARIO PARA BORRAR EL ÚLTIMO
        mDatabase.child("users").child(nick).child("amigos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            /**
             * Método para hacer la lectura de los amigos de un usario de la base de datos.
             * @param task
             */
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()==null){
                        mAmigos.clear();
                        listaAmigosAdapter= new ListaAmigosAdapter(ListaAmigos.this,R.layout.amigos_recycler, mAmigos,mCorreos,nick,modo,idLista,nombreLista);
                        recyclerViewAmigos.setAdapter(listaAmigosAdapter);
                    }
                }
            }
        });
        mDatabase.child("users").child(nick).child("amigos").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar los amigos de manera actualizada
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

                    listaAmigosAdapter= new ListaAmigosAdapter(ListaAmigos.this,R.layout.amigos_recycler, mAmigos,mCorreos,nick,modo,idLista,nombreLista);
                    recyclerViewAmigos.setAdapter(listaAmigosAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}