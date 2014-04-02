/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hilo;

import Cliente.Tarea;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import servidor.Ejecutor;

/**
 *
 * @author kid
 */
public class Consumidor extends Thread{
    
    String host;
    int port;
    
    public Consumidor(String host, int port){
    
        this.host = host;
        this.port = port;
    
    }

    
    
    public void run(){
    
     try {
          
            String nombreServicio = "Motor_Computo";
            System.out.println("Buscando Servicio... host: "+this.host+" port: "+this.port);
            Ejecutor ejecutor = (Ejecutor) Naming.lookup("rmi://" + host + ":" + port + "/" + nombreServicio);
            System.out.println("Servicio buscado... host: "+this.host+" port: "+this.port);
            
            boolean respuesta;
            //Tarea tarea = new Tarea_Impl();
            //respuesta=e.ejecutar(tarea); //Esta mandando a ejecutar un procedimiento remoto con un objeto local!!
            respuesta = ejecutor.ejecutar();
            if (respuesta) {
                System.out.println("Se ejecuta correctamente");
            } else {
                System.out.println("No se ejecuta correctamente");
            }
        } catch (MalformedURLException murle) {
            System.out.println();
            System.out.println(
                    "MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println(
                    "RemoteException");
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println();
            System.out.println(
                    "NotBoundException");
            System.out.println(nbe);
        } catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println(
                    "java.lang.ArithmeticException");
            System.out.println(ae);
        } catch (Exception e) {
            System.out.println();
            System.out.println("java.lang.Exception");
            System.out.println(e);
        }
    
    }
    
}
