package modelo.fabrica;

import modelo.*;

public class CreadorPiso implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		return new Piso();
	}

}
