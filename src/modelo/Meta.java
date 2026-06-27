package modelo;

import modelo.entidad.Tablero;

public class Meta implements Casilla{
	
	private boolean ocupada;

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
		return true;
	}

	@Override
	public boolean esPortal() { return false; }

	@Override
	public void ocupar() {
		ocupada = true;
	}

	@Override
	public void desocupar() {
		ocupada = false;
	}
	
	public boolean estaOcupada() {
		return ocupada;
	}

}
