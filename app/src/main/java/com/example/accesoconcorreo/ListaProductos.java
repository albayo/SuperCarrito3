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
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.ListaListAdapter;
import ModeloDominio.Producto;

/**
 * Esta clase define la actividad (llamada "activity_lista_producto") que dispondrá en pantalla los
 *  productos que componen una lista determinada
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */
public class ListaProductos extends AppCompatActivity {

        private DatabaseReference mDatabase;
         private ListaListAdapter mListaAdapter;
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

            recyclerViewproductos=(RecyclerView)findViewById(R.id.lista_prod_recycler);
            recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));
            toolbar=(Toolbar)findViewById(R.id.toolbar_list_prod);
            String nombreLista=getIntent().getStringExtra("nombreLista");
            toolbar.setTitle(nombreLista);

        }

    public void obtenerProductosLista(String listaid) {
        List<String> llistas = new ArrayList<>();
        mDatabase.child("listas").child(listaid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String nombre=ds.getValue().toString();
                        llistas.add(nombre);
                    }
                    mListaAdapter= new ListaListAdapter(R.layout.pantalla_listas_list,llistas);
                    recyclerViewproductos.setAdapter(mListaAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }
        );

    }


}