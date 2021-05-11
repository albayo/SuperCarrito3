package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.CrearLista;
import com.example.accesoconcorreo.Home;
import com.example.accesoconcorreo.ListaProductos;
import com.example.accesoconcorreo.R;

import java.io.Serializable;
import java.util.List;

import ModeloDominio.Lista;
import ModeloDominio.Producto;
import ModeloDominio.Usuario;

/**
 * Esta clase define el adapter necesario para que la capa de Presentación y Persistencia se comuniquen
 *  y se representen de forma visual los datos(las listas de un Usuario) de esta última
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */

public class ListaListAdapter extends RecyclerView.Adapter<ListaListAdapter.ListaViewHolder>{


    //
    //Representa las listas que se representarán
    private List<String> mListas;  // Cached copy of Listas
    //Represente los ids de las listas , se cambiará por un Map.
    private List<String> mListasId; //
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity a;

    /**
     * Constructor de la clase
     * @param resource Representa el contexto de la aplicación
     * @param l Representa la lista de Listas que se dispondrá
     * @param lid Representa la lista con los ids de cada Lista
     * @param a Representa el Activity siguiente al que queremos ir
     */
    public ListaListAdapter(Activity a, int resource, List<String> l,List<String> lid) {
        this.resource=resource;
        this.a = a;
        this.mListas = l;
        this.mListasId=lid;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ListaListAdapter.ListaViewHolder(itemView, this);
    }

    /**
     * Método que representa los datos en el holder en la posición determinada
     * @param holder Representa el ViewHolder en el cual se representarán los datos
     * @param position Representa la posición en la que se dispondrán los datos en el holder
     */
    @Override
    public void  onBindViewHolder(ListaListAdapter.ListaViewHolder holder, int  position) {
        if  ( mListas  !=  null || mListas.get(position)!=null) {
            String current =  mListas.get(position);
            holder.ListaNombreView.setText(current);
            holder.idLista.setText(mListasId.get(position));
        }  else  {
            // Covers the case of data not being ready yet.
            holder. ListaNombreView .setText( "No hay listas" );
        }
    }

    /**
     * Método que establece como lista de Listas la lista pasada por parámetro
     * @param Listas Representa la lista de Listas que se quiere asignar
     */
    public void  setListas(List<String> Listas){
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
     * @version: 02/05/2021
     */
    public class  ListaViewHolder  extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde sale el nombre de la lista
        private final TextView ListaNombreView;
       //Representa el TextView donde saldrá el id de la lista
        private final TextView idLista;
        //Representa una copia del adapter
        final ListaListAdapter adapter;
        //Representa el Checkbox que indicará si esta lista se quiere elminiar o no
        private final CheckBox checkbox;
        /**
         * Constructor de la clase
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter representa el adaptador que maneja los datos y views del RecyclerView
         */

        private  ListaViewHolder(View itemView, ListaListAdapter adapter) {
            super (itemView);
            this.view=itemView;
            this.ListaNombreView= (TextView) itemView.findViewById(R.id.text_lista_usuario);
            this.idLista=(TextView)itemView.findViewById(R.id.text_id_lista);
            checkbox=(CheckBox) itemView.findViewById(R.id.checkBox_eliminar);

            this.adapter = adapter;
            itemView.setOnClickListener(this);

            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    String current =  mListas.get(position);

                }
            });
        }

        /**
         * Método que representa el clickado en un item de la view
         * @param v View en el cual se ha clickado
         */
        @Override
        public void onClick(View v) {
            //Se obtiene la posicion del item que ha sido clickado
            int mPosicion = getLayoutPosition();
            String lista = mListas.get(mPosicion);
            String id = mListasId.get(mPosicion);

            Intent intent = new Intent(a, ListaProductos.class);
            intent.putExtra("nombreLista", lista);
            intent.putExtra("idLista", id);

            a.startActivity(intent);
        }
    }


}