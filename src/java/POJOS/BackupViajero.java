package POJOS;
// Generated 25-may-2020 2:38:58 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * BackupViajero generated by hbm2java
 */
public class BackupViajero  implements java.io.Serializable {


     private Integer id;
     private String dni;
     private String nombre;
     private String apellidos;
     private Date fechaBaja;
     private Set backupOcupacions = new HashSet(0);

    public BackupViajero() {
    }

	
    public BackupViajero(String dni, String nombre, String apellidos, Date fechaBaja) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaBaja = fechaBaja;
    }
    public BackupViajero(String dni, String nombre, String apellidos, Date fechaBaja, Set backupOcupacions) {
       this.dni = dni;
       this.nombre = nombre;
       this.apellidos = apellidos;
       this.fechaBaja = fechaBaja;
       this.backupOcupacions = backupOcupacions;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDni() {
        return this.dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return this.apellidos;
    }
    
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public Date getFechaBaja() {
        return this.fechaBaja;
    }
    
    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    public Set getBackupOcupacions() {
        return this.backupOcupacions;
    }
    
    public void setBackupOcupacions(Set backupOcupacions) {
        this.backupOcupacions = backupOcupacions;
    }




}


