package servidor;

import servidor.Ejecutor;
import java.rmi.*;
import java.rmi.server.*;

public class Ejecutor_Imp extends UnicastRemoteObject implements Ejecutor {

    protected Ejecutor_Imp() throws RemoteException {
        super();
    }

    public boolean ejecutar(Tarea t) throws RemoteException {
        return t.proceso();
    }

    public boolean ejecutar() throws RemoteException {
        System.out.println("Hola Mundo");
        return true;
    }
}
