import javax.swing.SwingUtilities;

import vista.NivelSwing;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NivelSwing nivel = new NivelSwing(
                    "niveles/nivel1.txt",
                    "niveles/nivel2.txt",
                    "niveles/nivel3.txt");
            VentanaPrincipal ventana = new VentanaPrincipal(nivel);
            ventana.setVisible(true);
        });
    }
}
