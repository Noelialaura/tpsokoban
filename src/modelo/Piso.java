package modelo;

public class Piso implements Casilla{

	@Override
	public boolean esTransitable() {
		return true;
	}

	@Override
	public boolean esResbaladiza() {
		return false;
	}

	@Override
	public boolean esMeta() {
		return false;
	}

	@Override
	public boolean esPortal() { return false; }
}
