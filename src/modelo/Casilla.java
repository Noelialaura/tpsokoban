package modelo;

import modelo.entidad.Tablero;
import modelo.builder.ConstructorNivel;

public interface Casilla {
	
	boolean esTransitable();
	boolean esResbaladiza();
	boolean esMeta();
	boolean esPortal();
	default Casilla obtenerDestinoPortal() { return null; };
	default void ocupar() {};
	default void desocupar() {};
	default void activar(Tablero tablero) {};
	default void desactivar(Tablero tablero) {};
	default void abrir() {};
	default void cerrar() {};
	default boolean esPocion() { return false; };
	default String getTipoPocion() { return null; };
	default void registrarEnConstructor(ConstructorNivel constructor) {};
	default void recoger(Tablero tablero) {};
}
