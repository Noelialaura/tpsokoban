package modelo.strategy;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

public class ComportamientoNormal implements ComportamientoCaja{

	@Override
	public boolean empujar(Caja caja, Movimiento movimiento, Tablero tablero) {
		return tablero.moverCaja(caja, movimiento);
	}

	@Override
	public ComportamientoCaja copiar() {
		return new ComportamientoNormal();
	}

	@Override
	public boolean trabajaConMeta() {
		return true;
	}

}
