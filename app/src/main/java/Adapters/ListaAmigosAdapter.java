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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.Home;
import com.example.accesoconcorreo.ListaProductos;
import com.example.accesoconcorreo.R;

import java.util.List;

public class ListaAmigosAdapter extends RecyclerView.Adapter<ListaAmigosAdapter.AmigosViewHolder>{
    //Representa las listas que se representarán
    private List<String> mListas;  // Cached copy of Listas
    //Represente los ids de las listas , se cambiará por un Map.
    private List<String> mListasId; //
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente activity al que iremos, para redirigir.
    private Activity a;

    public ListaAmigosAdapter(Activity a, int resource, List<String> l,List<String> lid) {
        Log.d("Construcor","ConstructorLanzado");
        this.resource=resource;
        this.a = a;
        this.mListas = l;
        this.mListasId=lid;
    }
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int  viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ListaAmigosAdapter.AmigosViewHolder(itemView,this);
    }
    @Override
    public void  onBindViewHolder(ListaAmigosAdapter.AmigosViewHolder holder, int  position) {
        Log.d("Holder","HolderLanzado");
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
    public class  AmigosViewHolder  extends  RecyclerView.ViewHolder{
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde sale el nombre de la lista
        private final TextView ListaNombreView;
        //Representa el TextView donde saldrá el id de la lista
        private final TextView idLista;
        //Representa una copia del adapter
        final ListaAmigosAdapter adapter;
        private final ImageButton btELiminarAmigo;
        /**
         * Constructor de la clase
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter representa el adaptador que maneja los datos y views del RecyclerView
         */

        private  AmigosViewHolder(View itemView, ListaAmigosAdapter adapter) {
            super (itemView);
            this.view=itemView;
            this.ListaNombreView= (TextView) itemView.findViewById(R.id.text_lista_usuario);
            this.idLista=(TextView)itemView.findViewById(R.id.text_id_lista);
            this.btELiminarAmigo=(ImageButton)itemView.findViewById(R.id.btEliminarAmigo);
            this.adapter = adapter;
            //itemView.setOnClickListener(this);

            /*btELiminarAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    androidx.appcompat.app.AlertDialog.Builder b= new androidx.appcompat.app.AlertDialog.Builder(this);
                    b.setTitle("Confirmación");
                    b.setMessage("¿Está seguro/a de que desea cerrar sesión?");
                    b.setPositiveButton("Aceptar",new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Home.super.onBackPressed();
                        }
                    });

                    b.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onResume();
                        }
                    });
                    AlertDialog alert=b.create();
                    alert.show();
                }
            });*/
        }

        /**
         * Método que representa el clickado en un item de la view
         * @param v View en el cual se ha clickado
         */
        /*@Override
        public void onClick(View v) {
            //Se obtiene la posicion del item que ha sido clickado
            int mPosicion = getLayoutPosition();
            String lista = mListas.get(mPosicion);
            String id = mListasId.get(mPosicion);

            Intent intent = new Intent(a, ListaProductos.class);
            intent.putExtra("nombreLista", lista);
            intent.putExtra("idLista", id);

            a.startActivity(intent);
        }*/
    }
}
