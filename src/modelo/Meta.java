package modelo;

public class Meta implements Casilla{
	
	private boolean ocupada;

	@Override
	public boolean esTransitable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean estaOcupada() {
		return ocupada;
	}

}
