package vista;

import modelo.Casilla;
import modelo.ResultadoCarga;
import modelo.entidad.Caja;
import modelo.entidad.Tablero;

import java.util.ArrayList;
import java.util.List;

import controlador.Controlador;

public class NivelSwing {
    private String ruta;
    private Casilla[][] tablero;
    private List<Caja> cajasIniciales;
    private int filaJugadorInicial;
    private int columnaJugadorInicial;
    private Controlador controlador;

    /** Constructor original (hardcodeado / demo). */
    public NivelSwing(
            Casilla[][] tablero,
            boolean[][] cajasIniciales,
            int filaJugadorInicial,
            int columnaJugadorInicial) {
        this.ruta = null;
        this.controlador = Controlador.getInstance();
        controlador.validarNivel(tablero, cajasIniciales, filaJugadorInicial, columnaJugadorInicial);
        this.tablero = tablero;
        this.cajasIniciales = crearListaCajas(cajasIniciales);
        this.filaJugadorInicial = filaJugadorInicial;
        this.columnaJugadorInicial = columnaJugadorInicial;
        
    }

    /**
     * Constructor que acepta el resultado del CargadorNivel.
     * Convierte la List<Caja> del DTO en el boolean[][] que usa internamente NivelSwing.
     */
    public NivelSwing(ResultadoCarga resultado) {
        this.ruta = null;
        controlador.cargarResultado(resultado, this);
    }

    public NivelSwing(String ruta) {
        this.ruta = ruta;
        recargar();
    }

    public void recargar() {
        if (ruta == null) {
            return;
        }
        if (controlador==null){
            this.controlador = controlador.getInstance();
        }
        controlador.cargarNivel(ruta, this);
    }
    
    public void setTablero(Casilla[][] tablero){
        this.tablero = tablero;
    }
    public void setCajasIniciales(List<Caja>cajas){
        this.cajasIniciales = cajas;
    }
    public void setFilaJugadorInicial(int fila){
        this.filaJugadorInicial= fila;
    }
    public void setColumnaJugadorInicial(int columna){
        this.columnaJugadorInicial = columna;
    }



    public Casilla[][] getTablero() { return tablero; }

    public Tablero crearTablero() {
    if (controlador == null) {
        controlador = Controlador.getInstance();
    }
    return controlador.crearTablero(
        tablero,
        copiarListaCajas(cajasIniciales),
        columnaJugadorInicial,
        filaJugadorInicial
    );
}



    private List<Caja> crearListaCajas(boolean[][] origen) {
        List<Caja> cajas = new ArrayList<>();
        for (int fila = 0; fila < origen.length; fila++) {
            for (int columna = 0; columna < origen[fila].length; columna++) {
                if (origen[fila][columna]) {
                    cajas.add(new Caja(columna, fila, new modelo.strategy.ComportamientoNormal()));
                }
            }
        }
        return cajas;
    }

    public List<Caja> copiarListaCajas(List<Caja> origen) {
        List<Caja> copia = new ArrayList<>();
        for (Caja caja : origen) {
            copia.add(caja.copiar());
        }
        return copia;
    }
}
