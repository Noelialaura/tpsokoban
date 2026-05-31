# Sokoban

Implementación del juego clásico **Sokoban** en **Java** utilizando **Swing** para la interfaz gráfica 2D.

El proyecto está desarrollado siguiendo la arquitectura **MVC (Model - View - Controller)** y aplicando principios de diseño orientado a objetos como:

- SOLID
- GRASP
- Patrones GoF

---

# 📂 Estructura del Proyecto
```
src/
├── Main.java
├── controlador/
│   
├── modelo/
│   ├── casilla/               
│   │   ├── Casilla.java
│   │   ├── Piso.java
│   │   ├── Pared.java
│   │   ├── Meta.java
│   │   ├── Hielo.java
│   │   ├── Portal.java
│   │   ├── Muro.java
│   │   └── Cerrojo.java
│   ├── entidad/
│   │   ├── Entidad.java      
│   │   ├── Jugador.java       
│   │   └── Caja.java           
│   ├── comportamiento/         Strategy 
│   │   
│   ├── fabrica/                
│   │   ├── FabricaSkinJugador.java
│   │   ├── SkinClasica.java
│   │   └── casilla/           
│   │       ├── CreadorCasilla.java
│   │       ├── Creador*.java
│   │       └── RegistroCreadores.java
│   ├── cargador/
│   │   ├── CargadorNivel.java 
│   │   ├── CargadorTxt.java
│   └── tablero/
│       ├── Tablero.java
│       ├── EstadoJuego.java
│       └── ResultadoCarga.java
├── vista/
│   ├── VentanaPrincipal.java
│   ├── PanelTablero.java
│   └── PanelHUD.java
├── recursos/
│   ├── imagenes/               
│   └── sonidos/               
└── niveles/
    └── nivel1.txt
```


---

# 📦 Descripción de Carpetas

| Carpeta | Descripción |
|---|---|
| `modelo/` | Lógica y estado del juego |
| `vista/` | Interfaz gráfica con Swing |
| `controlador/` | Manejo de eventos e interacción |
| `assets/` | Recursos del juego |

---
