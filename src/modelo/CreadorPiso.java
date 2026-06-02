package modelo;

public class CreadorPiso implements CreadorCasilla{

	@Override
	public Casilla crearCasilla() {
		// TODO Auto-generated method stub
		return new Piso();
	}

}
