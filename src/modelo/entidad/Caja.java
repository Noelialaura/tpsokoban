package modelo.entidad;

import modelo.Movimiento;
import modelo.strategy.ComportamientoCaja;

import java.util.UUID;

public class Caja {

    private final UUID id;
    private int x;
    private int y;
    private ComportamientoCaja comportamiento;
    private boolean rota;

    public Caja(int x, int y, ComportamientoCaja comportamiento) {
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.comportamiento = comportamiento;
    }

    public UUID getId() { return id; }
    public int getX()   { return x; }
    public int getY()   { return y; }

    public boolean estaEn(int fila, int columna) {
        return !rota && y == fila && x == columna;
    }

    public void moverA(int fila, int columna) {
        this.y = fila;
        this.x = columna;
    }

    public boolean empujar(Movimiento movimiento, Tablero tablero) {
        if (rota) {
            return false;
        }
        return comportamiento.empujar(this, movimiento, tablero);
    }

    public Caja copiar() {
        return new Caja(x, y, comportamiento.copiar());
    }

    public ComportamientoCaja getComportamiento() {
        return comportamiento;
    }

    public boolean trabajaConMeta() {
        return comportamiento.trabajaConMeta();
    }

    public void romper() {
        rota = true;
    }

    public void restaurarRota(boolean rota) {
        this.rota = rota;
    }

    public boolean estaRota() {
        return rota;
    }
}
