package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    //Representa el botón que permite añadir este producto a la lista de la compra
    private Button aniadirProducto;
    //Representa el identificador de la Lista a la que es posible que se añade el producto
    private String idLista;

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

        producto = (Producto) getIntent().getSerializableExtra("producto");
        idLista = getIntent().getStringExtra("idLista");

        fotoprod = findViewById(R.id.imagen_ficha_producto);
        fotonutri = findViewById(R.id.image_grado_nutrition);

        nombre = findViewById(R.id.text_nombre_producto);
        brand = findViewById(R.id.text_brand_prod);
        precio = findViewById(R.id.tv_nombre_precio);
        superm = findViewById(R.id.tv_nombre_super);
        categoria = findViewById(R.id.tv_nombre_categoria);
        if (producto.getCategoria() != null) {
            categoria.setText(producto.getCategoria());
        }
        if (producto.getSupermercado() != null) {
            superm.setText(producto.getSupermercado());
        }
        if (producto.getPrecio() != null) {
            precio.setText(producto.getPrecio() + "€");
        }
        String n = producto.getNombre();
        if (n.contains(","))
            nombre.setText(n.split(",")[1]);
        else
            nombre.setText(n);

        Uri uri = Uri.parse(producto.getImage());
        if (!producto.getImage().contains("imagen-no-disponible"))
            //fotoprod.setImageURI(uri);
            Glide.with(ficha_producto.this).load(Uri.parse(producto.getImage())).fitCenter().centerCrop().override(400, 400).transition(withCrossFade()).into(fotoprod);

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

        aniadirProducto = findViewById(R.id.btAniadir);
        if (idLista != null) {
            aniadirProducto.setOnClickListener(new View.OnClickListener() {
                /**
                 * Método que representa el clickado en un item de la view
                 *
                 * @param v View en el cual se ha clickado
                 */
                @Override
                public void onClick(View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("listas").child(idLista).child("productos").child(producto.getIdProducto()).get().addOnCompleteListener(
                            new OnCompleteListener<DataSnapshot>() {
                                /**
                                 * Método que tiene lugar cuando una tarea se ha acabado (tanto de forma satisfactoria como de forma errónea)
                                 *
                                 * @param task tarea que se ha completado
                                 */
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().getValue() == null) {
                                            mDatabase.child("listas").child(idLista).child("productos").child(producto.getIdProducto()).setValue(1);
                                        } else {
                                            int i = Integer.parseInt(task.getResult().getValue().toString()) + 1;
                                            mDatabase.child("listas").child(idLista).child("productos").child(producto.getIdProducto()).setValue(i);
                                        }

                                        Toast t = Toast.makeText(v.getContext(), "Se ha añadido el producto", Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                            });
                    onBackPressed();
                }
            });
        }else{
            aniadirProducto.setVisibility(View.GONE);
        }

            brand.setText(producto.getBrand());
        }
    }