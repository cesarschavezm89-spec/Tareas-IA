import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Minimax {

    public int nodosExplorados;

    public static class Movimiento {
        public int pila;
        public int cantidad;
        public int valor;

        public Movimiento(int pila, int cantidad, int valor) {
            this.pila = pila;
            this.cantidad = cantidad;
            this.valor = valor;
        }
    }

    // Nodo que representa un estado del árbol
    public static class Nodo {

        public String descripcion;
        public int[] estado;
        public int valor;
        public List<Nodo> hijos = new ArrayList<>();

        public Nodo(String descripcion, int[] estado, int valor) {
            this.descripcion = descripcion;
            this.estado = estado.clone();
            this.valor = valor;
        }

        @Override
        public String toString() {
            return descripcion
                + " | Estado: " + Arrays.toString(estado)
                + " | Valor: " + (valor > 0 ? "+1" : valor);
        }
    }

    // Resultado completo del análisis
    public static class ResultadoAnalisis {

        public List<Movimiento> movimientos;
        public Nodo arbol;
        public int nodosExplorados;

        public ResultadoAnalisis(
            List<Movimiento> movimientos,
            Nodo arbol,
            int nodosExplorados
        ) {
            this.movimientos = movimientos;
            this.arbol = arbol;
            this.nodosExplorados = nodosExplorados;
        }
    }

    // ALGORITMO MINIMAX
    public int minimax(int[] estado, boolean max) {

        nodosExplorados++;

        if (terminado(estado)) {
            return max ? -1 : 1;
        }

        if (max) {

            int mejor = Integer.MIN_VALUE;

            for (int i = 0; i < estado.length; i++) {

                for (int c = 1; c <= 3 && c <= estado[i]; c++) {

                    estado[i] -= c;

                    int valor = minimax(estado, false);

                    estado[i] += c;

                    mejor = Math.max(mejor, valor);
                }
            }

            return mejor;

        } else {

            int mejor = Integer.MAX_VALUE;

            for (int i = 0; i < estado.length; i++) {

                for (int c = 1; c <= 3 && c <= estado[i]; c++) {

                    estado[i] -= c;

                    int valor = minimax(estado, true);

                    estado[i] += c;

                    mejor = Math.min(mejor, valor);
                }
            }

            return mejor;
        }
    }

    // ANALIZAR MOVIMIENTOS Y CONSTRUIR UNA PARTE DEL ARBOL
    public ResultadoAnalisis analizar(int[] estado) {

        List<Movimiento> movimientos = new ArrayList<>();

        nodosExplorados = 0;

        Nodo raiz = new Nodo(
            "MAX - Computadora",
            estado,
            0
        );

        int mejorValor = Integer.MIN_VALUE;

        // Movimientos posibles de la computadora
        for (int i = 0; i < estado.length; i++) {

            for (int c = 1; c <= 3 && c <= estado[i]; c++) {

                int[] nuevoEstado = estado.clone();

                nuevoEstado[i] -= c;

                // Evaluacion completa del movimiento
                int valor = minimax(nuevoEstado, false);

                movimientos.add(new Movimiento(i, c, valor));

                mejorValor = Math.max(mejorValor, valor);

                Nodo nodoComputadora = new Nodo(
                    "MAX - Retira " + c + " de pila " + (i + 1),
                    nuevoEstado,
                    valor
                );

                raiz.hijos.add(nodoComputadora);

                // Respuestas posibles del jugador (MIN)
                if (!terminado(nuevoEstado)) {

                    for (int j = 0; j < nuevoEstado.length; j++) {

                        for (int cantidad = 1;
                             cantidad <= 3 && cantidad <= nuevoEstado[j];
                             cantidad++) {

                            int[] estadoJugador = nuevoEstado.clone();

                            estadoJugador[j] -= cantidad;

                            // Evaluacion completa de esta respuesta
                            int valorJugador = minimax(estadoJugador, true);

                            Nodo nodoJugador = new Nodo(
                                "MIN - Retira " + cantidad
                                + " de pila " + (j + 1),
                                estadoJugador,
                                valorJugador
                            );

                            nodoComputadora.hijos.add(nodoJugador);
                        }
                    }
                }
            }
        }

        raiz.valor = mejorValor;

        return new ResultadoAnalisis(
            movimientos,
            raiz,
            nodosExplorados
        );
    }

    // COMPROBAR SI TERMINO LA PARTIDA
    public static boolean terminado(int[] estado) {

        for (int n : estado) {
            if (n > 0) return false;
        }

        return true;
    }
}