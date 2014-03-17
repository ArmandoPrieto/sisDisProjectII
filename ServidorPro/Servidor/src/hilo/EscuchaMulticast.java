/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.GenericServer;
import servidor.NodoServidor;

/**
 *
 * @author kid
 */
public class EscuchaMulticast extends Thread {

    GenericServer server;

    public EscuchaMulticast(GenericServer server) throws IOException {

        this.server = server;
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
                escucha.receive(dgp);
                System.out.println("Escuchado");
// Obtención del dato ya relleno.
                String datoString = new String(dgp.getData());
                
                StringTokenizer token = new StringTokenizer(datoString,"|");
                String tipoMensaje = token.nextToken();
                String serverPort = token.nextToken();
                String serverName = token.nextToken();
                
                NodoServidor nodo = new NodoServidor();
                    nodo.setNombre(serverName);
                    nodo.setIp(dgp.getSocketAddress().toString());
                    nodo.setIsActive(true);
                    
                if ((tipoMensaje.compareTo("0") == 0) && (server.name.compareTo(serverName)!=0)) {
                    //Identificacion
                  
                    nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                   synchronized (server.nodoServidorList) {
                        server.nodoServidorList.add(nodo);
                   }
                     //Devolver ACK
                    enviarAck(dgp.getAddress(),Integer.parseInt(serverPort));
                    
                }else if ((tipoMensaje.compareTo("1") == 0)  && (server.name.compareTo(serverName)!=0)) {
                
                    //Identificacion de ACK
                        nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                        synchronized (server.nodoServidorList) {
                        server.nodoServidorList.add(nodo);
                        }
                        System.out.println("Ack Recibido");
                       
                    
                }else if ((tipoMensaje.compareTo("2") == 0)  && (server.name.compareTo(serverName)!=0)) {
                    
                        enviarAck(dgp.getAddress(),Integer.parseInt(serverPort));
                
                }else if ((tipoMensaje.compareTo("3") == 0)  && (server.name.compareTo(serverName)!=0)) {
                
                    server.task.interrupt();
                    System.out.println("Tarea detenida en servidor "+this.server.name);
                    
                }
               
               if(server.nodoServidorList.size() >= 2){
                synchronized (server) {
                        server.notify();
                }
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
            String datoString = "1|"+server.portServer+"|"+server.name+"|";
            dato= datoString.getBytes();
            DatagramSocket mySocket = new DatagramSocket( );
            DatagramPacket packet;
            packet = new DatagramPacket(dato, dato.length, address,port);
            mySocket.send(packet);
            System.out.println("Ack Enviado");
            mySocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
