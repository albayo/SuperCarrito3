package Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ModeloDominio.Solicitud;

public class SolicitudesAdapter extends RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder> {

    //No puede ser de lista de Strings
    private List<Solicitud> mSolicitudes=new ArrayList<>();
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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SolicitudViewHolder extends  RecyclerView.ViewHolder{

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
