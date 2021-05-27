package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.MiembrosListaAdapter;
import Adapters.ProductListAdapter;
import ModeloDominio.Lista;
import ModeloDominio.Producto;
import ModeloDominio.ReadAndWriteSnippets;
import okhttp3.internal.cache.DiskLruCache;

/**
 * Esta clase define la actividad (llamada "activity_lista_producto") que dispondrá en pantalla los
 * productos que componen una lista determinada
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 25/05/2021
 */
public class ListaProductos extends AppCompatActivity {
    private List<Producto> productos;
    private DatabaseReference mDatabase;
    private ProductListAdapter productosAdapter;
    private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán los Productos de la Lista
    private RecyclerView recyclerViewproductos;

    private String email;
    private String nick;
    private String nombreLista;
    private String idLista;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_lista_productos"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_productos2);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.listaProdToolbar);

        idLista = getIntent().getStringExtra("idLista");
        nombreLista = getIntent().getStringExtra("nombreLista");
        //idLista
        //nombreLista
        email=getIntent().getStringExtra("email");
        nick=getIntent().getStringExtra("nick");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        productos = new ArrayList<>();
        recyclerViewproductos = (RecyclerView) findViewById(R.id.lista_prod_recycler);
        recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
        //habría que cambiar el nombre del toolbar de la aplicación, el que aparece todo el rato
        //ir cambiandolo cada pantalla
        String nombreLista = getIntent().getStringExtra("nombreLista");

        drawerLayout=findViewById(R.id.drawer_layout_listaProd);
        navigationView=findViewById(R.id.nav_view_listaProd);
        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,toolbar,nick,email,ListaProductos.this,getApplicationContext());

        myToolbar.setTitle(nombreLista); // establece el titulo del appBar al nombre de la lista
        myToolbar.inflateMenu(R.menu.menu_lista_prod);
        ReadAndWriteSnippets.setNavigationView(drawerLayout,navigationView,myToolbar,nick,email,ListaProductos.this,getApplicationContext());

        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.carrito_productos) {
                    removeProds();

                    return true;
                } else {
                    if (id == R.id.opciones_añadir_amigos) {
                        Intent homeIntent = new Intent(ListaProductos.this, ListaAmigos.class); //debería ir la clase del Perfil
                        homeIntent.putExtra("email", email);
                        homeIntent.putExtra("nick", nick);
                        homeIntent.putExtra("modo","añadir");

                        homeIntent.putExtra("idLista",idLista);
                        homeIntent.putExtra("nombreLista",nombreLista);
                        startActivity(homeIntent);
                    }
                    else if(id==R.id.opciones_mostrar_miembros){ //FALTA MODIFICAR EL ADAPTER PARA QUE TE MUESTRE
                        Intent miembros = new Intent(ListaProductos.this, MostrarMiebrosLista.class); //debería ir la clase del Perfil
                        miembros.putExtra("email", email);
                        miembros.putExtra("nick", nick);
                        //miembros.putExtra("modo","añadir");

                        miembros.putExtra("idLista",idLista);
                        miembros.putExtra("nombreLista",nombreLista);
                        startActivity(miembros);
                    }
                }

                return false;
            }
        });

        String idLista = getIntent().getStringExtra("idLista");

        obtenerProductosLista(idLista);


        FloatingActionButton fabAñadirProductos = findViewById(R.id.fabAniadir_Productos);


        fabAñadirProductos.setOnClickListener(new View.OnClickListener() {

            /**
             * Método que lanza una nueva pantalla. Te lleva a la pantalla con todos los productos.
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaProductos.this,
                        TodosProductos.class);
                intent.putExtra("idLista", idLista);
                intent.putExtra("nombreLista", nombreLista);
                //intent.putExtra("email", email);
                startActivity(intent);
                //Intent replyIntent = new Intent();
                //Lista l = (Lista) intent.getSerializableExtra("lista");
                //hacer insert en el usuario internamente
            }
        });
    }

    public void removeProds() {
        for (Producto p : productos) {
            if (p.getCheckbox()) {
                //ELIMINAMOS DE LA LISTA
                mDatabase.child("listas").child(idLista).child("productos").child(p.getIdProducto()).removeValue();
            }
        }

        this.recreate();
        // onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_prod, menu);
        return true;
    }



    /**
     * Método que saca de la base de datos los productos de una lista con id "listaid
     *
     * @param listaid Representa el id de la lista de la que queremos
     */
    public void obtenerProductosLista(String listaid) {

        //NECESARIO PARA BORRAR EL ÚLTIMO
        mDatabase.child("listas").child(listaid).child("productos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().getValue()==null){
                        productos.clear();
                        productosAdapter = new ProductListAdapter(ListaProductos.this, R.layout.item_productos_lista, productos, listaid);
                        recyclerViewproductos.setAdapter(productosAdapter);
                    }
                }
            }
        });

        mDatabase.child("listas").child(listaid).child("productos").addValueEventListener(new ValueEventListener() {
            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
             * @param snapshot Representa el nodo de la base de datos a actualizar.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String cantidad = ds.getValue().toString();
                        addProducto(id, cantidad);

                    }
                    productosAdapter = new ProductListAdapter(ListaProductos.this, R.layout.item_productos_lista, productos, listaid);
                    recyclerViewproductos.setAdapter(productosAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }
        );


    }

    /**
     * Metodo que añade un producto a la lista con id "idProducto"
     *
     * @param idProducto Representa el id del producto que quieres aadir a la lista
     */

    public void addProducto(String idProducto, String cantidad) {

        mDatabase.child("json").child("results").child(idProducto.split("_")[0]).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String categoria=snapshot.child("Categoría").getValue().toString();
                    Log.d("ADDDD",""+productos.size());
                    DataSnapshot ds=snapshot.child("Ítems").child(idProducto.split("_")[1]);
                    if(ds.exists()){

                        String nombre = ds.child("Producto").getValue().toString();
                        String image = ds.child("RutaImagen").getValue().toString();
                        String brand = ds.child("Marca").getValue().toString();
                        String gradoNutricion = "";
                        String tienda=ds.child("ÍtemsTiendas").child("0").child("Tienda").getValue().toString();
                        String precio=ds.child("ÍtemsTiendas").child("0").child("Precio").getValue().toString();
                        double pre=Double.parseDouble(precio)*0.00022;

                        precio=pre+"";
                        Log.d("ADDDD",""+productos.size());
                        Producto p = new Producto(idProducto, nombre, brand, image, categoria, tienda, gradoNutricion,cantidad,precio);                    productos.add(p);
                        //ESTO ES UNA MIERDA

                        //productos.add(p);
                        Log.d("ADDDD",""+productos.size());
                        productosAdapter.setProductos(productos);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        Log.d("GETPRODUCTO", "INI");

        mDatabase.child("json").child("results").child(idProducto.split("_")[0]).child("Ítems").child(idProducto.split("_")[1]).addValueEventListener(new ValueEventListener() {

            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
             * @param snapshot Representa el nodo de la base de datos a actualizar.
             *sasasdasdasdadsasd/
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    String nombre = snapshot.child("Producto").getValue().toString();
                    String categoria = snapshot.child("ingredients_text").getValue().toString();
                    String image = snapshot.child("RutaImagen").getValue().toString();
                    String brand = snapshot.child("Marca").getValue().toString();
                    String gradoNutricion = "";


                    Producto p = new Producto(idProducto, nombre, brand, image, categoria, "Eroski", gradoNutricion,"1");                    productos.add(p);
                    //ESTO ES UNA MIERDA
                    productosAdapter.setProductos(productos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/
    }

}