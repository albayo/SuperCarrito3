package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.TodosProductosAdapter;
import ModeloDominio.Producto;
import ModeloDominio.ReadAndWriteSnippets;

/**
 * Esta clase define la actividad llamada "activity_todos_productos" que sirve para mostrar todos los productos de la base de datos.
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 20/05/2021
 */
public class TodosProductos extends AppCompatActivity{
    private String idLista;
    private List<Producto> productos;
    private List<Producto> todosProds;
    private EditText etbusqueda;
    private ImageView ivBusqueda;
    private SearchView svProductos;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewproductos;
    private TodosProductosAdapter todosProductosAdapter;
    private Toolbar toolbar;
    /*private Spinner spinnerSuper;
    private Spinner spinnerProd;//inutilizado de momento ya que la API no tiene categorías
    */

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_productos_comprar"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_productos);
        String nombreLista = getIntent().getStringExtra("nombreLista");
        idLista = getIntent().getStringExtra("idLista");
        recyclerViewproductos = (RecyclerView) findViewById(R.id.super_prod_list_recycler);
        recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
        /*etbusqueda = findViewById(R.id.etBusqueda);
        ivBusqueda = findViewById(R.id.ivBusqueda);*/
        svProductos = (android.widget.SearchView) findViewById(R.id.svProductos);
        svProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*if(query.trim().length() == 0){
                    mostrarProductos(todosProds);
                }else{
                    productos = ReadAndWriteSnippets.buscarProductos(todosProds, query);
                    mostrarProductos(productos);
                }
                return true;*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().length() == 0){
                    mostrarProductos(todosProds);
                }else{
                    productos = ReadAndWriteSnippets.buscarProductos(todosProds, newText);
                    mostrarProductos(productos);
                }
                return true;
            }
        });
        toolbar = findViewById(R.id.toolbar_lista_super_prod);
        toolbar.setTitle(nombreLista);
        toolbar.inflateMenu(R.menu.menu_todos_prod);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            /**
             * Método que sirve para comprobar si se ha hecho click en alguno de los elementos del menu
             * @param item Representa al elemento del menu sobre el cual se ha hecho click
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.opciones_productosSuper) {

                    return true;
                }
                return false;
            }
        });

        /*spinnerProd=findViewById(R.id.spinner_productos);
        spinnerSuper=findViewById(R.id.spinner_super);*/

        //ivBusqueda.setOnClickListener(new View.OnClickListener() {
            /**
             * Método que sirve para buscar el articulo cuyo nombre ha sido introducido en el campo de búsqueda
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
           /* @Override
            public void onClick(View v) {
                if(etbusqueda.getText().toString().trim().length() != 0){
                    productos = ReadAndWriteSnippets.buscarProductos(todosProds, etbusqueda.getText().toString());
                    mostrarProductos(productos);
                }else{
                    //Toast.makeText(TodosProductos.this, "Para buscar un articulo debe introducir su nombre", Toast.LENGTH_LONG).show();
                    mostrarProductos(todosProds);
                }
            }
        });*/

        mDatabase = FirebaseDatabase.getInstance().getReference();
        productos = new ArrayList<>();
        obtenerTodosProductos();
        todosProds = productos;
    }

    /**
     * Método que sirve para sacar todos los productos de la base de datos para mostrarlos al usuario
     * y darle la opción de añadirlos a su lista.
     */
    public void obtenerTodosProductos() {
        productos.clear();
        mDatabase.child("json").child("results").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Iterable<DataSnapshot> dataSnapshots = snapshot.getChildren();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.d("SIZEE",""+productos.size());
                        if(productos.size()>100){
                            break;
                        }
                        String id = ds.getKey();
                        String nombre = ds.child("product_name").getValue().toString();
                        String ingredients = ds.child("ingredients_text").getValue().toString();
                        String imgage = ds.child("image_url").getValue().toString();
                        String brand = ds.child("brand_owner").getValue().toString();
                        String gradoNutricion = ds.child("nutriscore_grade").getValue().toString();
                        Producto p = new Producto(id, nombre, brand, "", ingredients, "Eroski", gradoNutricion,"1");
                        productos.add(p);

                    }
                    mostrarProductos(productos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void mostrarProductos(List<Producto> productosMostrar) {
        todosProductosAdapter = new TodosProductosAdapter(TodosProductos.this, R.layout.item_productos_comprar, productosMostrar, idLista);
        recyclerViewproductos.setAdapter(todosProductosAdapter);
        recyclerViewproductos.setLayoutManager(new GridLayoutManager(TodosProductos.this,3));
    }


}