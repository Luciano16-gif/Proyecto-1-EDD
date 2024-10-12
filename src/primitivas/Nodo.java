
package primitivas;

public class Nodo<T> {
    
    private Nodo next;
    private Lista<Nodo> vecinos;
    private T data;
    private boolean tieneSucursal;

    //Campos de la clase
    //constructor
    public Nodo(T data){
        this.next = null;
        this.data = data;
        this.vecinos = new Lista<>();
        this.tieneSucursal = false;
    }


    public void agregarVecino(Nodo vecino){
        this.vecinos.append(vecino);
    }

    //getter del siguiente nodo
    public Nodo getNext() {
        return next;
    }

//setter del siguiente nodo
    public void setNext(Nodo next) {
        this.next = next;
    }

//getter de la informacion
    public T getData() {
        return data;
    }

//setter de la informacion
    public void setData(T data) {
        this.data = data;
    }

    /**
     * @return the vecinos
     */
    public Lista<Nodo> getVecinos() {
        return vecinos;
    }

    /**
     * @param vecinos the vecinos to set
     */
    public void setVecinos(Lista<Nodo> vecinos) {
        this.vecinos = vecinos;
    }

    /**
     * @return the tieneSucursal
     */
    public boolean isTieneSucursal() {
        return tieneSucursal;
    }

    /**
     * @param tieneSucursal the tieneSucursal to set
     */
    public void setTieneSucursal(boolean tieneSucursal) {
        this.tieneSucursal = tieneSucursal;
    }

}

