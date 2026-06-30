package modelo.cargador;

import modelo.*;
import modelo.builder.ConstructorNivel;
import modelo.builder.DirectorNivel;
import modelo.fabrica.*;
import modelo.PisoExamen;
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
 *   'E' = Examen (al pisarla, la skin del jugador cambia a godiozzz)
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
        CREADORES.put('E', new CreadorCasilla() {
            public Casilla crearCasilla() { return new PisoExamen(); }
        });
    }

    @Override
    public ResultadoCarga cargar(String ruta) {
        List<String> lineas = leerLineas(ruta);

        ConstructorNivel builder = new ConstructorNivel();
        DirectorNivel director = new DirectorNivel(builder);
        director.construirDesdeLineas(lineas, this::obtenerCreador);
        return builder.construir();
    }

    private CreadorCasilla obtenerCreador(char ch) {
        CreadorCasilla creador = CREADORES.get(ch);
        if (creador == null) {
            throw new IllegalArgumentException("Carácter desconocido en el nivel: '" + ch + "'");
        }
        return creador;
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

}
