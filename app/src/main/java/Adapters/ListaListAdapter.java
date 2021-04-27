package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.R;

import java.io.Serializable;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.Usuario;

/**
 * Esta clase define el adapter necesario para que la capa de Presentación y Persistencia se comuniquen
 *  y se representen de forma visual los datos(las listas de un Usuario) de esta última
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */

public class ListaListAdapter extends RecyclerView.Adapter<ListaListAdapter.ListaViewHolder> implements Serializable {
    //Representa
    private final LayoutInflater mInflater;

    //Representa el Usuario que se ha logueado en la aplicación
    //private Usuario u;

    //Representa las listas que se representarán
    private List<Lista> mListas;  // Cached copy of Listas

    //He añadido un usuario

    /**
     * Constructor de la clase
     * @param context Representa el contexto de la aplicación
     * @param l Representa la lista de Listas que se dispondrá
     */
    public ListaListAdapter(Context context,List<Lista> l) {
        mInflater  = LayoutInflater. from (context);
        //u = usuario;
        this.mListas = l;
    }


    /**
     * Método que proporciona un objeto del tipo ListaViewHolder el cual será necesario para representar
     *  los datos
     * @param parent Representa el View padre en el cual se añadirá el objeto ListaViewHolder creado
     * @param viewType Representa el tipo de View que será el nuevo View
     * @return El ViewHolder del tipo viewType que servirá para representar los datos
     */
    @Override
    public ListaListAdapter.ListaViewHolder onCreateViewHolder(ViewGroup parent, int  viewType) {
        View itemView =  mInflater .inflate(R.layout.pantalla_listas_list, parent,  false );
        return new ListaListAdapter.ListaViewHolder(itemView);
    }

    /**
     * Método que representa los datos en el holder en la posición determinada
     * @param holder Representa el ViewHolder en el cual se representarán los datos
     * @param position Representa la posición en la que se dispondrán los datos en el holder
     */
    @Override
    public void  onBindViewHolder(ListaListAdapter.ListaViewHolder holder, int  position) {
        if  ( mListas  !=  null ) {

            Lista current =  mListas .get(position);
            holder.ListaNombreView.setText(current.getNombre());

        }  else  {
            // Covers the case of data not being ready yet.
            holder. ListaNombreView .setText( "No hay listas" );
        }
    }

    /**
     * Método que establece como lista de Listas la lista pasada por parámetro
     * @param Listas Representa la lista de Listas que se quiere asignar
     */
    public void  setListas(List<Lista> Listas){
        mListas  = Listas;
        notifyDataSetChanged();
    }
    // getItemCount﴾﴿ is called many times, and when it is first called,
    // mListas has not been updated ﴾means initially, it's null, and we can't return null.

    /**
     * Método que devuelve el número de elementos a representar
     * @return El número de elementos a representar
     */
    @Override
    public int  getItemCount() {
        if  ( mListas  !=  null )
            return  mListas .size();
        else return  0;
    }

    /**
     * Esta clase define el tipo el cual será usado para representar los datos(las listas de un Usuario)
     * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
     * @version: 13/04/2021
     */
    class  ListaViewHolder  extends  RecyclerView.ViewHolder {
        //Representa el View donde se dispondrán los nombres de las listas
        private final TextView ListaNombreView;

        /**
         * Constructor de la clase
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         */
        private  ListaViewHolder(View itemView) {
            super (itemView);
            ListaNombreView=itemView.findViewById(R.id.text_lista_usuario);
        }
    }
}