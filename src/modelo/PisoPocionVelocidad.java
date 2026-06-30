package modelo;

import modelo.decorador.Velocidad;
import modelo.entidad.Tablero;

/**
 * Casilla de piso que contiene una poción de Velocidad ('Y' en el .txt).
 * Al ser recogida por el jugador, otorga el decorador Velocidad y desaparece del tablero.
 */
public class PisoPocionVelocidad implements Casilla {

    private boolean recogida = false;

    public void recoger() { this.recogida = true; }

    @Override
    public boolean esTransitable() { return true; }

    @Override
    public boolean esResbaladiza() { return false; }

    @Override
    public boolean esMeta() { return false; }

    @Override
    public boolean esPortal() { return false; }

    @Override
    public boolean esPocion() { return !recogida; }

    @Override
    public boolean esPocionVelocidad() { return !recogida; }

    @Override
    public String getTipoPocion() { return recogida ? null : "Velocidad"; }

    @Override
    public void recoger(Tablero tablero) {
        if (!recogida) {
            recogida = true;
            tablero.setHabilidad(new Velocidad(tablero.getHabilidadActiva()));
        }
    }
}
