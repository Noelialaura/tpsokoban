package modelo;

public class Movimiento {

    private final int fila;
    private final int columna;

    public Movimiento(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int aplicarAFila(int filaActual) {
        return filaActual + fila;
    }

    public int aplicarAColumna(int columnaActual) {
        return columnaActual + columna;
    }
}
