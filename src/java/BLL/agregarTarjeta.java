
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import POJOS.Cliente;
import POJOS.Tarjeta;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class agregarTarjeta extends HttpServlet {
    
    private SessionFactory SessionBuilder;
    
    @Override
    public void init(){
        SessionBuilder = HibernateUtil.getSessionFactory();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // codificación utf8
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        // obtiene los datos
        String numero = request.getParameter("numero");
        String caducidad = request.getParameter("caducidad");
        String cvv = request.getParameter("cvv");
        String tipo = request.getParameter("tipo");
        
        // comprueba que se han recibido los datos obligatorios
        if(numero != null && caducidad != null && cvv != null && tipo != null){

            // prepara el objeto tarjeta
            Tarjeta tarjeta = new Tarjeta();
            Date fechaCaducidad;
            
            // convierte la fecha
            try{
                fechaCaducidad = new SimpleDateFormat("yyyy-MM").parse(caducidad);
            } catch (ParseException ex) {
                response.sendRedirect("./views/error.jsp?code=exception&message=" + ex.getMessage());
                return;
            }
            
            // elimina los espacios del numero e introduce los datos al objeto
            tarjeta.setNumero(numero.replace(" ", "").getBytes(StandardCharsets.UTF_8));
            tarjeta.setCvv(Integer.parseInt(cvv));
            tarjeta.setTipo(tipo);
            tarjeta.setCaducidad(fechaCaducidad);

            // prepara la sesión
            HttpSession session = request.getSession(true);
            
            // comprueba que el cliente haya iniciado sesión
            if(session.getAttribute("cliente") != null){

                // indica quién es el propietario de la tarjeta
                Cliente cliente = (Cliente) session.getAttribute("cliente");
                tarjeta.setCliente(cliente);

                try{

                    // guarda la tarjeta en la base de datos
                    new Operacion().agregarTarjeta(SessionBuilder, tarjeta);

                    // se actualiza el cliente actual
                    Cliente nuevoCliente = new Operacion().iniciarSesion(SessionBuilder, cliente.getEmail(), cliente.getClave());
                    session.setAttribute("cliente", nuevoCliente);
                    
                    // obtiene el id de la tarjeta recién agregada
                    Integer idTarjeta = null;
                    Iterator itCTarjeta = nuevoCliente.getTarjetas().iterator();
                    while(itCTarjeta.hasNext()){
                        Tarjeta cTarjeta = (Tarjeta) itCTarjeta.next();
                        String numTarjeta = new String(tarjeta.getNumero(), StandardCharsets.UTF_8);
                        String numTarjetaC = new String(cTarjeta.getNumero(), StandardCharsets.UTF_8);
                        if(numTarjeta.equals(numTarjetaC)){
                            idTarjeta = cTarjeta.getId();
                        }
                    }
                    
                    // se confirma la compra
                    RequestDispatcher rd = request.getRequestDispatcher("./agregarCompra?id=" + idTarjeta);
                    rd.forward(request, response);

                }catch(HibernateException | BusException he){
                    response.sendRedirect("./views/error.jsp?code=exception&message=" + he.getMessage());
                }

            }else{
                response.sendRedirect("./views/error.jsp?message=Debes iniciar sesión para seguir");
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
