package Objetos;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import primitivas.Lista;


/**
 * Clase para manejar grafos de estaciones y arcos.
 */
public class Grafos {
    private Lista<Arco> arcos;
    private Lista<Estacion> estaciones;

    public Grafos() {
        arcos = new Lista<>();
        estaciones = new Lista<>();
    }

    public void addEstacion(Estacion estacion) {
        estaciones.append(estacion);
    }

    public void conectarEstaciones() {
        for (int i = 0; i < estaciones.len() - 1; i++) {
        }
    }

    public void mostrarGrafo() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("Grafo Metro");

        // Agregar nodos al grafo
        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            String nodeId = String.valueOf(i);
            graph.addNode(nodeId);
            graph.getNode(nodeId).setAttribute("ui.label", estacion.getNombre());
        }

        // Agregar arcos al grafo
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            String arcoId = arco.getSrc() + "-" + arco.getDest();
            graph.addEdge(arcoId, String.valueOf(arco.getSrc()), String.valueOf(arco.getDest()), true);
        }

        posicionarEstaciones(graph);
        graph.display();
    }

    private void posicionarEstaciones(Graph graph) {
        int xOffset = 100; // Espacio horizontal entre estaciones
        int yOffset = 50;  // Espacio vertical para simular zig-zag

        for (int i = 0; i < estaciones.len(); i++) {
        }
    }
}
