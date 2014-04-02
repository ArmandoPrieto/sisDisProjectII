/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilo;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Ejecutor;
import servidor.Ejecutor_Imp;
import servidor.GenericServer;

/**
 *
 * @author kid
 */
public class TaskThread extends Thread {

    public GenericServer server;

    public TaskThread(GenericServer server) {

        this.server = server;

    }

    public void run() {
        Ejecutor imp = null;
        try {
            imp = new Ejecutor_Imp(this.server);
            String nombreServicio = "Motor_Computo";
            Naming.rebind("rmi://localhost:" + server.port + "/" + nombreServicio, imp);
        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println("publicando y escuchando peticiones...");

    }
    
    

}
