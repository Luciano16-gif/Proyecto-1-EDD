/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;
import primitivas.Lista;

public class Sucursal {
    private String nombre;
    private Lista<Estacion> estaciones; // Lista de estaciones asociadas a la sucursal
    private int t; // Valor de t para la sucursal

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
            estaciones.append(estacion); // Agregar la estación solo si no existe
        }
    }
}

