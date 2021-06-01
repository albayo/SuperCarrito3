package ModeloDominio;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.example.accesoconcorreo.Perfil;
import com.example.accesoconcorreo.R;
import com.example.accesoconcorreo.SolicitudesAmigos;
import com.example.accesoconcorreo.SolicitudesLista;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Esta clase define métodos que serán usados para poder insertar, eliminar, ... elementos de la BD (entre otros)
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 31/05/2021
 */
public class ReadAndWriteSnippets {
    //represtenta la cadena que se usará para mandar mensajes al log y que así se puedan identificar
    private static final String TAG = "ReadAndWriteSnippets";

    // [START declare_database_ref]
    private static DatabaseReference mDatabase;

    private static String usuario;

    // [END declare_database_ref]

    /**
     * Constructor de la clase
     */
    public ReadAndWriteSnippets() {
        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    /**
     * Añade un usuario a una lista
     * @param nombreLista representa el nombre de la lista a la cual se quiere añadir el usuario
     * @param nick        representa el nick del usuario que quiere añadirse a la lista
     * @param idLista     representa el id de la lista a la cual se quiere añadir el usuario
     */
    public static void aniadirUsuarioaList(String nombreLista, String nick, String idLista) {
        mDatabase.child("users").child(nick).child("listas").child(idLista).setValue(nombreLista);
    }

    /**
     * Busca en la lista de productos "productos" todos los elementos cuyo nombre tiene similitud con la cadena pasada por parámetro y que pertenecen a la categoría pasada por parámetro
     * @param productos      representa la lista de productos que se quiere filtrar
     * @param nombreProds    representa el nombre de un producto o parte de este por el que se quiere filtrar la lista
     * @param categoriaSelec representa una de las categorías posibles en las que se subdividen los productos
     * @return devuelve una lista que es un subconjunto de la lista pasada por parámetro
     */
    public static List<Producto> buscarConCategoría(List<Producto> productos, String nombreProds, String categoriaSelec) {
        List<Producto> productoList = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(nombreProds.toLowerCase()) && p.getCategoria().equals(categoriaSelec)) {
                productoList.add(p);
            }
        }

