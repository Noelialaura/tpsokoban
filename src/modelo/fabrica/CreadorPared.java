package modelo.fabrica;

import modelo.*;

public class CreadorPared implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		// TODO Auto-generated method stub
		return new Pared();
	}

}
