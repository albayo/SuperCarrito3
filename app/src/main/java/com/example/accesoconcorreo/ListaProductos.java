package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.List;

import ModeloDominio.Producto;

/**
 * Esta clase define la actividad (llamada "activity_lista_producto") que dispondrá en pantalla los
 *  productos que componen una lista determinada
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */
public class ListaProductos extends AppCompatActivity {


        //Representa la clase de Lógica de Negocio la cuál será necesaria para comprobar información con la BD
       // private SuperViewModel superViewModel;

        //Representa el RecyclerView en el cual se dispondrán los Productos de la Lista
        private RecyclerView recyclerViewproductos;

        //Representa el adapter que se necesita para poder disponer los datos de las Productos de la Lista en el RecyclerView
       // private ProductoListAdapter adapatador;

        /**
         * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
         *  "activity_lista_productos"
         * @param savedInstanceState Representa el objeto donde se guarda la información
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_productos);


            recyclerViewproductos=(RecyclerView)findViewById(R.id.lista_prod_recycler);
            recyclerViewproductos.setLayoutManager(new LinearLayoutManager(this));



        }

        /**
         * Método que saca los productos de la BD y los representa en una lista de Productos
         * @return La lista de Productos disponibles en la BD
         */
        /*public List<Producto> obtenerProductos(){
            LiveData<List<Producto>> lproductos=superViewModel.getProductos();
            return lproductos.getValue();
        }*/
    }
}