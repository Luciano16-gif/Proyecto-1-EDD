package Objetos;

/**
 * Esta clase define el objeto Arco, con diferentes atributos y funciones.
 */
public class Arco implements Comparable<Arco> {
    private int src;
    private int dest;
    private boolean nodoEstacionVisitado = false;
    private int distancia;

    public Arco(int src, int dest, int distancia) {
        if (src < 0 || dest < 0) {
            throw new IllegalArgumentException("Los nodos no pueden tener valores negativos.");
        }
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa.");
        }
        this.src = src;
        this.dest = dest;
        this.distancia = distancia;
    }

    @Override
    public int compareTo(Arco compareArco) {
        return Integer.compare(this.distancia, compareArco.distancia);
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public void marcarComoVisitado() {
        this.nodoEstacionVisitado = true;
    }

    public boolean isNodoEstacionVisitado() {
        return nodoEstacionVisitado;
    }

    @Override
    public String toString() {
        return "Arco{" +
               "src=" + src +
               ", dest=" + dest +
               ", distancia=" + distancia +
               ", visitado=" + nodoEstacionVisitado +
               '}';
    }
}
