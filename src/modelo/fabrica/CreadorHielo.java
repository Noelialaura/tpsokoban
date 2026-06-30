package modelo.fabrica;

import modelo.*;

public class CreadorHielo implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		return new Hielo();
	}

}
