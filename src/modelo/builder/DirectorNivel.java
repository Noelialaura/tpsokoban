package modelo.builder;

import java.util.List;

import modelo.Casilla;
import modelo.entidad.Caja;
import modelo.fabrica.CreadorCasilla;
import modelo.strategy.ComportamientoFragil;
import modelo.strategy.ComportamientoLlave;
import modelo.strategy.ComportamientoNormal;

public class DirectorNivel {
    private BuilderNivel builder;

    public DirectorNivel(BuilderNivel builder) {
        this.builder = builder;
    }

    public void construirDesdeLineas(List<String> lineas, BuscadorCreadorCasilla buscadorCreador) {
        int filas = lineas.size();
        int columnas = lineas.stream().mapToInt(String::length).max().orElse(0);

        builder.crearGrilla(filas, columnas);

        for (int fila = 0; fila < filas; fila++) {
            String linea = lineas.get(fila);
            for (int columna = 0; columna < columnas; columna++) {
                char simbolo = columna < linea.length() ? linea.charAt(columna) : ' ';
                CreadorCasilla creador = buscadorCreador.obtener(simbolo);
                Casilla casilla = creador.crearCasilla();

                builder.agregarCasilla(fila, columna, casilla);
                procesarEntidad(simbolo, columna, fila);
            }
        }
    }

    private void procesarEntidad(char simbolo, int columna, int fila) {
        if (simbolo == '@' || simbolo == '+') {
            builder.ubicarJugador(columna, fila);
        }
        if (simbolo == '$' || simbolo == '*') {
            builder.agregarCaja(new Caja(columna, fila, new ComportamientoNormal()));
        }
        if (simbolo == 'F') {
            builder.agregarCaja(new Caja(columna, fila, new ComportamientoFragil(3)));
        }
        if (simbolo == 'L') {
            builder.agregarCaja(new Caja(columna, fila, new ComportamientoLlave()));
        }
    }

    public interface BuscadorCreadorCasilla {
        CreadorCasilla obtener(char simbolo);
    }
}
