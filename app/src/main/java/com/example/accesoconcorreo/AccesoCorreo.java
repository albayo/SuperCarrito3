package com.example.accesoconcorreo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AccesoCorreo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceder_con_correo);
        Bundle b= getIntent().getExtras();
        String email=b.getString("email");
        String provider=b.getString("provider");
        setup(email,provider);

    }
    private void setup(String email,String provider){
        EditText emailTV= findViewById(R.id.editText_email);
        emailTV.setText(email);
        EditText providerTV=findViewById(R.id.editText_contrasenia);
        providerTV.setText(provider);

        Button b=findViewById(R.id.btnAcceder);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                onBackPressed();
            }
        });


    }
}