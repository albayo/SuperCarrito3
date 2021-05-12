package Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.R;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Solicitud;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

    //No puede ser de lista de Strings
    private List<Solicitud> mSolicitudes;
    private Activity activity;
    private int resource;
    private String nick;

    public SolicitudesAdapter(Activity a,int resource,List<Solicitud> l,String nick){
        this.activity=a;
        this.resource=resource;
        this.mSolicitudes=l;
        this.nick=nick;
    }


    @NonNull
    @Override
    public SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new SolicitudesAdapter.SolicitudViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {
        if(mSolicitudes!=null){
            Solicitud current = mSolicitudes.get(position);

            holder.remitente.setText(current.getRemitente());
            if(current.getTipoSolicitud().equals("amistad"))
                holder.tipoSolicitud.setText("Solicitud de amistad de:");
            else
                holder.tipoSolicitud.setText("Solicitud de participar en la lista "+current.getNombreLista()+" de:");
        }

    }

    @Override
    public int getItemCount() {
        if(mSolicitudes!=null)
            return mSolicitudes.size();
        else
            return 0;

    }



    public class SolicitudViewHolder extends  RecyclerView.ViewHolder{

        private final EditText tipoSolicitud, remitente;
        private final ImageButton aceptar, declinar;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoSolicitud=itemView.findViewById(R.id.tipo_solicitud);
            remitente=itemView.findViewById(R.id.remitente);
            aceptar=itemView.findViewById(R.id.aceptar_sol);
            declinar=itemView.findViewById(R.id.declinar_sol);

            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            declinar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }
}
