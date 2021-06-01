package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ModeloDominio.Producto;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Esta clase define la actividad (llamada "activity_ficha_producto") que servirá para el disponer la información de un producto
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 31/05/2021
 */
public class ficha_producto extends AppCompatActivity {
    //Representa el producto del cual se dispondrá por pantalla la información
    private Producto producto;
    //Representa la foto que tiene el producto que se dispondrá en pantalla
    private ImageView fotoprod;
    //Representa la foto de grado de nutrición que tiene el producto que se dispondrá en pantalla
    private ImageView fotonutri;
    //Representa el nivel nutricional que tiene el producto que se dispondrá en pantalla
    private TextView nutricional;
    //Representa el nombre del producto que se dispondrá por pantalla
    private TextView nombre;
    //Representa la marca del producto que se dispondrá por pantalla
    private TextView brand;

    //Representa el precio del producto que se dispondrá por pantalla
    private TextView precio;

    //Representa el super del producto que se dispondrá por pantalla
    private TextView superm;
    //Representa la categoría del producto que se dispondrá por pantalla
    private TextView categoria;
    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_ficha_producto"
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
        /*if(producto==null){
            Log.d("marcos patan","jejejje");
        }*/

        fotoprod= findViewById(R.id.imagen_ficha_producto);
        fotonutri= findViewById(R.id.image_grado_nutrition);

        nombre= findViewById(R.id.text_nombre_producto);
        brand=findViewById(R.id.text_brand_prod);
        precio=findViewById(R.id.tv_nombre_precio);
        superm=findViewById(R.id.tv_nombre_super);
        categoria=findViewById(R.id.tv_nombre_categoria);
        if(producto.getCategoria()!=null){
            categoria.setText(producto.getCategoria());
        }
        if(producto.getSupermercado()!=null){
            superm.setText(producto.getSupermercado());
        }
        if(producto.getPrecio()!=null){
            precio.setText(producto.getPrecio()+"€");
        }
        String n=producto.getNombre();
        if(n.contains(","))
            nombre.setText(n.split(",")[1]);
        else
            nombre.setText(n);

        Uri uri=Uri.parse(producto.getImage());
        if(!producto.getImage().contains("imagen-no-disponible"))
            //fotoprod.setImageURI(uri);
            Glide.with(ficha_producto.this).load(Uri.parse(producto.getImage())).fitCenter().centerCrop().override(400,400).transition(withCrossFade()).into(fotoprod);

        else
            fotoprod.setImageResource(R.mipmap.pordefecto);


       /* String nutri=producto.getGradoNutrition();
        switch(nutri){
            case "a":fotonutri.setImageResource(R.mipmap.nutriscore_a);
            case "b":fotonutri.setImageResource(R.mipmap.nutriscore_b);
            case "c":fotonutri.setImageResource(R.mipmap.nutriscore_c);
            case "d":fotonutri.setImageResource(R.mipmap.nutriscore_d);
            case "e":fotonutri.setImageResource(R.mipmap.nutriscore_e);
        }*/



        brand.setText(producto.getBrand());
    }
}