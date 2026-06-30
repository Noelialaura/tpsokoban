package modelo;

import modelo.entidad.Tablero;
import modelo.builder.NivelBuilder;
import modelo.observer.EventoJuego;
import modelo.observer.PublicadorJuego;
import modelo.observer.SuscriptorJuego;

import java.util.ArrayList;
import java.util.List;

public class Cerrojo implements Casilla, PublicadorJuego {
	
	private boolean llaveIngresada;
	private List<SuscriptorJuego> suscriptores = new ArrayList<>();

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
	public boolean esCerrojo() { return true; }

	@Override
	public void activar(Tablero tablero) {
		llaveIngresada = true;
		notificar(EventoJuego.CERROJO_ACTIVADO);
	}

	@Override
	public void desactivar(Tablero tablero) {
		llaveIngresada = false;
		notificar(EventoJuego.CERROJO_DESACTIVADO);
	}

	@Override
	public void suscribir(SuscriptorJuego suscriptor) {
		suscriptores.add(suscriptor);
	}

	@Override
	public void desuscribir(SuscriptorJuego suscriptor) {
		suscriptores.remove(suscriptor);
	}

	@Override
	public void notificar(EventoJuego evento) {
		for (SuscriptorJuego suscriptor : suscriptores) {
			suscriptor.actualizar(evento);
		}
	}

	public boolean llaveIngresada() {
		return llaveIngresada;
	}

	@Override
	public void registrarEnBuilder(NivelBuilder builder) {
		builder.registrarCerrojo(this);
	}
}
