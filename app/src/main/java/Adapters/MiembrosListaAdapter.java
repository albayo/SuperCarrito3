package Adapters;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.example.accesoconcorreo.MostrarMiebrosLista;
import com.example.accesoconcorreo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ModeloDominio.ReadAndWriteSnippets;
/**
 * Esta clase define el adapter necesario para que la capa de Presentación y Persistencia se comuniquen
 *  y se representen de forma visual los datos(las listas de Miembros) de esta última
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 31/05/2021
 */
public class MiembrosListaAdapter extends RecyclerView.Adapter<MiembrosListaAdapter.MiembrosListaViewHolder> {
    //Representa las listas que se representarán
    private List<String> mMiembros;  // Cached copy of Listas
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity activity;
    //Representa el nick del usuario
    private String nick;
    //Representa el id de la lista
    private final String idLista;
    //Representa el nombre de la lista
    private final String nombreLista;
    //Representa si el usuario actual es el propietario de la lista
    private boolean propietario;

    /**
     * Constructor principal de la clase
     * @param a representa el siguiente activity al que iremos
     * @param resource representa el objeto necesario para la instanciacion en forma de View del layout necesario
     * @param l representa la lista de los que se representarán
     * @param nick representa el nick del usuario actual
     * @param idLista representa el id de la lista
     * @param nombreLista representa el nombre de la lista
     * @param prop representa si el usuario actual es el propietario de la lista
     */
    public MiembrosListaAdapter(Activity a, int resource, List<String> l, String nick, String idLista, String nombreLista,boolean prop) {
        Log.d("Adapter", "constr");
        this.resource = resource;
        this.activity = a;
        this.mMiembros = l;
        this.nick = nick;
        this.idLista = idLista;
        this.nombreLista = nombreLista;
        this.propietario=prop;
    }

    @Override
    public MiembrosListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adapter", "onCreate");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new MiembrosListaAdapter.MiembrosListaViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MiembrosListaAdapter.MiembrosListaViewHolder holder, int position) {
        if (mMiembros != null || mMiembros.get(position) != null) {
            Log.d("Adapter", "onBind");
            String current = mMiembros.get(position);

            holder.AmigoNombreView.setText(current);
            if(current.equals(nick)){
                holder.AmigoNombreView.setText(current +" (Tu)");
                holder.btELiminarAmigo.setVisibility(View.INVISIBLE);
            }

        } else {
            // Covers the case of data not being ready yet.
            holder.AmigoNombreView.setText("No hay listas");
        }
    }
    public void setPropietario(boolean b){
        propietario=b;
    }
    public void setMiembros(List<String> miembros) {
        mMiembros = miembros;

    }

    @Override
    public int getItemCount() {
        return mMiembros.size();
    }

    public class MiembrosListaViewHolder extends RecyclerView.ViewHolder {
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde sale el nombre de la lista
        private final TextView AmigoNombreView;


        //Representa una copia del adapter
        final MiembrosListaAdapter adapter;
        private final ImageButton btELiminarAmigo;


        /**
         * Constructor de la clase
         *
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter  representa el adaptador que maneja los datos y views del RecyclerView
         */

        private MiembrosListaViewHolder(View itemView, MiembrosListaAdapter adapter) {
            super(itemView);
            this.view = itemView;
            this.AmigoNombreView = (TextView) itemView.findViewById(R.id.text_nombre_miembro);
            this.adapter = adapter;
            this.btELiminarAmigo = (ImageButton) itemView.findViewById(R.id.btEliminarMiembro);
            if(!propietario ){
                btELiminarAmigo.setVisibility(View.INVISIBLE);
            }
            else{


            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            btELiminarAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLick","cliiick");
                    androidx.appcompat.app.AlertDialog.Builder b = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    b.setTitle("Confirmación");
                    b.setMessage("¿Está seguro/a de que desea eliminar el miembro de la lista?");

                    b.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String amigo = AmigoNombreView.getText().toString();
                            if(!amigo.equals(nick)){

                            mDatabase.child("listas").child(idLista).child("miembros").child(amigo).removeValue();
                            mDatabase.child("users").child(amigo).child("listas").child(idLista).removeValue();

                            mDatabase.child("listas").child(idLista).child("miembros").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                            else{
                                Toast.makeText(activity,"No puedes eliminarte a ti mismo",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    AlertDialog alert=b.create();
                    alert.show();
                }
            });
            }
        }
    }
}
