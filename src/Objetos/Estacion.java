
package Objetos;

/**

 * Esta clase define la clase estacion

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 13/10/2024

 */


public class Estacion {
    String nombre;
    String linea;
    String sistema;
    private String color;

    public Estacion(String nombre, String linea, String sistema) {
        this.nombre = nombre;
        this.linea = linea;
        this.sistema = sistema;
    }
    
    //no se que hace esto 
    //Estacion(String string, String linea, String sistema) {
    //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    //}

    @Override
    public String toString() {
        return "Estacion{" +
        "nombre='" + nombre + '\'' +
        ", linea='" + linea + '\'' +
        ", sistema='" + sistema + '\'' +
        ", color='" + getColor() + '\'' + '}';
        }
    //getter para el nombre
    public String getNombre() {
        return nombre;
    }
    //setter para el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //getter para obtener la linea
    public String getLinea() {
        return linea;
    }
    //setter para obtener la linea
    public void setLinea(String linea) {
        this.linea = linea;
    }
    //getter para el sistema
    public String getSistema() {
        return sistema;
    }
    //setter para el sistema
    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    
}
