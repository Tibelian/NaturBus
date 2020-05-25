
package MODEL;

import POJOS.Ocupacion;
import POJOS.Ruta;
import POJOS.Viaje;
import java.util.ArrayList;
import java.util.Date;

public class Reserva {
    
    private Date fechaSalida;                       // fecha de salida
    private Date fechaCompra;                       // fecha cuando se realiza la compra
    private int pasajeros;                          // cantidad de pasajeros
    private Ruta ruta;                              // la ruta seleccionada
    private Viaje viaje;                            // el viaje seleccionado
    private Ocupacion ocupacion;                    // 
    private ArrayList<ViajeroAsiento> viajeros;     // un viajero con su asiento
    private String ultimoPaso;                      // vista por si el usuario se sale para que retome la reserva
    
    public Reserva(){}
    
    public Reserva(Ruta ruta, Date fechaSalida, int pasajeros){
        this.ruta = ruta;
        this.fechaSalida = fechaSalida;
        this.pasajeros = pasajeros;
    }

    public ArrayList<ViajeroAsiento> getViajeros() {
        return viajeros;
    }

    public void setViajeros(ArrayList<ViajeroAsiento> viajeros) {
        this.viajeros = viajeros;
    }
    
    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(int pasajeros) {
        this.pasajeros = pasajeros;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Viaje getViaje() {
        return viaje;
    }

    public void setViaje(Viaje viaje) {
        this.viaje = viaje;
    }

    public Ocupacion getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupacion ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getUltimoPaso() {
        return ultimoPaso;
    }

    public void setUltimoPaso(String ultimoPaso) {
        this.ultimoPaso = ultimoPaso;
    }
    
    
}
