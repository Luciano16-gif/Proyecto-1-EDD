package Objetos;

import primitivas.Lista;

/**
 * Esta clase define la clase Estacion
 * 
 * @author:
 * @version: 16/10/2024
 */
public class Estacion {
    private String nombre;
    private Lista<String> lineas;
    private String sistema;
    private String color;
    private boolean esSucursal;

    // Lista estática para almacenar pares de línea y color
    private static Lista<LineaColor> lineaColorLista = new Lista<>();
    private static int colorIndex = 0;

    // Lista de colores disponibles
    private static final String[] COLORES_DISPONIBLES = {
        "red", "blue", "green", "yellow", "orange", "purple", "cyan", "magenta",
        "brown", "lime", "teal", "navy", "pink", "gray", "darkorange", "olive", "maroon"
    };

    /**
     *Constructor de la clase Estacion
     * 
     * @param nombre Nombre de la estacion
     * @param linea Linea de transporte que pasa por la estacion
     * @param sistema Sistema de transporte al que pertenece la estacion
     */
    public Estacion(String nombre, String linea, String sistema) {
        this.nombre = nombre.trim();
        this.lineas = new Lista<>();
        this.lineas.append(linea);
        this.sistema = sistema;
        this.color = asignarColor(linea);
        this.esSucursal = false; 
    }

    /**
     *Agrega una linea de transporte a la lista de lineas de la estacion
     * 
     * @param linea Linea de transporte a agregar
     */
    public void agregarLinea(String linea) {
        if (!lineas.exist(linea)) {
            lineas.append(linea);
        }
    }

    /**
     *Verifica si la estacion es una sucursal
     * @return true si la estacion es una sucursal, false en caso contrario
     */
    public boolean esSucursal() {
        return esSucursal;
    }
    
    /**
     *Establece si la estacion es una sucursl
     * 
     * @param esSucursal Valor booleano que indica si la estacion es una sucursal
     */
    public void setEsSucursal(boolean esSucursal) {
        this.esSucursal = esSucursal;
    }
    
    /**
     *Obtiene el nombre de la estacion
     * 
     * @return Nombre de la estacion
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la lista de lineas de transporte que pasan por la estacion
     * 
     * @return Lista de lineas de transporte
     */
    public Lista<String> getLineas() {
        return lineas;
    }

    /**
     * Obtiene el sistema de transporte al que pertenece la estación.
     * 
     * @return Sistema de transporte.
     */
    public String getSistema() {
        return sistema;
    }

     /**
     * Obtiene el color asociado a la estación.
     * 
     * @return Color de la estación.
     */
    public String getColor() {
        return color;
    }

    /**
     * Asigna un color a la línea de transporte de la estación.
     * 
     * @param linea Línea de transporte.
     * @return Color asignado a la línea.
     */
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
    /**
     * Genera una representación en cadena de la estación.
     * 
     * @return Cadena con la información de la estación.
     */
    public String toString() {
        return "Estacion{" +
               "nombre='" + nombre + '\'' +
               ", lineas=" + lineasToString() +
               ", sistema='" + sistema + '\'' +
               ", color='" + color + '\'' +
               '}';
    }

    /**
     * Genera una cadena con las líneas de transporte de la estación.
     * 
     * @return Cadena con las líneas de transporte.
     */
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

    // Modificaciones en equals y hashCode
    /**
     * Compara dos objetos Estación para ver si son iguales.
     * 
     * @param obj Objeto con el que comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estacion)) return false;
        Estacion estacion = (Estacion) obj;
        return this.nombre.equalsIgnoreCase(estacion.nombre);
    }

    /**
     * Genera un código hash para el objeto Estación.
     * 
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    // Clase interna para almacenar pares línea-color
    /**
     * Clase interna para almacenar pares línea-color.
     */
    private static class LineaColor {
        String linea;
        String color;
        /**
         * Constructor de la clase LineaColor.
         *
         * @param linea Línea de transporte.
         * @param color Color asociado a la línea.
         */
        LineaColor(String linea, String color) {
            this.linea = linea;
            this.color = color;
        }
    }
}
