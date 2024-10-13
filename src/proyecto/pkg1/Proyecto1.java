package proyecto.pkg1;

import Objetos.Funcion;

public class Proyecto1 {
    public static void main(String[] args) {
        // URL del JSON (ajusta esta URL a una fuente válida)
        String jsonUrl = "https://drive.google.com/uc?export=download&id=16Pb65nsY-Tchpf-OgqMhLBZnYV8F6auT";

        // Llamar a la función para leer el JSON y construir el grafo
        Funcion.ReadJsonMetro(jsonUrl);
    }
}