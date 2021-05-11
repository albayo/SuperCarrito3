package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import Adapters.ListaListAdapter;
import ModeloDominio.Lista;

import static android.widget.Toast.makeText;

public class ListaAmigos2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    //Representa la base de datos
    private FirebaseDatabase database;
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabaseReference;
    //Representa un adapter de tipo lista para poder mostrar las listas del usuario
    private ListaAmigosAdapter adapter;
    //
    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private Toolbar toolbar;
    private String email;
    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ENTRAMOOOSSS","AMIGOOOOSSS");
        setContentView(R.layout.activity_home);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        email = getIntent().getExtras().get("email").toString();
        //cambiar esto al user normal
        nick = getIntent().getStringExtra("nick");
        if(nick==null) nick="nick";
       // Toolbar myToolbar = (Toolbar) findViewById(R.id.amigosToolbar);

        //myToolbar.setTitle("SuperCarrito-"+nick );
        recyclerView = (RecyclerView) findViewById(R.id.recycler_lista_amigos);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListaAmigos2.this));
        FloatingActionButton fabAñadirLista = findViewById(R.id.fabAniadir_Amigo);
        fabAñadirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "++++",Toast.LENGTH_LONG);
                toast.show();
            }
        });
        setNavigationView();
        obtenerAmigosUsuario(nick);
    }
    public void obtenerAmigosUsuario(String nick) {
        List<String> lAmigos = new ArrayList<>();
        List<String> lCorreos=new ArrayList<>();
        mDatabaseReference.child("users").child(nick).child("amigos").addValueEventListener(new ValueEventListener() {
            /**
             * Método que cuando cambia un objeto en la base de datos se ejecuta para mostrar las listas de manera actualizada
             * @param snapshot
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lCorreos.clear();
                    lAmigos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getKey();
                        String correo = ds.getValue().toString();
                        lAmigos.add(nombre);
                        lCorreos.add(correo);
                    }
                    adapter= new ListaAmigosAdapter(ListaAmigos2.this,R.layout.pantalla_listas_list, lAmigos,lCorreos);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setNavigationView() {

        //NAVIGATION
        //--------------------------------------------------------------------------------------------------------
        //HOOKS AL MENU

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.homeToolbar);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //LISTENERS
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                    case R.id.nav_amigos:
                        Log.d("NAVIGATOR","A VECINOOOOS");
                        Intent intent = new Intent(ListaAmigos2.this,ListaAmigos.class);
                        intent.putExtra("email", email);
                        intent.putExtra("nick", nick);
                        Toast t= Toast.makeText(getApplicationContext(),"A VECINOS",Toast.LENGTH_LONG);
                        t.show();
                        startActivity(intent);

                    case R.id.nav_listas:
                        Intent homeIntent = new Intent(ListaAmigos2.this, Home.class);
                        homeIntent.putExtra("email", email);
                        homeIntent.putExtra("nick", nick);
                        //homeIntent.putExtra("provider", provider.name());
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_logout:
                        Intent intentlogout = new Intent(getApplicationContext(), Login.class);
                        startActivity(intentlogout);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
}