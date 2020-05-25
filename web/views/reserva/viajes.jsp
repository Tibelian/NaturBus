<%@page import="MODEL.ViajeResumido"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="POJOS.Viaje"%>
<%@page import="POJOS.Horario"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="MODEL.Reserva"%>
<%
    
// comprueba que exista el objeto reserva
if(session.getAttribute("reserva") == null){
    response.sendRedirect("../../");
    return;
}

// prepara el objeto reserva
Reserva reserva = (Reserva)session.getAttribute("reserva");

// prepara la fecha de salida
DateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
String fechaSalida = formatterFecha.format(reserva.getFechaSalida());

// recoge todos los viajes que tengan la fecha indicada
ArrayList<ViajeResumido> viajesResumidos = new ArrayList();
Iterator horarios = reserva.getRuta().getHorarios().iterator();
while(horarios.hasNext()){
    Horario horario = (Horario)horarios.next();
    Iterator viajes = horario.getViajes().iterator();
    while(viajes.hasNext()){
        Viaje viaje = (Viaje)viajes.next();
        if(reserva.getFechaSalida().equals(viaje.getFecha())){
            
            ViajeResumido viajeResumido = new ViajeResumido();
            viajeResumido.setId(viaje.getId());
            viajeResumido.setFecha(viaje.getFecha());
            viajeResumido.setPlazasDisponibles(viaje.getPlazasDisponibles());
            viajeResumido.setPlazasTotales(viaje.getPlazasTotales());
            
            viajeResumido.setIdHorario(horario.getId());
            viajeResumido.setHora(horario.getHora());
            viajeResumido.setTipo(horario.getTipo());
            
            viajesResumidos.add(viajeResumido);
            
        }
    }
}

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
    
    <!-- meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- css -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="../../assets/css/main.css">
    
    <title>Selecciona el viaje - Reserva - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0 d-flex flex-column" style="min-height: 100vh">
            
        <header class="bg-success text-center">
            <h1 class="pt-3 text-white">NaturBus</h1>
            <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb rounded-0 d-flex justify-content-center">
                    <li class="breadcrumb-item"><i class="fas fa-home"></i> Inicio</li>
                    <li class="breadcrumb-item"><span class="bg-light p-1 rounded"><i class="fas fa-bus"></i> Viajes</span></li>
                    <li class="breadcrumb-item"><i class="fas fa-users"></i> Pasajeros</li>
                    <li class="breadcrumb-item"><i class="fas fa-file-invoice"></i> Resumen</li>
                    <li class="breadcrumb-item"><i class="fas fa-coins"></i> Pago</li>
                    <li class="breadcrumb-item"><i class="fas fa-thumbs-up"></i> Completado</li>
                </ol>
            </nav>
        </header>
        
        <main class="d-flex flex-column align-items-center py-3">
            <section class="col-8 bg-light my-4 shadow-sm border">
                <h4 class="w-100 border-bottom text-center py-2 mb-3">Detalles de la reserva</h4>
                <div class="d-flex justify-content-around flex-wrap">
                    <p class="badge badge-secondary p-2"><i class="fas fa-map"></i> Origen: <%= reserva.getRuta().getEstacionByIdOrigen().getNombre() %></p>
                    <p class="badge badge-secondary p-2"><i class="fas fa-map-marked-alt"></i> Destino: <%= reserva.getRuta().getEstacionByIdDestino().getNombre() %></p>
                    <p class="badge badge-secondary p-2"><i class="fas fa-calendar"></i> Fecha: <%= fechaSalida %></p>
                    <p class="badge badge-secondary p-2"><i class="fas fa-money-bill-wave"></i> Precio: <%= reserva.getRuta().getPrecio() %> €</p>
                    <a href="../../eliminarReserva" class="mb-3 btn btn-sm btn-danger"><i class="fas fa-times"></i> Cancelar reserva </a>
                </div>
            </section>
                    
            <section class="col-8 my-3 text-justify">
                <i class="fas fa-info-circle"></i> A continuación debe elegir el viaje según la hora y las plazas que le convenga. Le recordamos que en cualquier momento puede cancelar su reserva haciendo click en el botón superior de color rojo donde dice "Cancelar reserva". Una vez completada la reserva y realizado el pago también tiene la posibilidad de anular la reserva.
            </section>

            <section class="col-8 row justify-content-between p-0 mt-3">
                
                
                <%
                    
                    // esto es para controlar el texto que saldrá
                    if(viajesResumidos.size() == 1){
                    %>
                        <p class="w-100 m-0 p-2 text-center bg-light shadow-sm border text-uppercase font-weight-bold">Se ha encontrado 1 viaje</p>
                    <%
                    }else{
                    %>
                        <p class="w-100 m-0 p-2 text-center bg-light shadow-sm border text-uppercase font-weight-bold">Se han encontrado <%= viajesResumidos.size() %> viajes</p>
                    <%
                    }
                    
                    // muestra todos los viajes disponibles con el filtro aplicado
                    for(ViajeResumido viaje:viajesResumidos){
                        
                        // formatea la fecha
                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        String hora = formatter.format(viaje.getHora());
                        
                        // calcula la duración del viaje
                        long llegadaTime = viaje.getHora().getTime() + reserva.getRuta().getDuracion().getTime();
                        String llegada = formatter.format(new Date(llegadaTime));
                        
                        // muestra la información
                        %>
                        <div class="bg-light mt-3 border shadow-sm p-4 d-flex justify-content-between align-items-center" style="width:49.4%;">
                            <div class="h-100 pr-3 border-right d-flex align-items-center">
                                <h5 class="text-center m-0">Hora salida: <br><strong><%= hora %></strong></h5>
                            </div>
                            <ul class="list-group list-group-flush" style="font-size: 14px;">
                                <li class="list-group-item p-1 bg-transparent">Hora llegada a la estación destino: <strong>~<%= llegada %></strong></li>
                                <li class="list-group-item p-1 bg-transparent">Duración del viaje: <strong><%= formatter.format(reserva.getRuta().getDuracion()) %> horas</strong></li>
                                <li class="list-group-item p-1 bg-transparent">Distancia entre las dos estaciones: <strong><%= reserva.getRuta().getKilometros()%> km</strong></li>
                                <li class="list-group-item p-1 bg-transparent border-0">Plazas disponibles: <strong><%= viaje.getPlazasDisponibles()%></strong></li>
                            </ul>
                            <%
                            if(viaje.getPlazasDisponibles() < reserva.getPasajeros()){
                            %>
                            <a href="#" class="h-100 d-flex flex-column align-items-center btn btn-success disabled"><i class="fas fa-times fa-2x"></i> <small>NO QUEDAN PLAZAS</small></a>
                            <%
                            }else{
                            %>
                            <a href="../../elegirViaje?id=<%= viaje.getId() %>" class="h-100 d-flex justify-content-center flex-column align-items-center btn btn-success"> <i class="fas fa-check fa-2x"></i> Elegir viaje</a>
                            <%
                            }
                            %>
                        </div>
                        <%
                    }
                    
                %>
                
                
            </section>
        </main>
        
        <!-- píe de página -->
        <jsp:include page="../footer.jsp" />
        
    </div>
    
    
    <!-- js -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/8eb2577e4b.js" crossorigin="anonymous"></script>
    <script src="../../assets/js/main.js"></script>
    
</body>
</html>