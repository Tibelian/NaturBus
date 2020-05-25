<%@page import="POJOS.Administrador"%>
<%@page import="POJOS.Cliente"%>
<%@page import="MODEL.WebSettings"%>
<%@page import="java.util.List"%>
<%@page import="POJOS.Estacion"%>
<%
    
// comprueba que se haya cargado el listado con las estaciones
if(session.getAttribute("listadoOrigen") == null){
    response.sendRedirect("../obtenerOrigen");
    return;
}
List<Estacion> listadoOrigen = (List)session.getAttribute("listadoOrigen");

// obtiene al cliente o admin si ha inciado sesión
Cliente cliente = null;
if(session.getAttribute("cliente") != null){
    cliente = (Cliente) session.getAttribute("cliente");
}
Administrador admin = null;
if(session.getAttribute("administrador") != null){
    admin = (Administrador) session.getAttribute("administrador");
}

%>
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
    
    <title>No más contaminación - NaturBus</title>
    
</head>
<body>

    <div class="container-fluid p-0">
        
        <header class="bg-white">
            
            <div class="container mx-auto p-2 row justify-content-between">
                <h3 clasS="h4 mb-0"><%= WebSettings.NAME %></h3>
                <div>
                    Síguenos: 
                    <a class="mx-1 text-primary" href="#"><i class="fab fa-facebook"></i></a>
                    <a class="mx-1 text-info" href="#"><i class="fab fa-twitter"></i></a>
                    <a class="mx-1 text-secondary" href="#"><i class="fab fa-instagram"></i></a>
                    <a class="mx-1 text-danger" href="#"><i class="fab fa-youtube"></i></a>
                </div>
            </div>
            
            <nav class="navbar navbar-expand-lg navbar-dark bg-success">
                <div class="container mx-auto">
                    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                        <ul class="navbar-nav d-flex justify-content-center w-100">
                            <%
                            if(cliente != null || admin != null){
                                String nombre = null;
                                try{
                                    nombre = cliente.getNombre();
                                }catch(NullPointerException ne){
                                    nombre = "Administrador";
                                }
                            %>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-white" href="#" id="dropdownCliente" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    <i class="fas fa-user-circle"></i> Hola <%= nombre %>!
                                </a>
                                <div class="dropdown-menu" aria-labelledby="dropdownCliente">
                                    <a class="dropdown-item" href="./cliente/anular-reserva.jsp">Anular reserva</a>
                                    <div class="dropdown-divider"></div>
                                    <%
                                    if(admin != null){
                                    %>
                                    <a class="dropdown-item" href="./cliente/finalizar-viaje.jsp">Finalizar viaje</a>
                                    <%
                                    }
                                    %>
                                    <a class="dropdown-item" href="../eliminarSesion">Cerrar sesión</a>
                                </div>
                            </li>
                            <%  
                            }else{
                            %>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="#" data-toggle="modal" data-target="#loginModal"><i class="fas fa-user-circle"></i> Iniciar sesión</a>
                                <!-- modal iniciar sesión -->
                                <jsp:include page="./cliente/login.jsp" />
                            </li>
                            <li class="nav-item ml-2">
                                <a class="nav-link text-white" href="./cliente/anular-reserva.jsp"><i class="fas fa-ban"></i> Anular reserva</a>
                            </li>
                            <%
                            }
                            %>
                        </ul>
                    </div>
                </div>
          </nav>
            
        </header>
        
        <main>
            
            <!-- buscador -->
            <section class="d-flex justify-content-center p-5 bg-light bg-image" style="background-image: url('../assets/img/bg1.jpg')">
                <div class="container p-5 d-flex flex-column align-items-center">
                    <h1 class="font-weight-bold text-white"> Viaja sin contaminar </h1>
                    <p class="text-uppercase text-success">En NaturHost nos preocupamos por el medio ambiente</p>

                    <form class="bg-white col-8 shadow rounded px-0 my-4" action="../obtenerRuta" method="post">
                        <div class="bg-success rounded-top p-2">
                            <h4 class="mb-0 text-white text-center"><i class="fas fa-map-signs"></i> Encuentra tu viaje</h4>
                        </div>
                        <div class="form-row p-4">
                            <div class="col-md-10 mb-3">
                                <div class="row mb-2">
                                    <div class="col-6 pr-1">
                                        <label for="origen"><i class="fas fa-map"></i> Origen</label>
                                        <select class="selectpicker show-tick" name="origen" id="origen" required data-style="border form-control" data-width="100%" title="Selecciona el origen...">
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
                                    <div class="col-6 pl-1">
                                        <label for="destino"><i class="fas fa-map-marked-alt"></i> Destino</label>
                                        <select class="selectpicker show-tick" name="destino" id="destino" required data-style="border form-control" data-width="100%" title="Selecciona el destino...">
                                            <option value=""></option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-6 pr-1">
                                        <label for="fecha"><i class="fas fa-calendar"></i> Fecha</label>
                                        <input type="date" class="form-control" name="fecha" id="fecha" required>
                                    </div>
                                    <div class="col-6 pl-1">
                                        <label for="pasajeros"><i class="fas fa-users"></i> Nº pasajeros</label>
                                        <input type="number" class="form-control" name="pasajeros" id="pasajeros" value="1" max="20" min="1" required>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-2    ">
                                <button type="submit" class="w-100 h-100 btn btn-success"><i class="fas fa-search fa-2x mb-2"></i> Buscar</button>
                            </div>
                      </div>
                    </form>
                </div>
            </section>
            
            <!-- cualidades -->
            <section class="d-flex flex-column align-items-center p-5 bg-light nosotros">
                <div class="container text-center p-5">
                    <h3 class="font-weight-bold">¿Por qué elegir NaturBus?</h3>
                    <p class="text-secondary mb-5">Somos fieles a los pilares de nuestro servicio</p>
                    <div class="row justify-content-between">
                        <div class="col-lg-4 col-sm-12">
                            <div class="border p-3 bg-white shadow">
                                <i class="far fa-star fa-4x text-success pt-3"></i>
                                <p class="h4 text-success border-bottom pb-4">Confianza</p>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> 11 años de experiencia</h5>
                                    <p class="text-secondary fs-15">Durante más de 11 años hemos estado ofreciendo tecnología puntera de confianza.</p>
                                </div>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> Seguridad y protección</h5>
                                    <p class="text-secondary fs-15">Nuestros autobuses cumplen con el certificado ISO 50001 cuyo propósito es el de permitirle una mejora continua de la eficiencia energética.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-12">
                            <div class="border p-3 bg-white shadow">
                                <i class="far fa-handshake fa-4x text-success pt-3"></i>
                                <p class="h4 text-success border-bottom pb-4">Honestidad</p>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> Ofertas transparentes</h5>
                                    <p class="text-secondary fs-15">¡Sin letra pequeña! Toda la información y los precios se muestran de manera transparente antes de que realices tu reserva.</p>
                                </div>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> Productos de alta gama</h5>
                                    <p class="text-secondary fs-15">Ofrecemos productos de alto rendimiento personalizados para tus necesidades. Ej: wifi, tv, radio...</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-sm-12">
                            <div class="border p-3 bg-white shadow">
                                <i class="fas fa-headset fa-4x text-success pt-3"></i>
                                <p class="h4 text-success border-bottom pb-4">Asistencia</p>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> Asistencia 24/7</h5>
                                    <p class="text-secondary fs-15">Los equipos de asistencia locales siempre están disponibles para ofrecer ayuda y asesoramiento por teléfono, correo electrónico o chat.</p>
                                </div>
                                <div class="py-3">
                                    <h5><i class="fas fa-check"></i> Asesor personal</h5>
                                    <p class="text-secondary fs-15">Un asistente personal estará siempre a tu disposición para ayudarte cuando lo necesites.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            
            <!-- info -->
            <section class="d-flex justify-content-center p-5 bg-image text-center" style="background-image: url('../assets/img/bg2.jpg')">
                <div class="d-flex col-8">
                    <div class="col-md-3">
                        <i class="fas fa-clock fa-3x text-success"></i>
                        <h5 class="text-white font-weight-bold mt-3">Disponibles 24h</h5>
                        <p class="text-success mb-0">Por chat, teléfono y ticket</p>
                    </div>
                    <div class="col-md-3">
                        <i class="fas fa-smile fa-3x text-success"></i>
                        <h5 class="text-white font-weight-bold mt-3">Experto y amigable</h5>
                        <p class="text-success mb-0">Siempre dispuestos a ayudar</p>
                    </div>
                    <div class="col-md-3">
                        <i class="fas fa-comments fa-3x text-success"></i>
                        <h5 class="text-white font-weight-bold mt-3">Ultrarrápido</h5>
                        <p class="text-success mb-0">Soluciones veloces</p>
                    </div>
                    <div class="col-md-3">
                        <i class="fas fa-heart fa-3x text-success"></i>
                        <h5 class="text-white font-weight-bold mt-3">Mejor puntuado</h5>
                        <p class="text-success mb-0">Clientes satisfechos</p>
                    </div>
                </div>
            </section>
            
            <!-- sobre nosotros -->
            <section class="p-5 bg-white">
                <div class="container p-5 d-flex justify-content-center">
                    <div class="col-md-6">
                        <img class="shadow" src="../assets/img/bg3.jpg" width="100%" alt="imagen sobre nosotros">
                    </div>
                    <div class="col-md-6">
                        <h3 class="mb-3">Reserva ahora</h3>
                        <p>
                            Consulta los horarios de los autobuses y disfruta de todas las ventajas de organizar tus rutas de autobús, conociendo los horarios de cada trayecto antes de reservar.  <br><br>
                            Si tienes en mente algo distinto, ponte en contacto con nosotros, y te ofreceremos las mejores soluciones en transporte. Destacamos por nuestro compromiso y gestión de vehículos para que de lo único que tengas que preocuparte sea de disfrutar de tu acto especial.
                        </p>
                    </div>
                </div>
            </section>
            
            <!-- el tiempo / rss -->
            <section class="p-5 bg-white pt-0 mt-0">
                <div class="container d-flex align-items-center flex-column">
                
                    <!-- start feedwind code --> <script type="text/javascript" src="https://feed.mikle.com/js/fw-loader.js" preloader-text="Loading" data-fw-param="130360/"></script> <!-- end feedwind code -->

                    <div class="my-4" id="c_45c8163106ae849d9f04be652865476e" class="ancho"></div><script type="text/javascript" src="https://www.eltiempo.es/widget/widget_loader/45c8163106ae849d9f04be652865476e"></script>
                    <div id="c_ae134e41fd23e040807f15ffae839b71" class="ancho"></div><script type="text/javascript" src="https://www.eltiempo.es/widget/widget_loader/ae134e41fd23e040807f15ffae839b71"></script>

                </div>
            </section>
            
        </main>
        
        <!-- píe de página -->
        <jsp:include page="./footer.jsp" />
        
    </div>
    
    
    <!-- js -->
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/8eb2577e4b.js" crossorigin="anonymous"></script>
    <script src="../assets/js/bootstrap-select.min.js"></script>
    <script src="../assets/js/main.js"></script>
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
            obtenerDestino(origen.value);
        };
        
    </script>

</body>
</html>