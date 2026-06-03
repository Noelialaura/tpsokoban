package modelo.fabrica;

import modelo.*;

public class CreadorMeta implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		// TODO Auto-generated method stub
		return new Meta();
	}

}
