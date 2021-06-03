package ModeloDominio;
/**
 * Clase que representa las solicitudes de amistad o de listas grupales
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 03/06/2021
 */
public class Solicitud {
    //Representa el remitente de una solicitud
    private String remitente;
    //Representa el tipo de solicitud que es (de amigos o de listas)
    private String tipoSolicitud;
    //Representa el nombre de la lista
    private String nombreLista;
    //Representa el id de la lista
    private String idLista;

    /**
     * Constructor principal de la clase
     * @param remitente representa el remitente de la solicitud
     */
    public Solicitud(String remitente) {
        this.remitente = remitente;
    }

    /**
     * Método que establece el tipo de solicitud a amistad
     */
    public void setSolicitudAmistad(){
        this.tipoSolicitud="amistad";
    }

    /**
     * Método que establece el tipo de solicitud a amistad
     */
    public void setSolicitudLista(String idLista, String nombreLista){
        this.tipoSolicitud="lista";
        this.idLista=idLista;
        this.nombreLista=nombreLista;
    }

    /**
     * Método que obtiene el remitente
     * @return remitente
     */
    public String getRemitente() {
        return remitente;
    }

    /**
     * Método que establece como remitente el remitente pasado por parámetro
     * @param remitente
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
     * Método que devuelve el tipo de la solicitud
     * @return tipoSolicitud
     */
    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    /**
     * Método que establece el tipo de la solicitud al tipo pasado por parámetro
     * @param tipoSolicitud representa el nuevo tipo de solicitud
     */
    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    /**
     * Método que devuelve el nombre de la lista
     * @return
     */
    public String getNombreLista() {
        return nombreLista;
    }

    /**
     * Método que establece el nombre de la lista al pasado por parámetro
     * @param nombreLista
     */
    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    /**
     * Método que devuelve el identificador de la lista
     * @return idLista
     */
    public String getIdLista() {
        return idLista;
    }

    /**
     * Método que establece el identificador de la lista al pasado por parámetro
     * @param idLista
     */
    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }


}
