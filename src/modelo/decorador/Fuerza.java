package modelo.decorador;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;


public class Fuerza extends DecoradorJugador {

    public Fuerza(IEntidad entidad) {
        super(entidad);
    }

    @Override
    public boolean empujarCaja(Caja caja, Movimiento movimiento, Tablero tablero) {
        
        int filaDetras    = movimiento.aplicarAFila(caja.getY());
        int columnaDetras = movimiento.aplicarAColumna(caja.getX());

        Caja segundaCaja = tablero.obtenerCaja(filaDetras, columnaDetras);

        if (segundaCaja != null) {

            boolean segundaSeMovio = segundaCaja.empujar(movimiento, tablero);
            if (!segundaSeMovio) {
                return false; // bloqueado
            }
        }

        
        return jugadorDecorado.empujarCaja(caja, movimiento, tablero);
    }

    @Override
    public String getNombreHabilidad() {
        String base = jugadorDecorado.getNombreHabilidad();
        return "Normal".equals(base) ? "Fuerza" : base + " + Fuerza";
    }
}
