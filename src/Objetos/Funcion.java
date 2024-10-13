package Objetos;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import primitivas.Lista;
import org.json.JSONObject;
import java.util.Iterator;

/**
 * Esta clase define las funciones referentes al JSON.
 * @author
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
            String inputLine;
            StringBuilder content = new StringBuilder(); // Usar StringBuilder en lugar de StringBuffer
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Convertir el contenido en un objeto JSON
            JSONObject jsonObject = new JSONObject(content.toString());

            // Procesar el JSON para cualquier sistema de transporte
            Iterator<String> sistemas = jsonObject.keys();
            while (sistemas.hasNext()) {
                String sistema = sistemas.next();
                JSONObject sistemaObj = jsonObject.getJSONObject(sistema);
                Iterator<String> lineas = sistemaObj.keys();
                while (lineas.hasNext()) {
                    String linea = lineas.next();
                    JSONObject lineaObj = sistemaObj.getJSONObject(linea);
                    Iterator<String> estacionesKeys = lineaObj.keys();
                    while (estacionesKeys.hasNext()) {
                        String estacionNombre = estacionesKeys.next();
                        estaciones.append(new Estacion(estacionNombre, linea, sistema));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }
}
