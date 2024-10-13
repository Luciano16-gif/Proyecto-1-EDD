package primitivas;

import java.util.HashMap;
import Objetos.Estacion;

public class Grafo<T> {
    private final HashMap<T, Nodo<T>> nodos;

    public Grafo() {
        nodos = new HashMap<>();
    }

    public void agregarNodo(T data) {
        if (!nodos.containsKey(data)) {
            nodos.put(data, new Nodo<>(data));
        }
    }

    public void agregarArista(T origen, T destino) {
        if (nodos.containsKey(origen) && nodos.containsKey(destino)) {
            nodos.get(origen).agregarVecino(nodos.get(destino));
            nodos.get(destino).agregarVecino(nodos.get(origen)); // Si el grafo es no dirigido
        }
    }

    public Nodo<T> obtenerNodo(T data) {
        return nodos.get(data);
    }

    public Lista<Nodo<T>> obtenerNodos() {
        Lista<Nodo<T>> listaNodos = new Lista<>();
        for (Nodo<T> nodo : nodos.values()) {
            listaNodos.append(nodo);
        }
        return listaNodos;
    }
    
}