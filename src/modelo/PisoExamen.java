package modelo;

import modelo.entidad.Tablero;
import modelo.observer.EventoJuego;

/**
 * Casilla de piso que contiene un examen ('E' en el .txt).
 * Al ser pisada por el jugador, se marca como recogida (el sprite desaparece)
 * y se dispara el evento SKIN_EXAMEN para cambiar la skin del jugador.
 */
public class PisoExamen implements Casilla {

    private boolean recogido = false;

    public void recoger() {
        this.recogido = true;
    }

    public boolean fueRecogido() {
        return recogido;
    }

    @Override
    public boolean esTransitable() { return true; }

    @Override
    public boolean esResbaladiza() { return false; }

    @Override
    public boolean esMeta() { return false; }

    @Override
    public boolean esPortal() { return false; }

    /** Reutilizamos esPocion() para que pintarDetalle() lo detecte por interfaz. */
    @Override
    public boolean esPocion() { return !recogido; }

    @Override
    public String getTipoPocion() { return recogido ? null : "Examen"; }

    @Override
    public void recoger(Tablero tablero) {
        if (!recogido) {
            recogido = true;
            tablero.notificar(EventoJuego.SKIN_EXAMEN);
        }
    }
}
