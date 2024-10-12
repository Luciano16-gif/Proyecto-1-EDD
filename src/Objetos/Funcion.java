package Objetos;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Funcion {
    
    public static void ReadJsonMetro(String args) {
        String urlString = args;
        List<Estacion> estaciones = readJson(urlString);

        // Imprimir todas las estaciones
        for (Estacion estacion : estaciones) {
            System.out.println(estacion);
        }
    }

    public static List<Estacion> readJson(String urlString) {
        List<Estacion> estaciones = new ArrayList<>();

        try {
            // Crear la URL y abrir la conexión
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Leer el contenido
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            // Convertir el contenido en un objeto JSON
            JSONObject jsonObject = new JSONObject(content.toString());

            // Procesar el JSON para cualquier sistema de transporte
            for (String sistema : jsonObject.keySet()) {
                for (Object lineaObject : jsonObject.getJSONArray(sistema)) {
                    if (lineaObject instanceof JSONObject) {
                        JSONObject lineaJson = (JSONObject) lineaObject;
                        for (String linea : lineaJson.keySet()) {
                            for (Object estacionObj : lineaJson.getJSONArray(linea)) {
                                if (estacionObj instanceof String) {
                                    estaciones.add(new Estacion((String) estacionObj, linea, sistema));
                                } else if (estacionObj instanceof JSONObject) {
                                    JSONObject estacionJson = (JSONObject) estacionObj;
                                    for (String estacionKey : estacionJson.keySet()) {
                                        estaciones.add(new Estacion(estacionKey, linea, sistema));
                                        estaciones.add(new Estacion(estacionJson.getString(estacionKey), linea, sistema));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }
}