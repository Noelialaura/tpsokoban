package modelo.builder;

import java.util.ArrayList;
import java.util.List;

import modelo.Casilla;
import modelo.Cerrojo;
import modelo.ParedCerrojo;
import modelo.Portal;
import modelo.ResultadoCarga;
import modelo.entidad.Caja;

public class ConstructorNivel implements BuilderNivel {
    private Casilla[][] grilla;
    private List<Caja> cajas;
    private List<Portal> portales;
    private List<Cerrojo> cerrojos;
    private List<ParedCerrojo> paredesCerrojo;
    private int jugadorX;
    private int jugadorY;
    private int filas;
    private int columnas;

    public ConstructorNivel() {
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
        casilla.registrarEnConstructor(this);
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
    }

    public ResultadoCarga construir() {
        linkearPortales();
        linkearCerrojos();
        return new ResultadoCarga(grilla, jugadorX, jugadorY, cajas, filas, columnas);
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
