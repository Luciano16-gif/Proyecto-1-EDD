package Objetos;

import primitivas.Lista;

/**
 * Esta clase define la clase Estacion
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 16/10/2024
 */
public class Estacion {
    private String nombre;
    private Lista<String> lineas;
    private String sistema;
    private String color;

    // Lista estática para almacenar pares de línea y color
    private static Lista<LineaColor> lineaColorLista = new Lista<>();
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
            // Opcional: Actualizar el color
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
        for (int i = 0; i < lineaColorLista.len(); i++) {
            LineaColor par = lineaColorLista.get(i);
            if (par.linea.equals(linea)) {
                return par.color;
            }
        }

        // Asignar el siguiente color disponible
        if (colorIndex < COLORES_DISPONIBLES.length) {
            String colorAsignado = COLORES_DISPONIBLES[colorIndex];
            lineaColorLista.append(new LineaColor(linea, colorAsignado));
            colorIndex++;
            return colorAsignado;
        } else {
            // Si se agotan los colores, usar 'black' por defecto
            return "black";
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
        return this.nombre.equals(estacion.nombre) &&
               this.sistema.equals(estacion.sistema);
    }

    @Override
    public int hashCode() {
        // Implementación manual de hashCode
        int hash = 7;
        hash = 31 * hash + (nombre != null ? nombre.hashCode() : 0);
        hash = 31 * hash + (sistema != null ? sistema.hashCode() : 0);
        return hash;
    }

    // Clase interna para almacenar pares línea-color
    private static class LineaColor {
        String linea;
        String color;

        LineaColor(String linea, String color) {
            this.linea = linea;
            this.color = color;
        }
    }
}
