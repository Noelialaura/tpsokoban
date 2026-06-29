package modelo.entidad;

import modelo.decorador.*;
import modelo.entidad.*;
import modelo.memento.Memento;
import modelo.Casilla;
import modelo.Movimiento;
import java.util.List;

public class Tablero {
    private Casilla[][] casillas;
    private List<Caja> cajas;
    private Jugador jugador;
    private IEntidad habilidadActiva;

    public Tablero(Casilla[][] casillas, List<Caja> cajas, Jugador jugador) {
        this.casillas = casillas;
        this.cajas = cajas;
        this.jugador = jugador;
        this.habilidadActiva = jugador; 
    }

    public void setHabilidad(IEntidad habilidad) {
        this.habilidadActiva = habilidad;
    }

    public IEntidad getHabilidadActiva() {
        return habilidadActiva;
    }

    public int getFilas()    { return casillas.length; }
    public int getColumnas() { return casillas[0].length; }
    public List<Caja> getCajas()  { return cajas; }
    public Jugador getJugador()   { return jugador; }

    public boolean estaDentro(int fila, int columna) {
        return fila >= 0 && fila < getFilas() && columna >= 0 && columna < getColumnas();
    }

    public Casilla obtenerCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    public boolean esTransitable(int fila, int columna) {
        return estaDentro(fila, columna) && obtenerCasilla(fila, columna).esTransitable();
    }

    public boolean puedeOcuparse(int fila, int columna) {
        return esTransitable(fila, columna) && !hayCaja(fila, columna);
    }

    public boolean hayCaja(int fila, int columna) {
        for (Caja caja : cajas) {
            if (caja.estaEn(fila, columna)) return true;
        }
        return false;
    }

    public Caja obtenerCaja(int fila, int columna) {
        for (Caja caja : cajas) {
            if (caja.estaEn(fila, columna)) return caja;
        }
        return null;
    }

    public void romperCaja(Caja caja) {
        obtenerCasilla(caja.getY(), caja.getX()).desocupar();
        caja.romper();
    }

    private void moverCajaA(Caja caja, int nuevaFila, int nuevaColumna) {
        Casilla casillaAnterior = obtenerCasilla(caja.getY(), caja.getX());
        if (!caja.trabajaConMeta()) {
            casillaAnterior.desactivar(this);
        }
        casillaAnterior.desocupar();
        caja.moverA(nuevaFila, nuevaColumna);
        obtenerCasilla(nuevaFila, nuevaColumna).ocupar();
    }


    public boolean moverJugador(Movimiento movimiento) {
        int nuevaFila    = movimiento.aplicarAFila(jugador.getY());
        int nuevaColumna = movimiento.aplicarAColumna(jugador.getX());

        if (!estaDentro(nuevaFila, nuevaColumna)) return false;

        Caja caja = obtenerCaja(nuevaFila, nuevaColumna);

        if (caja != null) {
            
            boolean cajaSeMovio = habilidadActiva.empujarCaja(caja, movimiento, this);
            if (!cajaSeMovio) return false;
        } else if (!puedeOcuparse(nuevaFila, nuevaColumna)) {
            return false;
        }

        
        if (caja != null) {
            
            jugador.mover(nuevaColumna, nuevaFila);
        } else {
            
            habilidadActiva.desplazarse(movimiento, this);
        }

        aplicarPortalAJugador();
        return true;
    }

    private void aplicarPortalAJugador() {
        Casilla casilla = obtenerCasilla(jugador.getY(), jugador.getX());
        Casilla destino = casilla.obtenerDestinoPortal();
        if (destino == null) return;

        for (int fila = 0; fila < getFilas(); fila++) {
            for (int columna = 0; columna < getColumnas(); columna++) {
                if (casillas[fila][columna] == destino && puedeOcuparse(fila, columna)) {
                    jugador.mover(columna, fila);
                    return;
                }
            }
        }
    }

    public boolean moverCaja(Caja caja, Movimiento movimiento) {
        int filaDestino    = movimiento.aplicarAFila(caja.getY());
        int columnaDestino = movimiento.aplicarAColumna(caja.getX());

        if (!puedeOcuparse(filaDestino, columnaDestino)) return false;

        moverCajaA(caja, filaDestino, columnaDestino);

        while (obtenerCasilla(caja.getY(), caja.getX()).esResbaladiza()) {
            int siguienteFila    = movimiento.aplicarAFila(caja.getY());
            int siguienteColumna = movimiento.aplicarAColumna(caja.getX());
            if (!puedeOcuparse(siguienteFila, siguienteColumna)) break;
            moverCajaA(caja, siguienteFila, siguienteColumna);
        }

        return true;
    }

    public void activarCasillaBajoCaja(Caja caja) {
        Casilla casilla = obtenerCasilla(caja.getY(), caja.getX());
        casilla.activar(this);
    }

    public boolean estaGanado() {
        for (Caja caja : cajas) {
            Casilla casilla = obtenerCasilla(caja.getY(), caja.getX());
            if (caja.trabajaConMeta() && (caja.estaRota() || !casilla.esMeta())) {
                return false;
            }
        }
        return true;
    }
    
    public Memento guardar() {
    	return new Memento(this.jugador, this.cajas);
    }
    
    public void restaurar(modelo.memento.Memento memento) {
        if (memento == null) return;

        this.jugador.mover(memento.getPosX(), memento.getPosY());

        List<modelo.memento.Memento.EstadoCaja> estados = memento.getEstadoCajas();
        for (int i = 0; i < cajas.size(); i++) {
            Caja cajaReal = cajas.get(i);
            modelo.memento.Memento.EstadoCaja estado = estados.get(i);
            
            int cFila = estado.y;
            int cColumna = estado.x;
            
            if (!estaDentro(cFila, cColumna) && estaDentro(cColumna, cFila)) {
                int temporal = cFila;
                cFila = cColumna;
                cColumna = temporal;
            }
            
            cajaReal.moverA(cFila, cColumna);
            
            if (estado.rota) {
                cajaReal.romper();
            }
        }

        for (int fila = 0; fila < getFilas(); fila++) {
            for (int columna = 0; columna < getColumnas(); columna++) {
                casillas[fila][columna].desocupar();
            }
        }
        
        for (Caja caja : cajas) {
            if (!caja.estaRota() && estaDentro(caja.getY(), caja.getX())) {
                obtenerCasilla(caja.getY(), caja.getX()).ocupar();
            }
        }
    }
}
