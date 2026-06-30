package modelo.fabrica;

import modelo.Casilla;
import modelo.ParedCerrojo;

public class CreadorParedCerrojo implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		return new ParedCerrojo();
	}

}
