package Objetos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import primitivas.Lista;
import org.json.JSONObject;

/**
 * Esta clase define las funciones referentes al JSON.
 *
 * @author
 * @version: 13/10/2024
 */
public class Funcion {

    public static void ReadJsonMetro() {
        Lista<Estacion> estaciones = readJson();

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

    public static Lista<Estacion> readJson() {
        Lista<Estacion> estaciones = new Lista<>();

        try {
            // Abrir un JFileChooser para seleccionar el archivo JSON
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione el archivo JSON de la red de transporte");

            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File jsonFile = fileChooser.getSelectedFile();

                // Leer el contenido del archivo JSON
                BufferedReader in = new BufferedReader(new FileReader(jsonFile));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Convertir el contenido en un objeto JSON
                JSONObject jsonObject = new JSONObject(content.toString());

                // Llamar a la función para extraer estaciones
                extractEstaciones(jsonObject, estaciones);
            } else {
                System.out.println("No se seleccionó ningún archivo.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }

    private static void extractEstaciones(JSONObject jsonObject, Lista<Estacion> estaciones) {
        int lineaIndex = 0;

        for (String sistema : jsonObject.keySet()) {
            Object sistemaObj = jsonObject.get(sistema);

            if (sistemaObj instanceof org.json.JSONArray) {
                org.json.JSONArray lineasArray = (org.json.JSONArray) sistemaObj;
                for (int i = 0; i < lineasArray.length(); i++) {
                    JSONObject lineaObj = lineasArray.getJSONObject(i);
                    for (String linea : lineaObj.keySet()) {
                        Object estacionesObj = lineaObj.get(linea);
                        if (estacionesObj instanceof org.json.JSONArray) {
                            org.json.JSONArray estacionesArray = (org.json.JSONArray) estacionesObj;
                            for (int j = 0; j < estacionesArray.length(); j++) {
                                Object estacion = estacionesArray.get(j);
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
                        lineaIndex++;
                    }
                }
            }
        }
    }
}
