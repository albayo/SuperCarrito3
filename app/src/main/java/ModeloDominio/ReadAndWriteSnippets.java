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

import java.util.List;

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
    public void insertarUsuario(String userId, String name, String email) {
        Usuario user = new Usuario(name, email);
        mDatabase.child("users").child(userId).setValue(user);
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
    public Usuario getUsuario(String userId){ //MIRAR BIEN
        Usuario u=(Usuario)mDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

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
    }

    public void insertarLista(String listaID, String nombre, List<Usuario> usuarios) {
        Lista lista=new Lista(listaID,nombre,usuarios);
        for (Usuario u:usuarios) {
            u.insertaLista(lista);
        }
        mDatabase.child("listas").child(listaID).setValue(lista);



    }
    public Usuario convertirAUsuario(FirebaseUser user){

        Usuario u=(Usuario)mDatabase.child("users").child(user.getUid()).get().getResult().getValue();
        return u;
    }
}
