package vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import modelo.Casilla;
import modelo.Cerrojo;
import modelo.Hielo;
import modelo.Meta;
import modelo.Pared;
import modelo.Piso;
import modelo.Portal;

public class PanelTablero extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TAMANIO_CELDA = 64;
    private static final int MARGEN = 14;
    private static final int ARCO = 10;
    private static final int SOLAPE_TERRENO = 3;
    private static final int DURACION_ANIMACION_MS = 140;

    private final NivelSwing nivel;
    private final Casilla[][] tablero;
    private final int filas;
    private final int columnas;
    private final EstadoListener estadoListener;
    private final int totalCajas;
    private final Map<String, BufferedImage> sprites = new HashMap<>();

    private boolean[][] cajas;
    private int filaJugador;
    private int columnaJugador;
    private int movimientos;
    private boolean ganado;
    private int filaJugadorAnterior;
    private int columnaJugadorAnterior;
    private int direccionFila = 1;
    private int direccionColumna;
    private long inicioAnimacion;
    private Timer animacionMovimiento;

    public PanelTablero(NivelSwing nivel, EstadoListener estadoListener) {
        this.nivel = nivel;
        tablero = nivel.getTablero();
        filas = tablero.length;
        columnas = tablero[0].length;
        this.estadoListener = estadoListener;
        totalCajas = contarCajas(nivel.crearCajas());

        cargarSprites();
        setBackground(new Color(19, 25, 27));
        setPreferredSize(new Dimension(
                columnas * TAMANIO_CELDA + MARGEN * 2,
                filas * TAMANIO_CELDA + MARGEN * 2));
        configurarAnimacion();
        configurarControles();
        reiniciar();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        pintarFondoTablero(g2);
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                pintarCasilla(g2, tablero[fila][columna], fila, columna);
            }
        }

        pintarCajas(g2);
        pintarJugador(g2);
        g2.dispose();
    }

    public void reiniciar() {
        cajas = nivel.crearCajas();
        filaJugador = nivel.getFilaJugadorInicial();
        columnaJugador = nivel.getColumnaJugadorInicial();
        filaJugadorAnterior = filaJugador;
        columnaJugadorAnterior = columnaJugador;
        direccionFila = 1;
        direccionColumna = 0;
        animacionMovimiento.stop();
        movimientos = 0;
        ganado = false;
        notificarEstado();
        repaint();
    }

    private void configurarAnimacion() {
        animacionMovimiento = new Timer(16, event -> {
            if (obtenerProgresoAnimacion() >= 1.0) {
                animacionMovimiento.stop();
            }
            repaint();
        });
        animacionMovimiento.setRepeats(true);
    }

    private void configurarControles() {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_UP, "arriba", -1, 0);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_DOWN, "abajo", 1, 0);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_LEFT, "izquierda", 0, -1);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_RIGHT, "derecha", 0, 1);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_W, "arriba-w", -1, 0);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_S, "abajo-s", 1, 0);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_A, "izquierda-a", 0, -1);
        registrarMovimiento(inputMap, actionMap, KeyEvent.VK_D, "derecha-d", 0, 1);
        registrarReinicio(inputMap, actionMap);
    }

    private void registrarMovimiento(
            InputMap inputMap,
            ActionMap actionMap,
            int codigoTecla,
            String nombreAccion,
            int cambioFila,
            int cambioColumna) {
        inputMap.put(KeyStroke.getKeyStroke(codigoTecla, 0), nombreAccion);
        actionMap.put(nombreAccion, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent event) {
                moverJugador(cambioFila, cambioColumna);
            }
        });
    }

    private void registrarReinicio(InputMap inputMap, ActionMap actionMap) {
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "reiniciar");
        actionMap.put("reiniciar", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent event) {
                reiniciar();
            }
        });
    }

    private void moverJugador(int cambioFila, int cambioColumna) {
        if (ganado || animacionMovimiento.isRunning()) {
            return;
        }

        int nuevaFila = filaJugador + cambioFila;
        int nuevaColumna = columnaJugador + cambioColumna;
        direccionFila = cambioFila;
        direccionColumna = cambioColumna;

        if (!estaLibreParaJugador(nuevaFila, nuevaColumna, cambioFila, cambioColumna)) {
            repaint();
            return;
        }

        if (hayCaja(nuevaFila, nuevaColumna)) {
            moverCaja(nuevaFila, nuevaColumna, cambioFila, cambioColumna);
        }

        filaJugadorAnterior = filaJugador;
        columnaJugadorAnterior = columnaJugador;
        direccionFila = cambioFila;
        direccionColumna = cambioColumna;
        filaJugador = nuevaFila;
        columnaJugador = nuevaColumna;
        aplicarPortalSiCorresponde();
        iniciarAnimacionMovimiento();
        movimientos++;
        ganado = estaCompleto();
        notificarEstado();
        repaint();

        if (ganado) {
            JOptionPane.showMessageDialog(this, "Nivel completado en " + movimientos + " movimientos.");
        }
    }

    private void iniciarAnimacionMovimiento() {
        inicioAnimacion = System.currentTimeMillis();
        animacionMovimiento.restart();
    }

    private void cargarSprites() {
        cargarSprite("caja");
        cargarSprite("caja_en_meta");
        cargarSprite("jugador");
        cargarSprite("pared");
        cargarSprite("piso");
        cargarSprite("meta");
        cargarSprite("hielo");
        cargarSprite("portal");
        cargarSprite("cerrojo");
    }

    private void cargarSprite(String nombre) {
        try {
            BufferedImage imagen = ImageIO.read(new File("assets/imagenes/sprites/" + nombre + ".png"));
            if (imagen != null) {
                sprites.put(nombre, imagen);
            }
        } catch (IOException exception) {
            // Si falta un asset, la vista conserva el dibujo Java2D como respaldo.
        }
    }

    private boolean pintarSprite(Graphics2D g2, String nombre, int x, int y, int margen) {
        BufferedImage imagen = sprites.get(nombre);
        if (imagen == null) {
            return false;
        }

        int tamanio = obtenerTamanioCelda() - margen * 2;
        g2.drawImage(imagen, x + margen, y + margen, tamanio, tamanio, null);
        return true;
    }

    private boolean pintarSpriteTerreno(Graphics2D g2, String nombre, int x, int y) {
        BufferedImage imagen = sprites.get(nombre);
        if (imagen == null) {
            return false;
        }

        int ajuste = SOLAPE_TERRENO;
        g2.drawImage(
                imagen,
                x - ajuste,
                y - ajuste,
                obtenerTamanioCelda() + ajuste * 2,
                obtenerTamanioCelda() + ajuste * 2,
                null);
        return true;
    }

    private boolean estaLibreParaJugador(int fila, int columna, int cambioFila, int cambioColumna) {
        if (!puedeOcuparse(fila, columna)) {
            return false;
        }

        if (!hayCaja(fila, columna)) {
            return true;
        }

        int filaDestinoCaja = fila + cambioFila;
        int columnaDestinoCaja = columna + cambioColumna;
        return puedeOcuparse(filaDestinoCaja, columnaDestinoCaja) && !hayCaja(filaDestinoCaja, columnaDestinoCaja);
    }

    private void moverCaja(int fila, int columna, int cambioFila, int cambioColumna) {
        cajas[fila][columna] = false;
        cajas[fila + cambioFila][columna + cambioColumna] = true;
    }

    private boolean puedeOcuparse(int fila, int columna) {
        return estaEnTablero(fila, columna) && tablero[fila][columna].esTransitable();
    }

    private boolean hayCaja(int fila, int columna) {
        return estaEnTablero(fila, columna) && cajas[fila][columna];
    }

    private boolean estaEnTablero(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    private void aplicarPortalSiCorresponde() {
        Casilla casilla = tablero[filaJugador][columnaJugador];
        if (!(casilla instanceof Portal)) {
            return;
        }

        Portal portal = (Portal) casilla;
        Portal extremoOpuesto = portal.getExtremo_opuesto();
        if (extremoOpuesto == null) {
            return;
        }

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (tablero[fila][columna] == extremoOpuesto && puedeOcuparse(fila, columna) && !hayCaja(fila, columna)) {
                    filaJugador = fila;
                    columnaJugador = columna;
                    return;
                }
            }
        }
    }

    private boolean estaCompleto() {
        return totalCajas > 0 && contarCajasEnMeta() == totalCajas;
    }

    private void notificarEstado() {
        if (estadoListener != null) {
            estadoListener.actualizarEstado(movimientos, contarCajasEnMeta(), totalCajas, ganado);
        }
    }

    private int contarCajas(boolean[][] cajasParaContar) {
        int cantidad = 0;
        for (int fila = 0; fila < cajasParaContar.length; fila++) {
            for (int columna = 0; columna < cajasParaContar[fila].length; columna++) {
                if (cajasParaContar[fila][columna]) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    private int contarCajasEnMeta() {
        int cantidad = 0;
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (cajas[fila][columna] && tablero[fila][columna] instanceof Meta) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    private void pintarFondoTablero(Graphics2D g2) {
        int tamanioCelda = obtenerTamanioCelda();
        int ancho = columnas * tamanioCelda;
        int alto = filas * tamanioCelda;
        g2.setPaint(new GradientPaint(0, 0, new Color(21, 30, 29), getWidth(), getHeight(), new Color(31, 38, 34)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillRoundRect(MARGEN - 4, MARGEN - 2, ancho + 8, alto + 9, 16, 16);
        g2.setColor(new Color(37, 45, 40));
        g2.fillRoundRect(MARGEN - 6, MARGEN - 6, ancho + 12, alto + 12, 16, 16);
    }

    private void pintarCasilla(Graphics2D g2, Casilla casilla, int fila, int columna) {
        int x = obtenerXCasilla(columna);
        int y = obtenerYCasilla(fila);

        if (casilla instanceof Pared) {
            pintarPared(g2, x, y);
            return;
        }

        pintarPiso(g2, x, y, fila, columna);
        pintarDetalle(g2, casilla, x, y);
    }

    private void pintarPiso(Graphics2D g2, int x, int y, int fila, int columna) {
        if (pintarSpriteTerreno(g2, "piso", x, y)) {
            return;
        }

        Color arriba = (fila + columna) % 2 == 0 ? new Color(184, 178, 156) : new Color(174, 169, 150);
        Color abajo = (fila + columna) % 2 == 0 ? new Color(151, 145, 126) : new Color(142, 137, 120);
        g2.setPaint(new GradientPaint(x, y, arriba, x, y + TAMANIO_CELDA, abajo));
        g2.fillRoundRect(x + 2, y + 2, TAMANIO_CELDA - 4, TAMANIO_CELDA - 4, ARCO, ARCO);

        g2.setColor(new Color(255, 255, 255, 36));
        g2.drawLine(x + 8, y + 9, x + TAMANIO_CELDA - 10, y + 9);
        g2.setColor(new Color(62, 55, 48, 55));
        g2.drawRoundRect(x + 2, y + 2, TAMANIO_CELDA - 4, TAMANIO_CELDA - 4, ARCO, ARCO);
    }

    private void pintarDetalle(Graphics2D g2, Casilla casilla, int x, int y) {
        if (casilla instanceof Meta) {
            pintarMeta(g2, x, y);
            return;
        }

        if (casilla instanceof Hielo) {
            pintarHielo(g2, x, y);
            return;
        }

        if (casilla instanceof Portal) {
            pintarPortal(g2, x, y);
            return;
        }

        if (casilla instanceof Cerrojo) {
            pintarCerrojo(g2, x, y);
        }
    }

    private void pintarPared(Graphics2D g2, int x, int y) {
        if (pintarSpriteTerreno(g2, "pared", x, y)) {
            return;
        }

        g2.setPaint(new GradientPaint(x, y, new Color(82, 97, 101), x, y + TAMANIO_CELDA, new Color(42, 55, 59)));
        g2.fillRoundRect(x + 1, y + 1, TAMANIO_CELDA - 2, TAMANIO_CELDA - 2, ARCO, ARCO);

        g2.setColor(new Color(121, 138, 138, 120));
        g2.drawLine(x + 7, y + 17, x + TAMANIO_CELDA - 8, y + 17);
        g2.drawLine(x + 7, y + 39, x + TAMANIO_CELDA - 8, y + 39);
        g2.drawLine(x + 21, y + 4, x + 21, y + 17);
        g2.drawLine(x + 43, y + 17, x + 43, y + 39);
        g2.drawLine(x + 27, y + 39, x + 27, y + TAMANIO_CELDA - 6);

        g2.setColor(new Color(16, 21, 23, 90));
        g2.drawRoundRect(x + 1, y + 1, TAMANIO_CELDA - 2, TAMANIO_CELDA - 2, ARCO, ARCO);
    }

    private void pintarMeta(Graphics2D g2, int x, int y) {
        if (pintarSpriteTerreno(g2, "meta", x, y)) {
            return;
        }

        int centroX = x + TAMANIO_CELDA / 2;
        int centroY = y + TAMANIO_CELDA / 2;

        g2.setColor(new Color(92, 70, 35, 55));
        g2.fillOval(centroX - 18, centroY - 16, 36, 32);
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(new Color(238, 193, 88));
        g2.drawOval(centroX - 17, centroY - 17, 34, 34);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(94, 73, 39, 150));
        g2.drawOval(centroX - 9, centroY - 9, 18, 18);
    }

    private void pintarHielo(Graphics2D g2, int x, int y) {
        if (pintarSpriteTerreno(g2, "hielo", x, y)) {
            return;
        }

        g2.setPaint(new GradientPaint(x, y, new Color(189, 229, 228, 160), x, y + TAMANIO_CELDA, new Color(101, 174, 188, 150)));
        g2.fillRoundRect(x + 7, y + 7, TAMANIO_CELDA - 14, TAMANIO_CELDA - 14, 14, 14);
        g2.setColor(new Color(255, 255, 255, 130));
        g2.drawLine(x + 16, y + 19, x + 38, y + 12);
        g2.drawLine(x + 23, y + 43, x + 48, y + 31);
    }

    private void pintarPortal(Graphics2D g2, int x, int y) {
        if (pintarSpriteTerreno(g2, "portal", x, y)) {
            return;
        }

        int centroX = x + TAMANIO_CELDA / 2;
        int centroY = y + TAMANIO_CELDA / 2;
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(new Color(99, 73, 159, 180));
        g2.drawOval(centroX - 19, centroY - 19, 38, 38);
        g2.setColor(new Color(109, 205, 196, 180));
        g2.drawArc(centroX - 14, centroY - 14, 28, 28, 20, 250);
        g2.setColor(new Color(244, 237, 255, 170));
        g2.fillOval(centroX - 5, centroY - 5, 10, 10);
    }

    private void pintarCerrojo(Graphics2D g2, int x, int y) {
        if (pintarSpriteTerreno(g2, "cerrojo", x, y)) {
            return;
        }

        int centroX = x + TAMANIO_CELDA / 2;
        int centroY = y + TAMANIO_CELDA / 2;
        g2.setStroke(new BasicStroke(4f));
        g2.setColor(new Color(73, 58, 51, 180));
        g2.drawArc(centroX - 12, centroY - 20, 24, 26, 0, 180);
        g2.setPaint(new GradientPaint(x, y, new Color(197, 81, 76), x, y + TAMANIO_CELDA, new Color(126, 47, 49)));
        g2.fillRoundRect(centroX - 16, centroY - 7, 32, 26, 7, 7);
        g2.setColor(new Color(255, 215, 160, 120));
        g2.drawRoundRect(centroX - 16, centroY - 7, 32, 26, 7, 7);
    }

    private void pintarCajas(Graphics2D g2) {
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                if (cajas[fila][columna]) {
                    pintarCaja(g2, fila, columna);
                }
            }
        }
    }

    private void pintarCaja(Graphics2D g2, int fila, int columna) {
        int x = obtenerXCasilla(columna);
        int y = obtenerYCasilla(fila);
        int margenCaja = 9;
        boolean enMeta = tablero[fila][columna] instanceof Meta;

        if (pintarSprite(g2, enMeta ? "caja_en_meta" : "caja", x, y, 0)) {
            return;
        }

        g2.setColor(new Color(0, 0, 0, 58));
        g2.fillRoundRect(x + margenCaja + 3, y + margenCaja + 5, TAMANIO_CELDA - margenCaja * 2, TAMANIO_CELDA - margenCaja * 2, 10, 10);

        Color arriba = enMeta ? new Color(98, 174, 111) : new Color(188, 123, 58);
        Color abajo = enMeta ? new Color(46, 119, 78) : new Color(126, 75, 38);
        g2.setPaint(new GradientPaint(x, y, arriba, x, y + TAMANIO_CELDA, abajo));
        g2.fillRoundRect(x + margenCaja, y + margenCaja, TAMANIO_CELDA - margenCaja * 2, TAMANIO_CELDA - margenCaja * 2, 10, 10);

        g2.setColor(enMeta ? new Color(214, 243, 211, 160) : new Color(255, 218, 162, 150));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(x + margenCaja, y + margenCaja, TAMANIO_CELDA - margenCaja * 2, TAMANIO_CELDA - margenCaja * 2, 10, 10);
        g2.drawLine(x + margenCaja + 7, y + margenCaja + 7, x + TAMANIO_CELDA - margenCaja - 8, y + TAMANIO_CELDA - margenCaja - 8);
        g2.drawLine(x + TAMANIO_CELDA - margenCaja - 8, y + margenCaja + 7, x + margenCaja + 7, y + TAMANIO_CELDA - margenCaja - 8);
    }

    private void pintarJugador(Graphics2D g2) {
        int x = obtenerXJugador();
        int y = obtenerYJugador();
        int centroX = x + TAMANIO_CELDA / 2;
        int centroY = y + TAMANIO_CELDA / 2;

        if (pintarSpriteJugador(g2, x, y)) {
            return;
        }

        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(centroX - 17, centroY + 8, 34, 12);

        g2.setPaint(new GradientPaint(x, y, new Color(78, 149, 205), x, y + TAMANIO_CELDA, new Color(31, 79, 141)));
        g2.fillOval(centroX - 14, centroY - 3, 28, 26);

        g2.setColor(new Color(246, 209, 157));
        g2.fillOval(centroX - 12, centroY - 22, 24, 24);

        Polygon cabello = new Polygon();
        cabello.addPoint(centroX - 12, centroY - 12);
        cabello.addPoint(centroX - 7, centroY - 23);
        cabello.addPoint(centroX + 12, centroY - 18);
        cabello.addPoint(centroX + 10, centroY - 9);
        g2.setColor(new Color(72, 52, 42));
        g2.fillPolygon(cabello);

        g2.setColor(new Color(255, 255, 255, 170));
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval(centroX - 14, centroY - 3, 28, 26);
        g2.drawOval(centroX - 12, centroY - 22, 24, 24);
        pintarIndicadorDireccion(g2, centroX, centroY);
    }

    private int obtenerXJugador() {
        double progreso = obtenerProgresoAnimacionSuavizado();
        double columna = columnaJugadorAnterior + (columnaJugador - columnaJugadorAnterior) * progreso;
        return MARGEN + (int) Math.round(columna * obtenerTamanioCelda());
    }

    private int obtenerYJugador() {
        double progreso = obtenerProgresoAnimacionSuavizado();
        double fila = filaJugadorAnterior + (filaJugador - filaJugadorAnterior) * progreso;
        return MARGEN + (int) Math.round(fila * obtenerTamanioCelda());
    }

    private double obtenerProgresoAnimacion() {
        if (!animacionMovimiento.isRunning()) {
            return 1.0;
        }

        long transcurrido = System.currentTimeMillis() - inicioAnimacion;
        return Math.min(1.0, transcurrido / (double) DURACION_ANIMACION_MS);
    }

    private double obtenerProgresoAnimacionSuavizado() {
        double progreso = obtenerProgresoAnimacion();
        return progreso * progreso * (3.0 - 2.0 * progreso);
    }

    private boolean pintarSpriteJugador(Graphics2D g2, int x, int y) {
        BufferedImage jugador = sprites.get("jugador");
        if (jugador == null) {
            return false;
        }

        int tamanioCelda = obtenerTamanioCelda();
        AffineTransform original = g2.getTransform();
        double centroX = x + tamanioCelda / 2.0;
        double centroY = y + tamanioCelda / 2.0;
        g2.rotate(obtenerAnguloJugador(), centroX, centroY);
        g2.drawImage(jugador, x, y, tamanioCelda, tamanioCelda, null);
        g2.setTransform(original);
        pintarIndicadorDireccion(g2, (int) Math.round(centroX), (int) Math.round(centroY));
        return true;
    }

    private double obtenerAnguloJugador() {
        if (direccionFila < 0) {
            return Math.PI;
        }
        if (direccionFila > 0) {
            return 0.0;
        }
        if (direccionColumna < 0) {
            return Math.PI / 2.0;
        }
        if (direccionColumna > 0) {
            return -Math.PI / 2.0;
        }
        return 0.0;
    }

    private void pintarIndicadorDireccion(Graphics2D g2, int centroX, int centroY) {
        int puntaX = centroX + direccionColumna * 21;
        int puntaY = centroY + direccionFila * 21;
        int lateralX = -direccionFila * 6;
        int lateralY = direccionColumna * 6;
        int baseX = centroX + direccionColumna * 10;
        int baseY = centroY + direccionFila * 10;

        Polygon indicador = new Polygon();
        indicador.addPoint(puntaX, puntaY);
        indicador.addPoint(baseX + lateralX, baseY + lateralY);
        indicador.addPoint(baseX - lateralX, baseY - lateralY);

        g2.setColor(new Color(255, 244, 198, 210));
        g2.fillPolygon(indicador);
        g2.setColor(new Color(52, 72, 85, 170));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawPolygon(indicador);
    }

    private int obtenerTamanioCelda() {
        int anchoDisponible = Math.max(TAMANIO_CELDA * columnas, getWidth() - MARGEN * 2);
        return Math.max(TAMANIO_CELDA, anchoDisponible / columnas);
    }

    private int obtenerXCasilla(int columna) {
        return MARGEN + columna * obtenerTamanioCelda();
    }

    private int obtenerYCasilla(int fila) {
        return MARGEN + fila * obtenerTamanioCelda();
    }

    public interface EstadoListener {
        void actualizarEstado(int movimientos, int cajasEnMeta, int totalCajas, boolean ganado);
    }
}
