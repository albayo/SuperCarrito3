package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

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

/**
 * Esta clase define la actividad llamada "activity_productos_compra" que sirve para mostrar todos los productos de la base de datos.
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 30/04/2021
 */
public class TodosProductos extends AppCompatActivity {
    private String idLista;
    private List<Producto> productos;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewproductos;
    private TodosProductosAdapter todosProductosAdapter;
    private Toolbar toolbar;
    private Spinner spinnerSuper;
    private Spinner spinnerProd;

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
        toolbar = findViewById(R.id.toolbar_lista_super_prod);
        toolbar.setTitle(nombreLista);
        spinnerProd=findViewById(R.id.spinner_productos);
        spinnerSuper=findViewById(R.id.spinner_super);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        productos = new ArrayList<>();
        obtenerTodosProductos();
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
                        if(productos.size()>50){
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
                    todosProductosAdapter = new TodosProductosAdapter(TodosProductos.this, R.layout.item_productos_comprar, productos, idLista);
                    recyclerViewproductos.setAdapter(todosProductosAdapter);
                    recyclerViewproductos.setLayoutManager(new GridLayoutManager(TodosProductos.this,3));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
                                                                       }
        );

    }
}