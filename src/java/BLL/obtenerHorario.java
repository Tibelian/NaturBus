
package BLL;

import DAO.HibernateUtil;
import DAO.Operacion;
import MODEL.BusException;
import POJOS.Horario;
import POJOS.Ruta;
import POJOS.Viaje;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

public class obtenerHorario extends HttpServlet {

    private SessionFactory SessionBuilder;

    @Override
    public void init() {
        SessionBuilder = HibernateUtil.getSessionFactory();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            // obtenemos los parametros
            Integer origen = Integer.parseInt(request.getParameter("origen"));
            Integer destino = Integer.parseInt(request.getParameter("destino"));
            String fecha = request.getParameter("fecha");

            // buscamos la ruta
            Ruta ruta = new Operacion().obtenerRuta(SessionBuilder, origen, destino);

            // aquí se guardarán los horarios válidos
            ArrayList<Horario> horarios = new ArrayList<>();

            // recorremos todos los horarios
            Iterator itHorario = ruta.getHorarios().iterator();
            while (itHorario.hasNext()) {
                Horario horario = (Horario) itHorario.next();
                
                // recorremos todos los viajes
                Iterator itViaje = horario.getViajes().iterator();
                while (itViaje.hasNext()) {
                    Viaje viaje = (Viaje) itViaje.next();

                    // comprobamos que tengan la misma fecha
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaTexto = formatter.format(viaje.getFecha());
                    
                    if (fechaTexto.equals(fecha)) {
                        horarios.add(horario);
                    }
                    
                }
                
            }

            // se muestran los horarios
            if(horarios.size() == 0){
                out.print("<option value='0'>No hay horarios disponibles</option>");
            }else{
                for (Horario horario:horarios) {

                    Date hora = horario.getHora();
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String salida = formatter.format(hora);

                    out.print("<option value='" + horario.getId() + "'> " + salida + " </option>");

                }
            }
            
        } catch (HibernateException HE) {
            HE.printStackTrace();
            throw HE;
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
