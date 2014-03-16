/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;
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

// Un array de bytes con tamaño suficiente para recoger el mensaje enviado, 
// bastaría con 4 bytes.
            byte[] dato = new byte[1024];

// Se espera la recepción. La llamada a receive() se queda
// bloqueada hasta que llegue un mesnaje.
            DatagramPacket dgp = new DatagramPacket(dato, dato.length);
            int i = 0;
            while (i < 2) {
                escucha.receive(dgp);
                System.out.println("Escuchado");
// Obtención del dato ya relleno.
//
                dato = dgp.getData();

               

                if (dato[0] == 0) {
                    //Identificacion
                    NodoServidor nodo = new NodoServidor();
                    nodo.setNombre("Nombre_" + i);
                    nodo.setIp(dgp.getSocketAddress().toString());
                    dato = Arrays.copyOfRange(dato, 1, dato.length);
                    ByteBuffer wrapped = ByteBuffer.wrap(dato); // big-endian by default
                    nodo.setPuertoUnicast(wrapped.getInt());
                    server.nodoServidorList.add(nodo);

                }
                i++;
            }
            synchronized (server) {
                server.notify();
            }

        } catch (Exception ex) {
            System.out.println(ex);
            Logger.getLogger(EscuchaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
