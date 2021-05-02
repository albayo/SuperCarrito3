package Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.ListaProductos;
import com.example.accesoconcorreo.R;
import com.example.accesoconcorreo.TodosProductos;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ModeloDominio.Producto;
/**
 * Esta clase define el adapter necesario para que la capa de Presentación y Persistencia se comuniquen
 *  y se representen de forma visual los datos(todos los Productos de los supermercados)
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */
public class TodosProductosAdapter  extends RecyclerView.Adapter<TodosProductosAdapter.TodosProductosHolder>{

    //Representa las listas de productos de la lista que se mostrarán
    private List<Producto> mProductos;  // Cached copy of Listas
    //Representa el id de la Lista a la que añadir si se añaden productos
    private String idLista;
    //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)
    private int resource;
    //Representa el siguiente Activity a donde queremos redirigir.
    private Activity a;

    /**
     * Constructor de la clase
     * @param resource Representa el contexto de la aplicación
     * @param l Representa la lista de Productos que se dispondrá
     * @param a Representa el siguiente Activity al que iremos
     * @param idLista Representa la lisa a la que añadiremos productos.
     */
    public TodosProductosAdapter(Activity a, int resource, List<Producto> l, String idLista) {
        this.resource=resource;
        this.a = a;
        this.mProductos = l;
        this.idLista=idLista;
    }


    /**
     * Método que proporciona un objeto del tipo ListaViewHolder el cual será necesario para representar
     *  los datos
     * @param parent Representa el View padre en el cual se añadirá el objeto ListaViewHolder creado
     * @param viewType Representa el tipo de View que será el nuevo View
     * @return El ViewHolder del tipo viewType que servirá para representar los datos
     */

    @Override
    public TodosProductosAdapter.TodosProductosHolder onCreateViewHolder(ViewGroup parent, int  viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new TodosProductosAdapter.TodosProductosHolder(itemView, this);
    }

    /**
     * Método que representa los datos en el holder en la posición determinada
     * @param holder Representa el ViewHolder en el cual se representarán los datos
     * @param position Representa la posición en la que se dispondrán los datos en el holder
     */
    @Override
    public void  onBindViewHolder(TodosProductosAdapter.TodosProductosHolder holder, int  position) {
        if  ( mProductos  !=  null || mProductos.get(position)!=null) {

            Producto current =  mProductos.get(position);
            String nomProd=current.getNombre();
            if(current.getNombre().contains(","))
                nomProd = current.getNombre().split(",")[1];
            //para poner la primera letra en MAY del nombre
            char[] arr = nomProd.trim().toCharArray();
            arr[0] = Character.toUpperCase(arr[0]);
            nomProd = new String(arr);


            holder.nombreProductoView.setText(nomProd);

            if(!current.getImage().equals(""))
                holder.imagenProducto.setImageURI(Uri.parse(current.getImage()));

            else {holder.imagenProducto.setImageResource(R.mipmap.pordefecto);}


            holder.nombreSuper.setText(current.getSupermercado());
              DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

            holder.añadirProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("listas").child(idLista).child("productos").child(current.getIdProducto()).setValue(current.getIdProducto());
                }
            });

        }  else  {
            // Covers the case of data not being ready yet.
            holder.nombreSuper.setText( "No Producto" );
        }
    }


    // getItemCount﴾﴿ is called many times, and when it is first called,
    // mListas has not been updated ﴾means initially, it's null, and we can't return null.

    /**
     * Método que devuelve el número de elementos a representar
     * @return El número de elementos a representar
     */
    @Override
    public int  getItemCount() {
        if  ( mProductos  !=  null )
            return  mProductos .size();
        else return  0;
    }

    /**
     * Metodo que sirve para cambiar la lista de productos
     * @param productos la nueva lista de productos que queremos mostrar
     */
    public void setProductos(List<Producto> productos) {
        this.mProductos=productos;
    }

    /**
     * Esta clase define el tipo el cual será usado para representar los datos(las listas de un Usuario)
     * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
     * @version: 02/05/2021
     */
    public class  TodosProductosHolder  extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //Representa el View donde se dispondrán los nombres de las listas
        public View view;
        //Representa el TextView donde se dispondrá el nombre del producto
        private final TextView nombreProductoView;
        //Representa el TextView donde se dispondrá el nombre del supermercado donde se dispondrá el producto
        private final TextView nombreSuper;
        //Representa la ImageView correspondiente a la imagen del producto.
        private final ImageView imagenProducto;
        //Representa el Botón para añadir un producto a la lista
        private final Button añadirProducto;
        //Representa el adapter al que corresponde
        final TodosProductosAdapter adapter;
        /**
         * Constructor de la clase
         * @param itemView View en el cual se busca el TextView en el cual se representará la información
         * @param adapter representa el adaptador que maneja los datos y views del RecyclerView
         */

        private TodosProductosHolder(View itemView, TodosProductosAdapter adapter) {
            super (itemView);
            this.view=itemView;
            this.nombreProductoView= (TextView) itemView.findViewById(R.id.nombre_producto);
            this.nombreSuper=(TextView)itemView.findViewById(R.id.super_producto);
            this.imagenProducto=(ImageView)itemView.findViewById(R.id.fotoproducto_super);
            this.añadirProducto=(Button)itemView.findViewById(R.id.btnAniadirProd);
            this.adapter = adapter;
            itemView.setOnClickListener(this);

        }
        /**
         * Método que representa el clickado en un item de la view
         * @param v View en el cual se ha clickado
         */

        @Override
        public void onClick(View v) {
            //Se obtiene la posicion del item que ha sido clickado
            int mPosicion = getLayoutPosition();
            Intent intent = new Intent(a, ListaProductos.class);
           // intent.putExtra("nombreLista", lista);
           // intent.putExtra("idLista", id);

            a.startActivity(intent);
        }
    }


}
