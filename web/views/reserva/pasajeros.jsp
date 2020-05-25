
<%@page import="POJOS.Ocupacion"%>
<%@page import="POJOS.Compra"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
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

// recoge los asientos ocupados de este viaje
ArrayList<Integer> ocupados = new ArrayList();
Iterator comprasIt = reserva.getViaje().getCompras().iterator();
while(comprasIt.hasNext()){
    Compra compra = (Compra)comprasIt.next();
    Iterator ocupacionIt = compra.getOcupacions().iterator();
    while(ocupacionIt.hasNext()){
        Ocupacion ocupacion = (Ocupacion)ocupacionIt.next();
        ocupados.add(ocupacion.getAsiento());
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
    
    <title>Datos de los pasajeros - Reserva - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0 d-flex flex-column" style="min-height: 100vh">
            
        <header class="bg-success text-center">
            <h1 class="pt-3 text-white">NaturBus</h1>
            <nav class="" aria-label="breadcrumb">
                <ol class="breadcrumb rounded-0 d-flex justify-content-center">
                    <li class="breadcrumb-item"><i class="fas fa-home"></i> Inicio</li>
                    <li class="breadcrumb-item"><i class="fas fa-bus"></i> Viajes</li>
                    <li class="breadcrumb-item"><span class="bg-light p-1 rounded"><i class="fas fa-users"></i> Pasajeros</span></li>
                    <li class="breadcrumb-item"><i class="fas fa-file-invoice"></i> Resumen</li>
                    <li class="breadcrumb-item"><i class="fas fa-coins"></i> Pago</li>
                    <li class="breadcrumb-item"><i class="fas fa-thumbs-up"></i> Completado</li>
                </ol>
            </nav>
        </header>
        
        <main class="d-flex flex-column align-items-center py-3">
            
            <!-- detalles reserva -->
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
                    
            <!-- info paso actual -->
            <section class="col-8 my-3 text-justify">
                <i class="fas fa-info-circle"></i> Ahora necesitamos que nos indique los datos de todos los pasajeros y los asientos que desean. Le recordamos que en cualquier momento puede cancelar su reserva haciendo click en el botón superior de color rojo donde dice "Cancelar reserva". Una vez completada la reserva y realizado el pago también tiene la posibilidad de anular la reserva.
            </section>

            <!-- formulario pasajeros -->
            <section class="col-8 row justify-content-center p-0 mt-3">
                <form class="row" method="post" action="../../agregarPasajeros" id="formulario-pasajeros">
                <%
                for(int i = 1; i <= reserva.getPasajeros(); i++){
                %>
                    <div class="bg-light m-3 p-3 shadow-sm rounded border">
                        <h4 class="border-bottom pb-2">Datos del pasajero <%= i %></h4>
                        <input type="text" required placeholder="-- Nombre --" name="pasajero<%= i %>-nombre" class="form-control">
                        <input type="text" required placeholder="-- Apellidos --" name="pasajero<%= i %>-apellidos" class="form-control">
                        <input type="text" required placeholder="-- DNI --" id="pasajero<%= i %>-dni" name="pasajero<%= i %>-dni" class="form-control">
                        <select id="pasajero<%= i %>-asiento" required name="pasajero<%= i %>-asiento" class="form-control" onchange="compruebaAsiento(this, <%= reserva.getPasajeros() %>)">
                            <option value="">-- Asiento --</option>
                            <%
                            for(int n = 1; n <= reserva.getViaje().getPlazasTotales(); n++){
                                    boolean ocupado = false;
                                    for(int j = 0; j < ocupados.size(); j++){
                                        if(n == ocupados.get(j)){
                                            ocupado = true;
                                        }
                                    }
                            %>
                            <option value="<%= n %>" <% if(ocupado){out.print("disabled");} %>><%= n %></option>
                            <%
                            }
                            %>
                        </select>
                    </div>
                <%
                }
                %>
                    <button type="submit" class="btn btn-success d-flex align-items-center mx-auto my-auto"> <i class="fas fa-check fa-3x mr-2"></i> Guardar datos</button>
                </form>
                
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
    <script src="../../assets/js/main.js"></script>
    <script>
        let form = document.getElementById("formulario-pasajeros");
            form.onsubmit = e => {
                e.preventDefault();
                compruebaPasajero(form, <%= reserva.getPasajeros() %>);
            }
    </script>
    
</body>
</html>