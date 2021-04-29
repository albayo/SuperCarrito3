package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import Adapters.ProductListAdapter;
import ModeloDominio.Producto;

public class ficha_producto extends AppCompatActivity {

    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //esta línea sirve para impedir que se pueda girar la pantalla
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_producto);
        producto=getIntent();
        View view=


    }