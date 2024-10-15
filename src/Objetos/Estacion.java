package Objetos;

import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import primitivas.Lista;

/**
 * Esta clase define la clase Estacion.
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 15/10/2024
 * 
 */
public class Estacion {
    private String nombre;
    private Lista<String> lineas;
    private String sistema;
    private String color;

    // Mapa estático de líneas a colores
    private static Map<String, String> lineaColorMap = new HashMap<>();
    private static int colorIndex = 0;

    // Lista de colores disponibles
    private static final String[] COLORES_DISPONIBLES = {
        "red", "blue", "green", "yellow", "orange", "purple", "cyan", "magenta",
        "brown", "lime", "teal", "navy", "pink", "gray", "darkorange", "olive", "maroon"
    };

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
            // Actualizar el color si es necesario (opcional)
            // this.color = asignarColor(linea);
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
        // Verificar si la línea ya tiene un color asignado
        if (lineaColorMap.containsKey(linea)) {
            return lineaColorMap.get(linea);
        } else {
            // Asignar el siguiente color disponible
            if (colorIndex < COLORES_DISPONIBLES.length) {
                String colorAsignado = COLORES_DISPONIBLES[colorIndex];
                lineaColorMap.put(linea, colorAsignado);
                colorIndex++;
                return colorAsignado;
            } else {
                // Si se agotan los colores, usar 'black' o algún color por defecto
                return "black";
            }
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
