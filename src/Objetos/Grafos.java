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

    public void addArco(int src, int dest) {
        if (!existeArco(src, dest) && !existeArco(dest, src)) {
            arcos.append(new Arco(src, dest, 1));
        }
    }

    private boolean existeArco(int src, int dest) {
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            if ((arco.getSrc() == src && arco.getDest() == dest) || (arco.getSrc() == dest && arco.getDest() == src)) {
                return true;
            }
        }
        return false;
    }

    public void addEstacion(Estacion estacion) {
        estaciones.append(estacion);
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

            // Si la estación pertenece a múltiples líneas, asignar un color especial
            String nodeColor = estacion.getColor();
            if (estacion.getLineas().len() > 1) {
                nodeColor = "white"; // Color para intersecciones
            }

            graph.getNode(nodeId).setAttribute("ui.style", "fill-color: " + nodeColor + "; shape: circle; size: 15px;");
        }

        // Agregar arcos al grafo
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            int src = arco.getSrc();
            int dest = arco.getDest();
            String arcoId;

            // Ordenar los nodos para el identificador de la arista
            if (src < dest) {
                arcoId = src + "-" + dest;
            } else {
                arcoId = dest + "-" + src;
                // Intercambiar src y dest para asegurar que la arista se agrega correctamente
                int temp = src;
                src = dest;
                dest = temp;
            }

            if (graph.getEdge(arcoId) == null) { // Evitar duplicados en GraphStream
                graph.addEdge(arcoId, String.valueOf(src), String.valueOf(dest), false);
                graph.getEdge(arcoId).setAttribute("ui.style", "fill-color: gray;");
            }
        }

        posicionarEstaciones(graph);
        graph.display();
    }

    private void posicionarEstaciones(Graph graph) {
        // Implementa tu lógica de posicionamiento aquí
    }
}
