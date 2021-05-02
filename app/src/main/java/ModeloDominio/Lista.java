package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL



import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase define objetos que representan a las listas de los usuarios de la aplicación
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */

public class Lista implements Serializable {
    //Representa el número de listas creadas
    private static int contLista=0;
    //Representa el tipo de la lista
    private String tipolista;
    //Representa el identificador de la lista (es único)
    private int idLista;
    //Representa el nombre de la lista
    private String nombre;
    //Representa el/los usario/s que participan en la lista (este atributo no podrá ser nulo)
    private List<String> usuarios;
    //Representa los productos que contiene la lista
    private List<Producto> productos;

    /**
     * Constructor vacío para una Lista
     * Inicializa todas sus componentes
     */
    public Lista (){
        ReadAndWriteSnippets.actualizaContadorListas();
        this.idLista=0;
        this.nombre="";
         this.usuarios=new ArrayList<>();
        this.productos=new ArrayList<>();
    }

    /**
     * Constructor completo para una Lista (ya que una lista se crea vacía, sin productos en ella)
     * @param nombre Representa el nombre de la Lista
     * @param u Representa los usuarios que participan en la Lista
     */
    public Lista(String nombre, List<String> u){
        ReadAndWriteSnippets.actualizaContadorListas();
        contLista++;
        this.idLista=contLista;
        this.usuarios=u;
        this.nombre=nombre;
        this.productos=new java.util.ArrayList<Producto>();
    }


    /**
     * Método que devuelve el identificador de la Lista
     * @return El identificador de la Lista (la cadena devuelta no podrá ser nula)
     */
    public int getIdLista() {
        return idLista;
    }

    /**
     * Método que establece como identificador de la Lista la cadena pasada por parámetro
     * @param idLista Representa el nuevo identificador de la Lista (no podrá ser nulo)
     */
    public void setIdLista(int idLista) {
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
    public List<Producto> getProductos() {
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
    public List<String> getUsuarios() {
        return usuarios;
     }

    /**
     * Método que establece como usuarios que participan en la Lista la lista pasada por parámetro
     * @param u Representa la nueva lista de usuario de la Lista
     */
    public void setUsuarios(List<String> u){
        this.usuarios=u;
    }

    /**
     * Devuleve el contador estático de listas
     * @return
     */
    public static int getContLista(){return contLista;}

    /**
     * Método que establece el atributo de la lista contLista al valor pasado por parámetro
     * @param n Representa el nuevo valor que se le quiere dar a contLista
     */
    public static void setContLista(int n){contLista=n;}

    /**
     * Método que devuelve un objeto HashMap que guarda la información de una Lista.
     */
    public void setTipolista(String tipolista) {
        this.tipolista = tipolista;
    }

    /**
     * Método que convierte la información contenida en la clase a un mapa
     * @return result (mapa con el contenido de la clase)
     */
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("idLista",idLista);
        result.put("nombre",nombre);
        result.put("usuarios",usuarios);
        result.put("productos",productos);

        return result;
    }
}
