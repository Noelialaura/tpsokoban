package modelo.entidad;

import java.util.UUID;

public class Caja {

    private final UUID id;
    private int x;
    private int y;

    public Caja(int x, int y) {
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
    }

    public UUID getId() { return id; }
    public int getX()   { return x; }
    public int getY()   { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
