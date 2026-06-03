package vista;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class VentanaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    public VentanaPrincipal(NivelSwing nivel) {
        super("Sokoban");
        configurarVentana();
        cargarComponentes(nivel);
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);
    }

    private void cargarComponentes(NivelSwing nivel) {
        PanelHUD panelHUD = new PanelHUD();
        PanelTablero panelTablero = new PanelTablero(nivel, panelHUD::actualizarEstado);
        panelHUD.setAccionReiniciar(panelTablero::reiniciar);

        add(panelHUD, BorderLayout.NORTH);
        add(panelTablero, BorderLayout.CENTER);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }
}
