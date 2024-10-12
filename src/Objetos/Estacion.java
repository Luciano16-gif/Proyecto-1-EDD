
package Objetos;

public class Estacion {
    private String nombre;
    private String linea;
    private String sistema;

    public Estacion(String nombre, String linea, String sistema) {
        this.nombre = nombre;
        this.linea = linea;
        this.sistema = sistema;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String toString() {
        return "Estacion{" +
                "nombre='" + nombre + '\'' +
                ", linea='" + linea + '\'' +
                ", sistema='" + sistema + '\'' +
                '}';
    }
}
