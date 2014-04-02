import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Client {
    
    List<JobServer> serverList;
    String multicastIp;
    int multicastPort;
    int MAX_NUMBER_SERVERS = 10;
    
     public Client(String multicastIp, int multicastPort){
        
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
        serverList = new ArrayList<JobServer>();
        
        this.readServers();
        this.doWork();
    
    }
    
    
    public void readServers(){
    
                    BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("src/serversList.txt"));
                        StringTokenizer token;
                        JobServer jServer;
			while ((sCurrentLine = br.readLine()) != null) {
                            jServer = new JobServer();
                            token = new StringTokenizer(sCurrentLine);
                            jServer.setIp(token.nextToken());
                            jServer.setPuerto(Integer.parseInt(token.nextToken()));
                            jServer.setNombre(token.nextToken());
                            this.serverList.add(jServer);
                        }
                        
                }catch(Exception e ){
                e.printStackTrace();
                
                }
    
    
    
    }
      
    
    public void doWork(){  
        
        try {
        
        int task, nro;
        
        Consumidor[] consumidor = new Consumidor[this.MAX_NUMBER_SERVERS]; //ArrayList?
        
        System.out.println("Introduzca tarea a Realizar [1-Calculo de Pi /2-Factorial/3-Fibonacci]: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        task = Integer.parseInt(br.readLine());
        System.out.println("Tarea Elegida: " + task);
        System.out.println("Introduzca el Valor de n:");
        nro = Integer.parseInt(br.readLine());
        System.out.println("Nro Elegido: " + nro);
        
        for(int i = 0; i<this.serverList.size();i++){
            consumidor[i] = new Consumidor(this.serverList.get(i).getIp(), this.serverList.get(i).getPuerto(), task,nro);
            try{
            consumidor[i].start();
            } catch (Exception ex) {
                System.out.println("En el Servidor: " + this.serverList.get(i).getNombre());
            }
            
        }
        } catch (Exception ex) {
            System.out.println(Client.class.getName() + " "+ex);
        }
       
        
    }
    
    


    
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
       
            
            Client client = new Client(args[0],Integer.parseInt(args[1]));
       
    }    
}