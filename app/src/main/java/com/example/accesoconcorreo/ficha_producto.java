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

public class ficha_producto extends AppCompatActivity {

    private Producto producto;

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


        ImageView fotoprod= findViewById(R.id.imagen_ficha_producto);
        ImageView fotonutri= findViewById(R.id.image_grado_nutrition);
        Uri uri=Uri.parse(producto.getImage());

        TextView nutricional= findViewById(R.id.textview_nutrientes);
        TextView nombre= findViewById(R.id.text_nombre_producto);
        nombre.setText(producto.getNombre());
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


    }
}