/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kid
 */
public class EscuchaMulticast extends Thread {

    ComputeEngine server;

    public EscuchaMulticast(ComputeEngine server) throws IOException {

        this.server = server;
        this.setName("EscuchaMulticast");
    }

    public void run() {

        // El mismo puerto que se uso en la parte de enviar.
        MulticastSocket escucha;
        try {

            escucha = new MulticastSocket(6789);

// Nos ponemos a la escucha de la misma IP de Multicast que se uso en la parte de enviar.
            escucha.joinGroup(InetAddress.getByName("230.0.0.1")); 

            byte[] dato = new byte[1024];
            
            DatagramPacket dgp = new DatagramPacket(dato, dato.length);
            
            while (true) {
                System.out.println("Escucha Multicast: Escuchando");
                escucha.receive(dgp);
                // Obtención del dato ya relleno.                
                String datoString = new String(dgp.getData());
                System.out.println("Escucha Multicast: Recibido " + datoString);                                                
                StringTokenizer token = new StringTokenizer(datoString,"|");
                String tipoMensaje = token.nextToken();
                String serverPort = token.nextToken();
                String serverName = token.nextToken();
                String nameService = token.nextToken();
                
                NodoServidor nodo = new NodoServidor();
                nodo.setNombre(serverName);                
                nodo.setIp(dgp.getSocketAddress().toString()); //es el mismo que la IP 230.0.0.1?NO
                nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                nodo.setIsActive(true);
                 System.out.println("Escucha Multicast: " + nodo.toString());   
                if ((tipoMensaje.compareTo("0") == 0) && (server.name.compareTo(serverName)!=0)
                        && (server.nombreServicio.compareTo(nameService)==0)) {
                    //Identificacion
                  
                   //nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                   synchronized (server.nodoServidorList) {
                       
                        boolean ok = server.nodoServidorList.add(nodo);

                        if (ok == true) System.out.println("Escucha Multicast: Nodo añadido a la lista!");
                          else System.out.println("Escucha Multicast: Nodo duplicado, no añadido a la lista!");
                        
                   }
                     //Devolver ACK
                    enviarAck(dgp.getAddress(),Integer.parseInt(serverPort)); //no entiendo porque envian 1? Por si no recibió el multicast con Ack 0
                    
                }else if ((tipoMensaje.compareTo("1") == 0)  && (server.name.compareTo(serverName)!=0) 
                        && (server.nombreServicio.compareTo(nameService)==0)) {
                
                        //Identificacion de ACK
                        System.out.println("Escucha Multicast: Ack 1 Recibido");
                        //nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                        synchronized (server.nodoServidorList) {
                        
                            boolean ok = server.nodoServidorList.add(nodo);                        
                        
                            if (ok == true) System.out.println("Escucha Multicast: Nodo añadido a la lista!");
                            else System.out.println("Escucha Multicast: Nodo duplicado, no añadido a la lista!");
                        }                                               
                    
                }else if ((tipoMensaje.compareTo("2") == 0)  && (server.name.compareTo(serverName)!=0)
                        && (server.nombreServicio.compareTo(nameService)==0)) {
                    //MUERTE DEL SERVIDOR:
                    //enviarAck(dgp.getAddress(),Integer.parseInt(serverPort));                    
                    
                    synchronized (server.nodoServidorList) {
                        
                            boolean ok = server.nodoServidorList.remove(nodo);                        
                        
                            if (ok == true) System.out.println("Escucha Multicast: Nodo removido de la lista!");
                            else System.out.println("Escucha Multicast: Nodo no removido de la lista!");
                        }                   
                    System.out.println("Generando un nuevo Servidor  por Muerte del anterior");
                    Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
                    //ComputeEngine nuevoProceso = new ComputeEngine(60010, 61010,"Server_10",server.nombreServicio);
                
                }else if ((tipoMensaje.compareTo("3") == 0)  && (server.name.compareTo(serverName)!=0)
                        && (server.nombreServicio.compareTo(nameService)==0)) {
                   /*
                    PROBRAR UNBIND RMI
                    server.registry.unbind("Compute");
                    boolean unexportObject = UnicastRemoteObject.unexportObject(server.registry,true); 
                    System.out.println("Server detenido: "+unexportObject);
                    */
                    server.global = true;
                   
                }
               
               if(server.nodoServidorList.size() >= 2){ //que hace esta instruccion??
                synchronized (server) {
                        server.notify();
                }
               }
               
            System.out.println(this.getName() + " Lista de servidores conectados a: "+this.server.toString());
            for (int i = 0; i < this.server.nodoServidorList.size(); i++) {

                System.out.println(this.server.nodoServidorList.get(i).toString());
            }               
               
         }            

        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(EscuchaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void enviarAck(InetAddress address, int port) {

        try {
            
            byte[] dato;
            String datoString = "1|"+server.portServer+"|"+server.name+"|"+server.nombreServicio+"|";
            dato= datoString.getBytes();
            DatagramSocket mySocket = new DatagramSocket( );
            DatagramPacket packet;
            packet = new DatagramPacket(dato, dato.length, address,port);
            mySocket.send(packet);
            System.out.println("Escucha Multicast: Ack 1 Enviado a la dirección: " + address.toString() + " puerto: " + port);
            mySocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
