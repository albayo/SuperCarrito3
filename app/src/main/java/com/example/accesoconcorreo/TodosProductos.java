package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

public class TodosProductos extends AppCompatActivity {
    private String idLista;
    private List<Producto> productos;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerViewproductos;
    private TodosProductosAdapter todosProductosAdapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todos_productos);
        String nombreLista=getIntent().getStringExtra("nombreLista");
        idLista=getIntent().getStringExtra("id");
        recyclerViewproductos=(RecyclerView)findViewById(R.id.super_prod_list_recycler);
        recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
        toolbar=findViewById(R.id.toolbar_lista_super_prod);
        toolbar.setTitle(nombreLista);
        mDatabase= FirebaseDatabase.getInstance().getReference();
        productos=new ArrayList<>();
        obtenerTodosProductos();
    }
    public void obtenerTodosProductos() {

        mDatabase.child("json").child("results").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getKey();
                        Log.d("Nombre",nombre);
                        if(productos.size()<5){
                            Log.d("AÑADIR","ENTRA");
                            addProducto(nombre);
                        }

                    }
                    todosProductosAdapter= new TodosProductosAdapter(TodosProductos.this,R.layout.item_productos_comprar,productos,idLista);
                    recyclerViewproductos.setAdapter(todosProductosAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }
        );

    }

    public void addProducto(String idProducto){
        Log.d("GETPRODUCTO","INI");

        mDatabase.child("json").child("results").child(idProducto).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (ds.exists()) {

                    String nombre = ds.child("product_name").getValue().toString();
                    String ingredients = ds.child("ingredients_text").getValue().toString();
                    String imgage = ds.child("image_url").getValue().toString();
                    String brand = ds.child("brand_owner").getValue().toString();
                    String gradoNutricion= ds.child("nutriscore_grade").getValue().toString();
                    Producto p = new Producto(idProducto, nombre, brand, "", ingredients, "Eroski",gradoNutricion);
                    productos.add(p);
                    Log.d("ADDPRODUCTO", "Nombre "+p.getNombre());

                    Log.d("ADDPRODUCTO", "Num "+productos.size());
                    Log.d("ADDPRODUCTO", "Num "+p.getImage());
                    //ESTO NO DEBERÍA IR AQUÍ PERO SINO EL ADAPTER NO SE INICIA
                    todosProductosAdapter.setProductos(productos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("GETPRODUCTO","FIN");

    }
}