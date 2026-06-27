package modelo.strategy;

import modelo.Movimiento;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

public class ComportamientoFragil implements ComportamientoCaja{
	private final int resistenciaInicial;
	private int resistencia;

	public ComportamientoFragil(int resistencia) {
		this.resistenciaInicial = resistencia;
		this.resistencia = resistencia;
	}

	@Override
	public boolean empujar(Caja caja, Movimiento movimiento, Tablero tablero) {
		boolean seMovio = tablero.moverCaja(caja, movimiento);

		if (seMovio) {
			resistencia--;

			if (resistencia <= 0) {
				tablero.romperCaja(caja);
			}
		}

		return seMovio;
	}

	@Override
	public ComportamientoCaja copiar() {
		return new ComportamientoFragil(resistenciaInicial);
	}

	@Override
	public boolean trabajaConMeta() {
		return true;
	}

}
