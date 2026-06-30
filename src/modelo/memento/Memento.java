package modelo.memento;

import java.util.ArrayList;
import java.util.List;

import modelo.entidad.Jugador;
import modelo.entidad.Caja;

public class Memento {
	private final int posX;
	private final int posY;
	private final List<EstadoCaja> estadoCajas;
	
	public Memento(Jugador jugador, List<Caja> cajas) {
		this.posX = jugador.getX();
		this.posY = jugador.getY();
		this.estadoCajas = new ArrayList<>();
		for(Caja c : cajas) {
			Integer resistencia = null;
			resistencia = c.getComportamiento().getResistencia();
			this.estadoCajas.add(new EstadoCaja(c.getX(), c.getY(), c.estaRota(), resistencia));
		}
	}
	
	public int getPosX() {return posX;}
	public int getPosY() {return posY;}
	public List<EstadoCaja> getEstadoCajas(){return estadoCajas;}
	
	public static class EstadoCaja{
		public final int x;
		public final int y;
		public final boolean rota;
		public final Integer resistencia;
		public EstadoCaja(int x, int y, boolean rota, Integer resistencia) {
			this.x = x;
			this.y = y;
			this.rota = rota;
			this.resistencia = resistencia;
		}
	}
}
