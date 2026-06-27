package modelo;

import modelo.entidad.Tablero;

public class Cerrojo implements Casilla {
	
	private boolean llaveIngresada;

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

	@Override
	public void activar(Tablero tablero) {
		llaveIngresada = true;
		tablero.abrirMurosCerrojo();
	}

	public boolean llaveIngresada() {
		return llaveIngresada;
	}
	
	public void setLlveIngresada(boolean llaveIngresada) {
		this.llaveIngresada = llaveIngresada;
	}
}
