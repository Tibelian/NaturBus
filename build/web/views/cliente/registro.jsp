
<%@page import="MODEL.WebSettings"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="modal fade" id="registroModal" tabindex="-1" role="dialog" aria-labelledby="CrearCuenta" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="CrearCuenta">Crear cuenta</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form class="text-left" method="post" action="<%= WebSettings.BASE_URL %>agregarCliente" id="registro-form">
                    <div class="form-group">
                        <label for="email"><i class="fas fa-envelope"></i> Correo electrónico:</label>
                        <input class="form-control" type="email" id="reg_email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password"><i class="fas fa-key"></i> Contraseña:</label>
                        <input class="form-control" type="password" id="reg_password" name="password" required>
                    </div>
                    <div class="form-row">
                        <div class="form-group col">
                            <label for="nombre"><i class="fas fa-user"></i> Nombre:</label>
                            <input class="form-control" type="text" id="reg_nombre" name="nombre" required>
                        </div>
                        <div class="form-group col">
                            <label for="apellidos"><i class="far fa-user"></i> Apellidos:</label>
                            <input class="form-control" type="text" id="reg_apellidos" name="apellidos" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col">
                            <label for="dni"><i class="fas fa-id-card"></i> DNI:</label>
                            <input class="form-control" maxlength="9" size="10" type="text" id="reg_dni" name="dni" required>
                        </div>
                        <div class="form-group col">
                            <label for="telefono"><i class="fas fa-phone"></i> Teléfono:</label>
                            <input class="form-control" size="10" maxlength="10" type="number" id="reg_telefono" name="telefono" required>
                        </div>
                    </div>
                    <div class="form-row justify-content-center">
                        <button class="btn btn-primary" type="submit"><i class="fas fa-user-plus"></i> Registrarse</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>