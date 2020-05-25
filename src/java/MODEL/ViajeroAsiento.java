
package MODEL;

public class ViajeroAsiento {
       
    private Integer id;
    private String dni;
    private String nombre;
    private String apellidos;
    private Integer asiento;

    public ViajeroAsiento() {
    }
    
    public ViajeroAsiento(Integer id, String dni, String nombre, String apellidos, Integer asiento) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.asiento = asiento;
    }

     
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Integer getAsiento() {
        return asiento;
    }

    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }
    
}
