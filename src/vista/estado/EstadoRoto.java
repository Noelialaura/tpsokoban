// vista/estado/EstadoRoto.java
package vista.estado;

import java.awt.Graphics2D;
import vista.PanelTablero;

public class EstadoRoto implements EstadoEstudiante {
    @Override
    public void pintar(Graphics2D g2, int x, int y, PanelTablero panel) {
        panel.pintarTextoSobreCasilla(g2, x, y, "Dejó la carrera");
    }
}