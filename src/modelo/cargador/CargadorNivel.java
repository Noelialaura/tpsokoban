package modelo.cargador;

import modelo.ResultadoCarga;


public interface CargadorNivel {
    ResultadoCarga cargar(String ruta);
}
