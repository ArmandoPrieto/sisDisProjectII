import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author kid
 */
public class Consumidor extends Thread{
    
    String host;
    int port;   
    int task, n;
    
    public Consumidor(String host, int port, int task, int n){
    
        this.host = host;
        this.port = port;  
        this.task = task;
        this.n = n;
        this.setName("Consumidor");
    
    }
   /* 
    public Task elegirTarea() {
        Task<Integer> t=null;
    try {
    if (task == 1) t = new TareaFibonacci(n);
    //else if (task == 2) t = new Tarea_Impl2();
    //else if (task == 3) t = new Tarea_Impl3();
    //t.setN(n);
    } catch (Exception ex) {
        System.out.println("Error al Asignar la tarea");
        System.out.println(ex);
    }
    System.out.println("Tarea Asignada");
    return t;
    }
 
*/
    
    
    public void run() {
    
    
           /* Ejecutor ejecutor = (Ejecutor) Naming.lookup("rmi://" + host + ":" + port + "/" + tarea.getNombreServicio());
                System.out.println("Servicio Encontrado...host: "+this.host+" port: "+this.port);*/
   try {   
            String name = "Compute";
            
            Registry registry = LocateRegistry.getRegistry(host,port);
            
            Compute comp = (Compute) registry.lookup(name);
            
            if(task == 1){
            Pi task = new Pi(n);
            BigDecimal pi = comp.executeTask(task);
            System.out.println("Pi: "+ pi);
            }else if(task ==2){
            Factorial task2 = new Factorial(n);
            int fact = comp.executeTask(task2);
            System.out.println("Factorial: "+fact);
            }else if(task ==3){
            Fibonacci task3 = new Fibonacci(n);
            int fib = comp.executeTask(task3);
            System.out.println("Fibonacci "+fib);
            }
            
            
        } catch (Exception e) {
            System.err.println("Compute exception:");
            e.printStackTrace();
        }
                         
        
    
    }
    
}
