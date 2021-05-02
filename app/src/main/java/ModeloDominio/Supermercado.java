package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL

/**
 * Esta clase define objetos que representan a los Supermercados de la aplicación
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */

public class Supermercado{
    //Representa el nombre del supermercado
    private String nombreSuper;

    /**
     * Constructor vacío para un Supermercado
     * Inicializa sus atributos
     */
    public Supermercado(){
        this.nombreSuper="";
    }

    /**
     * Constructor para un Supermercado con un atributo
     * @param nombre Representa el nombre que tiene el Supermercado
     */
    public Supermercado(String nombre){
        this.nombreSuper=nombre;
    }

    /**
     * Método para devuelve el nombre del Supermercado
     * @return El nombre del Supermercado
     */
    public String getNombreSuper() {
        return nombreSuper;
    }

    /**
     * Método que establece como nombre del Supermercado la cadena pasada por parámetro
     * @param nombre Representa el nombre que se le quiere asignar al Supermercado
     */
    public void setNombreSuper(String nombre) {
        this.nombreSuper= nombre;
    }
}