
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import MODEL.Reserva;
import POJOS.Ruta;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;

public class obtenerRuta extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            // comprueba que ha recibido los parámetros
            // origen, destino, salida y pasajeros
            if(
                request.getParameter("origen") != null && 
                request.getParameter("destino") != null && 
                request.getParameter("fecha") != null && 
                request.getParameter("pasajeros") != null
            ){

                // recibe los datos y los parsea
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaSalida = formatter.parse(request.getParameter("fecha"));
                int pasajeros = Integer.parseInt(request.getParameter("pasajeros"));
                int idOrigen = Integer.parseInt(request.getParameter("origen"));
                int idDestino = Integer.parseInt(request.getParameter("destino"));

                // devuelve la ruta
                Ruta ruta = new Operacion().obtenerRuta(SessionBuilder, idOrigen, idDestino);

                // se crea la reserva
                Reserva reserva = new Reserva(ruta, fechaSalida, pasajeros);
                reserva.setUltimoPaso("viajes.jsp");

                // se guarda la reserva en la sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("reserva", reserva);
                reserva.setUltimoPaso("./views/reserva/viajes.jsp");

                // redirecciona a la vista
                response.sendRedirect("./views/reserva/viajes.jsp");

            }else{
                response.sendRedirect("./views/error.jsp?message=Debes especificar todos los datos");
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(obtenerRuta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusException ex) {
            response.sendRedirect("./views/error.jsp?message=" + ex.getMessage());
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
