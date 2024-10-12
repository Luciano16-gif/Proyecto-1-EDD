package Objetos;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class Funcion {

    public static void ReadJsonMetroCaracas(String args) {
        try {
            // URL del archivo JSON
            String urlString = args;
            
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
            
            // Mostrar el contenido del JSON
            System.out.println(jsonObject.toString(2)); // Imprimir con formato
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void ReadJsonMetroBogota(String args) {
        try {
            // URL del archivo JSON
            String urlString = args;
            
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
            
            // Mostrar el contenido del JSON
            System.out.println(jsonObject.toString(2)); // Imprimir con formato
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    