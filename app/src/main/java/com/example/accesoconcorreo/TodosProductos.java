package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * @version: 03/06/2021
 */
public class TodosProductos extends AppCompatActivity{
    //Representa el identificar de la lista a la que se quiere añadir el producto
    private String idLista;
    //Representa todos los productos que hay en la BD
    private List<Producto> productos;
    //Representa todos los productos que hay en la BD para evitar acceder muchas veces
    private List<Producto> todosProds;
    //private EditText etbusqueda;
    //private ImageView ivBusqueda;
    //Representa el SearchView en el que se escribirá texto para poder realizar una búsqueda
    private SearchView svProductos;
    //Representa una referencia a la BD
    private DatabaseReference mDatabase;
    //Representa el recyclerView donde se dispondrán los datoss
    private RecyclerView recyclerViewproductos;
    //Representa el adapter que sirve para disponer los datos correspondientes
    private TodosProductosAdapter todosProductosAdapter;
    //Representa el toolbar de la actividad
    private Toolbar toolbar;
    //Representa todas las categorías que tienen los productos
    private List<String> categorias;
    //Representa el spinner el cual permitirá buscar productos por categorías
    private Spinner spinnerCategorias;
    //Representa la categoría que está seleccionada por el usuario
    private String categoriaSelec;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_productos_comprar"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categorias = new ArrayList<>();
        categorias.add("-Categorías-");
        categorias.add("Despensa");
        categorias.add("Bebidas");
        categorias.add("Frutas y Verduras");
        categorias.add("Carnes y Pescados");
        categorias.add("Panadería");
        categorias.add("Refrigerados");
        categorias.add("Dulces y Pasabocas");
        categorias.add("Cuidado Personal");
        categorias.add("Aseo del Hogar");
        categorias.add("Licores");
        categorias.add("Bebés");
        categorias.add("Mascotas");
        categorias.add("Varios");

        setContentView(R.layout.activity_todos_productos);
        String nombreLista = getIntent().getStringExtra("nombreLista");
        idLista = getIntent().getStringExtra("idLista");
        recyclerViewproductos = (RecyclerView) findViewById(R.id.super_prod_list_recycler);
        recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
        this.spinnerCategorias = findViewById(R.id.spinnerCategoria);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categorias);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(arrayAdapter);
        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Método que tiene lugar cuando alguno de los elementos del spinner es seleccionado
             * @param parent representa el View padre en el que se encuentra el spinner
             * @param view representa el View padre en el que se encuentra el spinner
             * @param position representa la posición del elemento clickado en el spinner
             * @param id representa el id del elemento clickado en el spinner
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSelec = parent.getItemAtPosition(position).toString();
                if(categoriaSelec.equals("-Categorías-")){
                    mostrarProductos(todosProds);
                }else{
                    productos = ReadAndWriteSnippets.buscarConCategoría(todosProds, "", categoriaSelec);
                    mostrarProductos(productos);
                }
            }

            /**
             * Método que tiene lugar cuando ninguno de los elementos ha sido seleccionado
             * @param parent representa el View padre en el que se encuentra el spinner
             */
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        svProductos = (android.widget.SearchView) findViewById(R.id.svProductos);
        svProductos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            /**
             * Método que tiene lugar cuando el texto contenido en el correspondiente SearchView se submitea
             * @param query representa el texto correspondiente del SearchView
             * @return false
             */
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

            /**
             * Método que tiene lugar cuando el texto contenido en el correspondiente SearchView cambia
             * @param newText representa el texto correspondiente del SearchView
             * @return true
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(TodosProductos.this, "Tamaño todosProds = " + todosProds.size(), Toast.LENGTH_LONG).show();
                if(newText.trim().length() == 0){
                    if(categoriaSelec.equals("-Categorías-")){
                        mostrarProductos(todosProds);
                    }else{
                        productos = ReadAndWriteSnippets.buscarConCategoría(todosProds, "", categoriaSelec);
                        mostrarProductos(productos);
                    }

                }else{
                    if(categoriaSelec.equals("-Categorías-")){
                        productos = ReadAndWriteSnippets.buscarProductos(todosProds, newText);
                        mostrarProductos(productos);
                    }else{
                        productos = ReadAndWriteSnippets.buscarConCategoría(todosProds, newText, categoriaSelec);
                        mostrarProductos(productos);
                    }
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
                        /*if(productos.size()>100){
                            break;
                        }*/
                        String id = ds.getKey();
                        String nombre = ds.child("Nombre").getValue().toString();
                        String categoria= ds.child("Categoría").getValue().toString();


                        if(ds.child("Ítems").child("0").exists()){
                            DataSnapshot ds1= ds.child("Ítems").child("0");
                        //for(DataSnapshot ds1 : ds.child("Ítems").getChildren()){
                            id+="_"+ds1.getKey();
                            String brand = ds1.child("Marca").getValue().toString();
                            String image = ds1.child("RutaImagen").getValue().toString();
                            String gradoNutricion="";
                            String tienda=ds1.child("ÍtemsTiendas").child("0").child("Tienda").getValue().toString();
                            String precio=ds1.child("ÍtemsTiendas").child("0").child("Precio").getValue().toString();
                            double pre=Double.parseDouble(precio)*0.00022;
                            double preR=Math.round(pre*100.0)/100.0;
                            precio=preR+"";

                            Producto p = new Producto(id, nombre, brand, image, categoria, tienda, gradoNutricion,"1",precio);
                            productos.add(p);
                        }



                       // String ingredients = ds.child("ingredients_text").getValue().toString();


                        //String gradoNutricion = ds.child("nutriscore_grade").getValue().toString();
                        //Producto p = new Producto(id, nombre, brand, "", ingredients, "Eroski", gradoNutricion,"1");
                        //productos.add(p);

                    }
                    mostrarProductos(productos);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    /**
     * Método que muestra los productos pasados por parámetro
     * @param productosMostrar representa los productos que se quieren mostrar
     */
    private void mostrarProductos(List<Producto> productosMostrar) {
        todosProductosAdapter = new TodosProductosAdapter(TodosProductos.this, R.layout.item_productos_comprar, productosMostrar, idLista);
        recyclerViewproductos.setAdapter(todosProductosAdapter);
        recyclerViewproductos.setLayoutManager(new GridLayoutManager(TodosProductos.this,3));
    }


}