
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import POJOS.Viaje;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;


public class finalizarViaje extends HttpServlet {
    
    private SessionFactory SessionBuilder;

    public void init() {
        SessionBuilder = HibernateUtil.getSessionFactory();
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            // obtiene los datos
            Integer origen = Integer.parseInt(request.getParameter("origen"));
            Integer destino = Integer.parseInt(request.getParameter("destino"));
            Integer horario = Integer.parseInt(request.getParameter("horario"));
            String fecha = request.getParameter("fecha");
            
            try {
                
                // busca el viaje y lo devuelve con sus datos inicializados
                Viaje viaje = new Operacion().obtenerViaje(SessionBuilder, origen, destino, horario, fecha);
                
                // prepara los objetos backup y los guarda
                new Operacion().guardarBackups(SessionBuilder, viaje);
                
                // borra el viaje original
                new Operacion().eliminarViaje(SessionBuilder, viaje);
                
                // redirecciona a la vista
                response.sendRedirect("./views/cliente/finalizar-viaje.jsp?ok=1");
                
            } catch (HibernateException HE) {
                response.sendRedirect("./views/error.jsp?code=exception&message=" + HE.getMessage());
            }

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
