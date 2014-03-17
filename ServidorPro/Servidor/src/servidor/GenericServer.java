package servidor;

import hilo.EscuchaMulticast;
import hilo.EnviaMulticast;
import hilo.EscuchaUnicast;
import hilo.TaskThread;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import servidor.Ejecutor;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.lang.*;


public class GenericServer {

    public int port;
    public int portServer;
    public int ipAddress;
    public String name;
    public Ejecutor imp;
    public List<NodoServidor> nodoServidorList;
    public TaskThread task;

    public GenericServer(int port,int portServer, String name) {
        this.nodoServidorList = new ArrayList<>();
        try {

            this.port = port;
            this.portServer = portServer;
            this.name = name;
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
            }

            System.out.println("Registrando multicast...");
            this.startMulticast();
            System.out.println("Multicast registrado...");
            
           task = new TaskThread(this);
           task.start();
           
           

        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public void startMulticast() throws IOException, InterruptedException {

        EscuchaUnicast escuchaUni = new EscuchaUnicast(this);
        escuchaUni.start();
        
        EscuchaMulticast escucha = new EscuchaMulticast(this);
        escucha.start();

        EnviaMulticast envia = new EnviaMulticast(this);
        envia.start();
        synchronized (this) {
            this.wait();
        }

        System.out.println("Lista de servidores conectados");
        for (int i = 0; i < this.nodoServidorList.size(); i++) {

            System.out.println("Servidor: " + nodoServidorList.get(i).nombre + " "+nodoServidorList.get(i).ip + " "+nodoServidorList.get(i).puertoUnicast);
        }

    }

    public void stopOtherServers(int task){
        System.out.println("Intentando detener");
        String datoString = "3|"+this.portServer+"|"+this.name+"|";
        MulticastSocket enviador;
        try {
            enviador = new MulticastSocket(6789);
            byte[] dato;
            dato = datoString.getBytes();
            DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), 6789);
            // Envío
            enviador.send(dgp);
            System.out.println("Deteniendo servidores");
        } catch (IOException ex) {
            Logger.getLogger(EnviaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    //Enviar mensaje por multicast
    
    }
    public static void main(String args[]) {
        int nroServ = 0, port = 0,portServer=0;
        String name="";

        if (!((0 < args.length) && (args.length < 4))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("CalculatorServer <port>");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(args[0]);
            portServer = Integer.parseInt(args[1]);
            name = (String) args[2];

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

        new GenericServer(port,portServer, name);
    }
}
