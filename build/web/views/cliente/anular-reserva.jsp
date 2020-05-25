
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
    
    <!-- meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- css -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="../../assets/css/main.css">
    
    <title>Anular reserva - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0 bg-light" style="min-height: 100vh">

        <div class="my-5 text-success text-center">
            <h1>NaturBus</h1>
            <h4 class="text-secondary">Anular reserva</h4>
        </div>
        
        <%
        if(request.getParameter("ok") != null){
        %>
        <div class="alert alert-success alert-dismissible fade show col-lg-6 mx-auto" role="alert">
            <strong>¡Completado!</strong> La reserva ha sido cancelada con éxito
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <%
        }
        %>
        
        <form class="border p-5 bg-white card-body col-lg-6 offset-lg-3 shadow-sm rounded" action="../../anularReserva" method="POST">
            <div class="form-group">
                <label for="dni"><i class="fas fa-id-card"></i> DNI</label>
                <input type="text" class="form-control" name="dni" id="dni" required>
            </div>
            <div class="form-group">
                <label for="clave"><i class="fas fa-key"></i> Contraseña</label>
                <input type="password" class="form-control" name="clave" id="clave" required>
            </div>
            <div class="form-group">
                <label for="localizador"><i class="fas fa-qrcode"></i> Localizador</label>
                <input type="text" class="form-control" id="localizador" name="localizador" required>
            </div>
            <div class="form-group mt-4 mb-0">
                <button type="submit" class="btn btn-success btn-lg btn-block"><i class="fas fa-ban"></i> Confirmar</button>
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
    <script src="../../assets/js/main.js"></script>

</body>
</html>