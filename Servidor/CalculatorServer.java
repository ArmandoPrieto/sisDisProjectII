import java.rmi.*;
import java.rmi.registry.*;
//import java.lang.*;

// este ejemplo fue realizado con fines ilustrativos
// no se hace enfasis en todas las verificaciones que
// que una aplicacion deberia tener.

// Fue conpilado y corrido usando jdk-1.2.2

public class CalculatorServer {
    
    private int port;
	private int nroServ;	

    public CalculatorServer(int port, int nroServ) {
	try {
	
		String nombreServicio="";
	    this.port = port;
		this.nroServ = nroServ;
		
		if (nroServ==1) {
			Calculator c = new CalculatorImpl();
			((CalculatorImpl)c).hello();
			nombreServicio="CalculatorService";						
			// Registra con el nombre CalculatorService al objeto c 
			// en el Registry que se encuentra el el host <localhost>
			// y puerto <port>			
			Naming.rebind("rmi://localhost:"+port+"/"+nombreServicio, c);			
			}

	} catch (Exception e) {
	    System.out.println("Trouble: " + e);
	}
    }
    
    public static void main(String args[]) {
	int nroServ=0, port = 0;	

	if (!((0 < args.length) && (args.length < 3))) {
	    System.err.print("Parametros incorrectos: ");
	    System.err.println("CalculatorServer <port>");
	    System.exit(1);
	}

	try {
	    port = Integer.parseInt(args[0]);
		nroServ = Integer.parseInt(args[1]);

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

	new CalculatorServer(port,nroServ);
    }
}
