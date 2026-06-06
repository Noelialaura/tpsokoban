package modelo.fabrica;

import modelo.*;

public class CreadorPortal implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		Portal portal = new Portal();
		portal.setId('A');
		return portal;
	}

}
