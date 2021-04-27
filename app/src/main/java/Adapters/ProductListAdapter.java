package Adapters;



import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.accesoconcorreo.R;

import java.util.List;

import ModeloDominio.Producto;
    /**
     * Esta clase define el Adapter necesario para que nuestra capa de presentacion se comunique
     * con la Persistencia y de esta manera mostrar los productos en ella.
     *
     * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
     * @version: 13/04/2021
     */
    public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductoViewHolder> {
        //Representa el objeto necesario para la instanciacion en forma de View del layout necesario en este caso(item:prod_list)

        private int resource;
        //Representa una lista de los productos que obtendremos de la base de datos para mostrarlos.
        private List<Producto> mProductos;  // Cached copy of Productos

        /**
         * Constructor de un adapter de la lista de productos
         * @param resource Representa el contexto de la aplicacion
         * @param Productos Representa la lista de productos a mostrar
         */
        public ProductListAdapter(int resource,List<Producto> Productos) {
            this.resource=resource;
            //u = usuario;
            this.mProductos = Productos;
        }

        /**
         * Metodo que devuelve un objeto tipo ProductoViewHolder en base a un XML que necesitaremos para representar los datos
         * @param parent Representa el padre en el cual se representara el objeto viewHolder creado
         * @param viewType
         */

        @Override
        public  ProductoViewHolder onCreateViewHolder(ViewGroup parent, int  viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
            return new ProductListAdapter.ProductoViewHolder(itemView);
        }

        /**
         * Metodo que en un objeto tipo ProductoViewHolder representa los datos en las posiciones especificas
         * @param holder Representa el objeto tipo ViewHolder
         * @param position Representa la posicion
         */
        @Override
        public void  onBindViewHolder(ProductoViewHolder holder,  int  position) {
            if  ( mProductos  !=  null ) {
                Producto current =  mProductos .get(position);
                holder. ProductoNombreView .setText(current.getNombre());
                holder.ProductoImageView.setImageURI(Uri.parse(current.getImage()));
                holder.ProductoSuperView.setText(current.getSupermercado());
            }  else  {
                // Covers the case of data not being ready yet.
                holder. ProductoNombreView .setText( "No Producto" );
            }
        }
        /**
         * Sustituye la lista de productos del adapter por otra lista de productos.
         * @param Productos Representa la lista de productos a sustituir
         */
        public void  setProductos(List<Producto> Productos){
            mProductos  = Productos;
            notifyDataSetChanged();
        }
        // getItemCount﴾﴿ is called many times, and when it is first called,
        // mProductos has not been updated ﴾means initially, it's null, and we can't return null.
        /**
         * Metodo que devuelve el tamaño de la lista de productos que dispone el adapter
         */
        @Override
        public int  getItemCount() {
            if  ( mProductos  !=  null )
                return  mProductos .size();
            else return  0;
        }

        /**
         * Esta clase define el ViewHolder necesario para utilizar en nuestro ListAdapter, obteniendo los datos de un View
         * con las id que los representa en esta
         *
         * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
         * @version: 13/04/2021
         */
        class  ProductoViewHolder  extends  RecyclerView.ViewHolder {
            // Representa un textView en el cual vamos a almacenar el descendente del nombre del producto
            private final TextView ProductoNombreView;
            // Representa un imageView en el cual vamos a almacenar el descendente de la imagen del producto
            private final ImageView ProductoImageView;
            // Representa un textView en el cual vamos a almacenar el descendente del supermercado del producto
            private final TextView ProductoSuperView;

            /**
             * Esta clase define el ViewHolder necesario para utilizar en nuestro ListAdapter, obteniendo los datos de un View
             * con las id que los representa en esta.
             *
             * @param itemView Representa la interfaz para obtener los datos
             */
            private  ProductoViewHolder(View itemView) {
                super (itemView);

                ProductoNombreView  = itemView.findViewById(R.id.nombre_producto);
                ProductoImageView=itemView.findViewById(R.id.fotoproducto_lista);
                ProductoSuperView=itemView.findViewById(R.id.super_producto);

            }
        }
    }


