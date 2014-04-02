/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;


/**
 *
 * @author kid
 */
public class EnviaMulticast extends Thread {

    ComputeEngine server;
    String mensaje;
    
    public EnviaMulticast(ComputeEngine server) throws IOException {
        this.server = server;
        this.setName("EnviaMulticast");
    }
    
    public void elegirMensaje(int nro){
        
        if (nro==0) mensaje = "0|"+server.portServer+"|"+server.name+"|"+server.nombreServicio+"|";
        else if (nro==2) mensaje = "2|"+server.portServer+"|"+server.name+"|"+server.nombreServicio+"|";
    
    }

    public void run() {

        MulticastSocket enviador;
        boolean ok = true;
        boolean terminar = false;
        
        try {
            enviador = new MulticastSocket(6789);
            byte[] dato;
            while (true) {                               

                if (ok){
                    dato = mensaje.getBytes();
                    DatagramPacket dgp = new DatagramPacket(dato, dato.length, InetAddress.getByName("230.0.0.1"), 6789);
                    ok = false;
                    // Envío
                    enviador.send(dgp);
                    System.out.println("Envia Multicast: " + mensaje);
                    if (terminar) break;
                    
                } else {
              //MUERTE DEL SERVIDOR:
//                    long espera = (long) (Math.random() * 10000);
//                     Thread.sleep(espera);
//                    if (Math.random()>=0.001) {
//                        elegirMensaje(2);
//                        ok = true;
//                        terminar = true;
//                        System.out.println("Servidor Muriendo");
//                        espera = (long) (Math.random() * 5000);                         
//                        Thread.sleep(espera);                      
//                    }
                }
                
            }//while
            //MUERTE DEL SERVIDOR:
//            this.server.escucha.interrupt();
//            this.server.escuchaUni.interrupt();
//            System.exit(0);
//            //this.interrupt(); //no hace falta
            
        } catch (Exception ex) {
            Logger.getLogger(EnviaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
