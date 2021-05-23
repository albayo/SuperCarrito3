package com.example.accesoconcorreo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import Adapters.ListaListAdapter;
import ModeloDominio.Constantes;
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

    private ProgressDialog progressDialog;

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
        progressDialog=new ProgressDialog(this);

        emailt.setText(email);
        nickt.setText(nick);
        //obtenerFoto(nick);


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

        String url= mDatabaseReference.child("users").child(name).child("fotoperfil").get().getResult().getValue().toString();
        //Picasso.get().load(url).into(fotoperfil);
        /*


        StorageReference filePath=mStorage.child("fotos").child(name);
        Task<Uri> uri=filePath.getDownloadUrl();
        if(uri.isSuccessful()){
            Uri foto=uri.getResult();
            String download=foto.toString();
            Glide.with(Perfil.this).load(download).fitCenter().centerCrop().into(fotoperfil);
        }
        else{
            ponerfotoporfefecto(name);
        }
        */

    }


/*
    public void ponerfotoporfefecto(String nick) {
        StorageReference filePath = mStorage.child(Constantes.URLFOTODEFECTO);
        Task<Uri> uri = filePath.getDownloadUrl();
        if (uri.isSuccessful()) {
            Uri foto = uri.getResult();
            String download = foto.toString();
            Glide.with(Perfil.this).load(download).fitCenter().centerCrop().into(fotoperfil);
        }
        else{
            Toast.makeText(Perfil.this,"Error al mostrar la foto",Toast.LENGTH_LONG).show();
        }
    }
    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setTitle("Subiendo foto a firebase");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Uri uri= data.getData();

            StorageReference filePath= mStorage.child("fotos").child(nick);

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(Perfil.this,"Foto subida exitosamente",Toast.LENGTH_LONG).show();
                    String url=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                    mDatabaseReference.child("users").child(nick).child("fotoperfil").setValue(url);
                }
            });
        }
    }
}