package modelo.builder;

import java.util.ArrayList;
import java.util.List;

import modelo.Casilla;
import modelo.Cerrojo;
import modelo.ParedCerrojo;
import modelo.Portal;
import modelo.entidad.Caja;
import modelo.entidad.Jugador;
import modelo.entidad.Tablero;

public class NivelBuilder implements BuilderNivel {
    private Casilla[][] grilla;
    private List<Caja> cajas;
    private List<Portal> portales;
    private List<Cerrojo> cerrojos;
    private List<ParedCerrojo> paredesCerrojo;
    private int jugadorX;
    private int jugadorY;
    private int filas;
    private int columnas;
    private boolean jugadorUbicado;

    public NivelBuilder() {
        cajas = new ArrayList<>();
        portales = new ArrayList<>();
        cerrojos = new ArrayList<>();
        paredesCerrojo = new ArrayList<>();
    }

    public void crearGrilla(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        grilla = new Casilla[filas][columnas];
    }

    public void agregarCasilla(int fila, int columna, Casilla casilla) {
        grilla[fila][columna] = casilla;
        casilla.registrarEnBuilder(this);
    }

    public void registrarPortal(Portal portal) {
        portales.add(portal);
    }

    public void registrarCerrojo(Cerrojo cerrojo) {
        cerrojos.add(cerrojo);
    }

    public void registrarParedCerrojo(ParedCerrojo paredCerrojo) {
        paredesCerrojo.add(paredCerrojo);
    }

    public void agregarCaja(Caja caja) {
        cajas.add(caja);
    }

    public void ubicarJugador(int x, int y) {
        jugadorX = x;
        jugadorY = y;
        jugadorUbicado = true;
    }

    public Tablero construir() {
        validarNivelConstruido();
        linkearPortales();
        linkearCerrojos();
        return new Tablero(grilla, cajas, new Jugador(jugadorX, jugadorY));
    }

    private void validarNivelConstruido() {
        if (grilla == null || filas == 0 || columnas == 0) {
            throw new IllegalStateException("El nivel no tiene tablero.");
        }
        if (!jugadorUbicado) {
            throw new IllegalStateException("El nivel no tiene jugador inicial.");
        }
        if (portales.size() % 2 != 0) {
            throw new IllegalStateException("Los portales deben estar en pares.");
        }
    }

    private void linkearPortales() {
        for (int i = 0; i + 1 < portales.size(); i += 2) {
            Portal a = portales.get(i);
            Portal b = portales.get(i + 1);
            a.setExtremoOpuesto(b);
            b.setExtremoOpuesto(a);
        }
    }

    private void linkearCerrojos() {
        for (Cerrojo cerrojo : cerrojos) {
            for (ParedCerrojo paredCerrojo : paredesCerrojo) {
                cerrojo.suscribir(paredCerrojo);
            }
        }
    }
}
