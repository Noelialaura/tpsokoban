package modelo;

import modelo.decorador.Fuerza;
import modelo.entidad.Tablero;

/**
 * Casilla de piso que contiene una poción de Fuerza ('U' en el .txt).
 * Al ser recogida por el jugador, otorga el decorador Fuerza y desaparece del tablero.
 */
public class PisoPocionFuerza implements Casilla {

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
    public String getTipoPocion() { return recogida ? null : "Fuerza"; }

    @Override
    public void recoger(Tablero tablero) {
        if (!recogida) {
            recogida = true;
            tablero.setHabilidad(new Fuerza(tablero.getHabilidadActiva()));
        }
    }
}
