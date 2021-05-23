package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Adapters.ListaListAdapter;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class Perfil extends AppCompatActivity {
    //Representa una la base de datos
    private FirebaseDatabase database;
    //Representa una referencia a la base de datos
    private DatabaseReference mDatabaseReference;
    //Representa un adapter de tipo lista para poder mostrar las listas del usuario
    private ListaListAdapter mListaAdapter;
    //Representa el layout del menu
    private DrawerLayout drawerLayout;
    //Representa un navigation
    private NavigationView navigationView;
    private Toolbar toolbar;
    private StorageReference mStorage;

    private String email;
    private String nick;
    private ImageView fotoperfil;
    private static final int GALLERY_INTENT=1;

    private ImageButton btnSubir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();
        email = getIntent().getExtras().get("email").toString();
        //cambiar esto al user normal
        nick = getIntent().getStringExtra("nick");
        mStorage= FirebaseStorage.getInstance().getReference();

        Usuario u = new Usuario(nick, email);

        drawerLayout= findViewById(R.id.drawer_layout_perfil);
        navigationView= findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.perftoolbar);
        Activity activity=this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,activity,getApplicationContext());

        TextView nickt=findViewById(R.id.text_username);
        TextView emailt=findViewById(R.id.text_username2);
        fotoperfil=findViewById(R.id.imageview_fotoperfil);
        btnSubir=findViewById(R.id.ibutt_subirfoto);

        toolbar.setTitle("Perfil");


        emailt.setText(email);
        nickt.setText(nick);
        obtenerFoto(nick);


        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


        }
    public void obtenerFoto(String name){
        mDatabaseReference.child("users").child(name).child("fotoperfil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    /*fotoperfil.setImageURI(snapshot.getValue());
                    Uri uri=new Uri();
                    fotoperfil.se
*/
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}