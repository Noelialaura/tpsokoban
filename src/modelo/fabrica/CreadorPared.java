package modelo.fabrica;

import modelo.*;

public class CreadorPared implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		return new Pared();
	}

}
