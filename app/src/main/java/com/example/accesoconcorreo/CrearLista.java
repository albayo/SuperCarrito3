package com.example.accesoconcorreo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Usuario;

public class CrearLista extends AppCompatActivity {
    //Representa el CardView que implica la creación de una lista compartida
    private CardView cardViewgrupo;

    //Representa el CardView que implica la creación de una lista personal
    private CardView cardViewsola;

    /**
     * Método que sirve para inicializar y cargar todos los elementos visuales de la actividad
     * "activity_pantalla_listas"
     *
     * @param savedInstanceState Representa el objeto donde se guarda la información
     */
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
                abrirFragment("grupal");
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
                abrirFragment("sola");
            }
        });

    }

    /**
     *
     * @param tipoLista Representa
     */
    public void abrirFragment(String tipoLista){
        ListaDialogFragment listaDialogFragment = new ListaDialogFragment(tipoLista);
        listaDialogFragment.show(getSupportFragmentManager(),"tag");

        String ultB = listaDialogFragment.getUltBoton();
        if(ultB.length() > 0){
            if(ultB.equals("Aceptar")){
                Intent intent = new Intent(this, ListaProductos.class);
                startActivity(intent);
            }
        }
    }
    public static class ListaDialogFragment extends DialogFragment
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

        //Representa el nombre del último botón en el cual se ha hecho click
        private String ultBoton="";

        /**
         * Constructor base
         * @param tipoLista
         */
        public ListaDialogFragment(String tipoLista){
            this.tipoLista = tipoLista;
        }

        /**
         * Devuelve el nombre del último botón que se a clickado o la cadena vacía en caso de que no se haya clickado ninguno
         * @return ultBoton
         */
        public String getUltBoton(){
            return this.ultBoton;
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
            persistencia= new ReadAndWriteSnippets();
            btnAceptar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Método que sirve para comprobar que lo introducido en los campos de usuario y
                 *  contraseña corresponden a un usuario existente
                 * @param v Representa al objeto View sobre el cual se ha hecho click
                 */
                @Override
                public void onClick(View v) {
                    ultBoton = "Aceptar";
                    String nombreLista = etNombre.getText().toString();
                    if(nombreLista != null && nombreLista.trim().length() > 0) {
                        if (tipoLista.equals("grupal")) {
                            //crear la lista GRUPAL con nombre "nombre"
                        } else {
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser(); //no sería mejor ir pasando el Usuario en los intents??
                            Log.d("CrearLista valor user", String.valueOf(user));
                            List<Usuario> lista=new ArrayList<>();
                            Usuario u=persistencia.convertirAUsuario(user);
                            lista.add(u);
                            persistencia.insertarLista("1",nombreLista,u.getEmail());
                        }
                    }else{
                        Toast.makeText(getActivity(),"Error, se debe introducir un nombre para la lista",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnCancelar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Método que sirve para comprobar que lo introducido en los campos de usuario y
                 *  contraseña corresponden a un usuario existente
                 * @param v Representa al objeto View sobre el cual se ha hecho click
                 */
                @Override
                public void onClick(View v) {
                    ultBoton = "Cerrar";
                    cerrarFragment();
                }
            });
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
         * Método que sirve para cerrar el Fragment
         */
        private void cerrarFragment() {
            getActivity().onBackPressed();
            //getFragmentManager().beginTransaction().remove(this).commit();
            /*Intent intent = new Intent(CrearLista.class, ListaProductos.class);
            startActivity(intent);*/
            //getFragmentManager().beginTransaction().remove(this).commit(); //no funciona, mirar con logger
        }
    }


}