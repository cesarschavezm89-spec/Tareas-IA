import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InterfazNim extends JFrame {

    // PALETA DE COLORES
    private final Color FONDO = new Color(18, 18, 24);
    private final Color MORADO = new Color(145, 70, 255);
    private final Color MORADO_CLARO = new Color(180, 130, 255);
    private final Color BLANCO = Color.WHITE;
    private final Color PANEL = new Color(30, 30, 40);
    private final Color BORDE = new Color(90, 50, 140);

    private final JuegoNim juego = new JuegoNim();
    private final Minimax minimax = new Minimax();

    private final JLabel[] etiquetas = new JLabel[3];

    private JComboBox<Integer> seleccionPila;
    private JComboBox<Integer> seleccionCantidad;

    private JTextArea registro;
    private JLabel etiquetaEstado;

    private JButton jugar;
    private JButton reiniciar;

    private JTree arbolVisual;
    private final Icon iconoTurno = new Icon() {
        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics copia = graphics.create();
            try {
                copia.setColor(MORADO);
                copia.fillRect(x, y + 3, 8, 8);
            } finally {
                copia.dispose();
            }
        }

        @Override
        public int getIconWidth() {
            return 8;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }
    };
    private JTabbedPane pestañas;

    public InterfazNim() {

        setTitle("Juego Nim - Algoritmo Minimax");
        setSize(1500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout(15, 15));

        // TITULO
        JLabel titulo = new JLabel(
            "JUEGO NIM - INTELIGENCIA ARTIFICIAL",
            SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(MORADO_CLARO);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));

        add(titulo, BorderLayout.NORTH);

        // TABLERO
        JPanel tablero = new JPanel(new GridLayout(3, 1, 10, 10));
        tablero.setBackground(FONDO);

        tablero.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE),
                "Tablero de juego",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 15),
                MORADO_CLARO
            )
        );

        for (int i = 0; i < 3; i++) {

            etiquetas[i] = new JLabel("", SwingConstants.CENTER);

            etiquetas[i].setFont(
                new Font("Monospaced", Font.BOLD, 23)
            );

            etiquetas[i].setForeground(BLANCO);
            etiquetas[i].setBackground(PANEL);
            etiquetas[i].setOpaque(true);

            tablero.add(etiquetas[i]);
        }

        add(tablero, BorderLayout.CENTER);

        // CONTROLES
        JPanel controles = new JPanel(new GridLayout(4, 2, 8, 8));
        controles.setBackground(PANEL);

        controles.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDE),
                "Tu turno",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 15),
                MORADO_CLARO
            )
        );

        seleccionPila = new JComboBox<>();
        seleccionCantidad = new JComboBox<>();

        estilizarCombo(seleccionPila);
        estilizarCombo(seleccionCantidad);

        jugar = new JButton("Retirar objetos");
        reiniciar = new JButton("Reiniciar partida");

        estilizarBoton(jugar);
        estilizarBoton(reiniciar);

        JLabel textoPila = new JLabel("Seleccionar pila:");
        JLabel textoCantidad = new JLabel("Cantidad de objetos:");

        textoPila.setForeground(BLANCO);
        textoCantidad.setForeground(BLANCO);

        controles.add(textoPila);
        controles.add(seleccionPila);

        controles.add(textoCantidad);
        controles.add(seleccionCantidad);

        controles.add(jugar);
        controles.add(reiniciar);

        etiquetaEstado = new JLabel("Tu turno: juega con X");
        etiquetaEstado.setForeground(BLANCO);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 13));

        controles.add(etiquetaEstado);

        add(controles, BorderLayout.SOUTH);

        // REGISTRO DE MINIMAX
        registro = new JTextArea();
        registro.setEditable(false);
        registro.setFont(new Font("Monospaced", Font.PLAIN, 13));
        registro.setBackground(PANEL);
        registro.setForeground(MORADO_CLARO);
        registro.setCaretColor(BLANCO);
        registro.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollRegistro = new JScrollPane(registro);
        scrollRegistro.getViewport().setBackground(PANEL);

        scrollRegistro.setBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        // ARBOL DE DECISIONES
        arbolVisual = new JTree(
            new DefaultMutableTreeNode("Esperando analisis de Minimax...")
        );

        arbolVisual.setBackground(PANEL);
        arbolVisual.setForeground(BLANCO);
        arbolVisual.setFont(new Font("Monospaced", Font.PLAIN, 13));
        arbolVisual.setRowHeight(28);

        // Eliminar lineas de conexion
        arbolVisual.setShowsRootHandles(false);
        arbolVisual.putClientProperty("JTree.lineStyle", "None");

        // Personalizar apariencia de los nodos
        arbolVisual.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean selected,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus
            ) {

                super.getTreeCellRendererComponent(
                    tree, value, selected, expanded,
                    leaf, row, hasFocus
                );

                // Eliminar iconos de carpetas y archivos
                setIcon(null);
                setOpenIcon(null);
                setClosedIcon(null);
                setLeafIcon(null);

                String textoNodo = String.valueOf(value);
                setText(textoNodo);

                if (textoNodo.startsWith("MAX")
                    || textoNodo.startsWith("MIN")) {
                    setIcon(iconoTurno);
                }

                // Colores
                setBackgroundNonSelectionColor(PANEL);
                setTextNonSelectionColor(BLANCO);
                setBackgroundSelectionColor(MORADO);
                setTextSelectionColor(BLANCO);

                // Espaciado
                setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

                return this;
            }
        });

        JScrollPane scrollArbol = new JScrollPane(arbolVisual);
        scrollArbol.getViewport().setBackground(PANEL);

        scrollArbol.setBorder(
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );

        // PESTAÑAS
        pestañas = new JTabbedPane();

        pestañas.setBackground(PANEL);
        pestañas.setForeground(BLANCO);

        pestañas.addTab("Registro", scrollRegistro);
        pestañas.addTab("Arbol Minimax", scrollArbol);

        pestañas.setPreferredSize(new Dimension(800, 0));

        add(pestañas, BorderLayout.EAST);

        // EVENTOS
        jugar.addActionListener(e -> movimientoJugador());
        reiniciar.addActionListener(e -> reiniciar());
        seleccionPila.addActionListener(e -> actualizarCantidades());

        actualizarTablero();

        setVisible(true);
    }

    // ESTILO DE BOTONES
    private void estilizarBoton(JButton boton) {

        boton.setBackground(MORADO);
        boton.setForeground(BLANCO);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ESTILO DE LISTAS DESPLEGABLES
    private void estilizarCombo(JComboBox<Integer> combo) {

        combo.setBackground(PANEL);
        combo.setForeground(BLANCO);
        combo.setFont(new Font("Arial", Font.BOLD, 13));
    }

    // ACTUALIZAR EL ARBOL VISUAL
    private void actualizarArbol(Minimax.Nodo raiz) {

        DefaultMutableTreeNode raizVisual =
            crearNodoVisual(raiz);

        DefaultTreeModel modelo =
            new DefaultTreeModel(raizVisual);

        arbolVisual.setModel(modelo);

        // Expandir todos los nodos para mostrar el arbol
        for (int i = 0; i < arbolVisual.getRowCount(); i++) {
            arbolVisual.expandRow(i);
        }

        pestañas.setSelectedIndex(1);
    }

    // CONVERTIR LOS NODOS DE MINIMAX EN NODOS DE JTREE
    private DefaultMutableTreeNode crearNodoVisual(
        Minimax.Nodo nodo
    ) {

        DefaultMutableTreeNode nodoVisual =
            new DefaultMutableTreeNode(nodo.toString());

        for (Minimax.Nodo hijo : nodo.hijos) {

            nodoVisual.add(crearNodoVisual(hijo));
        }

        return nodoVisual;
    }

    // MOVIMIENTO DEL JUGADOR
    private void movimientoJugador() {

        if (juego.terminado()) return;

        Integer pilaSeleccionada =
            (Integer) seleccionPila.getSelectedItem();

        Integer cantidadSeleccionada =
            (Integer) seleccionCantidad.getSelectedItem();

        if (pilaSeleccionada == null || cantidadSeleccionada == null) {
            return;
        }

        int pila = pilaSeleccionada - 1;
        int cantidad = cantidadSeleccionada;

        int[] pilasActuales = juego.getPilas();

        if (pila < 0 || pila >= pilasActuales.length ||
            cantidad > pilasActuales[pila] ||
            pilasActuales[pila] == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Movimiento no valido"
            );

            return;
        }

        juego.retirar(pila, cantidad);

        registro.append(
            "\nJUGADOR: Retiro " + cantidad +
            " objetos de la pila " + (pila + 1) + "\n"
        );

        actualizarTablero();

        if (juego.terminado()) {

            finalizar("¡Ganaste! La computadora perdio.");
            return;
        }

        jugar.setEnabled(false);
        reiniciar.setEnabled(false);
        seleccionPila.setEnabled(false);
        seleccionCantidad.setEnabled(false);

        etiquetaEstado.setText("La computadora esta pensando...");

        // EJECUTAR MINIMAX EN SEGUNDO PLANO
        SwingWorker<Minimax.ResultadoAnalisis, Void> trabajador =
            new SwingWorker<Minimax.ResultadoAnalisis, Void>() {

            @Override
            protected Minimax.ResultadoAnalisis doInBackground() {

                return minimax.analizar(juego.getPilas());
            }

            @Override
            protected void done() {

                try {

                    Minimax.ResultadoAnalisis resultado = get();

                    List<Minimax.Movimiento> opciones =
                        resultado.movimientos;

                    Minimax.Movimiento mejor = null;

                    StringBuilder texto = new StringBuilder();

                    texto.append("\n========== MINIMAX ==========\n");

                    texto.append("Evaluacion de movimientos:\n\n");

                    for (Minimax.Movimiento m : opciones) {

                        texto.append("Pila ")
                            .append(m.pila + 1)
                            .append(" | Retirar: ")
                            .append(m.cantidad)
                            .append(" | Valor: ")
                            .append(m.valor)
                            .append("\n");

                        if (mejor == null || m.valor > mejor.valor) {
                            mejor = m;
                        }
                    }

                    if (mejor == null) return;

                    texto.append("\nNodos explorados: ")
                        .append(resultado.nodosExplorados)
                        .append("\n");

                    texto.append("Mejor valor: ")
                        .append(mejor.valor)
                        .append("\n");

                    if (mejor.valor == 1) {
                        texto.append(
                            "Resultado: victoria garantizada.\n"
                        );
                    } else {
                        texto.append(
                            "Resultado: no puede garantizar la victoria.\n"
                        );
                    }

                    texto.append("==============================\n");

                    registro.append(texto.toString());

                    // MOSTRAR EL ARBOL REAL DE DECISIONES
                    actualizarArbol(resultado.arbol);

                    animarMovimientoComputadora(mejor);

                } catch (InterruptedException | ExecutionException ex) {

                    reiniciar.setEnabled(true);

                    JOptionPane.showMessageDialog(
                        InterfazNim.this,
                        "Error al ejecutar Minimax: " + ex.getMessage()
                    );
                }
            }
        };

        trabajador.execute();
    }

    private void animarMovimientoComputadora(Minimax.Movimiento movimiento) {
        int[] retirados = {0};
        Timer animacion = new Timer(450, null);

        animacion.addActionListener(e -> {
            juego.retirar(movimiento.pila, 1);
            retirados[0]++;
            actualizarTablero();

            if (retirados[0] == movimiento.cantidad) {
                animacion.stop();

                registro.append(
                    "COMPUTADORA: Retiro " +
                    movimiento.cantidad +
                    " objetos de la pila " +
                    (movimiento.pila + 1) + "\n"
                );

                if (juego.terminado()) {
                    finalizar("¡La computadora ha ganado!");
                } else {
                    etiquetaEstado.setText("Tu turno: juega con X");
                    jugar.setEnabled(true);
                    seleccionPila.setEnabled(true);
                    seleccionCantidad.setEnabled(true);
                }

                reiniciar.setEnabled(true);
            }
        });

        animacion.start();
    }

    // ACTUALIZAR TABLERO
    private void actualizarTablero() {

        int[] pilas = juego.getPilas();

        for (int i = 0; i < 3; i++) {

            StringBuilder texto = new StringBuilder();

            texto.append("<html><span style='color:white'>");
            texto.append("Pila ").append(i + 1).append(": ");
            texto.append("</span>");

            texto.append("<span style='color:#9146FF'>");

            for (int j = 0; j < pilas[i]; j++) {
                texto.append("● ");
            }

            texto.append("</span>");

            texto.append("<span style='color:white'>");
            texto.append(" (").append(pilas[i]).append(" objetos)");
            texto.append("</span></html>");

            etiquetas[i].setText(texto.toString());
        }

        actualizarPilasDisponibles();
    }

    // MOSTRAR SOLAMENTE LAS PILAS QUE TIENEN OBJETOS
    private void actualizarPilasDisponibles() {

        int[] pilas = juego.getPilas();

        Integer seleccionAnterior =
            (Integer) seleccionPila.getSelectedItem();

        DefaultComboBoxModel<Integer> modelo =
            new DefaultComboBoxModel<>();

        for (int i = 0; i < pilas.length; i++) {

            if (pilas[i] > 0) {
                modelo.addElement(i + 1);
            }
        }

        seleccionPila.setModel(modelo);

        if (seleccionAnterior != null) {

            for (int i = 0; i < modelo.getSize(); i++) {

                if (modelo.getElementAt(i).equals(seleccionAnterior)) {
                    seleccionPila.setSelectedItem(seleccionAnterior);
                    break;
                }
            }
        }

        actualizarCantidades();
    }

    // ACTUALIZAR CANTIDADES DISPONIBLES
    private void actualizarCantidades() {

        if (seleccionPila == null || seleccionCantidad == null) {
            return;
        }

        Integer seleccion =
            (Integer) seleccionPila.getSelectedItem();

        DefaultComboBoxModel<Integer> modelo =
            new DefaultComboBoxModel<>();

        if (seleccion != null) {

            int[] pilas = juego.getPilas();

            int pila = seleccion - 1;

            for (int i = 1; i <= 3 && i <= pilas[pila]; i++) {
                modelo.addElement(i);
            }
        }

        seleccionCantidad.setModel(modelo);
    }

    // REINICIAR PARTIDA
    private void reiniciar() {

        juego.reiniciar();

        registro.setText("");

        etiquetaEstado.setText("Tu turno: juega con X");

        seleccionPila.setEnabled(true);
        seleccionCantidad.setEnabled(true);

        actualizarTablero();

        arbolVisual.setModel(
            new DefaultTreeModel(
                new DefaultMutableTreeNode(
                    "Esperando analisis de Minimax..."
                )
            )
        );

        jugar.setEnabled(true);
        reiniciar.setEnabled(true);
    }

    // FINALIZAR PARTIDA
    private void finalizar(String mensaje) {

        etiquetaEstado.setText(mensaje);

        jugar.setEnabled(false);
        seleccionPila.setEnabled(false);
        seleccionCantidad.setEnabled(false);

        JOptionPane.showMessageDialog(this, mensaje);
    }
}