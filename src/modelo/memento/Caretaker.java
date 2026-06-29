package modelo.memento;

import java.util.Stack;

public class Caretaker {
	private Stack<Memento> historial;
	
	public Caretaker(){
		this.historial = new Stack<>();
	}
	
	public void guardarEstado(Memento memento) {
		if(historial.size()>=3)historial.remove(0);
		historial.push(memento);
	}
	
	public Memento deshacer() {

	    if(historial.size() <= 1)
	        return null;

	    historial.pop();

	    return historial.peek();
	}
	
	public void limpiarHistorial() {
		historial.clear();
	}
}
