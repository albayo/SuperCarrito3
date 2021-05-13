package ModeloDominio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.accesoconcorreo.Home;
import com.example.accesoconcorreo.ListaAmigos;
import com.example.accesoconcorreo.Login;
import com.example.accesoconcorreo.R;
import com.example.accesoconcorreo.Solicitudes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ReadAndWriteSnippets {

    private static final String TAG = "ReadAndWriteSnippets";

    // [START declare_database_ref]
    private static DatabaseReference mDatabase;

    private static String usuario;

    // [END declare_database_ref]

    public ReadAndWriteSnippets() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    public static void aniadirUsuarioaList(String nombreLista, String nick, String idLista){
        mDatabase.child("users").child(nick).child("listas").child(idLista).setValue(nombreLista);
    }

    // [START rtdb_write_new_user]
    public void insertarUsuario(String name, String email) {
        Usuario user = new Usuario(name, email);
        if(mDatabase.child("users").child(name).getKey()==null){
            mDatabase.child("users").child(name).setValue(user.toMap());
        }

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

       insertContadorListas(Lista.getContLista()+1);
    }

    public static void solicitudLista(String usuarioActual, String nick, String idLista, String nombreLista){
        mDatabase.child("users").child(nick).child("solicitudes").child("listas").child(usuarioActual).child(idLista).setValue(nombreLista);
    }

    public static void solicitudAmistad(String usuarioActual, String nick){
        mDatabase.child("users").child(nick).child("solicitudes").child("amistad").child(usuarioActual).setValue("pendiente");
    }

    public static void eliminarSolicitudLista(String usuarioActual,String remitente,String idLista){
        mDatabase.child("users").child(usuarioActual).child("solicitudes").child("listas").child(remitente).child(idLista).removeValue();
    }

    public static void eliminarSolicitudAmistad(String usuarioActual,String remitente){
        mDatabase.child("users").child(usuarioActual).child("solicitudes").child("amistad").child(remitente).removeValue();
    }

    public static void setNavigationView(DrawerLayout drawerLayout, NavigationView navigationView, androidx.appcompat.widget.Toolbar toolbar, String nick, String email, Activity activity, Context context){

        //NAVIGATION
        //--------------------------------------------------------------------------------------------------------
        //HOOKS AL MENU


        navigationView.bringToFront();

        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(activity,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //LISTENERS
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                    case R.id.nav_amigos:
                        Log.d("NAVIGATOR","A AMIGOS");
                        Intent intent = new Intent(context, ListaAmigos.class);
                        intent.putExtra("email", email);
                        intent.putExtra("nick", nick);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        Toast t= Toast.makeText(context,"A amigos",Toast.LENGTH_LONG);
                        t.show();
                        context.startActivity(intent);
                        break;
                    case R.id.nav_listas:
                        Intent homeIntent = new Intent(context, Home.class);
                        homeIntent.putExtra("email", email);
                        homeIntent.putExtra("nick", nick);
                        // homeIntent.putExtra("provider", provider.name());
                        homeIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(homeIntent);
                        break;
                    case R.id.nav_logout:
                        Intent intentlogout=new Intent(context, Login.class);
                        intentlogout.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentlogout);
                        break;
                    case R.id.nav_solicitudes:
                        Intent solIntent = new Intent(context, Solicitudes.class);
                        solIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        solIntent.putExtra("nick",nick);
                        context.startActivity(solIntent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        //--------------------------------------------------------------------------------------------------------
    }




}
