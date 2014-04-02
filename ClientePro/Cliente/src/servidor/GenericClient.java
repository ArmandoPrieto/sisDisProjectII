package Cliente;

import hilo.Consumidor;
import java.io.BufferedReader;
import java.io.FileReader;
import servidor.Ejecutor;
import java.rmi.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import servidor.JobServer;

public class GenericClient {

    List<JobServer> serverList;
    String multicastIp;
    int multicastPort;
    int MAX_NUMBER_SERVERS = 10;
    
    public GenericClient(String multicastIp, int multicastPort){
        
        this.multicastPort = multicastPort;
        this.multicastIp = multicastIp;
        serverList = new ArrayList<>();
        this.doWork();
    
    }
    
    public static void main(String[] args) {
      

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        if (!((0 < args.length) && (args.length < 3))) {
            System.err.print("Parametros incorrectos: ");
            System.err.println("CalculatorClient <hostName> <port>");
            System.exit(1);
        }
        
        GenericClient client = new GenericClient(args[0],Integer.parseInt(args[1]));
        

       
    }
    
    
    public void readServers(){
    
                    BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("src/Cliente/serversList.txt"));
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
      
        this.readServers();
        
        Consumidor[] consumidor = new Consumidor[this.MAX_NUMBER_SERVERS];
        
        for(int i = 0; i<this.serverList.size();i++){
            consumidor[i] = new Consumidor(this.serverList.get(i).getIp(), this.serverList.get(i).getPuerto());
            consumidor[i].start();
        }
        
       
        
    }
    
    
}
