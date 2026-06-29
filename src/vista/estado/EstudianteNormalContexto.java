// vista/estado/EstudianteNormalContexto.java
package vista.estado;

import java.awt.Graphics2D;
import vista.PanelTablero;

public class EstudianteNormalContexto {
    private EstadoEstudiante estadoActual;
    private String nombreSprite;
    private boolean esEstudiante = false;

    public EstudianteNormalContexto(String nombreSprite) {
        this.nombreSprite = nombreSprite;
        this.estadoActual = new EstadoNormal(nombreSprite);
    }

    public void activarSkinExamen(String nuevoSprite) {
        this.nombreSprite = nuevoSprite;
        this.esEstudiante = true;
        this.estadoActual = new EstadoNormal(nuevoSprite);
    }

    public void irAMeta() {
        if (esEstudiante) {
            estadoActual = new EstadoEnMeta(nombreSprite, "Se fue a previo");
        }
    }

    public boolean esEstudiante() {
        return esEstudiante;
    }

    public void reiniciar() {
        this.esEstudiante = false;
        this.nombreSprite = "caja";
        this.estadoActual = new EstadoNormal(nombreSprite);
    }

    public void pintar(Graphics2D g2, int x, int y, PanelTablero panel) {
        estadoActual.pintar(g2, x, y, panel);
    }
}