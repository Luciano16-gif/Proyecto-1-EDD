package primitivas;

/**

 * Esta clase define el objeto Lista, con la cual tiene diferentes atributos y funciones que lo definen

 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso

 * @version: 15/10/2024

 */



public class Lista<T> {
    private Nodo head;
    private Nodo tail;
    private int size;
    //Campos de la clase
    //constructor
    public Lista() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
//getter
    public Nodo getHead() {
        return head;
    }

//setter
    public void setHead(Nodo head) {
        this.head = head;
    }

//getter
    public Nodo getTail() {
        return tail;
    }

//setter
    public void setTail(Nodo tail) {
        this.tail = tail;
    }

//getter
    public int getSize() {
        return size;
    }

//setter
    public void setSize(int size) {
        this.size = size;
    }
    
//funcion de la clase Lista para conocer si lita esta vacia
    public boolean isEmpty(){
        return head == null;
    }
    
//funcion de la clase Lista para conocer tamaño lista

    public int len(){
        return getSize();
    }
    
//metodo de la clase Lista para borrar lista
    public void delete(){
        head = null;
        tail = null;
        size = 0;
    }
//metodo de la clase Lista para agregar datos
    
    public final void append(T data){
        Nodo newNode = new Nodo(data);
        if(isEmpty() == true){
            head = newNode;
            tail = newNode;
            size++;
        }
        else{
            tail.setNext(newNode);
            tail = newNode;
            size++;
        }
    }
    
//metodo de la clase Lista para eliminar y devolver como variable

    public void pop(int position){
        Nodo pointer = head;
        if(position == 0){
            head = head.getNext();
            size--;
        }
        else if(position == len()-1){
            for(int x = 2; x < len(); x++){
                pointer = pointer.getNext();
            }
            pointer.setNext(null);
            tail = pointer;
            size--;
        }
        else{
            for(int x = 0; x < position-1; x++){
                pointer = pointer.getNext();
            }
            pointer.setNext(pointer.getNext().getNext());
            size--;
        }
    }
//funcion de la clase Lista para buscar

    public int find(T data){
        if(head.getData() == data){
            return 0;
        }
        else if(tail.getData() == data){
            return size-1;
        }
        else{
            Nodo pointer = head;
            for(int x = 0; x < len(); x++){
                if(pointer.getData() == data){
                    return x;
                }
                pointer = pointer.getNext();
            }
        }
        return 0;
    } 
    
//metodo de la clase Lista para pre agregar

    public void preappend(T data){
        Nodo newNode = new Nodo(data);
        if(isEmpty() == true){
            head = newNode;
            tail = newNode;
            size++;
        }
        else{
            newNode.setNext(head);
            head = newNode;
            size++;
        }
    }
//funcion de la clase Lista para conocer puntero

    public T get(int position){
        Nodo pointer = head;
        if(position < 0 || position >= len()){
            return null;
        }
        else if(position == 0){
            return (T) head.getData();
        }
        else if(position == len()-1){
            return (T) tail.getData();
        }
        else{
            for(int x = 1; x <= position; x++){
                pointer = pointer.getNext();
            }
            return (T) pointer.getData();
        }
    }
//metodo de la clase Lista para insertar nodos

    public void insert(int position, T data){
        boolean run = true;
        Nodo newNode = new Nodo(data);
        while(run == true){
            if(position < 0 || position >= len()){
                run = false;
            }
            else{
                if(position == 0){
                    preappend(data);
                    run = false;
                }
                else{
                    Nodo pointer = head;
                    for(int x = 1; x < position; x++){
                        pointer = pointer.getNext();
                    }
                    newNode.setNext(pointer.getNext());
                    pointer.setNext(newNode);
                    size++;  
                    run = false;
                }
            }
        }
    }   

//metodo de la clase Lista para reemplazar informacion

    public void replace(int position, T data){
        boolean run = true;
 
        while(run == true){
            if(position < 0 || position >= len()){
                run = false;
            }
            else{
                if(position == 0){
                    head.setData(data);
                    run = false;
                }
                else if(position == len()-1){
                    tail.setData(data);
                    run = false;
                }
                else{
                    Nodo pointer = head;
                    for(int x = 0; x < position; x++){
                        pointer = pointer.getNext();
                    }
                    pointer.setData(data);
                    run = false;
                }
            }
        }
    }    
//funcion de la clase Lista para conocer existencia de un nodo

    public boolean exist(T data){
        if(head.getData() == data){
            return true;
        }
        else if(tail.getData() == data){
            return true;
        }
        else{
            Nodo pointer = head;
            for(int x = 0; x < len(); x++){
                if(pointer.getData() == data){
                    return true;
                }
                pointer = pointer.getNext();
            }
        }
        return false;
    }
    
    public int indexOf(T data) {
        Nodo<T> pointer = head;
        int index = 0;
        while (pointer != null) {
            if (pointer.getData().equals(data)) {
                return index;
            }
            pointer = pointer.getNext();
            index++;
        }
        return -1; // No encontrado
    }
    
    // Obtener el índice del elemento, si el índice es válido elimina el nodo en el índice encontrado
    public void remove(T data) {
        int index = indexOf(data); 
        if (index != -1) { 
            pop(index); 
        }
    }
    
    public boolean existenciaDistinta(String data) {
    if (head == null) { // Comprueba si la lista está vacía
        return false;
    }
    Nodo current = head;
    while (current != null) {
        if (current.getData().equals(data)) {
            return true;
        }
        current = current.getNext();
    }
    return false;
}
}