
// obtener destinos
function obtenerDestino(idOrigen, url = false){
    var destino = document.getElementById("destino");
    if(idOrigen != ""){
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
          if (this.readyState == 4 && this.status == 200) {
            destino.innerHTML = this.responseText;
            $('#destino').selectpicker('refresh');
          }
        };
        if(url){
            xhttp.open("GET", "../../obtenerDestino?id=" + idOrigen, true);
        }else{
            xhttp.open("GET", "../obtenerDestino?id=" + idOrigen, true);
        }
        xhttp.send();
    }else{
        destino.innerHTML = "";
    }
}

// comprueba si el dni indicado es válido
function validarDNI(dni) {
    var numero, let, letra;
    var expresion_regular_dni = /^[XYZ]?\d{5,8}[A-Z]$/;
    dni = dni.toUpperCase();
    if(expresion_regular_dni.test(dni) === true){
        numero = dni.substr(0,dni.length-1);
        numero = numero.replace('X', 0);
        numero = numero.replace('Y', 1);
        numero = numero.replace('Z', 2);
        let = dni.substr(dni.length-1, 1);
        numero = numero % 23;
        letra = 'TRWAGMYFPDXBNJZSQVHLCKET';
        letra = letra.substring(numero, numero+1);
        if (letra != let) {
            return false;
        }else{
            return true;
        }
    }else{
        return false;
    }
}

// muestra una alerta si el asiento seleccionado se repite
function compruebaAsiento(select, totalPasajeros){
    for(let i = 1; i <= totalPasajeros; i++){
        let otroSelect = document.getElementById("pasajero" + i + "-asiento");
        if(select.id !== otroSelect.id){
            if(select.value === otroSelect.value && select.value !== "0"){
                Swal.fire(
                    '¡Lo sentimos!',
                    'El asiento que ha seleccionado está ocupado',
                    'error'
                );
                select.value = "";
            }
        }
    }
}

// última validación antes de enviar
// el formulario con los datos de los pasajeros
function compruebaPasajero(form, totalPasajeros){
    
    // comprueba los dni
    let okDni = [];
    for(let i = 1; i <= totalPasajeros; i++){
        let dni = document.getElementById("pasajero" + i + "-dni");
        if(!validarDNI(dni.value)){
            okDni.push(i);
        }
    }
    
    // muestra en la alerta los pasajeros
    // que tienen el dni inválido
    if(okDni.length > 0){
        let texto = "";
        for(let i = 0; i < okDni.length; i++){
            texto += " " + okDni[i];
        }
        Swal.fire(
            '¡Lo sentimos!',
            'Los siguientes pasajeros no tienen un DNI válido: ' + texto,
            'error'
        );
    }else{
        form.submit();
    }
    
}

// en el resumen si el cliente no ha iniciado sesión
// se le mostrará una alerta con dos opciones:
function realizarPago(){
    Swal.fire({
        title: 'Debes identificarte',
        text: "Para realizar el pago debes iniciar sesión. En caso de que no tenga una cuenta puede registrarse.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Iniciar sesión',
        cancelButtonText: 'Crear cuenta'
    }).then((result) => {
        if (result.value) {
            $('#loginModal').modal('show');
        }else{
            $('#registroModal').modal('show');
        }
    });
}

// muestra una alerta para verificar
// si el cliente quiere seguir con la operación
function confirmarEliminar(form){
    Swal.fire({
        title: '¿Está usted seguro?',
        text: 'Una vez eliminada la tarjeta ya no podrá utilizarla. Le recomendamos que por lo menos tenga una tarjeta guardada para futuras compras, de este modo facilitandole la compra.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, quiero eliminarla',
        cancelButtonText: 'No, mejor no'
    }).then((result) => {
        if(result.value){
            form.submit();
        }
    });
}

// alerta para confirmar la operación
function confirmarPago(form, tarjeta, precio, cvv){
    Swal.fire({
        title: 'Escribe el CVV',
        icon: 'info',
        html: '<h5>Para completar el pago <strong>' + precio + ' €</strong> con la tarjeta <span class="text-uppercase">' + tarjeta + '</span></h5>',
        input: 'text', 
        inputAttributes: {
            maxlength: 10,
            autocapitalize: 'off',
            autocorrect: 'off',
            class: 'text-center'
        },
        confirmButtonText: 'Realizar pago',
        cancelButtonText: 'Cancelar',
        showCancelButton: true,
        inputValidator: (value) => {
            if (value == cvv) {
                form.submit();
            }else{
                return '¡El CVV que has introducido no es válido!'
            }
        }
    });
}

// crea una nueva ventana consolamente los billetes
// y se abre la impresión automáticamente
function imprimirBilletes(){
    let ventanaBilletes = window.open('','','width=' + screen.width + ',height=' + screen.height + ',fullscreen=yes');
    let billetes = document.getElementById("billetes");
    ventanaBilletes.document.write(`
        <html>
            <head>
                <link rel="stylesheet" href="../../assets/css/main.css">
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
                <script src="https://kit.fontawesome.com/8eb2577e4b.js" crossorigin="anonymous"><\/script>
                <title>Imprimir Billetes NaturHost</title>
            </head>
            <body>` + billetes.innerHTML + `</body>
        </html>
    `);
    ventanaBilletes.document.close()
    ventanaBilletes.focus();
    ventanaBilletes.print();
}


// devuelve las horas disponibles en option
function obtenerHorarios() {
    
    var fecha = document.getElementById("fecha");
    var origen = document.getElementById("origen");
    var destino = document.getElementById("destino");
    
    if(fecha.value != "" && origen.value != "" && destino.value != ""){
        
        var xmlhttp;
        if (window.XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
        } else {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
                document.getElementById("horario").innerHTML = xmlhttp.responseText;
                $('#horario').selectpicker('refresh');
            }
        };
        xmlhttp.open("GET", "../../obtenerHorario?destino=" + destino.value + "&origen=" + origen.value + "&fecha=" + fecha.value, true);
        xmlhttp.send();

    }
    
}