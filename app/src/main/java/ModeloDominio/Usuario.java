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
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 02/05/2021
 */

@IgnoreExtraProperties
public class Usuario {
    //Representa el numero de usuarios logueados en la aplicación
    private static int contUsuario=0;
    //Representa el identificador del usuario
    private int id;
    //Representa el nick del usuario
    private String nick;
    //Representa el email del usuario
    private String email;
    //Representa el nombre de las listas en la que participa el usuario
    private List<String> listas;
    //Representa la foto del usuario
    private String fotoPerfil;


    /**
     * Constructor base de la clase
     * @param email Representa el email con el que se ha logueado el usuario
     */
    public Usuario(String email){
        contUsuario++;
        id=contUsuario;
        this.nick="";
        this.email=email;
        this.listas=new ArrayList<>();
        this.fotoPerfil=Constantes.URLFOTODEFECTO;
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
        this.fotoPerfil=Constantes.URLFOTODEFECTO;
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

    /**
     * Método que añade la lista cuyo nombre es l a las listas del usuario
     * @param l Representa el nombre de una lista
     */
    public void insertaLista(String l){
        listas.add(l);
    }


    /**
     * Método que añade la foto cuya URL es s a la foto del usuario
     * @param s Representa la URL de la foto
     */
    public void aniadirfoto(String s){ this.fotoPerfil=s;}

    /**
     * Método que convierte la información contenida en la clase a un mapa
     * @return result (mapa con el contenido de la clase)
     */
    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("nick",nick);
        result.put("email",email);
        result.put("listas",listas);
        result.put("fotoperfil",fotoPerfil);

        return result;
    }
}
