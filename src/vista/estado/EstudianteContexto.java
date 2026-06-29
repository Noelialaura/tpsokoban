// vista/estado/EstudianteContexto.java
package vista.estado;

import java.awt.Graphics2D;
import vista.PanelTablero;

public class EstudianteContexto {
    private EstadoEstudiante estadoActual;
    private String nombreSprite;
    private boolean esEstudiante = false;
    private int xRoto = -1; // 👈 posición guardada al romperse
    private int yRoto = -1;

    public EstudianteContexto(String nombreSprite) {
        this.nombreSprite = nombreSprite;
        this.estadoActual = new EstadoNormal(nombreSprite);
    }

    public void activarSkinExamen(String nuevoSprite) {
        System.out.println("activarSkinExamen llamado con: " + nuevoSprite);
        this.nombreSprite = nuevoSprite;
        this.esEstudiante = true;
        this.estadoActual = new EstadoNormal(nuevoSprite);
    }

    public void irAMeta() {
    	if (esEstudiante) {
            estadoActual = new EstadoEnMeta(nombreSprite, "Recurso la materia");
        }
    }

    // 👇 guarda la posición en el momento de romperse
    public void romperse(int x, int y) {
        if (esEstudiante) {
            this.xRoto = x;
            this.yRoto = y;
            estadoActual = new EstadoRoto();
        }
    }

    public boolean estaRoto() {
        return xRoto != -1;
    }

    public boolean esEstudiante() {
        return esEstudiante;
    }

    public void reiniciar() {
        this.esEstudiante = false;
        this.nombreSprite = "caja_fragil";
        this.estadoActual = new EstadoNormal(nombreSprite);
        this.xRoto = -1;
        this.yRoto = -1;
    }

    public void pintar(Graphics2D g2, int x, int y, PanelTablero panel) {
        estadoActual.pintar(g2, x, y, panel);
    }

    // 👇 pintar usando la posición guardada (para cajas ya rotas)
    public void pintarRoto(Graphics2D g2, PanelTablero panel) {
        if (xRoto != -1) {
            estadoActual.pintar(g2, xRoto, yRoto, panel);
        }
    }
}