        return productoList;
    }

    // [START rtdb_write_new_user]

    /**
     * Inserta el usuario con el nombre "name" y el email "email" en la BD
     * @param name    representa el nombre que tendrá el usuario en la BD
     * @param email   representa el email con el que se ha logueado el usuario en la aplicación
     * @param context
     */
    public void insertarUsuario(String name, String email, Context context) {
        Usuario user = new Usuario(name, email);
        mDatabase.child("users").child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() == null) {
                        String[] emailnodo = email.split("@");
                        String emailinsert1 = "" + emailnodo[0] + emailnodo[1];
                        Log.d("email", emailinsert1);
                        String[] emailnodo2 = emailinsert1.split("\\.");
                        String emailinsert2 = "" + emailnodo2[0] + emailnodo2[1];
                        Log.d("email", emailinsert2);
                        mDatabase.child("correousuario").child(emailinsert2).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task2) {
                                if (task2.isSuccessful()) {
                                    if (task2.getResult().getValue() == null) {
                                        mDatabase.child("users").child(name).setValue(user.toMap());
                                        mDatabase.child("correousuario").child(emailinsert2).setValue(name);
                                    }
                                } else {
                                    Toast toast = new Toast(context);
                                    toast.makeText(context, "Este correo ya tiene un usuario, pruebe con otro correo", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                            }
                        });

                    } else {

                    }
                }
            }
        });

    }

    /**
     * Actualiza una variable de la BD que sirve para llevar cuenta de cuantas listas existen
     */
    public static void actualizaContadorListas() {

        mDatabase.child("contadorLista").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String n = dataSnapshot.getValue().toString();
                Lista.setContLista(Integer.valueOf(n));
                Log.d("CONTADOR", "Contador " + Lista.getContLista());
                Log.d("Contador", "msgCont " + n);
            }
        });

    }

    /**
     * Modifica el valor del contador de listas en la BD
     * @param n es el valor que se le quiere dar al contador de listas
     */
    public static void insertContadorListas(int n) {
        mDatabase.child("contadorLista").setValue(n);
    }

    /**
     * Inserta la lista pasada por parámetro
     * @param nombrelista representa el nombre de la lista
     * @param nick        representa el nick del usuario que ha creado la lista
     * @param compartida  representa que es una lista compartida en caso de que sea true, y que es una lista individual en caso de que sea false
     */
    public static void insertarLista(String nombrelista, String nick, boolean compartida) {

        actualizaContadorListas();

        List<String> listusuarios = new ArrayList<>();
        listusuarios.add(nick);
        Log.d("Contador", "INSERTAR:" + Lista.getContLista());
        Lista list = new Lista(nombrelista, listusuarios);

        Map<String, Object> postValues = list.toMap();
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("nombre").setValue(nombrelista);
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("productos").setValue("pr1");
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("propietario").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() == null) {
                        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("propietario").setValue(nick);
                    }
                }
            }
        });
        if (compartida) {
            mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("compartida").setValue("true");
        }
        mDatabase.child("listas").child(String.valueOf(list.getIdLista())).child("miembros").child(nick).setValue(nick);
        mDatabase.child("users").child(nick).child("listas").child(String.valueOf(list.getIdLista())).setValue(nombrelista);

        insertContadorListas(Lista.getContLista() + 1);
    }

    /**
     * Manda una solicitud para crear una lista compartida al usuario con nick "nick"
     * @param usuarioActual representa el nick del usuario actual
     * @param nick          representa el nick del usuario al que se quiere enviar la solicitud
     * @param idLista       representa el id de la lista que quiere convertirse en compartida
     * @param nombreLista   representa el nombre de la lista que quiere convertirse en compartida
     */
    public static void solicitudLista(String usuarioActual, String nick, String idLista, String nombreLista) {
        mDatabase.child("users").child(nick).child("solicitudes").child("listas").child(usuarioActual).child(idLista).setValue(nombreLista);
    }

    /**
     * Manda una solicitud para añadir al usuario con nick "nick" como amigo
     * @param usuarioActual representa el nick del usuario actual
     * @param nick          representa el nick del usuario al que se quiere enviar la solicitud
     */
    public static void solicitudAmistad(String usuarioActual, String nick) {
        mDatabase.child("users").child(nick).child("solicitudes").child("amistad").child(usuarioActual).setValue("pendiente");
    }

    /**
     * Elimina una solicitud para crear una lista compartida
     * @param usuarioActual representa el nick del usuario actual
     * @param remitente     representa el usuario al que se había mandado la solicitud
     * @param idLista       representa la lista sobre la cual se había enviado la solicitud
     */
    public static void eliminarSolicitudLista(String usuarioActual, String remitente, String idLista) {
        mDatabase.child("users").child(usuarioActual).child("solicitudes").child("listas").child(remitente).child(idLista).removeValue();
    }

    /**
     * Elimina una solicitud de amigo
     * @param usuarioActual representa el nick del usuario actual
     * @param remitente     representa el usuario al que se había mandado la solicitud
     */
    public static void eliminarSolicitudAmistad(String usuarioActual, String remitente) {
        mDatabase.child("users").child(usuarioActual).child("solicitudes").child("amistad").child(remitente).removeValue();
    }

    /**
     * Añade como amigo al usuario con nick "amigo"
     * @param usuarioActual representa el nick del usuario actual
     * @param amigo         representa el usuario que se quiere añadir como amigo
     */
    public static void aniadirAmigo(String usuarioActual, String amigo) {

        mDatabase.child("users").child(usuarioActual).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() != null) {
                        String correo = task.getResult().getValue().toString();
                        mDatabase.child("users").child(amigo).child("amigos").child(usuarioActual).setValue(correo);
                    }
                }
            }
        });

    }

    /**
     * Añade una lista a la BD
     *
     * @param nick        representa el usuario que ha creado la lista
     * @param idLista     representa el id de la lista que se va a añadir
     * @param nombreLista representa el nombre que se le ha dado a la lista que se quiere añadir
     */
    public static void aniadirLista(String nick, String idLista, String nombreLista) {
        mDatabase.child("users").child(nick).child("listas").child(idLista).setValue(nombreLista);
        mDatabase.child("listas").child(idLista).child("miembros").child(nick).setValue(nick);
        mDatabase.child("listas").child(idLista).child("compartida").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getValue() == null) {
                        mDatabase.child("listas").child(idLista).child("compartida").setValue("true");

                    }
                }
            }
        });

    }

    /**
     * Busca en la lista de productos "productos" todos los elementos cuyo nombre tiene similitud con la cadena pasada por parámetro
     *
     * @param productos   representa la lista de productos que se quiere filtrar
     * @param nombreProds representa el nombre de un producto o parte de este por el que se quiere filtrar la lista
     * @return devuelve una lista que es un subconjunto de la lista pasada por parámetro
     */
    public static List<Producto> buscarProductos(List<Producto> productos, String nombreProds) {
        List<Producto> productoList = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(nombreProds.toLowerCase())) {
                productoList.add(p);
            }
        }

        return productoList;
    }

    /**
     * Obtiene el nick de un usuario a partir de su email
     *
     * @param email representa el email del usuario
     * @return nick del usuario
     */
    public static String getNick(String email) {

        String[] emailnodo = email.split("@");
        String emailinsert1 = "" + emailnodo[0] + emailnodo[1];

        String[] emailnodo2 = emailinsert1.split("\\.");
        String emailinsert2 = "" + emailnodo2[0] + emailnodo2[1];

        return emailinsert2;
    }

    /**
     * Sirve para llevar al usuario al correspondiente activity según que "opción" del menú lateral alla seleccionado
     *
     * @param drawerLayout   representa el drawerLayout del menú con el que se ha interactuado
     * @param navigationView representa el menú lateral con el que se ha interactuado
     * @param toolbar        representa el toolbar del activity a la cual pertenecen los 2 anteriores parámetros
     * @param nick           representa el nick del usuario que está logueado
     * @param email          representa el email del usuario que está logueado
     * @param activity       representa la actividad en la que se estaba cuando se ha interactuado con el menú lateral
     * @param context        representa el contexto de la actividad
     */
    public static void setNavigationView(DrawerLayout drawerLayout, NavigationView navigationView, androidx.appcompat.widget.Toolbar toolbar, String nick, String email, Activity activity, Context context) {

        //NAVIGATION
        //--------------------------------------------------------------------------------------------------------
        //HOOKS AL MENU


        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //LISTENERS
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (!(toolbar.getId() == R.id.homeToolbar)) {
                            Intent intentH = new Intent(context, Home.class);
                            intentH.putExtra("email", email);
                            intentH.putExtra("nick", nick);
                            intentH.putExtra("menuitem", item.getItemId());
                            intentH.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intentH);
                        }
                        break;
                    case R.id.nav_amigos:

                        if (!(toolbar.getId() == R.id.amigos_toolbar)) {
                            Intent intent = new Intent(context, ListaAmigos.class);
                            intent.putExtra("email", email);
                            intent.putExtra("nick", nick);
                            intent.putExtra("menuitem", item.getItemId());
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.nav_logout:
                        /*Intent intentlogout = new Intent(context, Login.class);
                        intentlogout.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentlogout);*/
                        context.startActivity(new Intent(context, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | FLAG_ACTIVITY_NEW_TASK));
                        break;
                    case R.id.nav_solicitudesamigos:
                        Intent solamIntent = new Intent(context, SolicitudesAmigos.class);
                        solamIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        solamIntent.putExtra("nick", nick);
                        solamIntent.putExtra("email", email);
                        solamIntent.putExtra("menuitem", item.getItemId());
                        context.startActivity(solamIntent);
                        break;
                    case R.id.nav_solicitudeslista:
                        Intent solliIntent = new Intent(context, SolicitudesLista.class);
                        solliIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        solliIntent.putExtra("nick", nick);
                        solliIntent.putExtra("email", email);
                        solliIntent.putExtra("menuitem", item.getItemId());
                        context.startActivity(solliIntent);
                        break;
                    case R.id.nav_profile:
                        Intent profintent = new Intent(context, Perfil.class);
                        profintent.putExtra("email", email);
                        profintent.putExtra("nick", nick);
                        profintent.putExtra("menuitem", item.getItemId());
                        profintent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(profintent);
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        //--------------------------------------------------------------------------------------------------------
    }


}
