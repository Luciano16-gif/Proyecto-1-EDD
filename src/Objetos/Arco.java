
package Objetos;  
/**

 * Esta clase define el objeto Grafo, con la cual tiene diferentes atributos y funciones que lo definen

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 13/10/2024

 */
public class Arco {
    private int src;
    private int dest;
    private boolean nodoestacionVisitado = false;
    private int distancia;
    //Campos de la clase
    //constructor    
    
    public Arco(int src, int dest, int distancia){
        this.src = src;
        this.dest = dest;
        this.distancia = distancia;
    }
    
    public int compareTo(Arco compareArco) {
        return this.distancia - compareArco.distancia;
    }
    
    //getter para el nodo inicial del arco
    public int getSrc() {
        return src;
    }
    //setter para el nodo inicial
    public void setSrc(int src) {
        this.src = src;
    }
    //getter para el nodo final el arco
    public int getDest() {
        return dest;
    }
    //setter para el nodo final
    public void setDest(int dest) {
        this.dest = dest;
    }
    //metodo para visitar los nodos 
    public void visitarNodoEstacion(int name) {
        nodoestacionVisitado = true;
    }
}
