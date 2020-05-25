
package DAO;

import MODEL.BusException;
import POJOS.Administrador;
import POJOS.BackupCompra;
import POJOS.BackupOcupacion;
import POJOS.BackupViaje;
import POJOS.BackupViajero;
import POJOS.Cliente;
import POJOS.Compra;
import POJOS.Horario;
import POJOS.Ocupacion;
import POJOS.Ruta;
import POJOS.Tarjeta;
import POJOS.Viaje;
import POJOS.Viajero;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Operacion {

    
    
    // aqui se reciben todas las estaciones en un List
    // se usa para cargar el select de origen en el buscador
    public List obtenerOrigen(SessionFactory sessionBuilder){
        Session session = sessionBuilder.openSession();
        String hql = "from Estacion";
        Query query = session.createQuery(hql);
        List estaciones = query.list();
        session.close();
        return estaciones;
    }
    
    // devuelve todas las estaciones donde no tengan el id indicado
    // esto se usa en para el ajax en el buscador
    public List obtenerDestino(SessionFactory sessionBuilder, int id){
        Session session = sessionBuilder.openSession();
        String hql = "from Estacion where id != :id";
        Query query = session.createQuery(hql).setParameter("id", id);
        List estaciones = query.list();
        session.close();
        return estaciones;
    }
    
    
    
   
    ///////////////////////////////////////////
    // OPERACIONES PARA COMPLETAR LA RESERVA //
    ///////////////////////////////////////////
    
    
    // este método me devuelve un objeto Ruta con todos
    // sus viajes que tenga el origen y el destino especificado
    public Ruta obtenerRuta(SessionFactory sessionBuilder, int origen, int destino) throws BusException{
        
        // se conecta
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        
        try{
           
            // realiza la consulta hql
            tx = session.beginTransaction();
            String hql = "from Ruta where id_origen = :origen and id_destino = :destino";
            Query query = session.createQuery(hql);
            query.setParameter("origen", origen);
            query.setParameter("destino", destino);
            Ruta ruta = (Ruta)query.uniqueResult();

            if(ruta == null){
                throw new BusException(404, "No se han encontrado rutas con el origen y el destino indicado");
            }
            
            // esto para que el ORM devuelva los datos de los todos los objetos
            Hibernate.initialize(ruta.getEstacionByIdOrigen());
            Hibernate.initialize(ruta.getEstacionByIdDestino());
            Hibernate.initialize(ruta.getHorarios());

            // recorre todos los horarios para inicializar todos sus viajes
            Iterator horarios = ruta.getHorarios().iterator();
            while(horarios.hasNext()){
                Horario horario = (Horario)horarios.next();
                Hibernate.initialize(horario.getViajes());
            }
            
            tx.commit();

            // devuelve la ruta con todos sus datos
            return ruta;
            
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
        
    }
    
    // devuelve el objeto Viaje sabiendo el id
    // también se inicializan sus objetos
    public Viaje obtenerViaje(SessionFactory sessionBuilder, int id){
        Session session = sessionBuilder.openSession();
        String hql = "from Viaje where id = :id";
        Query query = session.createQuery(hql);
        query.setParameter("id", id);
        Viaje viaje = (Viaje)query.uniqueResult();
        Hibernate.initialize(viaje.getHorario());
        Hibernate.initialize(viaje.getCompras());
        Iterator compras = viaje.getCompras().iterator();
        while(compras.hasNext()){
            Compra compra = (Compra)compras.next();
            Hibernate.initialize(compra.getOcupacions());
        }
        session.close();
        return viaje;
    }
    
    // devuelve el id del viaje indicando su dni siendo este unique
    public Integer buscaIdViajero(SessionFactory sessionBuilder, String dni){
        Session session = sessionBuilder.openSession();
        String hql = "from Viajero where dni = :dni";
        Query query = session.createQuery(hql).setParameter("dni", dni);
        Viajero viajero = (Viajero)query.uniqueResult();
        session.close();
        if(viajero != null){
            return viajero.getId();
        }else{
            return null;
        }
    }
    
    // devuelve el objeto compra indicando el localizador
    public Compra obtenerCompra(SessionFactory sessionBuilder, String localizador){
        
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            
            tx = session.beginTransaction();
            String hql = "from Compra where localizador = :localizador";
            Query query = session.createQuery(hql).setParameter("localizador", localizador);
            Compra compra = (Compra) query.uniqueResult();
            tx.commit();
            
            if(compra != null){
                
                // recupera los datos
                Hibernate.initialize(compra.getViaje());
                Hibernate.initialize(compra.getOcupacions());
                Iterator itOcupacion = compra.getOcupacions().iterator();
                while(itOcupacion.hasNext()){
                    Ocupacion ocupacion = (Ocupacion) itOcupacion.next();
                    Hibernate.initialize(ocupacion.getViajero());
                }
                return compra;
                
            }else{
                return new Compra();
            }
            
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
        
    }
    
    // devuelve el objeto compra indicando el localizador
    public void anularReserva(SessionFactory sessionBuilder, Compra compra){
        
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            
            // prepara la transacion
            tx = session.beginTransaction();
            
            // se actualizan las plazas disponibles
            Integer nuevasPlazas = compra.getViaje().getPlazasDisponibles() + compra.getPasajeros();
            compra.getViaje().setPlazasDisponibles(nuevasPlazas);
            session.saveOrUpdate(compra.getViaje());
            
            // se elimina la compra y las ocupaciones
            session.delete(compra);
            
            // se eliminan los viajeros por separado ya que el XML
            // de los POJOS tiene un cascade="save-update" y no cascade="all"
            Iterator itOcupacion = compra.getOcupacions().iterator();
            while(itOcupacion.hasNext()){
                Ocupacion ocupacion = (Ocupacion) itOcupacion.next();
                session.delete(ocupacion.getViajero());
            }
            
            // confirma los cambios
            tx.commit();
            
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
        
    }
    
    // inserta la compra y actualiza los viajeros si ya existen
    public void guardarCompra(SessionFactory sessionBuilder, Compra compra) throws HibernateException{
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(compra);
            session.getTransaction().commit();
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
    }
    
    
    
    ////////////////////////////////////
    // GESTIONA LOS DATOS DEL CLIENTE //
    ////////////////////////////////////
    
    
    // obtiene al cliente que tenga la contraseña y el email/dni indicado
    public Cliente iniciarSesion(SessionFactory sessionBuilder, String email, String password) throws BusException{
        Session session = sessionBuilder.openSession();
        String hql = "from Cliente where (email = :email or dni = :email) and clave = :password";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("password", password);
        Cliente cliente = (Cliente)query.uniqueResult();
        if(cliente == null){
            throw new BusException(404, "No se han encontrado el cliente");
        }
        Hibernate.initialize(cliente.getTarjetas());
        session.close();
        return cliente;
    }
    
    // obtiene al admin que tenga la contraseña y el email indicado
    public Administrador iniciarSesionAdmin(SessionFactory sessionBuilder, String email, String password){
        Session session = sessionBuilder.openSession();
        String hql = "from Administrador where email = :email and clave = :password";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("password", password);
        Administrador admin = (Administrador)query.uniqueResult();
        session.close();
        return admin;
    }
    
    // comprueba si existe un cliente con el email o el dni indicado
    public boolean existeCuenta(SessionFactory sessionBuilder, String dni, String email){
        Session session = sessionBuilder.openSession();
        String hql = "from Cliente where dni = :dni OR email = :email";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("dni", dni);
        Cliente cliente = (Cliente)query.uniqueResult();
        session.close();
        return (cliente != null);
    }
    
    // añade en la base de datos un cliente
    public void crearCuenta(SessionFactory sessionBuilder, Cliente cliente) throws HibernateException{
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(cliente);
            session.getTransaction().commit();
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
    }
    
    // añade una tarjeta nueva
    public void agregarTarjeta(SessionFactory sessionBuilder, Tarjeta tarjeta) throws HibernateException{
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(tarjeta);
            session.getTransaction().commit();
        }catch(HibernateException HE){
            
            if(tx != null){
                tx.rollback();
            }
            throw HE;
            
        }finally{
            session.close();
        }
    }
    
    // elimina una tarjeta
    public void eliminarTarjeta(SessionFactory sessionBuilder, Tarjeta tarjeta) throws HibernateException{
        Session session = sessionBuilder.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.delete(tarjeta);
            session.getTransaction().commit();
        }catch(HibernateException HE){
            if(tx != null){
                tx.rollback();
            }
            throw HE;
        }finally{
            session.close();
        }
    }
    
    
    
    /////////////////////////
    // GESTIONA LOS BACKUP //
    /////////////////////////
    
    
    // devuelve un viaje con todos sus datos inicializados
    public Viaje obtenerViaje(SessionFactory sessionBuilder, int idOigen, int idDestino, int idHorario, String fecha) {
        Session session = sessionBuilder.openSession();
        Transaction Tx = null;
        try {
            
            // se prepara el viaje que se devolverá
            Viaje viaje = new Viaje();
            
            Tx = session.beginTransaction();
            String ordenHQL = "from Ruta R where R.estacionByIdOrigen = :origen and R.estacionByIdDestino = :destino";
            Query q = session.createQuery(ordenHQL);
            q.setParameter("origen", idOigen);
            q.setParameter("destino", idDestino);
            
            // devuelve la ruta encontrada
            Ruta ruta = (Ruta) q.uniqueResult();

            // inicializa los foreign
            Hibernate.initialize(ruta.getEstacionByIdOrigen());
            Hibernate.initialize(ruta.getEstacionByIdDestino());
            Hibernate.initialize(ruta.getHorarios());
            
            // recorre todos los horario
            Iterator itHorario = ruta.getHorarios().iterator();
            while (itHorario.hasNext()) {
                Horario horario = (Horario) itHorario.next();

                // comprueba que el horario sea el mismo
                if (horario.getId() == idHorario) {
                    
                    // inicializa los viajes
                    Hibernate.initialize(horario.getViajes());
                    Hibernate.initialize(horario.getBackupViajes());
                
                    Iterator itViaje = horario.getViajes().iterator();
                    while (itViaje.hasNext()) {
                        viaje = (Viaje) itViaje.next();
                        
                        // se formatea la fecha
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String fechaViaje = formatter.format(viaje.getFecha());

                        // se comprueba que sea la indicada
                        if (fechaViaje.equals(fecha)) {

                            // inicializa sus compras y las recorre para inicializar sus foreign
                            Hibernate.initialize(viaje.getCompras());
                            Iterator itCompra = viaje.getCompras().iterator();
                            while (itCompra.hasNext()) {
                                
                                Compra compra = (Compra) itCompra.next();
                                Hibernate.initialize(compra.getTarjeta());
                                Hibernate.initialize(compra.getOcupacions());
                                
                                // recorre sus ocupaciones para inicializar sus viajeros
                                Iterator itOcupacion = compra.getOcupacions().iterator();
                                while (itOcupacion.hasNext()) {
                                    Ocupacion ocupacion = (Ocupacion) itOcupacion.next();
                                    Hibernate.initialize(ocupacion.getViajero());
                                }

                            }
                        }

                    }
                    
                }

            }

            // devuelve el viaje con todos sus datos inicializados
            return viaje;

        } catch (HibernateException HE) {
            HE.printStackTrace();
            if (Tx != null) {
                Tx.rollback();
            }
            throw HE;
        } finally {
            session.close();
        }

    }

    // los objetos originales los transforma en backup y los guarda en la base de datos
    // como anteriormente se debería ejecutar obtenerViaje no hace falta inicializar 
    // otra vez los foreign
    public void guardarBackups(SessionFactory sessionBuilder, Viaje viaje) {
        Session session = sessionBuilder.openSession();
        Transaction Tx = null;
        try {

            // listado con backups
            Set<BackupCompra> backupCompraSet = new HashSet<>();
            Set<BackupOcupacion> backupOcupacionSet = new HashSet<>();
            Set<BackupViaje> backupViajeSet = new HashSet<>();
            
            // prepara fecha cuando se realiza el bakcup
            Date fechaBaja = new Date();
            
            // backup viaje preparado --------
            BackupViaje backupViaje = new BackupViaje();
            backupViaje.setFecha(viaje.getFecha());
            backupViaje.setHorario(viaje.getHorario());
            backupViaje.setPlazas(viaje.getPlazasDisponibles());
            backupViaje.setFechaBaja(fechaBaja);
            backupViaje.setBackupCompras(backupCompraSet);

            // recorre todas las compras del viaje
            Iterator itCompra = viaje.getCompras().iterator();
            while (itCompra.hasNext()) {
                Compra compra = (Compra) itCompra.next();

                // backup compra preparada --------
                BackupCompra backupCompra = new BackupCompra();
                backupCompra.setTarjeta(compra.getTarjeta());
                backupCompra.setLocalizador(compra.getLocalizador());
                backupCompra.setImporte(compra.getImporte());
                backupCompra.setFechaBaja(fechaBaja);
                backupCompra.setBackupViaje(backupViaje);
                backupCompra.setBackupOcupacions(backupOcupacionSet);
                
                // recorre todas las ocupaciones de la compra
                Iterator itOcupacion = compra.getOcupacions().iterator();
                while (itOcupacion.hasNext()) {
                    Ocupacion ocupacion = (Ocupacion) itOcupacion.next();

                    // backup viajero preparada --------
                    Viajero viajero = ocupacion.getViajero();
                    BackupViajero backupViajero = new Operacion().obtenerBackupViajero(sessionBuilder, viajero.getDni());
                    if (backupViajero.getId() == null) {
                        backupViajero = new BackupViajero();
                        backupViajero.setDni(viajero.getDni());
                        backupViajero.setNombre(viajero.getNombre());
                        backupViajero.setApellidos(viajero.getApellidos());
                        backupViajero.setFechaBaja(fechaBaja);
                        backupViajero.setBackupOcupacions(backupOcupacionSet);
                    }
                    
                    // backup ocupacion preparada --------
                    BackupOcupacion backupOcupacion = new BackupOcupacion();
                    backupOcupacion.setBackupCompra(backupCompra);
                    backupOcupacion.setBackupViajero(backupViajero);
                    backupOcupacion.setAsiento(ocupacion.getAsiento());
                    backupOcupacion.setImporte(ocupacion.getImporte());
                    backupOcupacion.setFechaBaja(fechaBaja);
                    
                    // añade la ocupacion al backup compra
                    backupOcupacionSet.add(backupOcupacion);
                    
                    // añade el backup compra al listado de compras
                    backupCompraSet.add(backupCompra);
                    
                }
            }

            // recoge los backups antiguos y añade el nuevo
            backupViajeSet = viaje.getHorario().getBackupViajes();
            backupViajeSet.add(backupViaje);
            viaje.getHorario().setBackupViajes(backupViajeSet);

            // ejecuta la consulta
            // como los xml de los pojos llevan
            // cascade en cada elemento foreign
            // se guarda todo lo demás al mismo tiempo
            Tx = session.beginTransaction();
            session.saveOrUpdate(backupViaje);
            Tx.commit();
            
        } catch (HibernateException HE) {
            HE.printStackTrace();
            if (Tx != null) {
                Tx.rollback();
            }
            throw HE;
        } finally {
            session.close();
        }
    }
    
    // busca un viajero en el bakcup que tenga el dni indicado
    // si no se encuentra devuelve un viajero vacío
    public BackupViajero obtenerBackupViajero(SessionFactory sessionBuilder, String dni) {
        Session session = sessionBuilder.openSession();
        Transaction Tx = null;
        try {
            
            Tx = session.beginTransaction();
            String ordenHQL = "from BackupViajero where dni = :dni";
            Query q = session.createQuery(ordenHQL);
            q.setParameter("dni", dni);
            BackupViajero viajero = (BackupViajero) q.uniqueResult();

            if (viajero == null) {
                return new BackupViajero();
            } else {
                return viajero;
            }

        } catch (HibernateException HE) {
            HE.printStackTrace();
            if (Tx != null) {
                Tx.rollback();
            }
            throw HE;
        } finally {
            session.close();

        }

    }

    // elimina un viaje con todos sus viajeros
    public void eliminarViaje(SessionFactory sessionBuilder, Viaje viaje) {
        
        Session session = sessionBuilder.openSession();
        Transaction Tx = null;
        try {
            
            // prepara una transación
            Tx = session.beginTransaction();
            
            // elimina el viaje
            // como un viaje tiene cascade en compra
            // este objeto también se borrará automáticamente
            // y así sucesivamente con todos los cascade que tiene compra
            session.delete(viaje);

            // devuelve todos los viajeros que se han quedado sin ocupación
            // ya que la ocupación se habrá borrado justo por el cascade de viaje -> compra -> set ocupación
            String ordenHQL = "from Viajero V where V.id NOT IN(select O.viajero from Ocupacion O)";
            Query q = session.createQuery(ordenHQL);
            ArrayList<Viajero> viajeros = (ArrayList) q.list();

            // los recorre y elimina uno por uno
            for (Viajero viajero : viajeros) {
                session.delete(viajero);
            }

            // confirma todos los cambios
            Tx.commit();
            
        } catch (HibernateException HE) {
            HE.printStackTrace();
            if (Tx != null) {
                Tx.rollback();
            }
            throw HE;
        } finally {
            session.close();
        }
        
    }
    
    
    
}
