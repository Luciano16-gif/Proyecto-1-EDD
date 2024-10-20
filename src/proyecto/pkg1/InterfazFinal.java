package proyecto.pkg1;

import Objetos.Funcion;
import Objetos.Grafos;
import Objetos.Estacion;
import Objetos.Sucursal;
import primitivas.Lista;
import javax.swing.JOptionPane;

/**
 * Esta clase es la interfaz
 * 
 * @author: Ricardo Paez - Luciano Minardo - Gabriele Colarusso
 * @version: 16/10/2024
 */
public class InterfazFinal extends javax.swing.JFrame {

    private int t = 0; // Valor inicial de t
    private Grafos grafo;
    private Lista<Estacion> estaciones;
    private Lista<Sucursal> sucursales; // Lista para almacenar sucursales
    private Lista<Lista<Integer>> coberturasSucursales;
    private String tipoBusqueda = "";

    public InterfazFinal() {
        initComponents();
        grafo = new Grafos();
        estaciones = new Lista<>();
        sucursales = new Lista<>();
        coberturasSucursales = new Lista<>();
    }
    
    private boolean sucursalExists(String nombreSucursal) {
        for (int i = 0; i < sucursales.len(); i++) {
            if (sucursales.get(i).getNombre().equals(nombreSucursal)) {
                return true; // La sucursal ya existe
            }
        }
    return false; // La sucursal no existe
}

    // Buscar estación por nombre
    private Estacion buscarEstacionPorNombre(String nombre) {
        nombre = nombre.trim().toLowerCase();
        for (int i = 0; i < estaciones.len(); i++) {
            Estacion estacion = estaciones.get(i);
            if (estacion.getNombre().trim().toLowerCase().equals(nombre)) {
                return estacion;
            }
        }
        return null;
    }

