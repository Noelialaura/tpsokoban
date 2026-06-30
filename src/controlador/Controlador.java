package controlador;

import java.util.List;
import java.util.ArrayList;

import modelo.Casilla;
import modelo.Movimiento;
import modelo.cargador.CargadorNivel;
import modelo.cargador.CargadorTxt;
import modelo.decorador.IEntidad;
import modelo.entidad.Caja;
import modelo.entidad.Jugador;
import modelo.entidad.Tablero;
import modelo.memento.Memento;
import modelo.nivel.GestorNiveles;
import modelo.observer.SuscriptorJuego;
import modelo.strategy.ComportamientoCaja;
import modelo.strategy.ComportamientoNormal;

public class Controlador {
    private static Controlador instance;
    private GestorNiveles gestorNiveles;
    private Tablero tableroActual;
    
    private Controlador() {
        instance = this;
    }

    public static Controlador getInstance() {
        if (instance == null) {
            new Controlador();
        }
        return instance;
    }
    
    private Tablero crearTablero(
            Casilla[][] tablero,
            List<Caja> cajas,
            int columnaJugadorInicial,
            int filaJugadorInicial) {
        return new Tablero(
                tablero,
                copiarListaCajas(cajas),
                new Jugador(columnaJugadorInicial, filaJugadorInicial));
    }

    public void cargarTablero(Casilla[][] tablero, boolean[][] cajas, int filaJugador, int columnaJugador) {
        validarNivel(tablero, cajas, filaJugador, columnaJugador);
        tableroActual = crearTablero(tablero, crearListaCajas(cajas), columnaJugador, filaJugador);
    }

    public void reiniciarNivelActual() {
        tableroActual = cargarNivelActual();
    }

    public boolean avanzarAlSiguienteNivel() {
        if (gestorNiveles == null || !gestorNiveles.avanzarNivel()) {
            return false;
        }
        tableroActual = cargarNivelActual();
        return true;
    }

    public int getFilasNivelActual() {
        return tableroActual == null ? 0 : tableroActual.getFilas();
    }

    public int getColumnasNivelActual() {
        return tableroActual == null ? 0 : tableroActual.getColumnas();
    }

    private Tablero cargarNivel(String ruta) {
        CargadorNivel cargador = new CargadorTxt();
        return cargador.cargar(ruta);
    }

    public void configurarNiveles(List<String> rutas) {
        gestorNiveles = new GestorNiveles(rutas);
    }

    public void configurarNivel(String ruta) {
        configurarNiveles(List.of(ruta));
    }

    private Tablero cargarNivelActual() {
        if (gestorNiveles == null) {
            return null;
        }
        return cargarNivel(gestorNiveles.getRutaActual());
    }

    public int getNumeroNivelActual() {
        return gestorNiveles == null ? 1 : gestorNiveles.getNumeroNivelActual();
    }

    public int getTotalNiveles() {
        return gestorNiveles == null ? 1 : gestorNiveles.getTotalNiveles();
    }

    private List<Caja> crearListaCajas(boolean[][] origen) {
        List<Caja> cajas = new ArrayList<>();
        for (int fila = 0; fila < origen.length; fila++) {
            for (int columna = 0; columna < origen[fila].length; columna++) {
                if (origen[fila][columna]) {
                    cajas.add(new Caja(columna, fila, new ComportamientoNormal()));
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

    public boolean moverJugador(Movimiento movimiento) {
        return tableroActual.moverJugador(movimiento);
    }

    public Casilla obtenerCasilla(int fila, int columna) {
        return tableroActual.obtenerCasilla(fila, columna);
    }

    public List<Caja> obtenerCajas() {
        return tableroActual.getCajas();
    }

    public int obtenerFilaJugador() {
        return tableroActual.getJugador().getY();
    }

    public int obtenerColumnaJugador() {
        return tableroActual.getJugador().getX();
    }

    public boolean cajaTrabajaConMeta(Caja caja) {
        return caja.trabajaConMeta();
    }

    public boolean cajaEstaRota(Caja caja) {
        return caja.estaRota();
    }

    public boolean cajaEstaEnMeta(Caja caja) {
        return obtenerCasilla(caja.getY(), caja.getX()).esMeta();
    }

    public ComportamientoCaja obtenerComportamientoCaja(Caja caja) {
        return caja.getComportamiento();
    }

    public void suscribir(SuscriptorJuego suscriptor) {
        tableroActual.suscribir(suscriptor);
    }

    public boolean estaGanado() {
        return tableroActual.estaGanado();
    }

    public Memento guardar() {
        return tableroActual.guardar();
    }

    public void restaurar(Memento memento) {
        tableroActual.restaurar(memento);
    }

    public void cambiarHabilidad(IEntidad habilidad) {
        tableroActual.setHabilidad(habilidad);
    }

    public void restaurarHabilidadBase() {
        tableroActual.setHabilidad(tableroActual.getJugador());
    }

    public String getNombreHabilidadActiva() {
        return tableroActual.getNombreHabilidadActiva();
    }

}
