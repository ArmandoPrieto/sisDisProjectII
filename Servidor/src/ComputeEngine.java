
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComputeEngine implements Compute {

    public List<NodoServidor> nodoServidorList;
    public int port; //rebind
    public int portServer;  //unicast
    public String name;
    public String nombreServicio;
    public InetAddress ipAddress;
    private EscuchaMulticast escucha;
    private EnviaMulticast envia;
    private EscuchaUnicast escuchaUni;
    Registry registry;
    public boolean global = false;
    public ComputeEngine() {
        super();
    }

    public <T> T executeTask(Task<T> t) {
      
       System.out.println("Ejecutando tarea de cliente");
       T te = t.execute();
       System.out.println("TAREA EJECUTADA");
       if(global==false){
           this.stopOtherServers();
           System.out.println("TE");
       return te;
       }else{
           System.out.println("NULL");
           return null;
       }
    }

    public void startServer(int port, int portServer, String name, String servicio) {
        this.nodoServidorList = new ArrayList<NodoServidor>();
        try {

            this.port = port;
            this.portServer = portServer;
            this.name = name;
            this.nombreServicio = servicio;
            this.ipAddress = InetAddress.getLocalHost();
            //this.ejecutarTarea = new Ejecutor_Imp(this);            
            System.out.println("Iniciando Servidor: " + this.toString());
            

            System.out.println("GenericServer: Registrando multicast...");
            this.startMulticast();
            System.out.println("GenericServer: Multicast registrado...");

            /* task = new TaskThread(this);
             task.start();
             */
            //Ejecutor imp = new Ejecutor_Imp2();            
            //Ejecutor stub = (Ejecutor) UnicastRemoteObject.exportObject(imp, 0);            
            //System.out.println("rmi://localhost:" + this.name + "/"+this.port + "/" + this.nombreServicio);
            //Naming.rebind("rmi://localhost:" + this.port + "/" + this.nombreServicio, imp);                        
            //System.out.println("TaskThread: publicando y escuchando peticiones...");                
            //la tarea murió se interrumpio.
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    public void startMulticast() throws IOException, InterruptedException {

         escuchaUni = new EscuchaUnicast(this);
         escuchaUni.start();
         
        escucha = new EscuchaMulticast(this);
        escucha.start();

        envia = new EnviaMulticast(this);
        envia.elegirMensaje(0);
        envia.start();

//        synchronized (this) { 
//            this.wait(); 
        //espera a que se sincronice el objeto server en cada uno de los hilos, 
        //pero el ultimo no se sincroniza bien y no registra todo esto no deberia pq estos proceso son while true
//        }
    }

    public void stopOtherServers() { // int task no se usa este parametro??
        System.out.println("Generic Server: Intentando detener");
        String datoString = "3|" + this.portServer + "|" + this.name + "|" + this.nombreServicio + "|";
        MulticastSocket enviador;
        try {
            enviador = new MulticastSocket(6789);
            byte[] dato;
            dato = datoString.getBytes();
            DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), 6789);
            // Envío
            enviador.send(dgp);
            //System.out.println("GenericServer: Deteniendo servidores");
        } catch (IOException ex) {
            Logger.getLogger(EnviaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Enviar mensaje por multicast

    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("GenericServer ");
        ret.append("Nombre='" + this.name + "'");
        ret.append("IP='" + this.ipAddress + "'");
        ret.append("puerto unicast='" + this.portServer + "'");
        ret.append("puerto multicast='" + this.port + "'");
        ret.append("Servicio que Publica='" + this.nombreServicio + "'");
        return ret.toString();
    }

    public static void main(String[] args) {
        int nroServ = 0, port = 0, portServer = 0;
        String nameServer = "", servicio = "";

        System.out.println(System.getProperty("java.version"));
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        port = Integer.parseInt(args[0]);
        portServer = Integer.parseInt(args[1]);
        nameServer = (String) args[2];
        servicio = (String) args[3];
        ComputeEngine eng = new ComputeEngine();
        eng.startServer(port, portServer, nameServer, servicio);

        //FUNCIONA
        try {
         String name = "Compute";
         Compute engine = eng;
         //Compute engine = new ComputeEngine();
         Compute stub =
         (Compute) UnicastRemoteObject.exportObject(engine,0);
         eng.registry= LocateRegistry.createRegistry(port);
         
         //Registry registry = LocateRegistry.getRegistry(port);
         eng.registry.rebind(name, stub);
         System.out.println("ComputeEngine bound");
         } catch (Exception e) {
         System.err.println("ComputeEngine exception:");
         e.printStackTrace();
         }
    }
}
