
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="es">
<head>
    
    <!-- meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- css -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <link rel="stylesheet" href="../assets/css/bootstrap-select.min.css">
    <link rel="stylesheet" href="../assets/css/main.css">
    
    <title>Ha ocurrido un error - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0">
        
        <main>
            <section class="d-flex justify-content-center p-5 bg-light bg-image" style="background-image: url('../assets/img/bg2.jpg'); height: 100vh">
                <div class="container p-5 d-flex flex-column justify-content-center align-items-center">
                    <h1 class="font-weight-bold text-white mb-4"> NaturHost </h1>

                    <div class="bg-white col-6 shadow rounded px-0 my-4" action="../" method="get">
                        <div class="bg-success rounded-top p-2">
                            <h4 class="mb-0 text-white text-center"><i class="fas fa-exclamation-triangle"></i> Ha ocurrido un error</h4>
                        </div>
                        
                        <%
                        String message = "Esto no debería haber pasado. <br> ¡Sentimos las molestias!";
                        if(request.getParameter("message") != null){
                            message = request.getParameter("message");
                        }
                        String code = "";
                        if(request.getParameter("code") != null){
                            code = request.getParameter("code");
                            if(code.equals("data-miss")){
                                message = "¡Faltan datos! Completa todos los campos obligatorios para evitar este tipo de errores.";
                            }
                            if(code.equals("exception")){
                                message = "EXCEPTION: <code>" + message + "</code>";
                            }
                            if(code.equals("no-user")){
                                message = "El DNI y la contraseña que has indicado no pertenece a ningún cliente.";
                            }
                        }
                        %>
                        
                        <div class="h-100 w-100 d-flex flex-column align-items-center justify-content-center p-3">
                            <h4 class="text-center"><%= message %></h4>
                            <a class="btn btn-success mx-auto my-3" href="../"><i class="fas fa-home"></i> Volver al inicio</a>
                        </div>
                        
                    </div>
                </div>
            </section>
            
        </main>
        
    </div>
    
    
    <!-- js -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/8eb2577e4b.js" crossorigin="anonymous"></script>

</body>
</html>