package modelo.entidad;

import modelo.Movimiento;
import modelo.decorador.IEntidad;


public class Jugador implements IEntidad {
    private static Jugador instance;

    private int x;
    private int y;

    private Jugador(int x, int y) {
        this.x = x;
        this.y = y;
        instance = this;
    }

    public static Jugador getInstance() {
        return instance;
    }
        public static Jugador getInstance(int x,int y) {
        if (instance == null){
            instance = new Jugador(x,y);
        }
        return instance;
        }

    public int getX() { return x; }
    public int getY() { return y; }

    public void mover(int x, int y) {
        this.x = x;
        this.y = y;
    }

    
    @Override
    public void desplazarse(Movimiento movimiento, Tablero tablero) {
        int nuevaFila    = movimiento.aplicarAFila(this.y);
        int nuevaColumna = movimiento.aplicarAColumna(this.x);
        if (tablero.puedeOcuparse(nuevaFila, nuevaColumna)) {
            mover(nuevaColumna, nuevaFila);
        }
    }

    
    @Override
    public boolean empujarCaja(Caja caja, Movimiento movimiento, Tablero tablero) {
        return caja.empujar(movimiento, tablero);
    }
}
