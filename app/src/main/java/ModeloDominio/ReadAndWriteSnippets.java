package ModeloDominio;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.accesoconcorreo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ListaListAdapter;

public class ReadAndWriteSnippets {

    private static final String TAG = "ReadAndWriteSnippets";

    // [START declare_database_ref]
    private static DatabaseReference mDatabase;

    // [END declare_database_ref]

    public ReadAndWriteSnippets() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    // [START rtdb_write_new_user]
    public void insertarUsuario(String name, String email) {
        Usuario user = new Usuario(name, email);
        mDatabase.child("users").child(name).setValue(user.toMap());
    }
    public void writeNewUserWithTaskListeners(String userId, String name, String email) {
        Usuario user = new Usuario(name, email);

        // [START rtdb_write_new_user_task]
        mDatabase.child("users").child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        // [END rtdb_write_new_user_task]
    }

    public Usuario convertirAUsuario(String nick){
        Usuario u=null;
       // mDatabase.child("users").child(nick).get().addLis;
       // Log.d("FIREBASE",String.valueOf(task.getResult().getValue()));
       // u=new Usuario(nick,(String)m.get("email"));
        //u.setListas((List<String>) m.get("listas"));
        return u;
    }
    public static void actualizaContadorListas(){

        mDatabase.child("contadorLista").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String n=dataSnapshot.getValue().toString();
                Lista.setContLista(Integer.valueOf(n));
                Log.d("CONTADOR","Contador "+Lista.getContLista());
                Log.d("Contador","msgCont "+n);
            }
        });

    }
    public void insertContadorListas(int n){
        mDatabase.child("contadorLista").setValue(n);
    }
    public void insertarLista(String nombrelista,String nick) {

        actualizaContadorListas();

        List<String> listusuarios=new ArrayList<>();
        listusuarios.add(nick);
        Log.d("Contador","INSERTAR:"+ Lista.getContLista());
        Lista list=new Lista(nombrelista,listusuarios);

        Map<String,Object> postValues = list.toMap();

        Usuario u=this.convertirAUsuario(nick);


        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("nombre").setValue(nombrelista);
        mDatabase.child("users").child(nick).child("listas").child(String.valueOf(list.getIdLista())).setValue(nombrelista);

       insertContadorListas(Lista.getContLista());
    }

    public List<String> obtenerListasbyUserID(String nick) {
        //Usuario u=this.convertirAUsuario(nick);
        List<String> llistas = new ArrayList<>();
        mDatabase.child("users").child(nick).child("listas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Iterable<DataSnapshot> ds= snapshot.getChildren();
                    for (DataSnapshot d:ds) {
                        llistas.add(String.valueOf(d.getValue()));

                    }
                    //crear adapter para mostrar en recycler
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }
        );
        return llistas;
    }
/*
    public void obtenerTodosProductos() {
        List<String> llistas = new ArrayList<>();
        mDatabase.child("json").child("results").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getValue().toString();
                        llistas.add(nombre);
                    }
                    mListaAdapter= new ListaListAdapter(R.layout.pantalla_listas_list,llistas);
                    recyclerView.setAdapter(mListaAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
                                                                                            }
        );

    }*/

}
