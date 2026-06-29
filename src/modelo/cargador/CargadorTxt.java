package modelo.cargador;

import modelo.*;
import modelo.entidad.Caja;
import modelo.fabrica.*;
import modelo.strategy.ComportamientoFragil;
import modelo.strategy.ComportamientoLlave;
import modelo.strategy.ComportamientoNormal;
import modelo.PisoPocionVelocidad;
import modelo.PisoPocionFuerza;


import java.io.*;
import java.util.*;

/**
 * Convención de caracteres:
 *   ' ' = Piso vacío
 *   '#' = Pared
 *   '.' = Meta
 *   'H' = Hielo
 *   'P' = Portal (se linkean en pares por orden de aparición)
 *   'C' = Cerrojo
 *   'X' = Pared cerrada por cerrojo
 *   '@' = Jugador sobre piso
 *   '+' = Jugador sobre meta
 *   '$' = Caja normal sobre piso
 *   '*' = Caja normal sobre meta
 *   'F' = Caja fragil sobre piso
 *   'L' = Caja llave sobre piso
 *   'Y' = Poción de Velocidad (el jugador avanza 2 casillas al recogerla)
 *   'U' = Poción de Fuerza (el jugador puede empujar 2 cajas al recogerla)
 */
public class CargadorTxt implements CargadorNivel {

    private static final Map<Character, CreadorCasilla> CREADORES = new HashMap<>();

    static {
        CREADORES.put(' ', new CreadorPiso());
        CREADORES.put('#', new CreadorPared());
        CREADORES.put('.', new CreadorMeta());
        CREADORES.put('H', new CreadorHielo());
        CREADORES.put('P', new CreadorPortal());
        CREADORES.put('C', new CreadorCasilla() {
            public Casilla crearCasilla() { return new Cerrojo(); }
        });
        CREADORES.put('X', new CreadorParedCerrojo());
        CREADORES.put('@', new CreadorPiso());
        CREADORES.put('+', new CreadorMeta());
        CREADORES.put('$', new CreadorPiso());
        CREADORES.put('*', new CreadorMeta());
        CREADORES.put('F', new CreadorPiso());
        CREADORES.put('L', new CreadorPiso());
        CREADORES.put('Y', new CreadorCasilla() {
            public Casilla crearCasilla() { return new PisoPocionVelocidad(); }
        });
        CREADORES.put('U', new CreadorCasilla() {
            public Casilla crearCasilla() { return new PisoPocionFuerza(); }
        });
    }

    @Override
    public ResultadoCarga cargar(String ruta) {
        List<String> lineas = leerLineas(ruta);

        int filas    = lineas.size();
        int columnas = lineas.stream().mapToInt(String::length).max().orElse(0);

        Casilla[][]  grilla     = new Casilla[filas][columnas];
        List<Caja>   cajas      = new ArrayList<>();
        List<Portal> portales   = new ArrayList<>();
        List<Cerrojo> cerrojos  = new ArrayList<>();
        List<ParedCerrojo> paredesCerrojo = new ArrayList<>();
        int[]        posJugador = { 0, 0 };

        for (int fila = 0; fila < filas; fila++) {
            String linea = lineas.get(fila);
            for (int col = 0; col < columnas; col++) {
                char ch = col < linea.length() ? linea.charAt(col) : ' ';

                grilla[fila][col] = crearCasilla(ch);

                if (ch == 'P') {
                    portales.add((Portal) grilla[fila][col]);
                }
                if (ch == 'C') {
                    cerrojos.add((Cerrojo) grilla[fila][col]);
                }
                if (ch == 'X') {
                    paredesCerrojo.add((ParedCerrojo) grilla[fila][col]);
                }

                procesarEntidad(ch, col, fila, cajas, posJugador);
            }
        }

        linkearPortales(portales);
        linkearCerrojos(cerrojos, paredesCerrojo);

        return new ResultadoCarga(grilla, posJugador[0], posJugador[1], cajas, filas, columnas);
    }

    private Casilla crearCasilla(char ch) {
        CreadorCasilla creador = CREADORES.get(ch);
        if (creador == null) {
            throw new IllegalArgumentException("Carácter desconocido en el nivel: '" + ch + "'");
        }
        return creador.crearCasilla();
    }

    private void procesarEntidad(char ch, int col, int fila,
                                 List<Caja> cajas, int[] posJugador) {
        switch (ch) {
            case '@':
            case '+':
                posJugador[0] = col;
                posJugador[1] = fila;
                break;
            case '$':
            case '*':
                cajas.add(new Caja(col, fila, new ComportamientoNormal()));
                break;
            case 'F':
                cajas.add(new Caja(col, fila, new ComportamientoFragil(3)));
                break;
            case 'L':
                cajas.add(new Caja(col, fila, new ComportamientoLlave()));
                break;
        }
    }

    private List<String> leerLineas(String ruta) {
        List<String> lineas = new ArrayList<>();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);
            if (is == null) {
                throw new RuntimeException("No se encontró el archivo: " + ruta);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String linea;
            while ((linea = reader.readLine()) != null) {
                lineas.add(linea);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el nivel: " + ruta, e);
        }
        return lineas;
    }

    private void linkearPortales(List<Portal> portales) {
        for (int i = 0; i + 1 < portales.size(); i += 2) {
            Portal a = portales.get(i);
            Portal b = portales.get(i + 1);
            a.setExtremoOpuesto(b);
            b.setExtremoOpuesto(a);
        }
    }

    private void linkearCerrojos(List<Cerrojo> cerrojos, List<ParedCerrojo> paredesCerrojo) {
        for (Cerrojo cerrojo : cerrojos) {
            for (ParedCerrojo paredCerrojo : paredesCerrojo) {
                cerrojo.suscribir(paredCerrojo);
            }
        }
    }
}