    private void agregarSucursalActionPerformed(java.awt.event.ActionEvent evt) {
        String nombreEstacion = agregarSurcusal.getText();
        if (nombreEstacion != null && !nombreEstacion.trim().isEmpty()) {
            nombreEstacion = nombreEstacion.trim();
            
            // Verificar si la estación existe
            Estacion estacionBase = buscarEstacionPorNombre(nombreEstacion);
            if (estacionBase != null) {
                // Verificar si la estación ya es una sucursal
                if (!estacionBase.esSucursal()) {
                    // Solicitar nombre para la nueva sucursal
                    String nombreSucursal = JOptionPane.showInputDialog(this, "Ingrese un nombre para la sucursal:");
                    if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
                        nombreSucursal = nombreSucursal.trim();
                        
                        // Verificar si ya existe una sucursal con ese nombre
                        if (!sucursalExists(nombreSucursal)) {
                            // Crear nueva sucursal
                            Sucursal nuevaSucursal = new Sucursal(nombreSucursal, t);
                            nuevaSucursal.agregarEstacion(estacionBase);
                            sucursales.append(nuevaSucursal);
                            
                            // Marcar la estación como sucursal
                            estacionBase.setEsSucursal(true);
                            
                            // Calcular y agregar la cobertura de la nueva sucursal
                            String[] opciones = {"BFS", "DFS"};
                            int opcionBusqueda = JOptionPane.showOptionDialog(this,
                                "Seleccione el tipo de búsqueda:",
                                "Tipo de Búsqueda",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                opciones,
                                opciones[0]);
                            
                            if (opcionBusqueda != -1) {
                                String tipoBusqueda = opciones[opcionBusqueda];
                                Lista<Integer> cobertura = calcularCobertura(estacionBase, t, tipoBusqueda);
                                coberturasSucursales.append(cobertura);
                                
                                // Actualizar la visualización del grafo
                                grafo.resaltarEstaciones(coberturasSucursales, estaciones);
                                
                                JOptionPane.showMessageDialog(this, 
                                    "Sucursal '" + nombreSucursal + "' agregada con éxito en la estación '" + 
                                    nombreEstacion + "'");
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Ya existe una sucursal con ese nombre.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Esta estación ya es una sucursal.");
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Estación no encontrada. Por favor, verifique el nombre.");
            }
        }
    }

    private void actualizarCoberturaEstaciones() {
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


    private void eliminarSucursal(String nombreEstacion) {
    if (nombreEstacion == null || nombreEstacion.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, 
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

                JOptionPane.showMessageDialog(this, 
                        "Sucursal en la estación '" + nombreEstacion + "' eliminada con éxito.", 
                        "Eliminar Sucursal", 
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    // Si no se encontró la estación o no es sucursal
    JOptionPane.showMessageDialog(this, 
            "No se encontró una sucursal en la estación indicada. Por favor, verifique el nombre.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
}


    // Calcular cobertura usando DFS o BFS y agregar nombres de estaciones cubiertas
    private Lista<Integer> calcularCobertura(Estacion estacionSucursal, int t, String tipoBusqueda) {
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

    private void bfs(int inicio, int limiteT, boolean[] visitados, Lista<Integer> cobertura) {
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

    private void dfs(int actual, int limiteT, int nivelActual, boolean[] visitados, Lista<Integer> cobertura) {
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

    private boolean todasEstacionesCubiertas() {
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
    
    private void actualizarCoberturasSucursales() {
        coberturasSucursales = new Lista<>();
        for (int i = 0; i < sucursales.len(); i++) {
            Estacion estacionSucursal = sucursales.get(i).getEstaciones().get(0); // Asumiendo una estación base por sucursal
            Lista<Integer> cobertura = calcularCobertura(estacionSucursal, t, tipoBusqueda);
            coberturasSucursales.append(cobertura);
        }
    }
    
    private void sugerirSucursales() {
    // Verificar si todas las estaciones están cubiertas
    
    actualizarCoberturasSucursales();
   
    if (todasEstacionesCubiertas()) {
        JOptionPane.showMessageDialog(this,
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
            JOptionPane.showMessageDialog(this,
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

    JOptionPane.showMessageDialog(this,
            mensaje.toString() + "Utilizando t = " + t + " y el método " + tipoBusqueda + ".",
            "Sugerencia de Sucursales",
            JOptionPane.INFORMATION_MESSAGE);
}



private void marcarComoCubiertas(Lista<Integer> cobertura, Lista<Estacion> estacionesNoCubiertas) {
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
private Lista<Estacion> obtenerEstacionesNoCubiertas() {
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

private void sugerirSucursal() {
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
        JOptionPane.showMessageDialog(this,
            "Se sugiere convertir la estación '" + mejorEstacion.getNombre() + 
            "' en sucursal, ya que cubre " + maxCoberturaNoCubierta + 
            " estaciones no cubiertas usando " + tipoBusqueda + ".",
            "Sugerencia de Sucursal",
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this,
            "No hay estaciones disponibles para sugerir como sucursal.",
            "Sugerencia de Sucursal",
            JOptionPane.WARNING_MESSAGE);
    }
}
    
    private Lista<Integer> filtrarEstacionesNoCubiertas(Lista<Integer> cobertura) {
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

    private boolean estaCubiertaPorOtraSucursal(int indiceEstacion) {
    for (int i = 0; i < coberturasSucursales.len(); i++) {
        Lista<Integer> coberturaSucursal = coberturasSucursales.get(i);
        if (coberturaSucursal.exist(indiceEstacion)) {
            return true; // La estación ya está cubierta
        }
    }
    return false; // La estación no está cubierta
}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        botonCargarJson = new javax.swing.JButton();
        botonEstablecerT = new javax.swing.JButton();
        obtenerT = new javax.swing.JTextField();
        dfs = new javax.swing.JButton();
        bfs = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        botonEliminarSucursal = new javax.swing.JButton();
        agregarSurcusal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        BotonVerificarCobertura = new javax.swing.JButton();
        botonAgregarSurcusal1 = new javax.swing.JButton();
        sugerirSurcusal = new javax.swing.JButton();
        agregarLineaNueva = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        jLabel6.setText("jLabel6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botonCargarJson.setBackground(new java.awt.Color(204, 255, 255));
        botonCargarJson.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonCargarJson.setText("Cargar JSON");
        botonCargarJson.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCargarJsonActionPerformed(evt);
            }
        });
        getContentPane().add(botonCargarJson, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 200, 60));

        botonEstablecerT.setBackground(new java.awt.Color(204, 255, 255));
        botonEstablecerT.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonEstablecerT.setText("Establecer T");
        botonEstablecerT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEstablecerTActionPerformed(evt);
            }
        });
        getContentPane().add(botonEstablecerT, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 430, 170, 50));
        getContentPane().add(obtenerT, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 390, 170, -1));

        dfs.setBackground(new java.awt.Color(204, 255, 255));
        dfs.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        dfs.setText("DFS");
        dfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dfsActionPerformed(evt);
            }
        });
        getContentPane().add(dfs, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 280, 100, 50));

        bfs.setBackground(new java.awt.Color(204, 255, 255));
        bfs.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        bfs.setText("BFS");
        bfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bfsActionPerformed(evt);
            }
        });
        getContentPane().add(bfs, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 100, 50));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel1.setText("Seleccione el algoritmo deseado para colocar ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, 310, 20));

        botonEliminarSucursal.setBackground(new java.awt.Color(204, 255, 255));
        botonEliminarSucursal.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonEliminarSucursal.setText("Eliminar Sucursal");
        botonEliminarSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSucursalActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarSucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, 210, 50));
        getContentPane().add(agregarSurcusal, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, 350, 30));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel2.setText("Introduzca el numero de T con el cual ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 340, 250, 20));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("desea implementar las sucursales.");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 360, 220, -1));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel4.setText("Escriba el nombre de la estacion para agregar o eliminar sucursal.");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, -1));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel5.setText("y ver la cobertura de la sucursal.");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 250, -1, -1));

        BotonVerificarCobertura.setBackground(new java.awt.Color(204, 255, 255));
        BotonVerificarCobertura.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        BotonVerificarCobertura.setText("Verificar Cobertura Completa");
        BotonVerificarCobertura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonVerificarCoberturaActionPerformed(evt);
            }
        });
        getContentPane().add(BotonVerificarCobertura, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 260, 50));

        botonAgregarSurcusal1.setBackground(new java.awt.Color(204, 255, 255));
        botonAgregarSurcusal1.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonAgregarSurcusal1.setText("Agregar sucursal");
        botonAgregarSurcusal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarSurcusal1ActionPerformed(evt);
            }
        });
        getContentPane().add(botonAgregarSurcusal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, 290, 50));

        sugerirSurcusal.setBackground(new java.awt.Color(204, 255, 255));
        sugerirSurcusal.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        sugerirSurcusal.setText("Sugerir Sucursal");
        sugerirSurcusal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sugerirSurcusalActionPerformed(evt);
            }
        });
        getContentPane().add(sugerirSurcusal, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 180, 50));

        agregarLineaNueva.setBackground(new java.awt.Color(204, 255, 255));
        agregarLineaNueva.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        agregarLineaNueva.setText("Agregar Linea Nueva");
        agregarLineaNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarLineaNuevaActionPerformed(evt);
            }
        });
        getContentPane().add(agregarLineaNueva, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, 200, 60));
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 260, 30));
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, 330, -1));

        jLabel10.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel10.setText("Antes de sugerir sucursal asegurese de haber");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 20));

        jLabel11.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel11.setText("establecido T y su algoritmo de busqueda.");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, -1, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dfsActionPerformed
        tipoBusqueda = "DFS";
        JOptionPane.showMessageDialog(this, "El tipo de búsqueda se ha establecido en DFS.");
    }//GEN-LAST:event_dfsActionPerformed

    private void bfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bfsActionPerformed
        tipoBusqueda = "BFS";
        JOptionPane.showMessageDialog(this, "El tipo de búsqueda se ha establecido en BFS.");
    }//GEN-LAST:event_bfsActionPerformed

    private void botonEstablecerTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEstablecerTActionPerformed
        String input = obtenerT.getText();
        try {
            int nuevoT = Integer.parseInt(input);
            if (nuevoT > 0) {
                t = nuevoT;
                JOptionPane.showMessageDialog(this, "El valor de t se ha establecido en " + t);
            } else {
                JOptionPane.showMessageDialog(this, "Ingrese un valor de t mayor que cero.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Entrada inválida. Por favor, ingrese un número entero.");
        }
    }//GEN-LAST:event_botonEstablecerTActionPerformed

    private void botonCargarJsonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCargarJsonActionPerformed
        grafo = new Grafos();
        estaciones = new Lista<>();
        Funcion.ReadJsonMetro(grafo, estaciones);
        grafo.mostrarGrafo(estaciones);

        System.out.println("Estaciones cargadas:");
        for (int i = 0; i < estaciones.len(); i++) {
            System.out.println("- '" + estaciones.get(i).getNombre() + "'");
        }
    }//GEN-LAST:event_botonCargarJsonActionPerformed

    private void botonEliminarSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarSucursalActionPerformed
       String nombreSucursal = agregarSurcusal.getText().trim();
    eliminarSucursal(nombreSucursal);
    }//GEN-LAST:event_botonEliminarSucursalActionPerformed

    private void BotonVerificarCoberturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonVerificarCoberturaActionPerformed
        if (todasEstacionesCubiertas()) {
            JOptionPane.showMessageDialog(this, "Todas las estaciones están cubiertas por una sucursal.");
        } else {
            JOptionPane.showMessageDialog(this, "No todas las estaciones están cubiertas.");
        }
    }//GEN-LAST:event_BotonVerificarCoberturaActionPerformed

    private void botonAgregarSurcusal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAgregarSurcusal1ActionPerformed
    String nombreEstacion = agregarSurcusal.getText().trim();
    
    // Verificar que se haya definido 't' y el tipo de búsqueda
    if (t == 0) {
        JOptionPane.showMessageDialog(this, "Por favor, establezca un valor para 't' antes de agregar una sucursal.");
        return;
    }

    if (tipoBusqueda == null || tipoBusqueda.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione el tipo de búsqueda (DFS o BFS).");
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
                JOptionPane.showMessageDialog(this, "Sucursal añadida exitosamente usando " + tipoBusqueda + ".");
            } else {
                JOptionPane.showMessageDialog(this, "La estación seleccionada ya es una sucursal.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Estación no encontrada. Por favor, verifique el nombre.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Ingrese el nombre de una estación válida.");
    }
    }//GEN-LAST:event_botonAgregarSurcusal1ActionPerformed

    private void sugerirSurcusalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sugerirSurcusalActionPerformed
        if (todasEstacionesCubiertas()) {
        JOptionPane.showMessageDialog(this, 
                "Todas las estaciones ya están cubiertas por al menos una sucursal.", 
                "Cobertura Completa", 
                JOptionPane.INFORMATION_MESSAGE);
    } else {
        sugerirSucursal();
    }
    }//GEN-LAST:event_sugerirSurcusalActionPerformed

    private void agregarLineaNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarLineaNuevaActionPerformed
    // Cargar una nueva línea y agregarla al grafo existente
    Funcion.agregarNuevaLinea(grafo, estaciones);
    
    // Mostrar el grafo actualizado
    grafo.mostrarGrafo(estaciones);
    
    // Imprimir las estaciones cargadas
    System.out.println("Estaciones cargadas:");
    for (int i = 0; i < estaciones.len(); i++) {
        System.out.println("- '" + estaciones.get(i).getNombre() + "'");
    }
    }//GEN-LAST:event_agregarLineaNuevaActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonVerificarCobertura;
    private javax.swing.JButton agregarLineaNueva;
    private javax.swing.JTextField agregarSurcusal;
    private javax.swing.JButton bfs;
    private javax.swing.JButton botonAgregarSurcusal1;
    private javax.swing.JButton botonCargarJson;
    private javax.swing.JButton botonEliminarSucursal;
    private javax.swing.JButton botonEstablecerT;
    private javax.swing.JButton dfs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField obtenerT;
    private javax.swing.JButton sugerirSurcusal;
    // End of variables declaration//GEN-END:variables
}
