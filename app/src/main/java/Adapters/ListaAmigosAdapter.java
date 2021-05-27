package Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.Home;
import com.example.accesoconcorreo.ListaAmigos;
import com.example.accesoconcorreo.ListaProductos;
import com.example.accesoconcorreo.Perfil;
import com.example.accesoconcorreo.Perfil_otros;
import com.example.accesoconcorreo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModeloDominio.ReadAndWriteSnippets;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ListaAmigosAdapter extends RecyclerView.Adapter<ListaAmigosAdapter.AmigosViewHolder>{
    //Representa las listas que se representarán
    private List<String> mAmigos;  // Cached copy of Listas
    //Represente los ids de las listas , se cambiará por un Map.
    private List<String> mCorreos; //
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity activity;
    private String modo;
    private String nick;
    private final String idLista;
    private final String nombreLista;



    public ListaAmigosAdapter(Activity a, int resource, List<String> l,List<String> lid,String nick,String modo,String idLista,String nombreLista) {

        this.resource=resource;
        this.activity = a;
        this.mAmigos = l;
        this.mCorreos=lid;
        this.modo=modo;
        this.nick=nick;
        this.idLista=idLista;
        this.nombreLista=nombreLista;
    }
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int  viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ListaAmigosAdapter.AmigosViewHolder(itemView,this);
    }
    @Override
    public void  onBindViewHolder(ListaAmigosAdapter.AmigosViewHolder holder, int  position) {

        if  ( mAmigos  !=  null || mAmigos.get(position)!=null) {
            String current =  mAmigos.get(position);
            holder.AmigoNombreView.setText(current);

        }  else  {
            // Covers the case of data not being ready yet.
            holder. AmigoNombreView .setText( "No hay listas" );
        }
    }

    /**
     * Método que establece como lista de Listas la lista pasada por parámetro
     * @param Listas Representa la lista de Listas que se quiere asignar
     */
    public void  setListas(List<String> Listas){
        mAmigos  = Listas;
    
    }
    // getItemCount﴾﴿ is called many times, and when it is first called,
    // mAmigos has not been updated ﴾means initially, it's null, and we can't return null.

    /**
     * Método que devuelve el número de elementos a representar
     * @return El número de elementos a representar
     */

    @Override
    public int  getItemCount() {
        if  ( mAmigos  !=  null )
            return  mAmigos .size();
        else return  0;
    }

    /**
     * Esta clase define el tipo el cual será usado para representar los datos(las listas de un Usuario)
     * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
     * @version: 02/05/2021
     */
    public class  AmigosViewHolder  extends  RecyclerView.ViewHolder{
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde sale el nombre de la lista
        private final TextView AmigoNombreView;


        //Representa una copia del adapter
        final ListaAmigosAdapter adapter;
        private final ImageButton btELiminarAmigo;
        private final ImageButton btSimboloAmigo;
        /**
         * Constructor de la clase
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter representa el adaptador que maneja los datos y views del RecyclerView
         */

        private  AmigosViewHolder(View itemView, ListaAmigosAdapter adapter) {
            super (itemView);
            this.view=itemView;
            this.AmigoNombreView= (TextView) itemView.findViewById(R.id.text_lista_usuario);
            this.btELiminarAmigo=(ImageButton)itemView.findViewById(R.id.btEliminarAmigo);
            this.adapter = adapter;
            this.btSimboloAmigo=(ImageButton) itemView.findViewById(R.id.RecyclerImagenAmigo);
            //itemView.setOnClickListener(this);
            DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
            if(modo.equals("añadir")){
                btSimboloAmigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ReadAndWriteSnippets.solicitudLista(nick,AmigoNombreView.getText().toString(),idLista,nombreLista);
                        Toast.makeText(view.getContext(),"Amigo Añadido a la lista "+nombreLista,Toast.LENGTH_LONG).show();
                    }
                });
            }
            btSimboloAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profintent = new Intent(view.getContext(), Perfil_otros.class);
                    profintent.putExtra("nick", AmigoNombreView.getText().toString());
                    profintent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(profintent);
                }
            });

            btELiminarAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    androidx.appcompat.app.AlertDialog.Builder b= new androidx.appcompat.app.AlertDialog.Builder(activity);
                    b.setTitle("Confirmación");
                    b.setMessage("¿Está seguro/a de que desea eliminar el amigo?");

                    b.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String amigo=AmigoNombreView.getText().toString();

                            mDatabase.child("users").child(nick).child("amigos").child(amigo).removeValue();
                            mDatabase.child("users").child(amigo).child("amigos").child(nick).removeValue();

                            mDatabase.child("users").child(nick).child("amigos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().getValue()==null){

                                            activity.recreate();
                                        }
                                    }
                                }
                            });
                        }
                    });

                    b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alert=b.create();
                    alert.show();
                }
            });
        }

        /**
         * Método que representa el clickado en un item de la view
         * @param v View en el cual se ha clickado
         */
        /*@Override
        public void onClick(View v) {
            //Se obtiene la posicion del item que ha sido clickado
            int mPosicion = getLayoutPosition();
            String lista = mAmigos.get(mPosicion);
            String id = mAmigosId.get(mPosicion);

            Intent intent = new Intent(a, ListaProductos.class);
            intent.putExtra("nombreLista", lista);
            intent.putExtra("idLista", id);

            a.startActivity(intent);
        }*/
    }
}
