package modelo;

public class Pared implements Casilla{

	@Override
	public boolean esTransitable() {
		return false;
	}

	@Override
	public boolean esResbaladiza() {
		return false;
	}

	@Override
	public boolean esMeta() { return false; }

	@Override
	public boolean esPortal() { return false; }
}
