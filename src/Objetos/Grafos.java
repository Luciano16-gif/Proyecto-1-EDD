package Objetos;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import primitivas.Lista;

/**
 * Clase para manejar grafos de estaciones y arcos.
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso
 * 
 * @version: 16/10/2024
 */
public class Grafos {
    private Lista<Arco> arcos;
    private Lista<Estacion> estaciones;
    private Graph graph;

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

    public void mostrarGrafo(Lista<Estacion> estaciones) {
        this.estaciones = estaciones;
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("Grafo Metro");

        // Agregar nodos al grafo
        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            String nodeId = String.valueOf(i);
            graph.addNode(nodeId);
            graph.getNode(nodeId).setAttribute("ui.label", estacion.getNombre());

            // Si la estación pertenece a múltiples líneas, asignar un color especial
            String nodeColor = estacion.getColor();
            if (estacion.getLineas().len() > 1) {
                nodeColor = "Gray"; // Color para intersecciones
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
        
    }

    public Lista<Integer> getAdyacentes(int nodo) {
        Lista<Integer> adyacentes = new Lista<>();
        for (int i = 0; i < arcos.len(); i++) {
            Arco arco = arcos.get(i);
            if (arco.getSrc() == nodo) {
                adyacentes.append(arco.getDest());
            } else if (arco.getDest() == nodo) {
                adyacentes.append(arco.getSrc());
            }
        }
        return adyacentes;
    }

    public void resaltarEstaciones(Lista<Lista<Integer>> coberturasSucursales, Lista<Estacion> estaciones) {
        // Crear un arreglo para rastrear las estaciones cubiertas y sucursales
        boolean[] estacionesCubiertas = new boolean[estaciones.len()];
        boolean[] estacionesSucursal = new boolean[estaciones.len()];

        // Marcar las estaciones que son sucursales
        for (int i = 0; i < estaciones.len(); i++) {
            if (estaciones.get(i).esSucursal()) {
                estacionesSucursal[i] = true;
        }
        }

        // Marcar las estaciones cubiertas por las sucursales
        for (int s = 0; s < coberturasSucursales.len(); s++) {
            Lista<Integer> cobertura = coberturasSucursales.get(s);
            for (int i = 0; i < cobertura.len(); i++) {
                int indiceEstacion = cobertura.get(i);
                estacionesCubiertas[indiceEstacion] = true;
            }
        }

        // Actualizar los estilos de las estaciones
        for (int i = 0; i < estaciones.len(); i++) {
            String nodeId = String.valueOf(i);
            Estacion estacion = estaciones.get(i);

            if (estacionesSucursal[i]) {
                // Estación que es una sucursal
                graph.getNode(nodeId).setAttribute("ui.style", "fill-color: red; shape: box; size: 20px;");
            } else if (estacionesCubiertas[i]) {
                // Estación cubierta por al menos una sucursal
                graph.getNode(nodeId).setAttribute("ui.style", "fill-color: gold; shape: circle; size: 20px;");
            } else {
                // Estación no cubierta
                String nodeColor = estacion.getColor();
                if (estacion.getLineas().len() > 1) {
                    nodeColor = "gray"; // Color para intersecciones
                }
                graph.getNode(nodeId).setAttribute("ui.style", "fill-color: " + nodeColor + "; shape: circle; size: 15px;");
            }
        }
    }
}
