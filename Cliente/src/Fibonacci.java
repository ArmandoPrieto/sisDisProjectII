import java.io.Serializable;
import java.math.BigDecimal;

public class Fibonacci implements Task<Integer>, Serializable {

    private static final long serialVersionUID = 227L;
  
    private final int digits;
    
    private String nombreTarea;
    
    /**
     * Construct a task to calculate pi to the specified
     * precision.
     */
    public Fibonacci(int digits) {
        this.digits = digits;
        this.nombreTarea = "Fibonacci";
    }

    /**
     * Calculate pi.
     */
    public Integer execute() {
        return compute(digits);
    }

   
    public static Integer compute(int digits) {
     int inf,x;
     Integer sup=0;
     
     try {
     if (digits==0 || digits==1)
        sup =  digits;
     
     inf=0;
     sup=1;
     for (int i=2; i<=digits; i++) {
      //  System.out.println("hilo ejecutandose: "+Thread.currentThread().getName());
        long espera = (long) (Math.random() * 5000); 
        //Thread.sleep(espera);
      //  System.out.println("Milisegundos Esperados: " +espera);
         
         x = inf;
         inf = sup;
         sup= x + inf;
         }
    
     } catch (Exception ex) {
         System.out.println(ex);
     }          
     return sup;       
    }
   
    /*
    public String getNombreServicio() {
        return nombreServicio;
    }

    
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }*/
}
