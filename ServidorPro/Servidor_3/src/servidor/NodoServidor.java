/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.util.List;

/**
 *
 * @author kid
 */
public class NodoServidor {

    String nombre;
    String ip;
    int puertoUnicast;

   
    List<Archivo> archivoList;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<Archivo> getArchivoList() {
        return archivoList;
    }

    public void setArchivoList(List<Archivo> archivoList) {
        this.archivoList = archivoList;
    }
 public int getPuertoUnicast() {
        return puertoUnicast;
    }

    public void setPuertoUnicast(int puertoUnicast) {
        this.puertoUnicast = puertoUnicast;
    }
}
