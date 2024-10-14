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
    private String[] coloresPredeterminados;

    public Grafos() {
        arcos = new Lista<>();
        estaciones = new Lista<>();
    }

    public void addArco(int src, int dest) {
        arcos.append(new Arco(src, dest, 1));
        arcos.append(new Arco(dest, src, 1)); // Grafo no dirigido
    }

    public void addEstacion(Estacion estacion) {
        estaciones.append(estacion);
    }

    public void conectarEstaciones() {
        for (int i = 0; i < estaciones.len() - 1; i++) {
            addArco(i, i + 1);
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
            graph.getNode(nodeId).setAttribute("ui.style", "fill-color: " + estacion.getColor() + "; shape: circle; size: 15px;");
        }

        // Agregar arcos al grafo
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            String arcoId = arco.getSrc() + "-" + arco.getDest();
            graph.addEdge(arcoId, String.valueOf(arco.getSrc()), String.valueOf(arco.getDest()), true);
            graph.getEdge(arcoId).setAttribute("ui.style", "fill-color: " + estaciones.get(arco.getSrc()).getColor() + ";");
        }

        posicionarEstaciones(graph);
        graph.display();
    }

    private void posicionarEstaciones(Graph graph) {
        int xOffset = 100; // Espacio horizontal entre estaciones
        int yOffset = 50;  // Espacio vertical para simular zig-zag

        for (int i = 0; i < estaciones.len(); i++) {
            int x = (i * xOffset) % 500; // Ciclar en X
            int y = (i / 5) * yOffset * (i % 2 == 0 ? 1 : -1); // Alternar en Y para zig-zag

            graph.getNode(String.valueOf(i)).setAttribute("xy", x, y);
        }
    }
}