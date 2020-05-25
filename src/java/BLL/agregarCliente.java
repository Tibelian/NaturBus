
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.MyHash;
import POJOS.Cliente;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class agregarCliente extends HttpServlet {
    
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
        
        try (PrintWriter out = response.getWriter()) {
            
            // obtiene los datos
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String dni = request.getParameter("dni");
            String telefono = request.getParameter("telefono");
            
            // comprueba que se han recibido todos los datos obligatorios
            if(email != null && password != null && nombre != null && apellidos != null && dni != null && telefono != null){
                
                // inicializa al cliente
                Cliente cliente = new Cliente();
                cliente.setEmail(email);
                cliente.setNombre(nombre);
                cliente.setApellidos(apellidos);
                cliente.setDni(dni);
                cliente.setClave(MyHash.sha1(password));
                
                // con esto se evita que se introduzcan un número inválido
                try{
                    cliente.setTelefono(telefono);
                }catch(NumberFormatException ne){
                    response.sendRedirect("./views/error.jsp?message=El número de teléfono introducido no es válido");
                    return;
                }
                
                try{
                    
                    // comprueba que no exista otra cuenta con el dni o el email indicado
                    if(new Operacion().existeCuenta(SessionBuilder, dni, email)){
                        response.sendRedirect("./views/error.jsp?message=Hemos detectado que ya existe un cliente registrado con ese DNI o EMAIL");
                    }else{
                    
                        // registra la cliente en la base de datos
                        new Operacion().crearCuenta(SessionBuilder, cliente);
                        
                        // redirecciona al servlet de login para iniciar sesión automáticamente
                        RequestDispatcher rd = request.getRequestDispatcher("./obtenerCliente?pagar=1");
                        rd.forward(request, response);

                    }
                    
                }catch(HibernateException ex){
                    // muestra la exception en la vista de error
                    response.sendRedirect("./views/error.jsp?code=exception&message=" + ex.getMessage());
                }
                
            }else{
                // indica que faltan datos por introducir
                response.sendRedirect("./views/error.jsp?code=data-miss");
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
