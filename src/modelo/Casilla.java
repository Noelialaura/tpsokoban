package modelo;

import modelo.entidad.Tablero;
import modelo.builder.NivelBuilder;

public interface Casilla {
	
	boolean esTransitable();
	boolean esResbaladiza();
	boolean esMeta();
	boolean esPortal();
	default boolean esParedCerrojo() { return false; };
	default boolean esPared() { return false; };
	default boolean esCerrojo() { return false; };
	default Casilla obtenerDestinoPortal() { return null; };
	default void ocupar() {};
	default void desocupar() {};
	default void activar(Tablero tablero) {};
	default void desactivar(Tablero tablero) {};
	default void abrir() {};
	default void cerrar() {};
	default boolean esPocion() { return false; };
	default boolean esExamen() { return false; };
	default boolean esPocionVelocidad() { return false; };
	default String getTipoPocion() { return null; };
	default void registrarEnBuilder(NivelBuilder builder) {};
	default void recoger(Tablero tablero) {};
}
