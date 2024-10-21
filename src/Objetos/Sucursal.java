package Objetos;

import primitivas.Lista;

public class Sucursal {
    private String nombre;
    private int t;
    private Lista<Estacion> estaciones; // Lista de estaciones base de la sucursal
    // Campos de la clase
    
    //Constructor
    /**
     *Constructor de clase Sucursal. Inicializa una nueva sucursal con el nombre y t proporcionados
     * 
     * @param nombre Nombre de la sucursal
     * @param t Valor t de la sucursal
     */
    public Sucursal(String nombre, int t) {
        this.nombre = nombre;
        this.t = t;
        this.estaciones = new Lista<>();
    }

    /**
     *Obtiene el nombre de la sucursal
     * 
     * @returnNombre de la sucursal
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el valor t de la sucursal.
     * 
     * @return Valor t de la sucursal.
     */
    public int getT() {
        return t;
    }

    /**
     * Obtiene la lista de estaciones base de la sucursal.
     * 
     * @return Lista de estaciones base de la sucursal.
     */
    public Lista<Estacion> getEstaciones() {
        return estaciones;
    }

    /**
     * Agrega una estación a la lista de estaciones base de la sucursal.
     * 
     * @param estacion Estación a agregar a la lista de estaciones base.
     */
    public void agregarEstacion(Estacion estacion) {
        if (!estaciones.exist(estacion)) {
            estaciones.append(estacion);
        }
    }
}
