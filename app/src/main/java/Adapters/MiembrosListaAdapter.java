package Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.MostrarMiebrosLista;
import com.example.accesoconcorreo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModeloDominio.ReadAndWriteSnippets;

public class MiembrosListaAdapter extends RecyclerView.Adapter<MiembrosListaAdapter.MiembrosListaViewHolder>{
    //Representa las listas que se representarán
    private List<String> mMiembros;  // Cached copy of Listas
    //Represente los ids de las listas , se cambiará por un Map.
    private List<String> mCorreos; //
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity activity;
    private String nick;
    private final String idLista;
    private final String nombreLista;

    public MiembrosListaAdapter(Activity a, int resource, List<String> l, List<String> lid, String nick, String idLista, String nombreLista) {
        this.resource=resource;
        this.activity = a;
        this.mMiembros = l;
        this.mCorreos=lid;
        this.nick=nick;
        this.idLista=idLista;
        this.nombreLista=nombreLista;
    }
    
    @Override
    public MiembrosListaAdapter.MiembrosListaViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new MiembrosListaAdapter.MiembrosListaViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull  MiembrosListaAdapter.MiembrosListaViewHolder holder, int position) {
        if  ( mMiembros  !=  null || mMiembros.get(position)!=null) {
            String current =  mMiembros.get(position);
            holder.AmigoNombreView.setText(current);

        }  else  {
            // Covers the case of data not being ready yet.
            holder. AmigoNombreView .setText( "No hay listas" );
        }
    }
    public void  setListas(List<String> Listas){
        mMiembros  = Listas;

    }
    @Override
    public int getItemCount() {
        return 0;
    }
    public class  MiembrosListaViewHolder  extends  RecyclerView.ViewHolder {
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde sale el nombre de la lista
        private final TextView AmigoNombreView;


        //Representa una copia del adapter
        final MiembrosListaAdapter adapter;
        private final ImageButton btELiminarAmigo;
        private final ImageButton btSimboloAmigo;

        /**
         * Constructor de la clase
         *
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter  representa el adaptador que maneja los datos y views del RecyclerView
         */

        private MiembrosListaViewHolder(View itemView, MiembrosListaAdapter adapter) {
            super(itemView);
            this.view = itemView;
            this.AmigoNombreView = (TextView) itemView.findViewById(R.id.text_lista_usuario);
            this.btELiminarAmigo = (ImageButton) itemView.findViewById(R.id.btEliminarAmigo);
            this.adapter = adapter;
            this.btSimboloAmigo = (ImageButton) itemView.findViewById(R.id.RecyclerImagenAmigo);
            //itemView.setOnClickListener(this);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


            btELiminarAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    b.setTitle("Confirmación");
                    b.setMessage("¿Está seguro/a de que desea eliminar el amigo?");

                    b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String amigo = AmigoNombreView.getText().toString();

                            mDatabase.child("users").child(nick).child("amigos").child(amigo).removeValue();
                            mDatabase.child("users").child(amigo).child("amigos").child(nick).removeValue();

                            mDatabase.child("users").child(nick).child("amigos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().getValue() == null) {

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
                    AlertDialog alert = b.create();
                    alert.show();
                }
            });
        }

    }
}
