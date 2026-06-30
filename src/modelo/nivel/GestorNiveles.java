package modelo.nivel;

import java.util.ArrayList;
import java.util.List;

public class GestorNiveles {
    private final List<String> rutas;
    private int indiceNivel;

    public GestorNiveles(List<String> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            throw new IllegalArgumentException("Debe existir al menos un nivel.");
        }
        this.rutas = new ArrayList<>(rutas);
        this.indiceNivel = 0;
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
