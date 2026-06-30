package modelo.builder;

import modelo.Casilla;
import modelo.ResultadoCarga;
import modelo.entidad.Caja;

public interface BuilderNivel {
    void crearGrilla(int filas, int columnas);
    void agregarCasilla(int fila, int columna, Casilla casilla);
    void agregarCaja(Caja caja);
    void ubicarJugador(int x, int y);
    ResultadoCarga construir();
}
