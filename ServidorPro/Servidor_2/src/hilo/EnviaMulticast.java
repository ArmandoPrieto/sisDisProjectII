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

/**
 *
 * @author kid
 */
public class EnviaMulticast extends Thread {

    public EnviaMulticast() throws IOException {

    }

    public void run() {

        MulticastSocket enviador;
        try {

            enviador = new MulticastSocket(6789);

// El dato que queramos enviar en el mensaje, como array de bytes.
            byte[] dato = new byte[]{0};

            ByteBuffer b = ByteBuffer.allocate(4);
//b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.

            b.putInt(60001);

            byte[] concatenate = new byte[dato.length + b.array().length];
            System.arraycopy(dato, 0, concatenate, 0, dato.length);
            System.arraycopy(b.array(), 0, concatenate, dato.length, b.array().length);

// Usamos la direccion Multicast 230.0.0.1, por poner alguna dentro del rango
// y el puerto 55557, uno cualquiera que esté libre.
            DatagramPacket dgp = new DatagramPacket(concatenate, concatenate.length, InetAddress.getByName("230.0.0.1"), 6789);

// Envío
            enviador.send(dgp);
            System.out.println("Enviado");
        } catch (IOException ex) {
            Logger.getLogger(EnviaMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
