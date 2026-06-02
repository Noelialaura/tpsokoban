package modelo;

public class Portal implements Casilla{
	
	private Portal extremo_opuesto;
	private char id;

	@Override
	public boolean esTransitable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public Portal getExtremo_opuesto() {
		return extremo_opuesto;
	}
	
	public void setId(char id) {
		this.id = id;
	}
	
	public char getId() {
		return id;	
	}
	
	public void setExtremo_opuesto(Portal extremo_opuesto) {
		this.extremo_opuesto = extremo_opuesto;
	}
}
