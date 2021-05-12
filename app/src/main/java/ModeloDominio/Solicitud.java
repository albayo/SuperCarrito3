package ModeloDominio;
/**
 * Clase que representa las solicitudes de amistad o de listas grupales
 *
 * @author: Pablo Ochoa, Javier Pérez, Marcos Moreno, Álvaro Bayo
 * @version: 12/05/2021
 */
public class Solicitud {
    private String remitente;
    private String tipoSolicitud;
    private String nombreLista;
    private String idLista;

    public Solicitud(String remitente) {
        this.remitente = remitente;
    }

    public void setSolicitudAmistad(){
        this.tipoSolicitud="amistad";
    }

    public void setSolicitudLista(String idLista, String nombreLista){
        this.tipoSolicitud="lista";
        this.idLista=idLista;
        this.nombreLista=nombreLista;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }


}
