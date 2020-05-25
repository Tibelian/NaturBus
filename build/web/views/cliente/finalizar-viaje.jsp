<%@page import="POJOS.Administrador"%>
<%@page import="java.util.List"%>
<%@page import="POJOS.Estacion"%>
<%
    
// comprueba que se haya cargado el listado con las estaciones
if(session.getAttribute("listadoOrigen") == null){
    response.sendRedirect("../../obtenerOrigen?finalizar=1");
    return;
}
List<Estacion> listadoOrigen = (List)session.getAttribute("listadoOrigen");

// obtiene al admin si ha inciado sesión
Administrador admin = null;
if(session.getAttribute("cliente") != null){
    if(session.getAttribute("cliente") instanceof Administrador){
        admin = (Administrador) session.getAttribute("cliente");
    }else{
        response.sendRedirect("../error.jsp?message=No tienes permisos suficientes para acceder");
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
    <link rel="stylesheet" href="../../assets/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="../../assets/css/main.css">
    
    <title>Finalizar viaje - Panel de administración - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0 bg-light" style="min-height: 100vh">

        <div class="my-5 text-success text-center">
            <h1>NaturBus</h1>
            <h4 class="text-secondary">Panel de administración</h4>
        </div>
        
        <%
        if(request.getParameter("ok") != null){
        %>
        <div class="alert alert-success alert-dismissible fade show col-lg-6 mx-auto" role="alert">
            <strong>¡Completado!</strong> El viaje ha sido finalizado con éxito.
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <%
        }
        %>
        
        <form class="border p-5 bg-white card-body col-lg-6 offset-lg-3 shadow-sm rounded" action="../../finalizarViaje" method="POST">
            <div class="form-group">
                <label for="origen"><i class="fas fa-map"></i> Origen</label>
                <select class="selectpicker show-tick w-100" name="origen" id="origen" required data-style="border form-control" data-width="100%" title="Selecciona el origen...">
                    <option value=""></option>
                    <%
                    for(Estacion estacion:listadoOrigen){
                    %>
                    <option value="<%= estacion.getId() %>"><%= estacion.getNombre() %> - <%= estacion.getDireccion() %></option>
                    <%
                    }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="destino"><i class="fas fa-map-marked-alt"></i> Destino</label>
                <select class="selectpicker show-tick w-100" name="destino" id="destino" required data-style="border form-control" data-width="100%" title="Selecciona el destino...">
                    <option value=""></option>
                </select>
            </div>
            <div class="form-group">
                <label for="fecha"><i class="fas fa-calendar"></i> Fecha</label>
                <input type="date" class="form-control" id="fecha" name="fecha" required>
            </div>
            <div class="form-group">
                <label for="horario"><i class="fas fa-clock"></i> Horario</label>
                <select class="selectpicker show-tick w-100" name="horario" id="horario" required data-style="border form-control" data-width="100%" title="Selecciona el horario...">
                    <option value=""></option>
                </select>
            </div>
            <div class="form-group mt-4 mb-0">
                <button type="submit" class="btn btn-success btn-lg btn-block"><i class="fas fa-ban"></i> Finalizar viaje</button>
            </div>
        </form>
        <div class="row pt-4 px-0 mx-0 justify-content-center">
            <a href="../../"><i class="fas fa-home"></i> Volver al inicio</a>
        </div>

    </div>
                                
    <!-- js -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/8eb2577e4b.js" crossorigin="anonymous"></script>
    <script src="../../assets/js/bootstrap-select.min.js"></script>
    <script src="../../assets/js/main.js"></script>
    <script>
        
        var fecha = new Date();
        var mes = fecha.getUTCMonth() + 1;
        var dia = fecha.getUTCDate();
        if(mes < 10){ mes = "0" + mes; }
        if(dia < 10){ dia = "0" + dia; }
        var fechaSalida = fecha.getUTCFullYear() + "-" + mes  + "-" + dia;
        document.getElementById("fecha").value = fechaSalida;
        
        $('.selectpicker').selectpicker();
        
        var origen = document.getElementById("origen");
        origen.onchange = function(){
            obtenerDestino(origen.value, true);
        };
       
        var destino = document.getElementById("destino");
        var fecha = document.getElementById("fecha");
        destino.onchange = obtenerHorarios;
        fecha.onchange = obtenerHorarios;
        
    </script>

</body>
</html>