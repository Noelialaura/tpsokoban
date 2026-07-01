package modelo.nivel;

import java.util.ArrayList;
import java.util.List;

public class GestorNiveles {
    private static GestorNiveles instance;
    private final List<String> rutas;
    private int indiceNivel;

    private GestorNiveles(List<String> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos un nivel.");
        }
        this.rutas = new ArrayList<>(rutas);
        this.indiceNivel = 0;
    }

    public static GestorNiveles getInstance(List<String> rutas) {
        if (instance == null) {
            instance = new GestorNiveles(rutas);
        }
        return instance;
    }

    public static GestorNiveles getInstance() {
        return instance;
    }

    public boolean avanzarNivel() {
        if (!tieneSiguienteNivel()) {
            return false;
        }

        indiceNivel++;
        return true;
    }

    public boolean tieneSiguienteNivel() {
        return indiceNivel + 1 < rutas.size();
    }

    public String getRutaActual() {
        return rutas.get(indiceNivel);
    }

    public int getNumeroNivelActual() {
        return indiceNivel + 1;
    }

    public int getTotalNiveles() {
        return rutas.size();
    }
}
