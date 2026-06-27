package vista;

import modelo.Casilla;
import modelo.ResultadoCarga;
import modelo.cargador.CargadorNivel;
import modelo.cargador.CargadorTxt;
import modelo.entidad.Caja;
import modelo.entidad.Jugador;
import modelo.entidad.Tablero;

import java.util.ArrayList;
import java.util.List;

public class NivelSwing {
    private String ruta;
    private Casilla[][] tablero;
    private List<Caja> cajasIniciales;
    private int filaJugadorInicial;
    private int columnaJugadorInicial;

    /** Constructor original (hardcodeado / demo). */
    public NivelSwing(
            Casilla[][] tablero,
            boolean[][] cajasIniciales,
            int filaJugadorInicial,
            int columnaJugadorInicial) {
        this.ruta = null;
        validarNivel(tablero, cajasIniciales, filaJugadorInicial, columnaJugadorInicial);
        this.tablero = tablero;
        this.cajasIniciales = crearListaCajas(cajasIniciales);
        this.filaJugadorInicial = filaJugadorInicial;
        this.columnaJugadorInicial = columnaJugadorInicial;
    }

    /**
     * Constructor que acepta el resultado del CargadorNivel.
     * Convierte la List<Caja> del DTO en el boolean[][] que usa internamente NivelSwing.
     */
    public NivelSwing(ResultadoCarga resultado) {
        this.ruta = null;
        cargarResultado(resultado);
    }

    public NivelSwing(String ruta) {
        this.ruta = ruta;
        recargar();
    }

    public void recargar() {
        if (ruta == null) {
            return;
        }

        CargadorNivel cargador = new CargadorTxt();
        cargarResultado(cargador.cargar(ruta));
    }

    private void cargarResultado(ResultadoCarga resultado) {
        Casilla[][] t    = resultado.getGrilla();
        int filas        = resultado.getFilas();
        int columnas     = resultado.getColumnas();
        boolean[][] cajas = new boolean[filas][columnas];

        for (Caja caja : resultado.getCajas()) {
            cajas[caja.getY()][caja.getX()] = true;
        }

        validarNivel(t, cajas, resultado.getJugadorY(), resultado.getJugadorX());
        this.tablero = t;
        this.cajasIniciales = copiarListaCajas(resultado.getCajas());
        this.filaJugadorInicial = resultado.getJugadorY();
        this.columnaJugadorInicial = resultado.getJugadorX();
    }

    public Casilla[][] getTablero() { return tablero; }

    public Tablero crearTablero() {
        return new Tablero(
                tablero,
                copiarListaCajas(cajasIniciales),
                new Jugador(columnaJugadorInicial, filaJugadorInicial));
    }

    private void validarNivel(
            Casilla[][] tablero,
            boolean[][] cajasIniciales,
            int filaJugadorInicial,
            int columnaJugadorInicial) {
        if (tablero == null || tablero.length == 0 || tablero[0].length == 0) {
            throw new IllegalArgumentException("El tablero no puede estar vacio.");
        }
        if (cajasIniciales == null || cajasIniciales.length != tablero.length) {
            throw new IllegalArgumentException("Las cajas deben coincidir con el tamanio del tablero.");
        }
        int columnas = tablero[0].length;
        for (int fila = 0; fila < tablero.length; fila++) {
            if (tablero[fila] == null || tablero[fila].length != columnas) {
                throw new IllegalArgumentException("Todas las filas del tablero deben tener el mismo largo.");
            }
            if (cajasIniciales[fila] == null || cajasIniciales[fila].length != columnas) {
                throw new IllegalArgumentException("Todas las filas de cajas deben tener el mismo largo.");
            }
            for (int columna = 0; columna < columnas; columna++) {
                if (tablero[fila][columna] == null) {
                    throw new IllegalArgumentException("El tablero no puede contener casillas nulas.");
                }
                if (cajasIniciales[fila][columna] && !tablero[fila][columna].esTransitable()) {
                    throw new IllegalArgumentException("Una caja no puede empezar sobre una casilla no transitable.");
                }
            }
        }
        if (!estaEnTablero(tablero, filaJugadorInicial, columnaJugadorInicial)) {
            throw new IllegalArgumentException("La posicion inicial del jugador no esta dentro del tablero.");
        }
        if (!tablero[filaJugadorInicial][columnaJugadorInicial].esTransitable()) {
            throw new IllegalArgumentException("El jugador no puede empezar sobre una casilla no transitable.");
        }
        if (cajasIniciales[filaJugadorInicial][columnaJugadorInicial]) {
            throw new IllegalArgumentException("El jugador no puede empezar sobre una caja.");
        }
    }

    private boolean estaEnTablero(Casilla[][] tablero, int fila, int columna) {
        return fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero[0].length;
    }

    private List<Caja> crearListaCajas(boolean[][] origen) {
        List<Caja> cajas = new ArrayList<>();
        for (int fila = 0; fila < origen.length; fila++) {
            for (int columna = 0; columna < origen[fila].length; columna++) {
                if (origen[fila][columna]) {
                    cajas.add(new Caja(columna, fila, new modelo.strategy.ComportamientoNormal()));
                }
            }
        }
        return cajas;
    }

    private List<Caja> copiarListaCajas(List<Caja> origen) {
        List<Caja> copia = new ArrayList<>();
        for (Caja caja : origen) {
            copia.add(caja.copiar());
        }
        return copia;
    }
}
