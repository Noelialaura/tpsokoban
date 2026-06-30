package modelo;

import modelo.builder.ConstructorNivel;

public class Portal implements Casilla {

	private Casilla extremoOpuesto;
	private char id;

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
	public boolean esPortal() { return true; }

	@Override
	public Casilla obtenerDestinoPortal() {
		return extremoOpuesto;
	}
	
	public void setId(char id) {
		this.id = id;
	}
	
	public char getId() {
		return id;	
	}
	
	public void setExtremoOpuesto(Casilla extremoOpuesto) {
		this.extremoOpuesto = extremoOpuesto;
	}

	@Override
	public void registrarEnConstructor(ConstructorNivel constructor) {
		constructor.registrarPortal(this);
	}
}
