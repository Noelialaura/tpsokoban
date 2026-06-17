package modelo;
import modelo.fabrica.CreadorCasilla;
public class CreadorCerrojo implements CreadorCasilla {

	@Override
	public Casilla crearCasilla() {
		return new Cerrojo();
	}

}

