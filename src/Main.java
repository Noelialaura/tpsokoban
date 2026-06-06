import javax.swing.SwingUtilities;

import modelo.Casilla;
import modelo.RegistroCasillas;
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

    private static NivelSwing crearNivelDemo() {
        String[] mapa = {
                "###########",
                "#.........#",
                "#..B.M....#",
                "#..##.....#",
                "#..B.M....#",
                "#....J....#",
                "#.........#",
                "###########"
        };

        Casilla[][] tablero = new Casilla[mapa.length][mapa[0].length()];
        boolean[][] cajas = new boolean[mapa.length][mapa[0].length()];
        int filaJugador = -1;
        int columnaJugador = -1;

        for (int fila = 0; fila < mapa.length; fila++) {
            for (int columna = 0; columna < mapa[fila].length(); columna++) {
                char simbolo = mapa[fila].charAt(columna);
                tablero[fila][columna] = RegistroCasillas.crearCasilla(simbolo);
                if (simbolo == 'J') {
                    filaJugador = fila;
                    columnaJugador = columna;
                }
                if (simbolo == 'B') {
                    cajas[fila][columna] = true;
                }
            }
        }

        return new NivelSwing(tablero, cajas, filaJugador, columnaJugador);
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
