
package primitivas;

public class Nodo<T> {
    
    private Nodo next;
    private T data;
    //Campos de la clase
    //constructor
    public Nodo(T data){
        this.next = null;
        this.data = data;
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
}
