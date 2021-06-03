package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Adapters.ListaListAdapter;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

/**
 * Esta clase define la actividad (llamada "activity_mostrar_perfil") que dispondrá en pantalla la información de un usuario
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 03/06/2021
 */
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
    //Representa el toolbar de la pantalla
    private Toolbar toolbar;
    //Referencia al almacenamiento de imágenes de Firebase
    private StorageReference mStorage;
    //Email del usuario
    private String email;
    //Nick del usuario
    private String nick;
    //Representación de la imágen de perfil del usuario
    private ImageView fotoperfil;

    private static final int GALLERY_INTENT=1;
    //Imagen de carga mientras se muestra la imágen en el perfil
    private ProgressDialog progressDialog;
    //Número de amigos que tiene el usuario
    private int numeroAmigos;
    //Representa un botón que te llevara a la lista de amigos del usuario
    private Button btnAmigos;
    //Representa un botón con imágen que te permite subir una imágen como foto de perfil
    private ImageButton btnSubir;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_mostrar_perfil"
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference();

        email = getIntent().getExtras().get("email").toString();

        nick = getIntent().getStringExtra("nick");
        mStorage= FirebaseStorage.getInstance().getReference();
        numeroAmigos=0;
        Usuario u = new Usuario(nick, email);

        drawerLayout= findViewById(R.id.drawer_layout_perfil);
        navigationView= findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.perftoolbar);
        navigationView.setCheckedItem(getIntent().getIntExtra("menuitem", 0));
        Activity activity=this;

        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,activity,getApplicationContext());

        TextView nickt=findViewById(R.id.text_username);
        TextView numAmigos=findViewById(R.id.text_numeroam);
        TextView emailt=findViewById(R.id.text_email);
        fotoperfil=findViewById(R.id.imageview_fotoperfil);
        btnSubir=findViewById(R.id.ibutt_subirfoto);
        btnAmigos=findViewById(R.id.button_amigos);


        toolbar.setTitle("Perfil");
        progressDialog=new ProgressDialog(this);
        numeroAmigos(numAmigos);
        emailt.setText(email);
        nickt.setText(nick);
        numAmigos.setText(String.valueOf(numeroAmigos));
        obtenerFoto(nick);

        btnAmigos.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que hace que al hacer click en el botón de amigos te lleve a la lista de amigos del usuario
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaAmigos.class);
                intent.putExtra("email", email);
                intent.putExtra("nick", nick);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });


        btnSubir.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que hace que al hacer click en el botón de subir puedas subir una foto como foto de perfil
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });
        }

    /**
     * Método que muestra el número de amigos de un usuario
     * @param numAmigos
     */
    private void numeroAmigos(TextView numAmigos) {
        /**
         * Método que mustra el número de amigos de forma actualizada
         */
        mDatabaseReference.child("users").child(nick).child("amigos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               numAmigos.setText(String.valueOf(snapshot.getChildrenCount()) );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Método que obtiene la imágen de perfil del usuarioa de firebase
     * @param name
     */
    public void obtenerFoto(String name){

        mDatabaseReference.child("users").child(name).child("fotoperfil").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            /**
             * Método que saca la url de la imágen de la base de datos
             * @param dataSnapshot
             */
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue().toString();
                Glide.with(Perfil.this).load(url).fitCenter().centerCrop().override(600,600).transition(withCrossFade()).into(fotoperfil);

            }

        });

    }

    /**
     * Método que sirve para subir la foto de perfil a firebase
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setTitle("Subiendo foto a firebase");
            progressDialog.setCancelable(false);
            progressDialog.show();
            Uri uri= data.getData();

            final StorageReference filePath= mStorage.child("fotos").child(nick);
            UploadTask uploadTask = filePath.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                /**
                 * Método que añade la imágen subida al árbol de a base de datos
                 * @param task
                 */
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(Perfil.this,"Foto subida exitosamente",Toast.LENGTH_LONG).show();
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();
                        mDatabaseReference.child("users").child(nick).child("fotoperfil").setValue(downloadURL);
                        obtenerFoto(nick);
                    } else {
                        Toast.makeText(Perfil.this,"Fallo al subir la foto",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}