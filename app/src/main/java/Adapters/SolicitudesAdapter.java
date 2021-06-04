package Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.ReadAndWriteSnippets;
import ModeloDominio.Solicitud;

/**
 * Esta clase define el Adapter necesario para que nuestra capa de presentacion se comunique
 * con la Persistencia y de esta manera mostrar las solicitudes en ella.
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 31/05/2021
 */
public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

    //Representa la lista de solicitudes que tiene un determinado usuario
    private List<Solicitud> mSolicitudes;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity activity;
    //Representa el entero necesario para la instanciacion en forma de View del layout necesario
    private int resource;
    //Representa el nick del usuario
    private String nick;

    /**
     * Constructor principal de la clase
     * @param a representa el siguiente activity al que iremos, para redirigir
     * @param resource representa el entero necesario para la instacicacón en forma de View del layout necesario
     * @param l representa la lista de solicitudes que tiene un determinado usuario
     * @param nick representa el nick del usuario
     */
    public SolicitudesAdapter(Activity a,int resource,List<Solicitud> l,String nick){
        this.activity=a;
        this.resource=resource;
        this.mSolicitudes=l;
        this.nick=nick;

    }

    /**
     * Metodo que devuelve un objeto tipo SolicitudViewHolder en base a un XML que necesitaremos para representar los datos
     * @param parent Representa el padre en el cual se representara el objeto viewHolder creado
     * @param viewType
     */
    @Override
    public SolicitudesAdapter.SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new SolicitudesAdapter.SolicitudViewHolder(itemView);
    }

    /**
     * Metodo que en un objeto tipo SolicitudViewHolder representa los datos en las posiciones especificas
     * @param holder Representa el objeto tipo ViewHolder
     * @param position Representa la posicion
     */
    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {

        if(mSolicitudes!=null){
            Solicitud current = mSolicitudes.get(position);

            if(current.getTipoSolicitud().equals("amistad")){
                holder.remitente.setText(current.getRemitente());
                holder.tipoSolicitud.setText("Solicitud de amistad de:");
            }
            else{
                holder.tipoSolicitud.setText("Solicitud de participar en la lista "+current.getNombreLista()+" de:");
                holder.remitente.setText(current.getRemitente());
            }
        }
    }

    /**
     * Método que devuelve el número de elementos a representar
     * @return El número de elementos a representar
     */
    @Override
    public int getItemCount() {
        if(mSolicitudes!=null)
            return mSolicitudes.size();
        else
            return 0;

    }

    /**
     * Método que establece la lista de solicitudes a la lista pasada por parámetro
     * @param mSolicitudes representa una lista de solicitudes
     */
    public void setmSolicitudes(List<Solicitud> mSolicitudes) {
        this.mSolicitudes=new ArrayList<>();
        this.mSolicitudes = mSolicitudes;
    }

    /**
     * Esta clase define el ViewHolder necesario para utilizar en nuestro SolicitudAdapter, obteniendo los datos de un View
     * con las id que los representa en esta
     *
     * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
     * @version: 31/05/2021
     */
    public class SolicitudViewHolder extends  RecyclerView.ViewHolder{

        // Representa los textViews que hay en el correspondiente layout
        private final TextView tipoSolicitud, remitente;
        //Representa los botones que hay en el correspondiente layout
        private final ImageButton aceptar, declinar;

        /**
         * Constructor de la clase
         * @param itemView
         */
        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoSolicitud=itemView.findViewById(R.id.tipo_solicitud);
            remitente=itemView.findViewById(R.id.remitente);
            aceptar=itemView.findViewById(R.id.aceptar_sol);
            declinar=itemView.findViewById(R.id.declinar_sol);

            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();


            aceptar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Método que representa el clickado en un item de la view
                 * @param v View en el cual se ha clickado
                 */
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    Solicitud current=mSolicitudes.get(position);

                    if(current.getTipoSolicitud().equals("lista")){
                        ReadAndWriteSnippets.aniadirLista(nick,current.getIdLista(),current.getNombreLista());
                        ReadAndWriteSnippets.eliminarSolicitudLista(nick,current.getRemitente(),current.getIdLista());

                    }
                    else{
                        ReadAndWriteSnippets.aniadirAmigo(nick,current.getRemitente());
                        ReadAndWriteSnippets.aniadirAmigo(current.getRemitente(),nick);
                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());

                    }
                    activity.recreate();
                    Toast.makeText(activity.getBaseContext(),"Solicitud Aceptada",Toast.LENGTH_LONG).show();

                }
            });

            declinar.setOnClickListener(new View.OnClickListener() {
                /**
                 * Método que representa el clickado en un item de la view
                 * @param v View en el cual se ha clickado
                 */
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    Solicitud current=mSolicitudes.get(position);
                    if(current.getTipoSolicitud().equals("lista")){
                        ReadAndWriteSnippets.eliminarSolicitudLista(nick,current.getRemitente(),current.getIdLista());

                    }
                    else{
                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());
                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());

                    }
                    activity.recreate();
                    Toast.makeText(activity.getBaseContext(),"Solicitud Rechazada",Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}
