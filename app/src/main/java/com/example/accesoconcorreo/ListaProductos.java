package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ProductListAdapter;
import ModeloDominio.Lista;
import ModeloDominio.Producto;

/**
 * Esta clase define la actividad (llamada "activity_lista_producto") que dispondrá en pantalla los
 * productos que componen una lista determinada
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        productos = new ArrayList<>();
        recyclerViewproductos = (RecyclerView) findViewById(R.id.lista_prod_recycler);
        recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
        //habría que cambiar el nombre del toolbar de la aplicación, el que aparece todo el rato
        //ir cambiandolo cada pantalla
        String nombreLista = getIntent().getStringExtra("nombreLista");

        myToolbar.setTitle(nombreLista); // establece el titulo del appBar al nombre de la lista
        myToolbar.inflateMenu(R.menu.menu_lista_prod);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.carrito_productos) {
                    removeProds();

                    return true;
                } else {
                    if (id == R.id.opciones_productos) {

                        return true;
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
        // onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_prod, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.carrito_productos) {
            removeProds();

            return true;
        }
        if (id == R.id.opciones_productos) {

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que saca de la base de datos los productos de una lista con id "listaid
     *
     * @param listaid Representa el id de la lista de la que queremos
     */
    public void obtenerProductosLista(String listaid) {
        productos.clear();
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
        Log.d("GETPRODUCTO", "INI");

        mDatabase.child("json").child("results").child(idProducto).addValueEventListener(new ValueEventListener() {

            /**
             * Listener que actualiza los datos en la aplicación cuando se realiza un cambio en la base de datos.
             * @param ds Representa el nodo de la base de datos a actualizar.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.exists()) {

                    String nombre = ds.child("product_name").getValue().toString();
                    String ingredients = ds.child("ingredients_text").getValue().toString();
                    String imgage = ds.child("image_small_url").getValue().toString();
                    String brand = ds.child("brand_owner").getValue().toString();
                    String gradoNutricion = ds.child("nutriscore_grade").getValue().toString();
                    Producto p = new Producto(idProducto, nombre, brand, imgage, ingredients, "", gradoNutricion, cantidad);
                    productos.add(p);
                    //ESTO ES UNA MIERDA
                    productosAdapter.setProductos(productos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}