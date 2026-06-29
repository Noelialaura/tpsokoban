// vista/estado/EstadoNormal.java
package vista.estado;

import java.awt.Graphics2D;
import vista.PanelTablero;

public class EstadoNormal implements EstadoEstudiante {
    private final String nombreSprite;

    public EstadoNormal(String nombreSprite) {
        this.nombreSprite = nombreSprite;
    }

    @Override
    public void pintar(Graphics2D g2, int x, int y, PanelTablero panel) {
        panel.pintarSprite(g2, nombreSprite, x, y, 0);
    }
}