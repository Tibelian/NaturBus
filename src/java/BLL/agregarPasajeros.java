
package BLL;

import DAO.HibernateUtil;
import MODEL.Reserva;
import MODEL.ViajeroAsiento;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;

public class agregarPasajeros extends HttpServlet {

    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // codificación utf8 para que guarde las tildes bien
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // recoge la reserva de la sesión
        HttpSession session = request.getSession(true);
        Reserva reserva = (Reserva) session.getAttribute("reserva");
        
        // inicializa el arryalist de viajeros
        reserva.setViajeros(new ArrayList<>());
        
        // recorre todos los pasajeros de la reserva y obtiene sus datos
        for(int i = 1; i <= reserva.getPasajeros(); i++){
            
            // recoge los parametros
            String nombre = request.getParameter("pasajero" + i + "-nombre");
            String apellidos = request.getParameter("pasajero" + i + "-apellidos");
            String dni = request.getParameter("pasajero" + i + "-dni");
            Integer asiento = Integer.parseInt(request.getParameter("pasajero" + i + "-asiento"));
            
            // crea el objeto viajero
            ViajeroAsiento viajero = new ViajeroAsiento();
            viajero.setNombre(nombre);
            viajero.setApellidos(apellidos);
            viajero.setDni(dni);
            viajero.setAsiento(asiento);
            
            // guarda al viajero en la reserva
            reserva.getViajeros().add(viajero);
            
        }
        
        // una vez completado redirecciona al siguiente paso
        // siendo este el resumen de la reserva
        reserva.setUltimoPaso("./views/reserva/resumen.jsp");
        response.sendRedirect("./views/reserva/resumen.jsp");
        
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
