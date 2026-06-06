package modelo;

public class Cerrojo implements Casilla{
	
	private boolean llaveIngresada;

	@Override
	public boolean esTransitable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean llaveIngresada() {
		return llaveIngresada;
	}
	
	public void setLlveIngresada(boolean llaveIngresada) {
		this.llaveIngresada = llaveIngresada;
	}
}
