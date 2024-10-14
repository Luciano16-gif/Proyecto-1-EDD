package proyecto.pkg1;

import Objetos.Funcion;

public class Proyecto1 {
    public static void main(String[] args) {
        // URL del JSON (ajusta esta URL a una fuente válida)
        String jsonUrl = "https://drive.google.com/uc?export=download&id=1rX-A17wDXpYW9uyU-JaJMIkzHx4Y9n9y";


        // Llamar a la función para leer el JSON y construir el grafo
        Funcion.ReadJsonMetro(jsonUrl);
    }
}