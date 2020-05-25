package POJOS;
// Generated 25-may-2020 2:38:58 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Ruta generated by hbm2java
 */
public class Ruta  implements java.io.Serializable {


     private Integer id;
     private Estacion estacionByIdOrigen;
     private Estacion estacionByIdDestino;
     private Date duracion;
     private int kilometros;
     private double precio;
     private Set horarios = new HashSet(0);

    public Ruta() {
    }

	
    public Ruta(Estacion estacionByIdOrigen, Estacion estacionByIdDestino, Date duracion, int kilometros, double precio) {
        this.estacionByIdOrigen = estacionByIdOrigen;
        this.estacionByIdDestino = estacionByIdDestino;
        this.duracion = duracion;
        this.kilometros = kilometros;
        this.precio = precio;
    }
    public Ruta(Estacion estacionByIdOrigen, Estacion estacionByIdDestino, Date duracion, int kilometros, double precio, Set horarios) {
       this.estacionByIdOrigen = estacionByIdOrigen;
       this.estacionByIdDestino = estacionByIdDestino;
       this.duracion = duracion;
       this.kilometros = kilometros;
       this.precio = precio;
       this.horarios = horarios;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Estacion getEstacionByIdOrigen() {
        return this.estacionByIdOrigen;
    }
    
    public void setEstacionByIdOrigen(Estacion estacionByIdOrigen) {
        this.estacionByIdOrigen = estacionByIdOrigen;
    }
    public Estacion getEstacionByIdDestino() {
        return this.estacionByIdDestino;
    }
    
    public void setEstacionByIdDestino(Estacion estacionByIdDestino) {
        this.estacionByIdDestino = estacionByIdDestino;
    }
    public Date getDuracion() {
        return this.duracion;
    }
    
    public void setDuracion(Date duracion) {
        this.duracion = duracion;
    }
    public int getKilometros() {
        return this.kilometros;
    }
    
    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }
    public double getPrecio() {
        return this.precio;
    }
    
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public Set getHorarios() {
        return this.horarios;
    }
    
    public void setHorarios(Set horarios) {
        this.horarios = horarios;
    }




}

