// vista/estado/EstadoEnMeta.java
package vista.estado;

import java.awt.Graphics2D;
import vista.PanelTablero;

public class EstadoEnMeta implements EstadoEstudiante {
 private final String nombreSprite;
 private final String texto;

 public EstadoEnMeta(String nombreSprite, String texto) {
     this.nombreSprite = nombreSprite;
     this.texto = texto;
 }

 @Override
 public void pintar(Graphics2D g2, int x, int y, PanelTablero panel) {
     panel.pintarSprite(g2, nombreSprite, x, y, 0);
     panel.pintarTextoSobreCasilla(g2, x, y, texto);
 }
}