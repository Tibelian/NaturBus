
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import MODEL.MyHash;
import POJOS.Compra;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;


public class anularReserva extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // codificación para recibir bien las tildes
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        try {
            
            // recibe los datos y comprueba que no falte ninguno
            String dni = request.getParameter("dni");
            String clave = request.getParameter("clave");
            String localizador = request.getParameter("localizador");
            if(dni != null && clave != null && localizador != null){
                
                // comprueba que existe el cliente con ese dni y contraseña
                new Operacion().iniciarSesion(SessionBuilder, dni, MyHash.sha1(clave));
                
                // devuelve el objeto compra que tenga ese localizador
                Compra compra = new Operacion().obtenerCompra(SessionBuilder, localizador);
                
                // comprueba que la compra sea válida
                if(compra.getId() != null){
                    
                    // se elimina
                    // 1. Compra
                    // 2. Ocupaciones
                    // 3. Viajeros
                    
                    // se actualiza
                    // 1. plazas disponibles
                    
                    new Operacion().anularReserva(SessionBuilder, compra);
                    response.sendRedirect("./views/cliente/anular-reserva.jsp?ok=1");
                    
                    
                }else{
                    response.sendRedirect("./views/error.jsp?message=No existe ninguna reserva que tenga ese localizador");
                }
                
            }else{
                response.sendRedirect("./views/error.jsp?code=data-miss");
            }
            
        } catch (BusException ex) {
            response.sendRedirect("./views/error.jsp?code=no-user");
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
