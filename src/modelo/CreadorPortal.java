package modelo;

public class CreadorPortal implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		// TODO Auto-generated method stub
		return new Portal();
	}

}
