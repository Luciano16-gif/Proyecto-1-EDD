package Objetos;

import javax.swing.JOptionPane;
import primitivas.Lista;

public class InterfazFunciones {
    public Grafos grafo;
    public Lista<Estacion> estaciones;
    public Lista<Sucursal> sucursales; // Lista para almacenar sucursales
    public Lista<Lista<Integer>> coberturasSucursales;

    public InterfazFunciones(Grafos grafo, Lista<Estacion> estaciones, Lista<Sucursal> sucursales) {
        this.grafo = grafo; // Asignar el grafo recibido a la variable de instancia
        this.estaciones = estaciones; // Asignar la lista de estaciones
        this.sucursales = sucursales; // Asignar la lista de sucursales
        this.coberturasSucursales = new Lista<>(); // Inicializar la lista de coberturas
    }

    
    public void inicializarGrafoYEstaciones() {
    grafo = new Grafos();
        estaciones = new Lista<>();
        Funcion.ReadJsonMetro(grafo, estaciones);
        grafo.mostrarGrafo(estaciones);

        System.out.println("Estaciones cargadas:");
        for (int i = 0; i < estaciones.len(); i++) {
            System.out.println("- '" + estaciones.get(i).getNombre() + "'");
        }
    }
    
    public void agregarNuevaLinea() {
    // Cargar una nueva línea y agregarla al grafo existente
    Funcion.agregarNuevaLinea(grafo, estaciones);
    
    // Mostrar el grafo actualizado
    grafo.mostrarGrafo(estaciones);
    
    // Imprimir las estaciones cargadas
    System.out.println("Estaciones cargadas:");
    for (int i = 0; i < estaciones.len(); i++) {
        System.out.println("- '" + estaciones.get(i).getNombre() + "'");
    }
    }
    
    public boolean sucursalExists(String nombreSucursal) {
        for (int i = 0; i < sucursales.len(); i++) {
            if (sucursales.get(i).getNombre().equals(nombreSucursal)) {
                return true; // La sucursal ya existe
            }
        }
    return false; // La sucursal no existe
    }

