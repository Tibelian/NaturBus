package POJOS;
// Generated 25-may-2020 2:38:58 by Hibernate Tools 4.3.1



/**
 * Administrador generated by hbm2java
 */
public class Administrador  implements java.io.Serializable {


     private Integer id;
     private String email;
     private String clave;

    public Administrador() {
    }

    public Administrador(String email, String clave) {
       this.email = email;
       this.clave = clave;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getClave() {
        return this.clave;
    }
    
    public void setClave(String clave) {
        this.clave = clave;
    }




}


