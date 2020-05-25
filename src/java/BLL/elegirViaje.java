
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.Reserva;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;

public class elegirViaje extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        try{
            
            // comprueba que se ha recibido el parámetro id
            if(request.getParameter("id") != null){
                
                // devuelve la sesión
                HttpSession session = request.getSession(true);
                if(session.getAttribute("reserva") != null){
                    
                    // parsea el id
                    Integer id = Integer.parseInt(request.getParameter("id"));
                    
                    // castea la reserva
                    Reserva reserva = (Reserva) session.getAttribute("reserva");
                    
                    // busca el viaje en la base de datos
                    reserva.setViaje(new Operacion().obtenerViaje(SessionBuilder, id));
                    
                    // indica el siguiente paso
                    reserva.setUltimoPaso("./views/reserva/pasajeros.jsp");
                    response.sendRedirect("./views/reserva/pasajeros.jsp");
                    
                }else{
                    response.sendRedirect("./views/error.jsp?message=La reserva no es válida");
                }
                
            }else{
                response.sendRedirect("./views/error.jsp?code=data-miss");
            }
            
        }catch(IOException e){
            response.sendRedirect("./views/error.jsp?code=exception&message=" + e.getMessage());
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
