package servidor;

import hilo.EscuchaMulticast;
import hilo.EnviaMulticast;
import java.io.IOException;
import servidor.Ejecutor;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.List;
//import java.lang.*;

// este ejemplo fue realizado con fines ilustrativos
// no se hace enfasis en todas las verificaciones que
// que una aplicacion deberia tener.
// Fue conpilado y corrido usando jdk-1.2.2
public class GenericServer {

    private int port;

    public List<NodoServidor> nodoServidorList;

    public GenericServer(int port) {
        this.nodoServidorList = new ArrayList<>();
        try {

            this.port = port;
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }

            System.out.println("Registrando multicast...");
            this.startMulticast();
            System.out.println("Multicast registrado...");
            Ejecutor imp = new Ejecutor_Imp();
            String nombreServicio = "Motor_Computo";
            // Registra con el nombre CalculatorService al objeto c 
            // en el Registry que se encuentra el el host <localhost>
            // y puerto <port>			
            Naming.rebind("rmi://localhost:" + port + "/" + nombreServicio, imp);
            System.out.println("publicando y escuchando peticiones...");

        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public void startMulticast() throws IOException, InterruptedException {

        EscuchaMulticast escucha = new EscuchaMulticast(this);
        escucha.start();

        EnviaMulticast envia = new EnviaMulticast();
        envia.start();
        synchronized (this) {
            this.wait();
        }

        System.out.println("Lista de servidores conectados");
        for (int i = 0; i < this.nodoServidorList.size(); i++) {

            System.out.println("Servidor: " + nodoServidorList.get(i).nombre + " "+nodoServidorList.get(i).ip + " "+nodoServidorList.get(i).puertoUnicast);
        }

    }

    public static void main(String args[]) {
        int nroServ = 0, port = 0;

        if (!((0 < args.length) && (args.length < 2))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("CalculatorServer <port>");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(args[0]);

            // Crea un Registry en el puerto especificado
            LocateRegistry.createRegistry(port);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        } catch (Exception e) {
            System.out.println();
            System.out.println("java.lang.Exception");
            System.out.println(e);
        }

        new GenericServer(port);
    }
}
