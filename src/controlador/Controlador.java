package controlador;

import java.util.List;

import modelo.Casilla;
import modelo.Movimiento;
import modelo.ResultadoCarga;
import modelo.cargador.CargadorNivel;
import modelo.cargador.CargadorTxt;
import modelo.entidad.Caja;
import modelo.entidad.Jugador;
import modelo.entidad.Tablero;
import vista.NivelSwing;
import vista.PanelTablero;

public class Controlador{
    private static Controlador instance;
    
    private Controlador(){
        this.instance = this;
    }

    public static Controlador getInstance(){
        if (instance==null){
            new Controlador();
        }
        return instance;
    }
    
    
    public Tablero crearTablero(Casilla[][]tablero ,List<Caja> cajas, int columnaJugadorInicial, int filaJugadorInicial){
        return new Tablero(
                tablero,
                cajas,
                Jugador.getInstance(columnaJugadorInicial, filaJugadorInicial));
    }
    public void cargarNivel(String ruta, NivelSwing nivel){
        CargadorNivel cargador = new CargadorTxt();
        cargarResultado(cargador.cargar(ruta), nivel);
    }
    public void cargarResultado(ResultadoCarga resultado, NivelSwing nivel) {
        Casilla[][] t    = resultado.getGrilla();
        int filas        = resultado.getFilas();
        int columnas     = resultado.getColumnas();
        boolean[][] cajas = new boolean[filas][columnas];

        for (Caja caja : resultado.getCajas()) {
            cajas[caja.getY()][caja.getX()] = true;
        }
        validarNivel(t, cajas, resultado.getJugadorY(), resultado.getJugadorX());
        nivel.setTablero(t);
        nivel.setCajasIniciales(nivel.copiarListaCajas(resultado.getCajas()));
        nivel.setFilaJugadorInicial(resultado.getJugadorY());
        nivel.setColumnaJugadorInicial(resultado.getJugadorX());
    }
    public void validarNivel(
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



    /* 
    public boolean procesarMovimientoModelo(int cambioFila, int cambioColumna) {
        Movimiento movimiento = new Movimiento(cambioFila, cambioColumna);
        return modeloTablero.moverJugador(movimiento);
    }

    public void actualizarPosicionJugador() {
        filaJugador = modeloTablero.getJugador().getY();
        columnaJugador = modeloTablero.getJugador().getX();
    }

    public void actualizarEstadoGanador(PanelTablero tablero, Tablero modeloTablero) {
        tablero.ganado = modeloTablero.estaGanado();
    }*/

}
