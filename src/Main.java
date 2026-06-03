import javax.swing.SwingUtilities;

import modelo.ResultadoCarga;
import modelo.cargador.CargadorNivel;
import modelo.cargador.CargadorTxt;
import vista.NivelSwing;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NivelSwing nivel = cargarNivel("niveles/nivelPochoclera.txt");
            VentanaPrincipal ventana = new VentanaPrincipal(nivel);
            ventana.setVisible(true);
        });
    }

    /**
     * Carga un nivel desde un archivo .txt ubicado en el classpath.
     * Usa CargadorTxt (implementación de CargadorNivel) para parsear el archivo
     * y construye el NivelSwing a partir del ResultadoCarga devuelto.
     *
     * Para cambiar el formato de archivo (ej: CSV, JSON) basta con
     * pasar otra implementación de CargadorNivel sin tocar nada más.
     */
    private static NivelSwing cargarNivel(String ruta) {
        CargadorNivel cargador = new CargadorTxt();
        ResultadoCarga resultado = cargador.cargar(ruta);
        return new NivelSwing(resultado);
    }
}
