/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.List;

/**
 *
 * @author kid
 */
public class NodoServidor {

    String nombre;
    String ip;
    int puertoUnicast;
    boolean isActive;
    List<Archivo> archivoList; //?


    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
   
    
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
    
   public String toString(){
          StringBuffer ret = new StringBuffer();
          ret.append( "NodoServidor " );
          ret.append( "Nombre='" + this.nombre + "'");
          ret.append( "IP='" + this.ip + "'");
          ret.append( "puerto unicast='" + this.puertoUnicast + "'");
          ret.append( "Activo='" + this.isActive + "'");
          return ret.toString();
   }    
}
