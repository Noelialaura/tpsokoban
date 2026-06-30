package modelo.cargador;

import modelo.entidad.Tablero;


public interface CargadorNivel {
    Tablero cargar(String ruta);
}
