package modelo.decorador;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;



public interface IEntidad {
    void desplazarse(Movimiento movimiento, Tablero tablero);
    boolean empujarCaja(Caja caja, Movimiento movimiento, Tablero tablero);
    default String getNombreHabilidad() { return "Normal"; }
}
