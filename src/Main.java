import javax.swing.SwingUtilities;

import modelo.Casilla;
import modelo.Cerrojo;
import modelo.Hielo;
import modelo.Meta;
import modelo.Pared;
import modelo.Piso;
import modelo.Portal;
import vista.NivelSwing;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal(crearNivelDemo());
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
                tablero[fila][columna] = crearCasilla(simbolo);
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

    private static Casilla crearCasilla(char simbolo) {
        switch (simbolo) {
            case '#':
                return new Pared();
            case 'M':
                return new Meta();
            case 'H':
                return new Hielo();
            case 'P':
                Portal portal = new Portal();
                portal.setId('A');
                return portal;
            case 'C':
                return new Cerrojo();
            default:
                return new Piso();
        }
    }
}
