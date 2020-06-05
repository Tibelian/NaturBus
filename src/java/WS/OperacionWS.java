
package WS;

import DAO.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class OperacionWS {

    private SessionFactory sessionBuilder;

    public void init() {
        sessionBuilder = HibernateUtil.getSessionFactory();
    }

    ArrayList<Ruta> getListadoRutas() {
        
        init();
        
        Session sesion = sessionBuilder.openSession();
        Transaction Tx = null;
        try {
            
            // ejecuta la consulta y devuelve un listado
            Tx = sesion.beginTransaction();
            String ordenHQL = "from Ruta";
            Query q = sesion.createQuery(ordenHQL);
            List listadoRutas = q.list();
            
            // prepara array que se devolverá
            ArrayList<Ruta> listadoFinal = new ArrayList<>();
            
            // recorre todas las rutas
            Iterator itRuta = listadoRutas.iterator();
            while (itRuta.hasNext()) {
                POJOS.Ruta rutaPOJOS = (POJOS.Ruta) itRuta.next();

                // inicializa los horarios y los recorre
                Hibernate.initialize(rutaPOJOS.getHorarios());
                Iterator itHorario = rutaPOJOS.getHorarios().iterator();
                while (itHorario.hasNext()) {
                    POJOS.Horario horarioPOJOS = (POJOS.Horario) itHorario.next();

                    // le da formato a la hora de salida
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    String horaSalida = formatter.format(horarioPOJOS.getHora());
           
                    // inicializa los viajes y los recoorre
                    Hibernate.initialize(horarioPOJOS.getViajes());
                    Iterator itViaje = horarioPOJOS.getViajes().iterator();
                    while (itViaje.hasNext()) {
                        POJOS.Viaje viajePOJOS = (POJOS.Viaje) itViaje.next();

                        // le da formato a la fecha del viaje
                        SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
                        String fechaViaje = formatter2.format(viajePOJOS.getFecha());
                        String precio = String.valueOf(rutaPOJOS.getPrecio());
                        
                        // aquí se guardarán las ocupaciones
                        ArrayList<Integer> ocupaciones = new ArrayList<>();
                        
                        // ahora toca obtener los asientos libres
                        // por lo tanto vamos a recoger los ocupados
                        Hibernate.initialize(viajePOJOS.getCompras());
                        Iterator itCompra = viajePOJOS.getCompras().iterator();
                        while(itCompra.hasNext()){
                            POJOS.Compra compraPOJOS = (POJOS.Compra) itCompra.next();
                            
                            // aquí se encuentra el asiento ocupado
                            Hibernate.initialize(compraPOJOS.getOcupacions());
                            Iterator itOcupacion = compraPOJOS.getOcupacions().iterator();
                            while(itOcupacion.hasNext()){
                                POJOS.Ocupacion ocupacionPOJOS = (POJOS.Ocupacion) itOcupacion.next();
                                
                                // agrega el asiento ocupado al listado
                                ocupaciones.add(ocupacionPOJOS.getAsiento());
                                
                            }
                            
                        }
                        
                        // calcula los asientos libres
                        ArrayList<Integer> libres = new ArrayList<>();
                        for(int i = 1; i <= viajePOJOS.getPlazasTotales(); i++){
                            boolean encontrado = false;
                            for(int n = 0; n < ocupaciones.size(); n++){
                                if(i == ocupaciones.get(n)){
                                    encontrado = true;
                                }
                            }
                            if(!encontrado){
                                libres.add(i);
                            }
                        }
                        
                        // inicializa la ruta del servico web
                        Ruta ruta = new Ruta();
                        ruta.setOrigen(rutaPOJOS.getEstacionByIdOrigen().getNombre());
                        ruta.setDestino(rutaPOJOS.getEstacionByIdDestino().getNombre());
                        ruta.setFecha(fechaViaje);
                        ruta.setHoraSalida(horaSalida);
                        ruta.setPlazasDisponibles(Integer.toString(viajePOJOS.getPlazasDisponibles()));
                        ruta.setPlazasTotales(Integer.toString(viajePOJOS.getPlazasTotales()));
                        ruta.setPrecio(precio);
                        ruta.setAsientosLibres(libres);
                        
                        // agrega la ruta al listado
                        listadoFinal.add(ruta);
                        
                    }

                }
                
            }
            
            // devuelve el listado
            return listadoFinal;

        } catch (HibernateException HE) {
            HE.printStackTrace();
            if (Tx != null) {
                Tx.rollback();
            }
            throw HE;
        } finally {
            sesion.close();
        }


    }

}
