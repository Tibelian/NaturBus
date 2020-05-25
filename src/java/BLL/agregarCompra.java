
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.Localizador;
import MODEL.Reserva;
import MODEL.ViajeroAsiento;
import POJOS.Cliente;
import POJOS.Compra;
import POJOS.Ocupacion;
import POJOS.Tarjeta;
import POJOS.Viajero;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class agregarCompra extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // establece la codificación utf8
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
            
        // comprueba que se conoce la tarjeta
        // con la cual se ha realizado el pago
        if (request.getParameter("id") != null) {

            // parsea el id a entero
            int id = Integer.parseInt(request.getParameter("id"));
            
            // recoge la sesión
            HttpSession session = request.getSession(true);
            
            // comprueba que el cliente haya iniciado sesión
            if (session.getAttribute("cliente") != null) {
                
                // comprueba que la reserva exista
                if (session.getAttribute("reserva") != null) {

                    // prepara los objetos
                    Cliente cliente = (Cliente) session.getAttribute("cliente");
                    Reserva reserva = (Reserva) session.getAttribute("reserva");
                    Compra compra = new Compra();

                    // fecha actual
                    compra.setFecha(new Date());

                    // precio total
                    compra.setImporte(reserva.getViajeros().size() * reserva.getRuta().getPrecio());

                    // recoge el viaje
                    compra.setViaje(reserva.getViaje());

                    // establece cantidad de pasajeros
                    compra.setPasajeros(reserva.getViajeros().size());

                    // genera un string aleatorio de 8 carácteres
                    compra.setLocalizador(Localizador.generar(8));

                    // inserta los viajeros con sus ocupaciones
                    for (ViajeroAsiento viajeroAsiento : reserva.getViajeros()) {
                        
                        // crea al viajero
                        Viajero viajero = new Viajero();
                        viajero.setApellidos(viajeroAsiento.getApellidos());
                        viajero.setNombre(viajeroAsiento.getNombre());
                        viajero.setDni(viajeroAsiento.getDni());
                        
                        // busca en la bd si ya existe el viajero para que haga update 
                        // y no salte la excepción de duplicate entry
                        // si no existe el viajero el id será null
                        Integer idViajero = new Operacion().buscaIdViajero(SessionBuilder, viajero.getDni());
                        viajero.setId(idViajero);
                        
                        // crea la ocupación
                        Ocupacion ocupacion = new Ocupacion();
                        ocupacion.setAsiento(viajeroAsiento.getAsiento());
                        ocupacion.setViajero(viajero);
                        ocupacion.setImporte(reserva.getRuta().getPrecio());
                        ocupacion.setCompra(compra);
                        
                        // añade la ocupación
                        compra.getOcupacions().add(ocupacion);
                        
                    }

                    // resta plazas disponibles
                    compra.getViaje().setPlazasDisponibles(
                        compra.getViaje().getPlazasDisponibles() - compra.getPasajeros()
                    );

                    // selecciona la tarjeta con la que se ha realizado la compra
                    Iterator itTarjetas = cliente.getTarjetas().iterator();
                    while (itTarjetas.hasNext()) {
                        Tarjeta xTarjeta = (Tarjeta) itTarjetas.next();
                        if (xTarjeta.getId() == id) {
                            compra.setTarjeta(xTarjeta);
                            break;
                        }
                    }

                    try {

                        // guarda el objeto compra en la base de datos
                        new Operacion().guardarCompra(SessionBuilder, compra);
                        
                        // crea la sesión con la compra finalizada
                        session.setAttribute("compra", compra);
                        
                        // indica y redirecciona a la vista final
                        reserva.setUltimoPaso("./views/reserva/completada.jsp");
                        response.sendRedirect("./");

                    } catch(HibernateException ex) {
                        response.sendRedirect("./views/error.jsp?code=exception&message=" + ex.getMessage());
                    }

                } else {
                    response.sendRedirect("./views/error.jsp?message=La reserva no es válida");
                }
            } else {
                response.sendRedirect("./views/error.jsp?message=Debes iniciar sesión para seguir");
            }

        } else {
            response.sendRedirect("./views/error.jsp?message=Falta indicar la tarjeta");
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
