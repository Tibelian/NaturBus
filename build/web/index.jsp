
<%@page import="MODEL.Reserva"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    if(session.getAttribute("reserva") != null){
        Reserva reserva = (Reserva) session.getAttribute("reserva");
        response.sendRedirect(reserva.getUltimoPaso());
    }else{
        response.sendRedirect("./views/landpage.jsp");
    }

%>