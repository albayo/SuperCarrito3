package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class CrearLista extends AppCompatActivity {
    private CardView cardViewgrupo;
    private CardView cardViewsola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_lista);
        cardViewsola = (CardView)findViewById(R.id.lista_personal_card);
        cardViewgrupo = (CardView)findViewById(R.id.grupo_compra_card);
        //superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
        registerForContextMenu(cardViewsola);
        registerForContextMenu(cardViewgrupo);
        //usuario=firebaseAuth.getCurrentUser();
        cardViewgrupo.setOnClickListener(new View.OnClickListener(){

            /**
             * Método que sirve sacar un menú contextual para indicar el nombre de la lista que se
             *  va a crear
             * @param v Representa al objeto View sobre el cual se ha hecho click
             */
            @Override
            public void onClick(View v) {
                ListaDialogFragment listaDialogFragment = new ListaDialogFragment("grupal");
                listaDialogFragment.show(getSupportFragmentManager(),"tag");
            }
        });

        /**
         * Método que sirve sacar un menú contextual para indicar el nombre de la lista que se
         *  va a crear
         * @param v Representa al objeto View sobre el cual se ha hecho click
         */
        cardViewsola.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ListaDialogFragment listaDialogFragment = new ListaDialogFragment("sola");
                listaDialogFragment.show(getSupportFragmentManager(),"tag");
            }
        });
    }
    public static class ListaDialogFragment extends DialogFragment implements View.OnClickListener
    {
        //Representa el EditText en el cual habrá que introducir el nombre que se le querra dar a la lista
        private EditText etNombre;

        //Representa el botón de Aceptar del Fragment
        private Button btnAceptar;

        //Representa el botón de Cancelar del Fragment
        private Button btnCancelar;

        //Representa una cadena que indica el tipo de lista que se tendrá que crear
        private String tipoLista;

        //Representa la clase de Lógica de Negocio la cuál será necesaria para sacar la información de la BD
       // private SuperViewModel superViewModel;
        private ReadAndWriteSnippets persistencia;
        /**
         * Constructor base
         * @param tipoLista
         */
        public ListaDialogFragment(String tipoLista){
            this.tipoLista = tipoLista;
        }

        /**
         * Método que inicializa las componentes del dialogfragment
         * @param view
         * @param savedInstanceState
         */
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //superViewModel = new ViewModelProvider(this).get(SuperViewModel.class);
            etNombre = (EditText) view.findViewById(R.id.etNombre);
            btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
            btnCancelar = (Button) view.findViewById(R.id.btnCancelar);
        }

        /**
         *
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_introducir_nom_lista, container, false);
        }

        /**
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if(v.equals(btnAceptar)){
                String nombre = etNombre.getText().toString();
                if(nombre != null && nombre.trim().length() > 0) {
                    if (tipoLista.equals("grupal")) {
                        //crear la lista GRUPAL con nombre "nombre"
                    } else {
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        List<Usuario> lista=new ArrayList<>();
                        Usuario u=new Usuario(user.getEmail());
                         lista.add(u);
                        persistencia.insertarLista("1",nombre,lista);

                        //HACER REMOVE EN EL GRAGMENT E IR A LISTA PRODUCTOS
                    }
                }else{
                    Toast.makeText(getActivity(),"Error, se debe introducir un nombre para la lista",Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


}