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
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kid
 */
public class EscuchaUnicast extends Thread {

    ComputeEngine server;

    public EscuchaUnicast(ComputeEngine server) throws IOException {

        this.server = server;
        this.setName("EscuchaUnicast");
    }

    public void run() {

        try {            
            int localPortNum = server.portServer;
            DatagramSocket mySocket = new DatagramSocket(localPortNum);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Escucha Unincast: nro. de servidores en lista: " + server.nodoServidorList.size());
            while (true) { // (server.nodoServidorList.size() < 2) deberia ser un while true 
                System.out.println("Escucha Unincast: Escuchando ");
                mySocket.receive(packet); //se queda bloqueada la operacion
                               
                //System.out.println(packet.getData().toString());
                //System.out.println(buffer.toString());
                
                String datoString = new String(buffer);
                StringTokenizer token = new StringTokenizer(datoString, "|");
                String tipoMensaje = token.nextToken();
                String serverPort = token.nextToken();
                String serverName = token.nextToken();
                String nameService = token.nextToken();

                if ((tipoMensaje.compareTo("1") == 0)) {

                    //Identificacion de ACK
                    NodoServidor nodo = new NodoServidor();
                    nodo.setNombre(serverName);
                    nodo.setIp(packet.getSocketAddress().toString());
                    nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                    
                    nodo.setIsActive(true);
                    System.out.println("Escucha Unicast: '"+nodo.toString()+"'");
                    synchronized (server.nodoServidorList) {
                    boolean flag=false;    
                    for(int i=0; i< server.nodoServidorList.size();i++){

                        if((server.nodoServidorList.get(i).getNombre().compareTo(nodo.getNombre())==0) || 
                                (server.nombreServicio.compareTo(nameService)!=0)){
                        flag = true;
                        break;
                        }
                    
                    }    
                    
                    if(flag==false){
                        boolean ok = server.nodoServidorList.add(nodo);

                        if (ok == true) System.out.println("Escucha Unicast: Nodo añadido a la lista!");
                          else System.out.println("Escucha Unicast: Nodo duplicado, no añadido a la lista!");
                        
                    }
                    
                    }
                    System.out.println("Escucha Unincast: Ack 1 Recibido");

                }

            System.out.println(this.getName() + " Lista de servidores conectados a: "+this.server.toString());
            for (int i = 0; i < this.server.nodoServidorList.size(); i++) {

                System.out.println(this.server.nodoServidorList.get(i).toString());
            }
            
            }
//            System.out.println("Escucha Unincast: Cerrando escucha Unicast");
//            synchronized (server) {
//                server.notify();
//            }            
//            mySocket.close();
            
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(EscuchaUnicast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
