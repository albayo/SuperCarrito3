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

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

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

    @Override
    public SolicitudesAdapter.SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new SolicitudesAdapter.SolicitudViewHolder(itemView);
    }

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

    @Override
    public int getItemCount() {
        if(mSolicitudes!=null)
            return mSolicitudes.size();
        else
            return 0;

    }

    public void setmSolicitudes(List<Solicitud> mSolicitudes) {
        this.mSolicitudes=new ArrayList<>();
        this.mSolicitudes = mSolicitudes;
    }

    public class SolicitudViewHolder extends  RecyclerView.ViewHolder{

        private final TextView tipoSolicitud, remitente;
        private final ImageButton aceptar, declinar;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            tipoSolicitud=itemView.findViewById(R.id.tipo_solicitud);
            remitente=itemView.findViewById(R.id.remitente);
            aceptar=itemView.findViewById(R.id.aceptar_sol);
            declinar=itemView.findViewById(R.id.declinar_sol);

            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();


            aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    Solicitud current=mSolicitudes.get(position);

                    if(current.getTipoSolicitud().equals("lista")){
                        ReadAndWriteSnippets.aniadirLista(nick,current.getIdLista(),current.getNombreLista());
                        ReadAndWriteSnippets.eliminarSolicitudLista(nick,current.getRemitente(),current.getIdLista());
                        mDatabase.child("users").child(nick).child("listas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                    else{
                        ReadAndWriteSnippets.aniadirAmigo(nick,current.getRemitente());
                        ReadAndWriteSnippets.aniadirAmigo(current.getRemitente(),nick);

                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());
                        mDatabase.child("users").child(nick).child("amistad").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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


                }
            });

            declinar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getLayoutPosition();
                    Solicitud current=mSolicitudes.get(position);
                    if(current.getTipoSolicitud().equals("lista")){
                        ReadAndWriteSnippets.eliminarSolicitudLista(nick,current.getRemitente(),current.getIdLista());
                        mDatabase.child("users").child(nick).child("listas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                    else{
                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());
                        ReadAndWriteSnippets.eliminarSolicitudAmistad(nick,current.getRemitente());
                        mDatabase.child("users").child(nick).child("amistad").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                }
            });
        }


    }
}
