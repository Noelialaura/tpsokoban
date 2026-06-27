package modelo.strategy;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

public interface ComportamientoCaja {
	boolean empujar(Caja caja, Movimiento movimiento, Tablero tablero);
	ComportamientoCaja copiar();
	boolean trabajaConMeta();
}
