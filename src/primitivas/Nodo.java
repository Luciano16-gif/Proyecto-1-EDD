
package primitivas;

/**

 * Esta clase define el objeto Nodo, con la cual tiene diferentes atributos y funciones que lo definen

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 15/10/2024

 */

public class Nodo<T> {
    
    private Nodo next;
    private Lista<Nodo> vecinos;
    private T data;
    private boolean tieneSucursal;

    //Campos de la clase
    //constructor

    /**
     *Constructor de la clase Nodo. Inicializa un nuevo nodo con el dato proporcionado
     * 
     * @param data Dato a almacenar en el nodo
     */
    public Nodo(T data){
        this.next = null;
        this.data = data;
        this.vecinos = new Lista<>();
        this.tieneSucursal = false;
    }

    /**
     * Agrega un nodo vecino a la lista de vecinos del nodo actual.
     *
     * @param vecino Nodo vecino a agregar a la lista de vecinos.
     */
    public void agregarVecino(Nodo vecino){
        this.vecinos.append(vecino);
    }

    //getter del siguiente nodo
    /**
     * Obtiene el siguiente nodo en la lista.
     *
     * @return Siguiente nodo en la lista.
     */
    public Nodo getNext() {
        return next;
    }

    //setter del siguiente nodo
    /**
     * Establece el siguiente nodo en la lista.
     *
     * @param next Nuevo siguiente nodo en la lista.
     */
    public void setNext(Nodo next) {
        this.next = next;
    }

    //getter de la informacion
    /**
     * Obtiene el dato almacenado en el nodo.
     *
     * @return Dato almacenado en el nodo.
     */
    public T getData() {
        return data;
    }

    //setter de la informacion
    /**
     * Establece el dato almacenado en el nodo.
     *
     * @param data Nuevo dato a almacenar en el nodo.
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Obtiene la lista de vecinos del nodo
     * 
     * @return Lista de vecinos del nodo 
     */
    public Lista<Nodo> getVecinos() {
        return vecinos;
    }

    /**
     * Establece la lista de vecinos del nodo.
     * 
     * @param vecinos Nueva lista de vecinos del nodo
     */
    public void setVecinos(Lista<Nodo> vecinos) {
        this.vecinos = vecinos;
    }

    /**
     * Verifica si el nodo tiene sucursal
     * 
     * @return true si el nodo tiene sucursal, false en caso contrario
     */
    public boolean isTieneSucursal() {
        return tieneSucursal;
    }

    /**
     * Establece si el nodo tiene sucursal
     * 
     * @param tieneSucursal Nuevo estado de si el nodo tiene sucursal
     */
    public void setTieneSucursal(boolean tieneSucursal) {
        this.tieneSucursal = tieneSucursal;
    }
}

