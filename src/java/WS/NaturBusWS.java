
package WS;

import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "NaturBusWS")
public class NaturBusWS {

    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "listadoRutas")
    public ArrayList<Ruta> listadoRutas() {
        return new OperacionWS().getListadoRutas();
    }
    
}
