package modelo;

import java.util.List;
import modelo.entidad.Caja;

/**
 * DTO que devuelve el CargadorNivel.
 * SOLID - SRP: única responsabilidad = transportar los datos del nivel cargado.
 */
public class ResultadoCarga {

    private final Casilla[][] grilla;
    private final int jugadorX;
    private final int jugadorY;
    private final List<Caja> cajas;
    private final int filas;
    private final int columnas;

    public ResultadoCarga(Casilla[][] grilla, int jugadorX, int jugadorY,
                          List<Caja> cajas, int filas, int columnas) {
        this.grilla    = grilla;
        this.jugadorX  = jugadorX;
        this.jugadorY  = jugadorY;
        this.cajas     = cajas;
        this.filas     = filas;
        this.columnas  = columnas;
    }

    public Casilla[][] getGrilla()  { return grilla; }
    public int getJugadorX()        { return jugadorX; }
    public int getJugadorY()        { return jugadorY; }
    public List<Caja> getCajas()    { return cajas; }
    public int getFilas()           { return filas; }
    public int getColumnas()        { return columnas; }
}
