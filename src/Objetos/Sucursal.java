package Objetos;

import primitivas.Lista;

/**
 * Esta clase Surcusal define la clase Surcusal
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso
 * @version: 16/10/2024
 */


public class Sucursal {
    private String nombre;
    private int t;
    private Lista<Estacion> estaciones; // Lista de estaciones base de la sucursal
    

    public Sucursal(String nombre, int t) {
        this.nombre = nombre;
        this.t = t;
        this.estaciones = new Lista<>();
        
    }

    public String getNombre() {
        return nombre;
    }

    public int getT() {
        return t;
    }

    public Lista<Estacion> getEstaciones() {
        return estaciones;
    }

    public void agregarEstacion(Estacion estacion) {
        if (!estaciones.exist(estacion)) {
            estaciones.append(estacion);
        }
    }
}
