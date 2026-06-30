package modelo.fabrica;

import modelo.*;

public class CreadorMeta implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		return new Meta();
	}

}
