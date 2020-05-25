
package MODEL;

import java.security.SecureRandom;

public class Localizador {
    
    public static String generar(int tamano) {
    
        String caracter = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom aleatorio = new SecureRandom();
        StringBuilder sb = new StringBuilder(tamano);
        for (int i = 0; i < tamano; i++) {
            sb.append(
                caracter.charAt(
                    aleatorio.nextInt(caracter.length())
                )
            );
        }
        return sb.toString();
        
    }
    
}
