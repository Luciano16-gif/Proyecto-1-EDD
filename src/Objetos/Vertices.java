/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

/**
 *
 * @author nicolagabrielecolarusso
 */

/**
 * Lista de Sucursales para gestionar las estaciones cubiertas.
 */
public class Vertices {
    private NodoSucursal sfirst; // Primer nodo de la lista
    private int size;

    public Vertices() {
        this.size = 0;
        this.sfirst = null;
    }

    public boolean esVacio() {
        return sfirst == null;
    }

    // Buscar una sucursal basada en el nombre de la sucursal
    public Sucursal buscarSucursal(String nombreSucursal) {
        NodoSucursal aux = this.getSfirst();
        while (aux != null) {
            if (aux.getSucursal().getNombre().equalsIgnoreCase(nombreSucursal)) {
                return aux.getSucursal(); // Retorna la sucursal encontrada
            }
            aux = aux.getNext();
        }
        return null; // No se encontró la sucursal
    }

    // Agregar una nueva sucursal
    public void agregarSucursal(Sucursal sucursal) {
        if (esVacio()) {
            sfirst = new NodoSucursal(sucursal);
        } else if (buscarSucursal(sucursal.getNombre()) == null) {
            NodoSucursal newNode = new NodoSucursal(sucursal);
            newNode.setNext(sfirst);
            sfirst = newNode;
        }
        this.size++;
    }

    // Eliminar una sucursal basada en su nombre
    public void eliminarSucursal(String nombreSucursal) {
        if (!esVacio() && nombreSucursal != null) {
            if (sfirst.getSucursal().getNombre().equals(nombreSucursal)) {
                sfirst = sfirst.getNext(); // Eliminar el primer nodo
            } else {
                NodoSucursal prev = sfirst;
                NodoSucursal curr = sfirst.getNext();
                while (curr != null) {
                    if (curr.getSucursal().getNombre().equals(nombreSucursal)) {
                        prev.setNext(curr.getNext()); // Saltar el nodo a eliminar
                        break;
                    }
                    prev = curr;
                    curr = curr.getNext();
                }
            }
            this.size--;
        }
    }

    // Imprimir la lista de sucursales
    public String print() {
        StringBuilder cadena = new StringBuilder();
        NodoSucursal aux = sfirst;
        while (aux != null) {
            cadena.append(aux.getSucursal().getNombre()).append("\n");
            aux = aux.getNext();
        }
        return cadena.toString();
    }

    public NodoSucursal getSfirst() {
        return sfirst;
    }

    public int getSize() {
        return size;
    }
}

