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

    public static void aniadirUsuarioaList(String nombreLista, String nick, String idLista,String email){
        mDatabase.child("listas").child(idLista).child("usuarios").child(nick).setValue(email);
        mDatabase.child("users").child(nick).child("listas").child(idLista).setValue(nombreLista);
    }

    // [START rtdb_write_new_user]
    public void insertarUsuario(String name, String email) {
        Usuario user = new Usuario(name, email);
        if(mDatabase.child("users").child(name).getKey()==null){
            mDatabase.child("users").child(name).setValue(user.toMap());
        }

    }

    public String getNick(String email){
        Usuario user = new Usuario(email);
        String nick = mDatabase.child("users").
        if(nick == null){
            nick = email.split("@")[0];
        }

        return nick;
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
    public static void insertContadorListas(int n){
        mDatabase.child("contadorLista").setValue(n);
    }
    public static void insertarLista(String nombrelista,String nick) {

        actualizaContadorListas();

        List<String> listusuarios=new ArrayList<>();
        listusuarios.add(nick);
        Log.d("Contador","INSERTAR:"+ Lista.getContLista());
        Lista list=new Lista(nombrelista,listusuarios);

        Map<String,Object> postValues = list.toMap();
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("nombre").setValue(nombrelista);
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("productos").setValue("pr1");
        mDatabase.child("users").child(nick).child("listas").child(String.valueOf(list.getIdLista())).setValue(nombrelista);

       insertContadorListas(Lista.getContLista());
    }



}
