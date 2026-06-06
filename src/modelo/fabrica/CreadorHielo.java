package modelo.fabrica;

import modelo.*;

public class CreadorHielo implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		// TODO Auto-generated method stub
		return new Hielo();
	}

}
