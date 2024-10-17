package Objetos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import primitivas.Lista;

/**
 * Esta clase define al referente a la lectura del JSON
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 16/10/2024
 */
public class Funcion {

    public static void ReadJsonMetro(Grafos grafo, Lista<Estacion> estaciones) {
        readJson(grafo, estaciones);
    }

    public static Lista<Estacion> readJson(Grafos grafo, Lista<Estacion> estaciones) {
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
                    content.append(inputLine.trim()); // Eliminar espacios en blanco al inicio y fin
                }
                in.close();

                // Convertir el contenido en una cadena
                String jsonString = content.toString();

                // Llamar a la función para extraer estaciones
                parseJsonString(jsonString, estaciones, grafo);
            } else {
                System.out.println("No se seleccionó ningún archivo.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return estaciones;
    }

    private static void parseJsonString(String jsonString, Lista<Estacion> estaciones, Grafos grafo) {
        // El formato del JSON es conocido, podemos parsearlo manualmente

        // Buscar el inicio del sistema
        int sistemaStart = jsonString.indexOf("{");
        int sistemaEnd = jsonString.lastIndexOf("}");

        if (sistemaStart == -1 || sistemaEnd == -1) {
            System.out.println("Error en el formato del JSON.");
            return;
        }

        String sistemaContent = jsonString.substring(sistemaStart + 1, sistemaEnd).trim();

        // Obtener el nombre del sistema
        int sistemaNameEnd = sistemaContent.indexOf(":");
        if (sistemaNameEnd == -1) {
            System.out.println("Error en el formato del JSON.");
            return;
        }

        String sistemaName = sistemaContent.substring(0, sistemaNameEnd).replace("\"", "").trim();
        System.out.println("Procesando sistema: " + sistemaName);

        String lineasContent = sistemaContent.substring(sistemaNameEnd + 1).trim();

        // Remover corchetes iniciales y finales
        if (lineasContent.startsWith("[")) {
            lineasContent = lineasContent.substring(1);
        }
        if (lineasContent.endsWith("]")) {
            lineasContent = lineasContent.substring(0, lineasContent.length() - 1);
        }

        // Separar las líneas
        String[] lineasArray = DividirCadenaTexto(lineasContent, "},");
        for (int idx = 0; idx < lineasArray.length; idx++) {
            String lineaItem = lineasArray[idx].trim();
            if (lineaItem.startsWith("{")) {
                lineaItem = lineaItem.substring(1);
            }
            if (!lineaItem.endsWith("}")) {
                lineaItem = lineaItem + "}"; // Agregar } faltante
            }

            // Obtener el nombre de la línea
            int lineaNameEnd = lineaItem.indexOf(":");
            if (lineaNameEnd == -1) {
                System.out.println("Error en el formato de la línea.");
                continue;
            }

            String lineaName = lineaItem.substring(0, lineaNameEnd).replace("\"", "").trim();
            System.out.println("Procesando línea: " + lineaName);

            String estacionesContent = lineaItem.substring(lineaNameEnd + 1).trim();

            // Remover corchetes iniciales y finales
            if (estacionesContent.startsWith("[")) {
                estacionesContent = estacionesContent.substring(1);
            }
            if (estacionesContent.endsWith("]}")) {
                estacionesContent = estacionesContent.substring(0, estacionesContent.length() - 2);
            } else if (estacionesContent.endsWith("]")) {
                estacionesContent = estacionesContent.substring(0, estacionesContent.length() - 1);
            }

            // Separar las estaciones
            String[] estacionesArray = DividirCadenaTexto(estacionesContent, ",");

            int estacionIndexAnterior = -1;

            for (int j = 0; j < estacionesArray.length; j++) {
                String estacionItem = estacionesArray[j].trim();

                int estacionIndexActual = -1;

                if (estacionItem.startsWith("{") && estacionItem.endsWith("}")) {
                    // Estación especial (intersección)
                    estacionItem = estacionItem.substring(1, estacionItem.length() - 1).trim();
                    String[] estacionPair = estacionItem.split(":");
                    if (estacionPair.length == 2) {
                        String nombre1 = estacionPair[0].replace("\"", "").trim();
                        String nombre2 = estacionPair[1].replace("\"", "").trim();
                        System.out.println("Procesando estación especial: " + nombre1 + " - " + nombre2);

                        Estacion estacion1 = new Estacion(nombre1, lineaName, sistemaName);
                        Estacion estacion2 = new Estacion(nombre2, lineaName, sistemaName);

                        int index1 = estaciones.indexOf(estacion1);
                        if (index1 == -1) {
                            estaciones.append(estacion1);
                            index1 = estaciones.len() - 1;
                        } else {
                            // Agregar la línea a la estación existente
                            estaciones.get(index1).agregarLinea(lineaName);
                        }

                        int index2 = estaciones.indexOf(estacion2);
                        if (index2 == -1) {
                            estaciones.append(estacion2);
                            index2 = estaciones.len() - 1;
                        } else {
                            estaciones.get(index2).agregarLinea(lineaName);
                        }

                        // Conectar las dos estaciones entre sí
                        grafo.addArco(index1, index2);

                        estacionIndexActual = index1;
                    } else {
                        System.out.println("Error en el formato de la estación especial.");
                    }
                } else {
                    // Estación normal
                    if (estacionItem.startsWith("\"") && estacionItem.endsWith("\"")) {
                        estacionItem = estacionItem.substring(1, estacionItem.length() - 1);
                    }
                    String nombreEstacion = estacionItem.trim();
                    System.out.println("Procesando estación: " + nombreEstacion);

                    Estacion estacionActual = new Estacion(nombreEstacion, lineaName, sistemaName);

                    int index = estaciones.indexOf(estacionActual);
                    if (index == -1) {
                        estaciones.append(estacionActual);
                        index = estaciones.len() - 1;
                    } else {
                        // Agregar la línea a la estación existente
                        estaciones.get(index).agregarLinea(lineaName);
                    }
                    estacionIndexActual = index;
                }

                // Conectar la estación actual con la anterior en la línea
                if (estacionIndexAnterior != -1 && estacionIndexActual != -1) {
                    grafo.addArco(estacionIndexAnterior, estacionIndexActual);
                }

                estacionIndexAnterior = estacionIndexActual;
            }

            // No incrementamos lineaIndex, ya que no lo necesitamos para colores
        }
    }


    private static String[] DividirCadenaTexto(String input, String delimiter) {
        Lista<String> result = new Lista<>();
        StringBuilder sb = new StringBuilder();
        int braceLevel = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '{') {
                braceLevel++;
            } else if (c == '}') {
                braceLevel--;
            }

            if (braceLevel == 0 && input.startsWith(delimiter, i)) {
                result.append(sb.toString());
                sb.setLength(0);
                i += delimiter.length() - 1; // Saltar el delimitador
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            result.append(sb.toString());
        }

        // Convertir Lista a arreglo
        String[] array = new String[result.len()];
        for (int i = 0; i < result.len(); i++) {
            array[i] = result.get(i);
        }

        return array;
    }
}
