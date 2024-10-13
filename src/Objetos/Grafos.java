/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objetos;

/**
 *
 *
 */
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class Grafos {
    private List<Edge> edges;
    private List<Estacion> estaciones;
    private Map<String, String> colorMap;

    public Grafos() {
        edges = new ArrayList<>();
        estaciones = new ArrayList<>();
        colorMap = new HashMap<>();
    }

    public void addEdge(int src, int dest, String linea) {
        edges.add(new Edge(src, dest, 1)); // Peso arbitrario
    }

    public void addEstacion(Estacion estacion) {
        estaciones.add(estacion);
    }

    // Conectar estaciones en línea recta
    public void conectarEstaciones() {
        for (int i = 0; i < estaciones.size() - 1; i++) {
            addEdge(i, i + 1, estaciones.get(i).linea);
        }
    }

    public void mostrarGrafo() {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph = new SingleGraph("Grafo Metro");

        // Agregar nodos al grafo
        for (int i = 0; i < estaciones.size(); i++) {
            String nodeId = String.valueOf(i);
            graph.addNode(nodeId);
            graph.getNode(nodeId).setAttribute("ui.label", estaciones.get(i).nombre);
            graph.getNode(nodeId).setAttribute("ui.style", "fill-color: " + estaciones.get(i).color + "; shape: circle;");
        }

        // Agregar aristas al grafo
        for (Edge edge : edges) {
            String edgeId = edge.src + "-" + edge.dest;
            graph.addEdge(edgeId, String.valueOf(edge.src), String.valueOf(edge.dest), true);
            String color = colorMap.get(estaciones.get(edge.src).linea);
            graph.getEdge(edgeId).setAttribute("ui.style", "fill-color: " + color + ";");
        }

        posicionarEstaciones(graph);
        graph.display();
    }

    private void posicionarEstaciones(Graph graph) {
        int xOffset = 200; // Espacio horizontal entre estaciones
        int yOffset = 100; // Espacio vertical entre líneas
        Map<String, Integer> lineaOffset = new HashMap<>();

        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacion = estaciones.get(i);
            int lineIndex = lineaOffset.getOrDefault(estacion.linea, 0);
            int x = lineIndex * xOffset; // Posición X
            int y = (i % 2) * yOffset; // Alternar en Y para líneas rectas

            graph.getNode(String.valueOf(i)).setAttribute("xy", x, y);
            lineaOffset.put(estacion.linea, lineIndex + 1); // Aumentar índice para la siguiente estación de la misma línea
        }
    }

    public static void ReadJsonMetro(String args) {
        String urlString = args;
        List<Estacion> estaciones = readJson(urlString);
        
        Grafos grafo = new Grafos();
        
        for (Estacion estacion : estaciones) {
            grafo.addEstacion(estacion);
        }

        grafo.conectarEstaciones();
        grafo.mostrarGrafo();
    }

    public static List<Estacion> readJson(String urlString) {
        List<Estacion> estaciones = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject jsonObject = new JSONObject(content.toString());
            Map<String, String> colorMap = new HashMap<>();
            colorMap.put("Linea 1", "red");
            colorMap.put("Linea 2", "blue");
            colorMap.put("Linea 3", "green");
            colorMap.put("Linea 4", "orange");

            for (String sistema : jsonObject.keySet()) {
                JSONArray lineasArray = jsonObject.getJSONArray(sistema);
                for (int i = 0; i < lineasArray.length(); i++) {
                    JSONObject lineaJson = lineasArray.getJSONObject(i);
                    for (String linea : lineaJson.keySet()) {
                        JSONArray estacionesArray = lineaJson.getJSONArray(linea);
                        for (int j = 0; j < estacionesArray.length(); j++) {
                            String nombreEstacion = estacionesArray.getString(j);
                            String color = colorMap.getOrDefault(linea, "gray"); // Color por defecto
                            estaciones.add(new Estacion(nombreEstacion, linea, sistema, color));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }

    public static class Estacion {
        String nombre;
        String linea;
        String sistema;
        String color;

        public Estacion(String nombre, String linea, String sistema, String color) {
            this.nombre = nombre;
            this.linea = linea;
            this.sistema = sistema;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Estacion{" +
                    "nombre='" + nombre + '\'' +
                    ", linea='" + linea + '\'' +
                    ", sistema='" + sistema + '\'' +
                    ", color='" + color + '\'' +
                    '}';
        }
    }

    public static class Edge {
        int src, dest, weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }
}