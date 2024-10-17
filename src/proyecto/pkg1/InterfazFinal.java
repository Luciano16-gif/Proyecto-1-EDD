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
    private Lista<Lista<String>> estacionesCubiertasPorSucursal; // Lista para nombres de estaciones cubiertas por cada sucursal
    private Lista<Sucursal> sucursales; // Lista de sucursales

    public InterfazFinal() {
        initComponents();
        grafo = new Grafos();
        estaciones = new Lista<>();
        estacionesCubiertasPorSucursal = new Lista<>();
        sucursales = new Lista<>(); // Lista para almacenar sucursales
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

    private void agregarSucursal(String nombreSucursal, int tSucursal, Lista<String> estacionesNombres) {
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            if (!sucursalExists(nombreSucursal.trim())) {
                Sucursal nuevaSucursal = new Sucursal(nombreSucursal.trim(), tSucursal); // Crear nueva sucursal

                // Agregar estaciones a la sucursal
                for (int i = 0; i < estacionesNombres.len(); i++) {
                    String nombreEstacion = estacionesNombres.get(i).trim();
                    Estacion estacion = buscarEstacionPorNombre(nombreEstacion);
                    if (estacion != null) {
                        nuevaSucursal.agregarEstacion(estacion); // Agregar estación a la nueva sucursal
                    }
                }

                sucursales.append(nuevaSucursal); // Agregar la nueva sucursal a la lista de sucursales
                // Calcular cobertura para la primera estación de la sucursal (puedes ajustar esto si es necesario)
                if (nuevaSucursal.getEstaciones().len() > 0) {
                    Estacion primeraEstacion = nuevaSucursal.getEstaciones().get(0);
                    calcularCobertura(primeraEstacion, tSucursal, "DFS"); // Calcular cobertura
                }

                JOptionPane.showMessageDialog(this, "Sucursal '" + nombreSucursal + "' agregada con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Sucursal ya existe.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de sucursal válido.");
        }
    }

    private boolean sucursalExists(String nombreSucursal) {
        for (int i = 0; i < sucursales.len(); i++) {
            if (sucursales.get(i).getNombre().equals(nombreSucursal)) {
                return true; // La sucursal ya existe
            }
        }
        return false; // La sucursal no existe
    }

    private void quitarSucursal(String nombreSucursal) {
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            for (int i = 0; i < sucursales.len(); i++) {
                if (sucursales.get(i).getNombre().equals(nombreSucursal.trim())) {
                    sucursales.pop(i); // Eliminar la sucursal de la lista
                    // Eliminar la cobertura asociada a la sucursal
                    estacionesCubiertasPorSucursal.pop(i); // Eliminar las estaciones cubiertas por esta sucursal
                    
                    JOptionPane.showMessageDialog(this, "Sucursal '" + nombreSucursal + "' eliminada con éxito.");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Sucursal no encontrada. Por favor, verifique el nombre.");
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre de sucursal válido.");
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

        // Agregar la cobertura a la lista correspondiente
        Lista<String> coberturaSucursal = new Lista<>();
        for (int i = 0; i < cobertura.len(); i++) {
            int indice = cobertura.get(i);
            coberturaSucursal.append(estaciones.get(indice).getNombre());
        }
        estacionesCubiertasPorSucursal.append(coberturaSucursal);

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
            String nombreEstacion = estaciones.get(i).getNombre();
            boolean cubierta = false;
            for (int j = 0; j < estacionesCubiertasPorSucursal.len(); j++) {
                if (estacionesCubiertasPorSucursal.get(j).exist(nombreEstacion)) {
                    cubierta = true;
                    break; // Si encontramos la estación cubierta, salimos del bucle
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
        verificarsurcusal = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        BotonVerificarCobertura = new javax.swing.JButton();
        botonVerificarSurcusal1 = new javax.swing.JButton();

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
        getContentPane().add(verificarsurcusal, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 350, 30));

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel2.setText("Introduzca el numero de T con el cual ");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 250, 20));

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel3.setText("desea implementar las sucursales");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 220, -1));

        jLabel4.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        jLabel4.setText("Escriba el nombre de la estacion para verificar su existencia");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, -1, -1));

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

        botonVerificarSurcusal1.setBackground(new java.awt.Color(204, 255, 255));
        botonVerificarSurcusal1.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botonVerificarSurcusal1.setText("Verificar existencia de la estacion");
        botonVerificarSurcusal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerificarSurcusal1ActionPerformed(evt);
            }
        });
        getContentPane().add(botonVerificarSurcusal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 290, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dfsActionPerformed
         String nombreSucursal = verificarsurcusal.getText();
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            Estacion estacionSucursal = buscarEstacionPorNombre(nombreSucursal.trim());
            if (estacionSucursal != null) {
                Lista<Integer> cobertura = calcularCobertura(estacionSucursal, t, "DFS");
                grafo.resaltarEstaciones(cobertura, estaciones);
            } else {
                JOptionPane.showMessageDialog(this, "Estación no encontrada. Por favor, verifique el nombre.");
            }
        }
    }//GEN-LAST:event_dfsActionPerformed

    private void bfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bfsActionPerformed
        String nombreSucursal = verificarsurcusal.getText();
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            Estacion estacionSucursal = buscarEstacionPorNombre(nombreSucursal.trim());
            if (estacionSucursal != null) {
                Lista<Integer> cobertura = calcularCobertura(estacionSucursal, t, "BFS");
                grafo.resaltarEstaciones(cobertura, estaciones);
            } else {
                JOptionPane.showMessageDialog(this, "Estación no encontrada. Por favor, verifique el nombre.");
            }
        }
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
       String nombreSucursal = verificarsurcusal.getText();
        quitarSucursal(nombreSucursal); // Llama a la función para quitar la sucursal
    }//GEN-LAST:event_botonEliminarSucursalActionPerformed

    private void BotonVerificarCoberturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonVerificarCoberturaActionPerformed
        if (todasEstacionesCubiertas()) {
            JOptionPane.showMessageDialog(this, "Todas las estaciones están cubiertas por una sucursal.");
        } else {
            JOptionPane.showMessageDialog(this, "No todas las estaciones están cubiertas.");
        }
    }//GEN-LAST:event_BotonVerificarCoberturaActionPerformed

    private void botonVerificarSurcusal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerificarSurcusal1ActionPerformed
        String nombreSucursal = verificarsurcusal.getText();
        if (nombreSucursal != null && !nombreSucursal.trim().isEmpty()) {
            Estacion estacionSucursal = buscarEstacionPorNombre(nombreSucursal.trim());
            if (estacionSucursal != null) {
                JOptionPane.showMessageDialog(this, "Estación encontrada.");
            } else {
                JOptionPane.showMessageDialog(this, "Estación no encontrada. Por favor, verifique el nombre.");
            }
        }
    }//GEN-LAST:event_botonVerificarSurcusal1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazFinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazFinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazFinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazFinal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazFinal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonVerificarCobertura;
    private javax.swing.JButton bfs;
    private javax.swing.JButton botonCargarJson;
    private javax.swing.JButton botonEliminarSucursal;
    private javax.swing.JButton botonEstablecerT;
    private javax.swing.JButton botonVerificarSurcusal1;
    private javax.swing.JButton dfs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField obtenerT;
    private javax.swing.JTextField verificarsurcusal;
    // End of variables declaration//GEN-END:variables
}
