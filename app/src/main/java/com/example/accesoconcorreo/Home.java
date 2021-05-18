package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaListAdapter;
import ModeloDominio.Lista;
import ModeloDominio.Producto;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * Esta clase define la actividad (llamada "activity_home") que dispondrá en pantalla las
 *  listas que tiene un determinado usuario
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */

public class Home extends AppCompatActivity {
    //Representa el objeto recycler view de las listas
    private RecyclerView recyclerView;
    //Representa la base de datos
    private FirebaseDatabase database;
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabaseReference;
    //Representa un adapter de tipo lista para poder mostrar las listas del usuario
    private ListaListAdapter mListaAdapter;
    //
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private List<Lista> listas;

    private Toolbar toolbar;
    private String email;
    private String nick;



    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     *  "activity_home" y poder mostrar las listas de los usuarios
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        email = getIntent().getExtras().get("email").toString();
        //cambiar esto al user normal
        nick = getIntent().getStringExtra("nick");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.homeToolbar);

        myToolbar.setTitle("SuperCarrito-" + nick);
        myToolbar.inflateMenu(R.menu.menu_home_toolbar);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.carrito_productos) {
                    showPerfil(nick,email);

                    return true;
                }

                if(id == R.id.borrar_lista){
                    removeLista();

                    return true;
                }
                return false;
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_lista_super_prod);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fabAñadirLista = findViewById(R.id.fabAniadir_lista);

        /**
         * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
         *  "activity_crear_lista" que sirve para que un usario añade una lista
         */
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
                listas.add(l);
            }
        });



        Usuario u = new Usuario(nick, email);

        this.obtenerListasUsuario(u.getNick());

        drawerLayout= findViewById(R.id.drawer_layout_home);
        navigationView= findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.homeToolbar);
        Activity activity=this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,activity,getApplicationContext());

    }

    private void showPerfil(String nick, String email) {
        Intent homeIntent = new Intent(Home.this, Home.class); //debería ir la clase del Perfil
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("nick", nick);
    }

    public void removeLista() {
        for(Lista l:listas){
            if(l.isCheckboxEliminar()){
                mDatabaseReference.child("listas").child(String.valueOf(l.getIdLista())).removeValue();
                mDatabaseReference.child("users").child(nick).child("listas").child(String.valueOf(l.getIdLista())).removeValue();

            }
        }
        obtenerListasUsuario(nick);

    }

    /**
     * Método que sirve para volver a la activity anterior
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{


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
    }

    /*@Override
    protected void onPause() {
        OnBackPressedDispatcher backPressedDispatcher = getOnBackPressedDispatcher();
        backPressedDispatcher.
        super.onPause();

    }*/
    /**
     * Método que sirve para obtener las listas de un usario accendiendo a la base de datos por su nick
     * @param nick
     */
    public void obtenerListasUsuario(String nick) {
        listas=new ArrayList<>();

        //PARA BORRAR LA ULTIMA LISTA
        mDatabaseReference.child("users").child(nick).child("listas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()==null){
                        listas.clear();
                        mListaAdapter = new ListaListAdapter(Home.this,R.layout.pantalla_listas_list,listas);
                        recyclerView.setAdapter(mListaAdapter);
                    }
                }
            }
        });



        mDatabaseReference.child("users").child(nick).child("listas").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar las listas de manera actualizada
             * @param snapshot
             */
            boolean grupal=false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   listas.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        String id=ds.getKey();
                        Log.d("Obtener",id);
                        String nombre = ds.getValue().toString();

                        Lista l =new Lista(nombre,id);
                        listas.add(l);
                    }
                    Log.d("Adapter","LISta");

                    mListaAdapter = new ListaListAdapter(Home.this,R.layout.pantalla_listas_list,listas);
                    recyclerView.setAdapter(mListaAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }




}