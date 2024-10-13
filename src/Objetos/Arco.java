
package Objetos;  
/**

 * Esta clase define el objeto Grafo, con la cual tiene diferentes atributos y funciones que lo definen

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 13/10/2024

 */
public class Arco {
    private String start;
    private String end;
    private boolean nodoestacionVisitado = false;
    //Campos de la clase
    //constructor    
    public Arco(String start, String end){
        this.start = start;
        this.end = end;
    }
    //getter para el nodo inicial del arco
    public String getStart() {
        return start;
    }
    //setter para el nodo inicial
    public void setStart(String start) {
        this.start = start;
    }
    //getter para el nodo final el arco
    public String getEnd() {
        return end;
    }
    //setter para el nodo final
    public void setEnd(String end) {
        this.end = end;
    }
    //metodo para visitar los nodos 
    public void visitarNodoEstacion(String name) {
        nodoestacionVisitado = true;
    }
}
