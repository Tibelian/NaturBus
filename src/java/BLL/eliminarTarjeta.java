
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import POJOS.Cliente;
import POJOS.Tarjeta;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class eliminarTarjeta extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        // obtiene el id de la tarjeta
        // que se va a eliminar
        String sId = request.getParameter("id");
        if(sId != null){

            // transfroma String en Integer
            int id = Integer.parseInt(sId);
            try{

                // obtiene la sesión y comprueba que 
                // el cliente haya iniciado sesión
                HttpSession session = request.getSession(true);
                if(session.getAttribute("cliente") != null){

                    // busca que el id de tarjeta indicado 
                    // pertenezca a una tarjeta del mismo cliente
                    // y que no sea de otro cliente
                    Cliente cliente = (Cliente) session.getAttribute("cliente");
                    Tarjeta tarjeta = null;
                    Iterator itTarjetas = cliente.getTarjetas().iterator();
                    while(itTarjetas.hasNext()){
                        Tarjeta xTarjeta = (Tarjeta)itTarjetas.next();
                        if(xTarjeta.getId() == id){
                            tarjeta = xTarjeta;
                            break;
                        }
                    }
                    
                    // verifica que se haya encontrado la tarjeta
                    if(tarjeta != null){

                        // elimina de la base de datos la tarjeta indicada
                        new Operacion().eliminarTarjeta(SessionBuilder, tarjeta);
                        
                        // se actualiza el cliente
                        Cliente nCliente = new Operacion().iniciarSesion(SessionBuilder, cliente.getEmail(), cliente.getClave());
                        session.setAttribute("cliente", nCliente);
                        
                        // redirecciona a la vista
                        response.sendRedirect("./");

                    }else{
                        response.sendRedirect("./views/error.jsp?message=NO SE HA ENCONTRADO LA TARJETA QUE SE DESEA ELIMINAR");
                    }

                }else{
                    response.sendRedirect("./views/error.jsp?message=DEBES INICIAR SESIÓN PARA REALIZAR ESTA OPERACIÓN");
                }

            }catch(HibernateException | BusException he){
                response.sendRedirect("./error.jsp?code=exception&message=" + he.getMessage());
            }

        }else{
            response.sendRedirect("./error.jsp?code=data-miss");
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
