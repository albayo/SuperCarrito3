package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaListAdapter;
import Adapters.ProductListAdapter;
import ModeloDominio.Producto;
import ModeloDominio.ReadAndWriteSnippets;

/**
 * Esta clase define la actividad (llamada "activity_lista_producto") que dispondrá en pantalla los
 *  productos que componen una lista determinada
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */
public class ListaProductos extends AppCompatActivity {
        private List<Producto> productos;
        private DatabaseReference mDatabase;
        private ProductListAdapter productosAdapter;
        private Toolbar toolbar;        //Representa el RecyclerView en el cual se dispondrán los Productos de la Lista
        private RecyclerView recyclerViewproductos;

        /**
         * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
         *  "activity_lista_productos"
         * @param savedInstanceState Representa el objeto donde se guarda la información
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_productos2);
            mDatabase=FirebaseDatabase.getInstance().getReference();
            productos=new ArrayList<>();
            recyclerViewproductos=(RecyclerView)findViewById(R.id.lista_prod_recycler);
            recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
            toolbar=(Toolbar)findViewById(R.id.toolbar_list_prod);//este toolbar no debería estar
            //habría que cambiar el nombre del toolbar de la aplicación, el que aparece todo el rato
            //ir cambiandolo cada pantalla
            String nombreLista=getIntent().getStringExtra("nombreLista");
            toolbar.setTitle(nombreLista);
            String idLista=getIntent().getStringExtra("idLista");
            Log.d("IDLista",idLista);
            obtenerProductosLista(idLista);
        }

    public void obtenerProductosLista(String listaid) {

        mDatabase.child("listas").child(listaid).child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productos.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getValue().toString();
                        Producto p=null;

                        addProducto(nombre);
                        Log.d("ObtenerProduct",nombre);
                        Log.d("ObtenerProduct","Numero " +productos.size());
                    }
                    productosAdapter= new ProductListAdapter(ListaProductos.this,R.layout.item_productos_lista,productos);
                    recyclerViewproductos.setAdapter(productosAdapter);
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
                    Producto p = new Producto(String.valueOf(idProducto), nombre, brand, imgage, ingredients, "",gradoNutricion);
                    productos.add(p);
                    Log.d("ADDPRODUCTO", "Nombre "+p.getNombre());

                    Log.d("ADDPRODUCTO", "Num "+productos.size());
                    Log.d("ADDPRODUCTO", "Num "+p.getImage());
                    //ESTO NO DEBERÍA IR AQUÍ PERO SINO EL ADAPTER NO SE INICIA
                    productosAdapter.setProductos(productos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("GETPRODUCTO","FIN");

    }



}