/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

import primitivas.Lista;

/**
 *
 * @author nicolagabrielecolarusso
 */
/**
 * Nodo que representa una sucursal y su lista de estaciones cubiertas.
 */
public class NodoSucursal {

    private Sucursal sucursal; // Información de la sucursal
    private Lista<Estacion> estacionesCubiertas; // Lista de estaciones cubiertas por esta sucursal
    private NodoSucursal next; // Enlace al siguiente nodo

    // Constructor: Crea un nodo para una sucursal
    public NodoSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
        this.estacionesCubiertas = new Lista<>();
        this.next = null;
    }

    // Getters y setters
    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Lista<Estacion> getEstacionesCubiertas() {
        return estacionesCubiertas;
    }

    public void setEstacionesCubiertas(Lista<Estacion> estacionesCubiertas) {
        this.estacionesCubiertas = estacionesCubiertas;
    }

    public NodoSucursal getNext() {
        return next;
    }

    public void setNext(NodoSucursal next) {
        this.next = next;
    }
}

