import java.rmi.*;
import java.rmi.registry.*;
//import java.lang.*;

// este ejemplo fue realizado con fines ilustrativos
// no se hace enfasis en todas las verificaciones que
// que una aplicacion deberia tener.

// Fue conpilado y corrido usando jdk-1.2.2

public class GenericServer {
    
    private int port;
	   	
	
    public GenericServer(int port) {
	try {
			
	    this.port = port;
        if (System.getSecurityManager()==null) System.setSecurityManager(new RMISecurityManager());
        Ejecutor imp=new Ejecutor_Imp();
		String nombreServicio="Motor_Computo";						
		// Registra con el nombre CalculatorService al objeto c 
		// en el Registry que se encuentra el el host <localhost>
		// y puerto <port>			
		Naming.rebind("rmi://localhost:"+port+"/"+nombreServicio, imp);	
		System.out.println("publicando y escuchando peticiones...");

	} catch (Exception e) {
	    System.out.println("Trouble: " + e);
	}
    }	
    
    public static void main(String args[]) {
	int nroServ=0, port = 0;	

	if (!((0 < args.length) && (args.length < 2))) {
	    System.err.print("Parametros incorrectos: ");
	    System.err.println("CalculatorServer <port>");
	    System.exit(1);
	}

	try {
	    port = Integer.parseInt(args[0]);

	    // Crea un Registry en el puerto especificado
	    LocateRegistry.createRegistry(port);	    
	}
	catch (RemoteException re) {
	    System.out.println();
	    System.out.println("RemoteException");
	    System.out.println(re);
	}
	catch (Exception e) {
	    System.out.println();
	    System.out.println("java.lang.Exception");
	    System.out.println(e);
	}

	new GenericServer(port);
    }
}
