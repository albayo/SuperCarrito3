package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase define objetos que representan al usuario de la aplicación
 * Además representa la tabla llamada "usuario_table" en la BD
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */

@IgnoreExtraProperties //ESTO LO HE VISTO EN EL EJEMPLO DE FIREBASE, CREO QUE NO HACE FALTA QUE SEA SERIALIZABLE
public class Usuario implements Serializable {

    private String nick;
    private String contra;
    private List<Lista> listas;

    /**
     * Constructor vacío para el Usuario
     * Se inicializan todos los atributos con contenido vacío
     */
    public Usuario(){
        this.nick="";
        this.contra="";
        this.listas=new ArrayList<>();
    }

    /**
     * Constructor completo para el Usuario
     * @param u Representa el nombre que tiene el usuario
     * @param c Representa la constraseña que usará para loguearse
     * @param l Representa el conjunto de listas que tiene el usuario
     */
    public Usuario(String u, String c, ArrayList<Lista> l) {
        this.nick = u;
        this.contra = c;
        this.listas = l;

    }

    /**
     * Constructor para el Usuario sin listas (Constructor inicial)
     * @param u Representa el nombre que tiene el usuario
     * @param c Representa la constraseña que usará para loguearse
     */
    public Usuario (String u, String c){
        this.nick = u;
        this.contra = c;
        this.listas = new ArrayList<Lista>();
    }

    /**
     * Método que devuelve el nombre del Usuario
     * @return El nombre del Usuario
     */
    public String getNick() {
        return nick;
    }

    /**
     * Método que establece como nombre del Usuario la cadena pasada por parámetro
     * @param u Representa el nombre que se le quiere dar al usuario
     */
    public void setNick(String u) {
        this.nick = u;
    }

    /**
     * Método que devuelve la contraseña del Usuario
     * @return La contraseña del Usuario
     */
    public String getContra() {
        return contra;
    }

    /**
     * Método que establece como contraseña del Usuario la cadena pasada por parámetro
     * @param c Representa la constraseña que se le quiere asignar al usuario
     */
    public void setContra(String c) {
        this.contra = c;
    }

    /**
     * Método que devuelve la todas las listas del Usuario
     * @return La lista de Listas del Usuario
     */
    public List<Lista> getListas() {
        return listas;
    }

    /**
     * Método que establece las listas del Usuario a las listas pasadas por parámetro
     * @param l Representa las listas que se le quieren asignar al usuario
     */
    public void setListas(List<Lista> l) {
        this.listas = l;
    }
}
