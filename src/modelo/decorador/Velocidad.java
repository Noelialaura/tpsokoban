package modelo.decorador;

import modelo.Movimiento;
import modelo.entidad.Jugador;
import modelo.entidad.Tablero;


public class Velocidad extends DecoradorJugador {

    public Velocidad(IEntidad entidad) {
        super(entidad);
    }

    @Override
    public void desplazarse(Movimiento movimiento, Tablero tablero) {
        
        jugadorDecorado.desplazarse(movimiento, tablero);

        
        Jugador jugador = Jugador.getInstance();
        int segundaFila    = movimiento.aplicarAFila(jugador.getY());
        int segundaColumna = movimiento.aplicarAColumna(jugador.getX());

        if (tablero.puedeOcuparse(segundaFila, segundaColumna)) {
            jugador.mover(segundaColumna, segundaFila);
        }
        
    }

    @Override
    public String getNombreHabilidad() {
        String base = jugadorDecorado.getNombreHabilidad();
        return "Normal".equals(base) ? "Velocidad" : base + " + Velocidad";
    }
}
