

<%@page import="MODEL.WebSettings"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="IniciarSesion" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="IniciarSesion">Iniciar Sesión</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="text-left" method="post" action="<%= WebSettings.BASE_URL %>obtenerCliente" id="login-form">
                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Correo electrónico:</label>
                        <input class="form-control" type="text" id="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password"><i class="fas fa-key"></i> Contraseña:</label>
                        <input class="form-control" type="password" id="password" name="password" required>
                    </div>
                    <div class="form-row justify-content-center">
                        <button class="btn btn-primary" type="submit"><i class="fas fa-sign-in-alt"></i> Entrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>