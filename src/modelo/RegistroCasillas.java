package modelo;

import modelo.fabrica.*;
import modelo.fabrica.CreadorExamen;

import java.util.HashMap;
import java.util.Map;


public class RegistroCasillas {
	
	private static final Map<Character, CreadorCasilla> REGISTRO = new HashMap<>();


	static {
		REGISTRO.put('#', new CreadorPared());
		REGISTRO.put('M', new CreadorMeta());
		REGISTRO.put('H', new CreadorHielo());
		REGISTRO.put('P', new CreadorPortal());
		REGISTRO.put('C', new CreadorCerrojo());
		REGISTRO.put('.', new CreadorPiso());
		REGISTRO.put('J', new CreadorPiso()); // El jugador se coloca sobre un piso
		REGISTRO.put('E', new CreadorExamen()); // Casilla Examen: cambia la skin del jugador
	}

	private static CreadorCasilla obtenerCreador(char simbolo) {
		return REGISTRO.getOrDefault(simbolo, new CreadorPiso());
	}

	public static Casilla crearCasilla(char simbolo) {
		CreadorCasilla creador = obtenerCreador(simbolo);
		return creador.crearCasilla();
	}
	

	public static void registrar(char simbolo, CreadorCasilla creador) {
		REGISTRO.put(simbolo, creador);
	}
}

