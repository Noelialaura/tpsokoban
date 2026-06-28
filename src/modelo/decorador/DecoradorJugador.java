package modelo.decorador;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;


public abstract class DecoradorJugador implements IEntidad {

    protected final IEntidad jugadorDecorado;

    public DecoradorJugador(IEntidad entidad) {
        this.jugadorDecorado = entidad;
    }

    @Override
    public void desplazarse(Movimiento movimiento, Tablero tablero) {
        jugadorDecorado.desplazarse(movimiento, tablero);
    }

    @Override
    public boolean empujarCaja(Caja caja, Movimiento movimiento, Tablero tablero) {
        return jugadorDecorado.empujarCaja(caja, movimiento, tablero);
    }
}
