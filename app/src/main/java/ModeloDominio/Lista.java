package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL



import java.io.Serializable;
import java.util.ArrayList;

/**
 * Esta clase define objetos que representan al las listas de los usuarios de la aplicación
 * Además representa la tabla llamada "lista_table" en la BD
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */



public class Lista implements Serializable {


    private String idLista;

    private String nombre;

    //Representa el/los usario/s que participan en la lista (este atributo no podrá ser nulo)
    private ArrayList<Usuario> usuarios;


    private ArrayList<Producto> productos;


    /**
     * Constructor vacío para una Lista
     * Inicializa todas sus componentes
     */
    public Lista (){
        this.idLista="";
        this.nombre="";
         this.usuarios=new ArrayList<>();
        this.productos=new ArrayList<>();
    }

    /**
     * Constructor completo para una Lista (ya que una lista se crea vacía, sin productos en ella)
     * @param idLista Representa el identificador de la Lista
     * @param nombre Representa el nombre de la Lista
     * @param u Representa los usuarios que participan en la Lista
     */
    public Lista(String idLista, String nombre, ArrayList<Usuario> u){
        this.idLista=idLista;
        // this.usuarios=u;
        this.nombre=nombre;
        //this.productos=new java.util.ArrayList<Producto>();
    }


    /**
     * Método que devuelve el identificador de la Lista
     * @return El identificador de la Lista (la cadena devuelta no podrá ser nula)
     */
    public String getIdLista() {
        return idLista;
    }

    /**
     * Método que establece como identificador de la Lista la cadena pasada por parámetro
     * @param idLista Representa el nuevo identificador de la Lista (no podrá ser nulo)
     */
    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    /**
     * Método que devuelve el nombre de la Lista
     * @return El nombre de la Lista
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método que establece como nombre de la Lista la cadena pasada por parámetro
     * @param nombre Representa el nuevo nombre de la Lista
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método que devuelve los productos de la Lista
     * @return Una lista de los producto de la Lista
     */
    public ArrayList<Producto> getProductos() {
        return productos;
    }

    /**
     * Método que establece como productos de la Lista la lista pasada por parámetro
     * @param productos Representa la nueva lista de productos de la Lista
     */
    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    /**
     * Método que devuelve los usuarios que participan en la Lista
     * @return Una lista con los usuarios que participan en la Lista
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
     }

    /**
     * Método que establece como usuarios que participan en la Lista la lista pasada por parámetro
     * @param u Representa la nueva lista de usuario de la Lista
     */
    public void setUsuarios(ArrayList<Usuario> u){
        this.usuarios=u;
    }
}
