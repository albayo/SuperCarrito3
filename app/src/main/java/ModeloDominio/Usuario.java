package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase define objetos que representan al usuario de la aplicación
 * Además representa la tabla llamada "usuario_table" en la BD
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 13/04/2021
 */

@IgnoreExtraProperties //ESTO LO HE VISTO EN EL EJEMPLO DE FIREBASE, CREO QUE NO HACE FALTA QUE SEA SERIALIZABLE
public class Usuario {
    private static int contUsuario=0;
    private int id;
    private String nick;
    private String email;
    private List<String> listas;

    public Usuario(String email){
        contUsuario++;
        id=contUsuario;
        this.nick="";
        this.email=email;
        this.listas=new ArrayList<>();
    }

    /**
     * Constructor para el Usuario sin listas (Constructor inicial)
     * @param nick Representa el nombre que tiene el usuario
     * @param email Representa la constraseña que usará para loguearse
     */
    public Usuario (String nick, String email) {
        contUsuario++;
        id=contUsuario;
        this.nick = nick;
        this.email = email;
        this.listas = new ArrayList<String>();
    }
    public int getId(){ return id; }
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

    public String getEmail(){ return  email;}
    public void setEmail(String e){this.email=e;}
    /**
     * Método que devuelve la todas las listas del Usuario
     * @return La lista de Listas del Usuario
     */
    public List<String> getListas() {
        return listas;
    }

    /**
     * Método que establece las listas del Usuario a las listas pasadas por parámetro
     * @param l Representa las listas que se le quieren asignar al usuario
     */
    public void setListas(List<String> l) {
        this.listas = l;
    }

    public void insertaLista(String l){
        listas.add(l);
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("nick",nick);
        result.put("email",email);
        result.put("listas",listas);

        return result;
    }
}
