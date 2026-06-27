package modelo;

import modelo.entidad.Tablero;

public class Hielo implements Casilla{

	@Override
	public boolean esTransitable() { return true; }

	@Override
	public boolean esResbaladiza() { return true; }

	@Override
	public boolean esMeta() { return false; }

	@Override
	public boolean esPortal() { return false; }
}
