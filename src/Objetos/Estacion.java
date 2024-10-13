
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
    private String color;
    

    public Estacion(String nombre, String linea, String sistema, int lineaIndex) {
        this.nombre = nombre;
        this.linea = linea;
        this.sistema = sistema;
        this.color = asignarColor(lineaIndex); // Asignar color basado en el índice de la línea
    }

    public String getNombre() {
        return nombre;
    }

    public String getLinea() {
        return linea;
    }

    public String getColor() {
        return color;
    }

    private String asignarColor(int lineaIndex) {
        switch (lineaIndex) {
            case 0: return "red";
            case 1: return "blue";
            case 2: return "green";
            case 3: return "orange";
            case 4: return "purple";    
            case 5: return"cyan";      
            case 6: return"yellow";   
            case 7: return"magenta";   
            case 8: return"brown";     
            case 9: return"lime";  
            case 10: return"teal";      
            case 11: return"navy";      
            case 12: return"pink";      
            case 13: return"lightgray"; 
            case 14: return"darkorange"; 
            default: return "black"; 
        }
    }

    @Override
    public String toString() {
        return "Estacion{" +
        "nombre='" + nombre + '\'' +
        ", linea='" + linea + '\'' +
        ", sistema='" + sistema + '\'';
        }
    
    
}
