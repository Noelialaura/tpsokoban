package modelo.builder;

import modelo.Casilla;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

public interface BuilderNivel {
    void crearGrilla(int filas, int columnas);
    void agregarCasilla(int fila, int columna, Casilla casilla);
    void agregarCaja(Caja caja);
    void ubicarJugador(int x, int y);
    Tablero construir();
}
