package modelo.strategy;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

public class ComportamientoLlave implements ComportamientoCaja{

	@Override
	public boolean empujar(Caja caja, Movimiento movimiento, Tablero tablero) {
		boolean seMovio = tablero.moverCaja(caja, movimiento);

		if (seMovio) {
			tablero.activarCasillaBajoCaja(caja);
		}

		return seMovio;
	}

	@Override
	public ComportamientoCaja copiar() {
		return new ComportamientoLlave();
	}

	@Override
	public boolean trabajaConMeta() {
		return false;
	}

}
