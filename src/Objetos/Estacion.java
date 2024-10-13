
package Objetos;

/**

 * Esta clase define la clase estacion

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 13/10/2024

 */


public class Estacion {
    private String nombre;
    private String linea;
    private String sistema;
    //Campos de la clase
    //constructor
    public Estacion(String nombre, String linea, String sistema) {
        this.nombre = nombre;
        this.linea = linea;
        this.sistema = sistema;
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
    //metodo para imprimir los datos
    public String toString() {
        return "Estacion{" +
                "nombre='" + nombre + '\'' +
                ", linea='" + linea + '\'' +
                ", sistema='" + sistema + '\'' +
                '}';
    }
}
