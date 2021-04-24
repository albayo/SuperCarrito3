package ModeloDominio;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAndWriteSnippets {

    private static final String TAG = "ReadAndWriteSnippets";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;

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
   /* public Usuario getUsuario(String userId){ //MIRAR BIEN
        mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            //NOSE PARA QUE USA ESTO , NOSE COMO SACAR EL USUARIO DESPUES DEL ON COMPLETE.
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));


                }
            }
        }).getResult().getValue();

        return u;
    }*/

    public Usuario convertirAUsuario(String nick){
        Usuario u=null;
        mDatabase.child("users").child(nick).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("FIREBASE",String.valueOf(task.getResult().getValue()));
                }
            }
        });
       // Log.d("FIREBASE",String.valueOf(task.getResult().getValue()));
       // u=new Usuario(nick,(String)m.get("email"));
        //u.setListas((List<String>) m.get("listas"));
        return u;
    }


    public void insertarLista(String nombrelista,String nick){

        List<String> listusuarios=new ArrayList<>();
        listusuarios.add(nick);
        Lista list=new Lista(nombrelista,listusuarios);
        Map<String,Object> postValues = list.toMap();

        Usuario u=this.convertirAUsuario(nick);

        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("nombre").setValue(nombrelista);
        mDatabase.child("users").child(nick).child("listas").child(String.valueOf(list.getIdLista())).setValue(nombrelista);
        /*
        Map<String,Object> childUpdates= new HashMap<>();
        //No sabemos si es asi la url en la que inserta.
        childUpdates.put("/users/"+u.getNick()+"/listas/"+IDLista,nombrelista);
        childUpdates.put("/listas/"+IDLista,postValues);

        mDatabase.updateChildren(childUpdates);
         */
    }

    public List<String> obtenerListasbyUserID(String nick){
        Usuario u=this.convertirAUsuario(nick);
        return (List<String>) mDatabase.child("users").child(nick).child("listas").get().getResult().getValue();
    }
}
