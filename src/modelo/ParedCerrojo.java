package modelo;

public class ParedCerrojo implements Casilla {
	
	private boolean transitable;

	public ParedCerrojo() {
		transitable = false;
	}
	
	@Override
	public boolean esTransitable() {
		return transitable;
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

	@Override
	public void abrir() {
		transitable = true;
	}
}
