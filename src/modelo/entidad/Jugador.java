package modelo.entidad;

public class Jugador {
	private int x;
	private int y;
	private int skin;

	public Jugador(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void mover(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
