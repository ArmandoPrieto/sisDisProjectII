package servidor;

import java.rmi.*;

public interface Ejecutor
        extends java.rmi.Remote {

    public boolean ejecutar(Tarea t) throws RemoteException;

    public boolean ejecutar() throws RemoteException;
}
