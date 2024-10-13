package Objetos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import primitivas.Lista;
import org.json.JSONObject;

/**
 * Esta clase define las funciones referentes al JSON. 
 * 
 * @author Gabriele Colarusso, Luciano Minardo, Ricardo Paez
 * 
 * @version: 13/10/2024
 */
public class Funcion {

    public static void ReadJsonMetro(String args) {
        String urlString = args;
        Lista<Estacion> estaciones = readJson(urlString);

        Grafos grafo = new Grafos();

        // Imprimir todas las estaciones
        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            grafo.addEstacion(estacion);
            System.out.println(estacion);
        }

        grafo.conectarEstaciones();
        grafo.mostrarGrafo();
    }

    public static Lista<Estacion> readJson(String urlString) {
        Lista<Estacion> estaciones = new Lista<>();

        try {
            // Crear la URL y abrir la conexión
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Verificar la respuesta
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.out.println("Error al conectar: Código de respuesta " + responseCode);
                return estaciones;
            }

            // Leer el contenido
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Convertir el contenido en un objeto JSON
            JSONObject jsonObject = new JSONObject(content.toString());

            // Llamar a la función para extraer estaciones
            extractEstaciones(jsonObject, estaciones);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }

    private static void extractEstaciones(JSONObject jsonObject, Lista<Estacion> estaciones) {
        int lineaIndex = 0;

        for (String sistema : jsonObject.keySet()) {
            Object sistemaObj = jsonObject.get(sistema);

            if (sistemaObj instanceof Iterable) {
                for (Object lineaObj : (Iterable<?>) sistemaObj) {
                    JSONObject lineaJson = (JSONObject) lineaObj;
                    for (String linea : lineaJson.keySet()) {
                        Object estacionesObj = lineaJson.get(linea);
                        if (estacionesObj instanceof Iterable) {
                            for (Object estacion : (Iterable<?>) estacionesObj) {
                                if (estacion instanceof String) {
                                    estaciones.append(new Estacion((String) estacion, linea, sistema, lineaIndex));
                                } else if (estacion instanceof JSONObject) {
                                    JSONObject estacionJson = (JSONObject) estacion;
                                    for (String nombre : estacionJson.keySet()) {
                                        estaciones.append(new Estacion(nombre, linea, sistema, lineaIndex));
                                    }
                                }
                            }
                        }
                        lineaIndex++; // Incrementar el índice para la siguiente línea
                    }
                }
            }
        }
    }
}
