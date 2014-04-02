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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import servidor.GenericServer;

/**
 *
 * @author kid
 */
public class EnviaMulticast extends Thread {

    GenericServer server;
    
    public EnviaMulticast(GenericServer server) throws IOException {
        this.server = server;
    }

    public void run() {

        MulticastSocket enviador;
        try {

            enviador = new MulticastSocket(6789);
            byte[] dato;

            String datoString = "0|"+server.portServer+"|"+server.name+"|";
            dato = datoString.getBytes();
            DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), 6789);

            // Envío
            enviador.send(dgp);
            System.out.println("Enviado");
        } catch (IOException ex) {
            Logger.getLogger(EnviaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
