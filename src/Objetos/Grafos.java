package Objetos;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import primitivas.Lista;
import primitivas.Nodo;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para manejar grafos de estaciones y arcos.
 */
public class Grafos {
    private Lista<Arco> arcos;
    private Lista<Estacion> estaciones;
    private Map<String, String> colorMap;

    public Grafos() {
        arcos = new Lista<>();
        estaciones = new Lista<>();
        colorMap = new HashMap<>();
        inicializarColorMap();
    }

    private void inicializarColorMap() {
        // Inicializar colores para diferentes líneas
        colorMap.put("Linea1", "red");
        colorMap.put("Linea2", "blue");
        colorMap.put("Linea3", "green");
        // Agrega más líneas y colores según sea necesario
    }

    public void addArco(int src, int dest, String linea) {
        arcos.append(new Arco(src, dest, 1)); // Peso arbitrario
    }

    public void addEstacion(Estacion estacion) {
        estaciones.append(estacion);
    }

    // Conectar estaciones en línea recta
    public void conectarEstaciones() {
        for (int i = 0; i < estaciones.len() - 1; i++) {
            addArco(i, i + 1, estaciones.get(i).getLinea());
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
            graph.getNode(nodeId).setAttribute("ui.style", "fill-color: " + estacion.getColor() + "; shape: circle;");
        }

        // Agregar arcos al grafo
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            String arcoId = arco.getSrc() + "-" + arco.getDest();
            graph.addEdge(arcoId, String.valueOf(arco.getSrc()), String.valueOf(arco.getDest()), true);
            String color = colorMap.get(estaciones.get(arco.getSrc()).getLinea());
            if (color != null) {
                graph.getEdge(arcoId).setAttribute("ui.style", "fill-color: " + color + ";");
            } else {
                graph.getEdge(arcoId).setAttribute("ui.style", "fill-color: black;"); // Color por defecto
            }
        }

        posicionarEstaciones(graph);
        graph.display();
    }

    private void posicionarEstaciones(Graph graph) {
        int xOffset = 200; // Espacio horizontal entre estaciones
        int yOffset = 100; // Espacio vertical entre líneas
        Map<String, Integer> lineaOffset = new HashMap<>();

        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            int lineIndex = lineaOffset.getOrDefault(estacion.getLinea(), 0);
            int x = lineIndex * xOffset; // Posición X
            int y = (i % 2) * yOffset; // Alternar en Y para líneas rectas

            graph.getNode(String.valueOf(i)).setAttribute("xy", x, y);
            lineaOffset.put(estacion.getLinea(), lineIndex + 1); // Aumentar índice para la siguiente estación de la misma línea
        }
    }
}
