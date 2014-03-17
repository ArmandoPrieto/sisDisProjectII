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
import java.net.Socket;
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
public class EscuchaUnicast extends Thread {

    GenericServer server;

    public EscuchaUnicast(GenericServer server) throws IOException {

        this.server = server;
    }

    public void run() {

        try {
            int localPortNum;
            localPortNum = server.portServer;
            DatagramSocket mySocket = new DatagramSocket(localPortNum);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            while (server.nodoServidorList.size() < 2) {
                mySocket.receive(packet);
                String datoString = new String(buffer);
                StringTokenizer token = new StringTokenizer(datoString, "|");
                String tipoMensaje = token.nextToken();
                String serverPort = token.nextToken();
                String serverName = token.nextToken();

                if ((tipoMensaje.compareTo("1") == 0)) {

                    //Identificacion de ACK
                    NodoServidor nodo = new NodoServidor();
                    nodo.setNombre(serverName);
                    nodo.setIp(packet.getSocketAddress().toString());
                    nodo.setPuertoUnicast(Integer.parseInt(serverPort));
                    nodo.setIsActive(true);
                    synchronized (server.nodoServidorList) {
                    boolean flag=false;    
                    for(int i=0; i< server.nodoServidorList.size();i++){

                        if(server.nodoServidorList.get(i).getNombre().compareTo(nodo.getNombre())==0){
                        flag = true;
                        break;
                        }
                    
                    }    
                    
                    if(flag==false){
                        server.nodoServidorList.add(nodo);
                    }
                    
                    }
                    System.out.println("Ack Recibido");

                }

            }
            synchronized (server) {
                server.notify();
            }
            mySocket.close();
        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(EscuchaUnicast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
