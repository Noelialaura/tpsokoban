package modelo;

public class ParedCerrojo implements Casilla{
	
	private boolean transitable;
	private boolean llaveIngresada;

	public ParedCerrojo() {
		transitable = false;
		llaveIngresada = false;
	}
	
	@Override
	public boolean esTransitable() {
		// TODO Auto-generated method stub
		return transitable;
	}
	
	public void abrir(boolean llaveIngresada) {
		if(llaveIngresada==true) {
			this.llaveIngresada = llaveIngresada;
			transitable = true;
		}
	}
	
}
