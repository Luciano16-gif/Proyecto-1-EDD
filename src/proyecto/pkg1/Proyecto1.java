
package proyecto.pkg1;
import InterfazGUI.InterfazFinal;


/**

 * Esta clase que inicializa el proyecto mediante el main
  
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 15/10/2024

 */


public class Proyecto1 {
    public static void main(String[] args) {
        // Crear la ventana de la interfaz donde se va a visualizar la interfaz
        InterfazFinal window = new InterfazFinal();
        window.setResizable(false);
        window.setVisible(true);   
    }
}
