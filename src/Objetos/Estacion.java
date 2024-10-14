package Objetos;

import java.util.Objects;
import primitivas.Lista;

/**
 * Esta clase define la clase Estacion.
 */
public class Estacion {
    private String nombre;
    private Lista<String> lineas;
    private String sistema;
    private String color;

    public Estacion(String nombre, String linea, String sistema) {
        this.nombre = nombre;
        this.lineas = new Lista<>();
        this.lineas.append(linea);
        this.sistema = sistema;
        this.color = asignarColor(linea);
    }

    public void agregarLinea(String linea) {
        if (!lineas.exist(linea)) {
            lineas.append(linea);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Lista<String> getLineas() {
        return lineas;
    }

    public String getSistema() {
        return sistema;
    }

    public String getColor() {
        return color;
    }

    private String asignarColor(String linea) {
        switch (linea) {
            case "Linea 1": return "red";
            case "Linea 2": return "blue";
            case "Linea 3": return "green";
            case "Linea 4": return "yellow";
            // Puedes agregar más líneas y colores si es necesario
            default: return "black";
        }
    }

    @Override
    public String toString() {
        return "Estacion{" +
               "nombre='" + nombre + '\'' +
               ", lineas=" + lineasToString() +
               ", sistema='" + sistema + '\'' +
               ", color='" + color + '\'' +
               '}';
    }

    private String lineasToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lineas.len(); i++) {
            sb.append(lineas.get(i));
            if (i < lineas.len() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estacion)) return false;
        Estacion estacion = (Estacion) obj;
        return nombre.equals(estacion.nombre) &&
               sistema.equals(estacion.sistema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, sistema);
    }
}
