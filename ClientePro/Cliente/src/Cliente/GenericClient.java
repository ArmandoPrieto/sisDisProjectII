package Cliente;

import servidor.Ejecutor;
import java.rmi.*;
import java.net.MalformedURLException;

public class GenericClient {
    public static void main(String[] args) {
	String host = null;
	int port =0;
	String servicio="";

    if (System.getSecurityManager()==null) System.setSecurityManager(new RMISecurityManager());
	
	if (!((0 < args.length) && (args.length < 3))) {
	    System.err.print("Parametros incorrectos: ");
	    System.err.println("CalculatorClient <hostName> <port>");
	    System.exit(1);
	}

	try {
	    host = args[0];
	    port = Integer.parseInt(args[1]);
		
		String nombreServicio="Motor_Computo";
System.out.println("Host: "+host);		
System.out.println("Port: "+port);

	    // Busca al objeto que ofrece el servicio con nombre 
	    // CalculatorService en el Registry que se encuentra en
	    // el host <host> y puerto <port>
		System.out.println("Buscando Servicio...");
	    Ejecutor e = (Ejecutor) Naming.lookup("rmi://" + host + ":" + port+ "/"+ nombreServicio);
		System.out.println("Servicio buscado");
        Tarea tarea= new Tarea_Impl();
		boolean respuesta;
        //respuesta=e.ejecutar(tarea); //Esta mandando a ejecutar un procedimiento remoto con un objeto local!!
	    respuesta=e.ejecutar();		
		if (respuesta) System.out.println("Se ejecuta correctamente");
		else System.out.println("No se ejecuta correctamente");
	}
	catch (MalformedURLException murle) {
	    System.out.println();
	    System.out.println(
			       "MalformedURLException");
	    System.out.println(murle);
	}
	catch (RemoteException re) {
	    System.out.println();
	    System.out.println(
			       "RemoteException");
	    System.out.println(re);
	}
	catch (NotBoundException nbe) {
	    System.out.println();
	    System.out.println(
			       "NotBoundException");
	    System.out.println(nbe);
	}
	catch (java.lang.ArithmeticException ae) {
	    System.out.println();
	    System.out.println(
			       "java.lang.ArithmeticException");
	    System.out.println(ae);
	}
	catch (Exception e) {
	    System.out.println();
	    System.out.println("java.lang.Exception");
	    System.out.println(e);
	}
    }
}
