package modelo.fabrica;

import modelo.PisoExamen;

/**
 * Fábrica para crear casillas de tipo Examen ('E').
 */
public class CreadorExamen implements CreadorCasilla {

    @Override
    public modelo.Casilla crearCasilla() {
        return new PisoExamen();
    }
}
