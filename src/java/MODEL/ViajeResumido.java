
package MODEL;




import java.util.Date;


public class ViajeResumido {
    
    private Integer id;
    private Integer plazasTotales;
    private Integer plazasDisponibles;
    private Date fecha;
     
    private Integer idHorario;
    private Date hora;
    private String tipo;

    public ViajeResumido() {
    }
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlazasTotales() {
        return plazasTotales;
    }

    public void setPlazasTotales(Integer plazasTotales) {
        this.plazasTotales = plazasTotales;
    }

    public Integer getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(Integer plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
     

}
