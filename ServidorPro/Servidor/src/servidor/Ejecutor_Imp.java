package servidor;

import servidor.Ejecutor;
import java.rmi.*;
import java.rmi.server.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Ejecutor_Imp extends UnicastRemoteObject implements Ejecutor {

    GenericServer server;
    
    public Ejecutor_Imp(GenericServer server) throws RemoteException {
        
        super();
        this.server = server;
    }

    public boolean ejecutar(Tarea t) throws RemoteException {
        return t.proceso();
    }

    
    @Override
    public  boolean ejecutar() throws RemoteException {
        try {
            Thread.sleep((long)(Math.random() * 5000));
        } catch (InterruptedException ex) {
            Logger.getLogger(Ejecutor_Imp.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Hola Mundo");
        server.stopOtherServers(0);
        
        return true;
    }
    }

