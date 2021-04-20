package ModeloDominio;
//Falta arreglar la base de datos ESTA MAL




public class Producto {


    private String idProducto;
    private String nombre;

    private String brand;

    private String image;


    private String ingred; // Una string para ingredienteS ????
    private String supermercado;

    /**
     * Constructor vacío para un Producto
     * Inicializa todas sus componentes
     */
    public Producto(){
        this.idProducto = "";
        this.nombre = "";
        this.brand = "";
        this.image = "";
        this.ingred = "";
        this.supermercado=null;
    }

    /**
     * Constructor completo para un Producto
     * @param idProducto Representa el identificador del Producto
     * @param nombre Representa el nombre del Producto
     * @param brand Representa la marca del Producto
     * @param image Representa la imagen(la URL de la imagen) del Producto
     * @param ingred Representa los ingredientes del Producto
     * @param s Representa el supermercado en el que se puede comprar el Producto
     */
    public Producto(String idProducto, String nombre, String brand, String image, String ingred,String s) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.brand = brand;
        this.image = image;
        this.ingred = ingred;
        this.supermercado=s;
    }

    /**
     * Método que devuelve el identificador del Producto
     * @return El identificador del Producto
     */
    public String getIdProducto() {
        return idProducto;
    }

    /**
     * Método que establece como identificador del Producto la cadena pasada por parámetro
     * @param idProducto Representa el nuevo identificador del Producto
     */
    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Método que devuelve el nombre del Producto
     * @return El nombre del Producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método que establece como nombre del Producto la cadena pasada por parámetro
     * @param nombre Representa el nuevo nombre del Producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método que devuelve la marca del Producto
     * @return La marca del Producto
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Método que establece como marca del Producto la cadena pasada por parámetro
     * @param brand Representa la marca que se le quiere asignar al Producto
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Método que devuelve la imagen(la URL de la imagen) del Producto
     * @return La imagen(la URL de la imagen) del Producto
     */
    public String getImage() {
        return image;
    }

    /**
     * Método que establece como imagen(la URL de la imagen) del Producto la cadena pasada por parámetro
     * @param image Representa la imagen(la URL de la imagen) del Producto
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Método que devuelve los ingredientes del Producto
     * @return Los ingredientes del Producto en forma de String (????)
     */
    public String getIngred() {
        return ingred;
    }

    /**
     * Método que establece como ingredientes del Producto la cadena pasada por parámetro
     * @param ingred Representa los ingredientes del Producto
     */
    public void setIngred(String ingred) {
        this.ingred = ingred;
    }


    /**
     * Método que devuelve el nombre del supermercado del Producto
     * @return El nombre del supermercado del Producto (la cadena devuelta no puede ser nula)
     */
    public String getSupermercado() {
        return supermercado;
    }

    /**
     * Método que establece como nombre del supermercado del Producto la cadena pasada por parámetro
     * @param sup Representa el nombre del supermercado del Producto (no puede ser nula)
     */
    public void setSupermercado( String sup) {
        this.supermercado = sup;
    }
}