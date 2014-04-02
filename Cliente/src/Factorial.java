import java.io.Serializable;
import java.math.BigDecimal;

public class Factorial implements Task<Integer>, Serializable {

    private static final long serialVersionUID = 227L;

   
    private final int digits;
    private String nombreTarea;
    
    public Factorial(int digits) {
        this.digits = digits;
          this.nombreTarea = "Factorial";
    }

    public Integer execute() {
        return new Integer(computeFactorial(digits));
    }

    public static int computeFactorial(int digits) {
     int f=1;
     int j;          
     try {
        for (j=digits;j>=1;j--) {
          //System.out.println("hilo ejecutandose: "+Thread.currentThread().getName());
          long espera = (long) (Math.random() * 5000); 
          Thread.sleep(espera);
          //System.out.println("Milisegundos Esperados: " +espera);
           f = f * j;  
        }
      }catch(Exception e){
              
              e.printStackTrace();
      }
     return f;
     }  

   
    }
 
  

