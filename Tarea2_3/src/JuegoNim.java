public class JuegoNim {

    private int[] pilas = {3, 4, 5};

    public int[] getPilas() {
        return pilas.clone();
    }

    public void retirar(int pila, int cantidad) {

        if (pila < 0 || pila >= pilas.length ||
            cantidad < 1 || cantidad > 3 ||
            cantidad > pilas[pila]) {

            throw new IllegalArgumentException("Movimiento invalido");
        }

        pilas[pila] -= cantidad;
    }

    public boolean terminado() {
        return Minimax.terminado(pilas);
    }

    public void reiniciar() {
        pilas = new int[]{3, 4, 5};
    }
}