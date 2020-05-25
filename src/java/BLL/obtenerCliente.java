
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import MODEL.MyHash;
import MODEL.Reserva;
import POJOS.Administrador;
import POJOS.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.SessionFactory;

public class obtenerCliente extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // codificación utf8 para evitar errores de carácteres
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
            
        // comprueba que se hayan introducido los datos obligatorios
        if(request.getParameter("email") != null && request.getParameter("password") != null){

            // obtiene los datos
            String email = request.getParameter("email");
            String password = MyHash.sha1(request.getParameter("password"));

            // prepara la sesión
            HttpSession session = request.getSession(true);

            // se utlizará para gestionar la sesión
            // de un cliente o un admin si el primero no existe
            boolean ok = false;

            try{

                // busca en la base de datos el cliente
                Cliente cliente = new Operacion().iniciarSesion(SessionBuilder, email, password);
                session.setAttribute("cliente", cliente);
                ok = true;

            }catch(BusException ex){

                // si salta la exception significa que no existe
                // ningún cliente con esos datos, por lo tanto,
                // se buscará un administrador que coincida
                if(ex.getCode() == 404){

                    Administrador admin = new Operacion().iniciarSesionAdmin(SessionBuilder, email, password);
                    if(admin != null){
                        session.setAttribute("administrador", admin);
                        ok = true;
                    }

                }else{
                    // este sería un caso extraño
                    response.sendRedirect("./views/error.jsp?code=exception&message=" + ex.getMessage());
                }

            }

            if(ok){
                // sesión iniciada con éxito 
                if(request.getParameter("pagar") != null){
                    Reserva reserva = (Reserva) session.getAttribute("reserva");
                    reserva.setUltimoPaso("./views/reserva/pagar.jsp");
                }
                response.sendRedirect("./");
            }else{
                response.sendRedirect("./views/error.jsp?message=Los datos que has introducido son incorrectos");
            }

        }else{
            response.sendRedirect("./views/error.jsp?code=data-miss");
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
