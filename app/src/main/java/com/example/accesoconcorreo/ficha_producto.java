package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import Adapters.ProductListAdapter;
import ModeloDominio.Producto;

/**
 * Esta clase define la actividad (llamada "activity_ficha_producto") que servirá para el disponer la información de un producto
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
public class ficha_producto extends AppCompatActivity {
    //Representa el producto del cual se dispondrá por pantalla la información
    private Producto producto;
    private ImageView fotoprod;
    private ImageView fotonutri;
    private TextView nutricional;
    private TextView nombre;
    private TextView brand;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_pantalla_listas"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_producto);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.fichaProdToolbar);
        myToolbar.setTitle("SuperCarrito");

        producto=(Producto) getIntent().getSerializableExtra("producto");
        if(producto==null){
            Log.d("marcos patan","jejejje");
        }


        fotoprod= findViewById(R.id.imagen_ficha_producto);
        fotonutri= findViewById(R.id.image_grado_nutrition);
        nutricional= findViewById(R.id.textview_nutrientes);
        nombre= findViewById(R.id.text_nombre_producto);
        brand=findViewById(R.id.text_brand_prod);

        String n=producto.getNombre();
        if(n.contains(","))
            nombre.setText(n.split(",")[1]);
        else
            nombre.setText(n);

        Uri uri=Uri.parse(producto.getImage());
        if(!producto.getImage().equals(""))
            fotoprod.setImageURI(uri);
        else
            fotoprod.setImageResource(R.mipmap.pordefecto);
        
        String nutri=producto.getGradoNutrition();
        switch(nutri){
            case "a":fotonutri.setImageResource(R.mipmap.nutriscore_a);
            case "b":fotonutri.setImageResource(R.mipmap.nutriscore_b);
            case "c":fotonutri.setImageResource(R.mipmap.nutriscore_c);
            case "d":fotonutri.setImageResource(R.mipmap.nutriscore_d);
            case "e":fotonutri.setImageResource(R.mipmap.nutriscore_e);
        }

        nutricional.setText(producto.getIngred());

        brand.setText(producto.getBrand());




    }
}