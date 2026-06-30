package modelo;

import modelo.builder.ConstructorNivel;
import modelo.observer.EventoJuego;
import modelo.observer.SuscriptorJuego;

public class ParedCerrojo implements Casilla, SuscriptorJuego {
	
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

	@Override
	public void cerrar() {
		transitable = false;
	}

	@Override
	public void actualizar(EventoJuego evento) {
		if (evento == EventoJuego.CERROJO_ACTIVADO) {
			abrir();
		}
		if (evento == EventoJuego.CERROJO_DESACTIVADO) {
			cerrar();
		}
	}

	@Override
	public void registrarEnConstructor(ConstructorNivel constructor) {
		constructor.registrarParedCerrojo(this);
	}
}
