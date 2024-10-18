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

    private int t = 1; // Valor inicial de t
    private Grafos grafo;
    private Lista<Estacion> estaciones;
    private Lista<Sucursal> sucursales; // Lista para almacenar sucursales
    private Lista<Lista<Integer>> coberturasSucursales;
    
    private String tipoBusqueda = "BFS";

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



    private void quitarSucursal(String nombreSucursal) {
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            for (int i = 0; i < sucursales.len(); i++) {
                if (sucursales.get(i).getNombre().equals(nombreSucursal.trim())) {
                    // Obtener la estación base de la sucursal
                    Estacion estacionBase = sucursales.get(i).getEstaciones().get(0);
                    estacionBase.setEsSucursal(false);
                
                    // Eliminar la sucursal y su cobertura
                    sucursales.pop(i);
                    coberturasSucursales.pop(i);

                    // Actualizar la visualización del grafo
                    grafo.resaltarEstaciones(coberturasSucursales, estaciones);

                    JOptionPane.showMessageDialog(this, 
                        "Sucursal '" + nombreSucursal + "' eliminada con éxito.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, 
                "Sucursal no encontrada. Por favor, verifique el nombre.");
        }
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
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
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

        jTextField1.setText("jTextField1");

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
        getContentPane().add(botonCargarJson, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 150, 60));

        botonEstablecerT.setBackground(new java.awt.Color(204, 255, 255));
        botonEstablecerT.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonEstablecerT.setText("Establecer T");
        botonEstablecerT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEstablecerTActionPerformed(evt);
            }
        });
        getContentPane().add(botonEstablecerT, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 210, 130, 40));
        getContentPane().add(obtenerT, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 170, -1));

        dfs.setBackground(new java.awt.Color(204, 255, 255));
        dfs.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        dfs.setText("DFS");
        dfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dfsActionPerformed(evt);
            }
        });
        getContentPane().add(dfs, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 210, 100, 50));

        bfs.setBackground(new java.awt.Color(204, 255, 255));
        bfs.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        bfs.setText("BFS");
        bfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bfsActionPerformed(evt);
            }
        });
        getContentPane().add(bfs, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 210, 100, 50));

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel1.setText("Seleccione el algoritmo deseado para colocar ");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 160, 310, 20));

        botonEliminarSucursal.setBackground(new java.awt.Color(204, 255, 255));
        botonEliminarSucursal.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonEliminarSucursal.setText("Eliminar Sucursal");
        botonEliminarSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarSucursalActionPerformed(evt);
            }
        });
        getContentPane().add(botonEliminarSucursal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 320, 160, 50));
        getContentPane().add(agregarSurcusal, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 350, 30));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel2.setText("Introduzca el numero de T con el cual ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 250, 20));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("desea implementar las sucursales");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 220, -1));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel4.setText("Escriba el nombre de la estacion para agregar sucursal");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, -1, -1));

        jLabel5.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel5.setText("y ver la cobertura de la sucursal.");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, -1, -1));

        BotonVerificarCobertura.setBackground(new java.awt.Color(204, 255, 255));
        BotonVerificarCobertura.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        BotonVerificarCobertura.setText("Verificar Cobertura Completa");
        BotonVerificarCobertura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonVerificarCoberturaActionPerformed(evt);
            }
        });
        getContentPane().add(BotonVerificarCobertura, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 260, 40));

        botonAgregarSurcusal1.setBackground(new java.awt.Color(204, 255, 255));
        botonAgregarSurcusal1.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonAgregarSurcusal1.setText("Agregar sucursal");
        botonAgregarSurcusal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAgregarSurcusal1ActionPerformed(evt);
            }
        });
        getContentPane().add(botonAgregarSurcusal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 290, 50));

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
       String nombreSucursal = agregarSurcusal.getText();
        quitarSucursal(nombreSucursal); // Llama a la función para quitar la sucursal
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
    if (nombreEstacion != null && !nombreEstacion.isEmpty()) {
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
                JOptionPane.showMessageDialog(this, "Sucursal '" + nombreSucursal + "' añadida exitosamente usando " + tipoBusqueda + ".");
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

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonVerificarCobertura;
    private javax.swing.JTextField agregarSurcusal;
    private javax.swing.JButton bfs;
    private javax.swing.JButton botonAgregarSurcusal1;
    private javax.swing.JButton botonCargarJson;
    private javax.swing.JButton botonEliminarSucursal;
    private javax.swing.JButton botonEstablecerT;
    private javax.swing.JButton dfs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField obtenerT;
    // End of variables declaration//GEN-END:variables
}
