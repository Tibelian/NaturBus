
package MODEL;

public class BusException extends Exception {
    
    private int code;
    
    public BusException(int code, String message){
        super(message);
        this.code = code;
    }
    
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return code;
    }
    
}