    // Buscar estación por nombre
    public Estacion buscarEstacionPorNombre(String nombre) {
        nombre = nombre.trim().toLowerCase();
        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            if (estacion.getNombre().trim().toLowerCase().equals(nombre)) {
                return estacion;
            }
        }
        return null;
    }

    public void actualizarCoberturaEstaciones() {
    // Crear un arreglo de booleanos para marcar estaciones cubiertas
    boolean[] cubiertas = new boolean[estaciones.len()];

    // Revisar todas las coberturas de las sucursales restantes
    for (int i = 0; i < coberturasSucursales.len(); i++) {
        Lista<Integer> cobertura = coberturasSucursales.get(i);
        for (int j = 0; j < cobertura.len(); j++) {
            cubiertas[cobertura.get(j)] = true; // Marcar estación como cubierta
        }
    }

    // Marcar las estaciones que ya no están cubiertas
    for (int i = 0; i < estaciones.len(); i++) {
        if (!cubiertas[i]) {
            estaciones.get(i).setEsSucursal(false); // Marcar como no cubierta
        }
    }
}


    public void eliminarSucursal(String nombreEstacion) {
    if (nombreEstacion == null || nombreEstacion.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, 
                "Ingrese un nombre válido de estación.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    nombreEstacion = nombreEstacion.trim().toLowerCase(); // Normalización

    // Buscar la estación en la lista
    Estacion estacion = buscarEstacionPorNombre(nombreEstacion);

    if (estacion != null && estacion.esSucursal()) {
        // Buscar la sucursal asociada a esta estación y su índice
        for (int i = 0; i < sucursales.len(); i++) {
            Sucursal sucursal = sucursales.get(i);

            // Verificar si la estación está dentro de la sucursal
            if (sucursal.getEstaciones().exist(estacion)) {
                // Marcar la estación como no sucursal
                estacion.setEsSucursal(false);

                // Eliminar la sucursal y su cobertura
                sucursales.pop(i);
                coberturasSucursales.pop(i);

                // Actualizar las coberturas restantes
                actualizarCoberturaEstaciones();

                // Actualizar la visualización del grafo
                grafo.resaltarEstaciones(coberturasSucursales, estaciones);

                JOptionPane.showMessageDialog(null, 
                        "Sucursal en la estación '" + nombreEstacion + "' eliminada con éxito.", 
                        "Eliminar Sucursal", 
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    // Si no se encontró la estación o no es sucursal
    JOptionPane.showMessageDialog(null, 
            "No se encontró una sucursal en la estación indicada. Por favor, verifique el nombre.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
}


    // Calcular cobertura usando DFS o BFS y agregar nombres de estaciones cubiertas
    public Lista<Integer> calcularCobertura(Estacion estacionSucursal, int t, String tipoBusqueda) {
        int indiceSucursal = estaciones.indexOf(estacionSucursal);
        boolean[] visitados = new boolean[estaciones.len()];
        Lista<Integer> cobertura = new Lista<>();

        if (tipoBusqueda.equals("BFS")) {
            bfs(indiceSucursal, t, visitados, cobertura);
        } else {
            dfs(indiceSucursal, t, 0, visitados, cobertura);
        }
        
        return cobertura;
    }

    public void bfs(int inicio, int limiteT, boolean[] visitados, Lista<Integer> cobertura) {
        Lista<Integer> cola = new Lista<>();
        int[] niveles = new int[estaciones.len()];

        cola.append(inicio);
        visitados[inicio] = true;
        niveles[inicio] = 0;
        cobertura.append(inicio);

        while (cola.len() > 0) {
            int actual = cola.get(0);
            cola.pop(0);

            if (niveles[actual] >= limiteT) continue;

            Lista<Integer> adyacentes = grafo.getAdyacentes(actual);
            for (int i = 0; i < adyacentes.len(); i++) {
                int vecino = adyacentes.get(i);
                if (!visitados[vecino]) {
                    cola.append(vecino);
                    visitados[vecino] = true;
                    niveles[vecino] = niveles[actual] + 1;
                    cobertura.append(vecino);
                }
            }
        }
    }

    public void dfs(int actual, int limiteT, int nivelActual, boolean[] visitados, Lista<Integer> cobertura) {
        if (nivelActual > limiteT) return;
        visitados[actual] = true;
        cobertura.append(actual);

        Lista<Integer> adyacentes = grafo.getAdyacentes(actual);
        for (int i = 0; i < adyacentes.len(); i++) {
            int vecino = adyacentes.get(i);
            if (!visitados[vecino]) {
                dfs(vecino, limiteT, nivelActual + 1, visitados, cobertura);
            }
        }
    }

    public boolean todasEstacionesCubiertas() {
        for (int i = 0; i < estaciones.len(); i++) {
            boolean cubierta = false;
            for (int j = 0; j < coberturasSucursales.len(); j++) {
                Lista<Integer> cobertura = coberturasSucursales.get(j);
                if (cobertura.exist(i)) {
                    cubierta = true;
                    break;
                }
            }
            if (!cubierta) {
                return false; // Si una estación no está cubierta, retornamos false
            }
        }
        return true;
    }
    
    public void actualizarCoberturasSucursales(int t, String tipoBusqueda) {
        coberturasSucursales = new Lista<>();
        for (int i = 0; i < sucursales.len(); i++) {
            Estacion estacionSucursal = sucursales.get(i).getEstaciones().get(0); // Asumiendo una estación base por sucursal
            Lista<Integer> cobertura = calcularCobertura(estacionSucursal, t, tipoBusqueda);
            coberturasSucursales.append(cobertura);
        }
    }
    
    public void sugerirSucursales(int t, String tipoBusqueda) {
    // Verificar si todas las estaciones están cubiertas
    
    actualizarCoberturasSucursales(t, tipoBusqueda);
   
    if (todasEstacionesCubiertas()) {
        JOptionPane.showMessageDialog(null,
                "Todas las estaciones ya están cubiertas por alguna sucursal.",
                "Sugerencia de Sucursal",
                JOptionPane.INFORMATION_MESSAGE);
        return; // No es necesario sugerir más sucursales
    }
    
    
    int n = estaciones.len();
    boolean[] estacionesCubiertas = new boolean[n];

    // Marcar las estaciones ya cubiertas por las sucursales existentes
    for (int i = 0; i < coberturasSucursales.len(); i++) {
        Lista<Integer> cobertura = coberturasSucursales.get(i);
        for (int j = 0; j < cobertura.len(); j++) {
            int idx = cobertura.get(j);
            estacionesCubiertas[idx] = true;
        }
    }

    Lista<Estacion> sucursalesSugeridas = new Lista<>();

    // Continuar mientras haya estaciones no cubiertas
    while (true) {
        // Verificar si todas las estaciones están cubiertas
        boolean todasCubiertas = true;
        for (int i = 0; i < n; i++) {
            if (!estacionesCubiertas[i]) {
                todasCubiertas = false;
                break;
            }
        }
        if (todasCubiertas) {
            break; // Todas las estaciones están cubiertas
        }

        Estacion mejorEstacion = null;
        int maxCobertura = 0;
        Lista<Integer> mejorCobertura = new Lista<>();

        // Buscar la estación que cubre más estaciones no cubiertas
        for (int i = 0; i < n; i++) {
            if (!estacionesCubiertas[i]) {
                Estacion estacion = estaciones.get(i);

                // Asegurarse de que la estación no es una sucursal existente
                if (!estacion.esSucursal()) {
                    // Calcular cobertura de esta estación
                    Lista<Integer> cobertura = calcularCobertura(estacion, t, tipoBusqueda);

                    // Contar cuántas estaciones de la cobertura no están cubiertas
                    int count = 0;
                    for (int j = 0; j < cobertura.len(); j++) {
                        int idxCubierta = cobertura.get(j);
                        if (!estacionesCubiertas[idxCubierta]) {
                            count++;
                        }
                    }

                    // Verificar si esta estación cubre más estaciones no cubiertas
                    if (count > maxCobertura) {
                        maxCobertura = count;
                        mejorEstacion = estacion;
                        mejorCobertura = cobertura; // Guardar la mejor cobertura
                    }
                }
            }
        }

        // Agregar la mejor estación como sucursal sugerida
        if (mejorEstacion != null) {
            sucursalesSugeridas.append(mejorEstacion);

            // Marcar las estaciones cubiertas por esta sucursal
            for (int j = 0; j < mejorCobertura.len(); j++) {
                int idx = mejorCobertura.get(j);
                estacionesCubiertas[idx] = true;
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "No se pudo cubrir todas las estaciones con t = " + t,
                    "Error en la sugerencia",
                    JOptionPane.WARNING_MESSAGE);
            break;
        }
    }

    // Mostrar las estaciones sugeridas al usuario
    StringBuilder mensaje = new StringBuilder("Se sugiere colocar sucursales en:\n");
    for (int i = 0; i < sucursalesSugeridas.len(); i++) {
        mensaje.append("- ").append(sucursalesSugeridas.get(i).getNombre()).append("\n");
    }

    JOptionPane.showMessageDialog(null,
            mensaje.toString() + "Utilizando t = " + t + " y el método " + tipoBusqueda + ".",
            "Sugerencia de Sucursales",
            JOptionPane.INFORMATION_MESSAGE);
}



public void marcarComoCubiertas(Lista<Integer> cobertura, Lista<Estacion> estacionesNoCubiertas) {
    // Almacenamos las estaciones a eliminar en una lista temporal
    Lista<Estacion> estacionesAEliminar = new Lista<>();
    
    for (int i = 0; i < cobertura.len(); i++) {
        int indiceEstacion = cobertura.get(i);
        Estacion estacionCubierta = estaciones.get(indiceEstacion); // Obtén la estación correspondiente
        estacionesAEliminar.append(estacionCubierta); // Agregar a la lista temporal
    }

    // Eliminar las estaciones cubiertas de la lista estacionesNoCubiertas
    for (int i = 0; i < estacionesAEliminar.len(); i++) {
        estacionesNoCubiertas.remove(estacionesAEliminar.get(i)); // Eliminar la estación de la lista
    }
}


// Función para obtener las estaciones no cubiertas por las sucursales actuales
public Lista<Estacion> obtenerEstacionesNoCubiertas() {
    Lista<Estacion> noCubiertas = new Lista<>();
    for (int i = 0; i < estaciones.len(); i++) {
        Estacion estacion = estaciones.get(i);
        boolean cubierta = false;

        // Verificar si la estación está cubierta por alguna sucursal
        for (int j = 0; j < coberturasSucursales.len(); j++) {
            Lista<Integer> cobertura = coberturasSucursales.get(j);
            if (cobertura.exist(i)) {
                cubierta = true;
                break;
            }
        }

        if (!cubierta) {
            noCubiertas.append(estacion);
        }
    }
    return noCubiertas;
}

public void sugerirSucursal(int t, String tipoBusqueda) {
    Estacion mejorEstacion = null;
    int maxCoberturaNoCubierta = 0;

    // Iterar sobre todas las estaciones
    for (int i = 0; i < estaciones.len(); i++) {
        Estacion estacion = estaciones.get(i);

        // Ignorar estaciones que ya son sucursales o que están cubiertas
        if (estacion.esSucursal() || estaCubiertaPorOtraSucursal(i)) {
            continue;
        }

        // Calcular la cobertura para esta estación
        Lista<Integer> cobertura = calcularCobertura(estacion, t, tipoBusqueda);

        // Filtrar las estaciones que ya están cubiertas
        Lista<Integer> estacionesNoCubiertas = filtrarEstacionesNoCubiertas(cobertura);

        // Verificar si esta estación tiene más cobertura no cubierta
        if (estacionesNoCubiertas.len() > maxCoberturaNoCubierta) {
            maxCoberturaNoCubierta = estacionesNoCubiertas.len();
            mejorEstacion = estacion;
        }
    }

    // Mostrar sugerencia al usuario
    if (mejorEstacion != null) {
        JOptionPane.showMessageDialog(null,
            "Se sugiere convertir la estación '" + mejorEstacion.getNombre() + 
            "' en sucursal, ya que cubre " + maxCoberturaNoCubierta + 
            " estaciones no cubiertas usando " + tipoBusqueda + ".",
            "Sugerencia de Sucursal",
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(null,
            "No hay estaciones disponibles para sugerir como sucursal.",
            "Sugerencia de Sucursal",
            JOptionPane.WARNING_MESSAGE);
    }
}
    
    public Lista<Integer> filtrarEstacionesNoCubiertas(Lista<Integer> cobertura) {
    Lista<Integer> estacionesNoCubiertas = new Lista<>();

    for (int i = 0; i < cobertura.len(); i++) {
        int indiceEstacion = cobertura.get(i);

        boolean estaCubierta = false;
        // Verificar si la estación ya está cubierta por alguna sucursal existente
        for (int j = 0; j < coberturasSucursales.len(); j++) {
            Lista<Integer> coberturaSucursal = coberturasSucursales.get(j);
            if (coberturaSucursal.exist(indiceEstacion)) {
                estaCubierta = true;
                break;
            }
        }

        // Si no está cubierta, agregarla a la lista de estaciones no cubiertas
        if (!estaCubierta) {
            estacionesNoCubiertas.append(indiceEstacion);
        }
    }

    return estacionesNoCubiertas;
}

    public boolean estaCubiertaPorOtraSucursal(int indiceEstacion) {
    for (int i = 0; i < coberturasSucursales.len(); i++) {
        Lista<Integer> coberturaSucursal = coberturasSucursales.get(i);
        if (coberturaSucursal.exist(indiceEstacion)) {
            return true; // La estación ya está cubierta
        }
    }
    return false; // La estación no está cubierta
    }
    
    public void agregarSucursal(String nombreEstacion, int t,String tipoBusqueda) {
    // Verificar que se haya definido 't' y el tipo de búsqueda
    if (t == 0) {
        JOptionPane.showMessageDialog(null, "Por favor, establezca un valor para 't' antes de agregar una sucursal.");
        return;
    }

    if (tipoBusqueda == null || tipoBusqueda.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione el tipo de búsqueda (DFS o BFS).");
        return;
    }

    if (!nombreEstacion.isEmpty()) {
        Estacion estacionSucursal = buscarEstacionPorNombre(nombreEstacion);
        if (estacionSucursal != null) {
            if (!estacionSucursal.esSucursal()) {
                // Crear una nueva sucursal
                String nombreSucursal = "Sucursal_" + sucursales.len();
                Sucursal nuevaSucursal = new Sucursal(nombreSucursal, t);
                nuevaSucursal.agregarEstacion(estacionSucursal);
                sucursales.append(nuevaSucursal);

                // Marcar la estación como sucursal
                estacionSucursal.setEsSucursal(true);

                // Calcular la cobertura utilizando el tipo de búsqueda seleccionado
                Lista<Integer> cobertura = calcularCobertura(estacionSucursal, t, tipoBusqueda);
                coberturasSucursales.append(cobertura);

                // Actualizar la visualización del grafo
                grafo.resaltarEstaciones(coberturasSucursales, estaciones);

                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(null, "Sucursal añadida exitosamente usando " + tipoBusqueda + ".");
            } else {
                JOptionPane.showMessageDialog(null, "La estación seleccionada ya es una sucursal.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Estación no encontrada. Por favor, verifique el nombre.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Ingrese el nombre de una estación válida.");
    }
}

}