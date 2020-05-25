
<%@page import="MODEL.WebSettings"%>
<%@page import="MODEL.ViajeroAsiento"%>
<%@page import="POJOS.Ocupacion"%>
<%@page import="POJOS.Compra"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="MODEL.Reserva"%>
<%
    
// comprueba que exista el objeto reserva y compra
if(session.getAttribute("reserva") == null && session.getAttribute("compra") == null){
    response.sendRedirect("../../");
    return;
}

// prepara el objeto reserva y compa
Reserva reserva = (Reserva) session.getAttribute("reserva");
Compra compra = (Compra) session.getAttribute("compra");

// prepara la fecha y hora de salida
DateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy");
String fechaSalida = formatterFecha.format(reserva.getFechaSalida());
SimpleDateFormat formatterHora = new SimpleDateFormat("HH:mm");
String horaSalida = formatterHora.format(compra.getViaje().getHorario().getHora());


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
    
    <title>Pago realizado con éxito - Reserva - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0 d-flex flex-column" style="min-height: 100vh">
            
        <header class="bg-success text-center">
            <h1 class="pt-3 text-white">NaturBus</h1>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb rounded-0 d-flex justify-content-center">
                    <li class="breadcrumb-item"><i class="fas fa-home"></i> Inicio</li>
                    <li class="breadcrumb-item"><i class="fas fa-bus"></i> Viajes</li>
                    <li class="breadcrumb-item"><i class="fas fa-users"></i> Pasajeros</li>
                    <li class="breadcrumb-item"><i class="fas fa-file-invoice"></i> Resumen</li>
                    <li class="breadcrumb-item"><i class="fas fa-coins"></i> Pago</li>
                    <li class="breadcrumb-item"><span class="bg-light p-1 rounded"><i class="fas fa-thumbs-up"></i> Completado</span></li>
                </ol>
            </nav>
        </header>
        
        <main class="d-flex flex-column align-items-center py-3">
            
            <!-- detalles reserva -->
            <section class="col-8 bg-light my-4 shadow-sm border">
                <h4 class="w-100 border-bottom text-center py-2 mb-3">¡ Reserva completada !</h4>
                <p> 
                    El pago ha sido realizado con éxito, a continuación se muestra el resumen de su reserva junto al localizador, además podrá <a href="#" onclick="imprimirBilletes()">imprimir</a> sus billetes. Si cambia de opinión, también puede <a href="../cliente/anular-reserva.jsp">anular su reserva</a>. Si lo desea, también, puede <a href="../../eliminarReserva">volver a reservar</a>.
                </p>
            </section>

            <!-- billetes -->
            <section class="col-8 row justify-content-center" id="billetes">
                
                <%
                String script = "";
                for (ViajeroAsiento viajero : reserva.getViajeros()) {
                %>
                <div class="cardWrap">
                    <div class="card cardLeft shadow-sm">
                        <h4><%= WebSettings.NAME %></h4>
                        <div class="text">
                            <h5><%= viajero.getApellidos() + ", " + viajero.getNombre() %></h5>
                            <span>nombre</span>
                        </div>
                        <div class="text">
                            <h5><%= compra.getLocalizador() %></h5>
                            <span>localizador</span>
                        </div>
                        <div class="text">
                            <h5><%= fechaSalida + " " + horaSalida %></h5>
                            <span>fecha y hora</span>
                        </div>
                        <div class="text">
                            <h5><%= viajero.getAsiento() %></h5>
                            <span>asiento</span>
                        </div>
                    </div>
                    <div class="card cardRight shadow-sm d-flex justify-content-center">
                        <i class="fas fa-eye"></i>
                        <div class="price">
                            <h5><%= reserva.getRuta().getPrecio() %>€</h5>
                        </div>
                        <div class="d-flex justify-content-center mt-2" id="qrcode<%= viajero.getAsiento() %>"></div>
                      </div>
                </div>
                <%
                script += "new QRCode("
                        + "document.getElementById('qrcode" + viajero.getAsiento() + "'),{"
                        + "width: 70,"
                        + "height: 70,"
                        + "text: 'LOC: " + compra.getLocalizador() + " DNI: " + viajero.getDni() + "'"
                        + "});";
                %>
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
    <script src="../../assets/js/sweetalert2@9.js"></script>
    <script src="../../assets/js/qrcode.min.js"></script>
    <script src="../../assets/js/main.js"></script>
    <script>
        <%= script %>
    </script>
    
</body>
</html>