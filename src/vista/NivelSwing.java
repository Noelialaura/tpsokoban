package vista;

import controlador.Controlador;
import modelo.Casilla;

import java.util.Arrays;
import java.util.List;

public class NivelSwing {
    private final Controlador controlador = Controlador.getInstance();
    private Casilla[][] tableroManual;
    private boolean[][] cajasManuales;
    private int filaJugadorManual;
    private int columnaJugadorManual;

    /** Constructor original (hardcodeado / demo). */
    public NivelSwing(
            Casilla[][] tablero,
            boolean[][] cajasIniciales,
            int filaJugadorInicial,
            int columnaJugadorInicial) {
        this.tableroManual = tablero;
        this.cajasManuales = cajasIniciales;
        this.filaJugadorManual = filaJugadorInicial;
        this.columnaJugadorManual = columnaJugadorInicial;
        controlador.cargarTablero(tablero, cajasIniciales, filaJugadorInicial, columnaJugadorInicial);
    }

    public NivelSwing(String ruta) {
        controlador.configurarNivel(ruta);
        recargar();
    }

    public NivelSwing(List<String> rutas) {
        controlador.configurarNiveles(rutas);
        recargar();
    }

    public NivelSwing(String... rutas) {
        this(Arrays.asList(rutas));
    }

    public void recargar() {
        if (tableroManual != null) {
            controlador.cargarTablero(tableroManual, cajasManuales, filaJugadorManual, columnaJugadorManual);
        } else {
            controlador.reiniciarNivelActual();
        }
    }

    public boolean avanzarNivel() {
        return controlador.avanzarAlSiguienteNivel();
    }

}
