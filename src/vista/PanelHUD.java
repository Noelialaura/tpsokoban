package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelHUD extends JPanel {
    private static final long serialVersionUID = 1L;

    private final TarjetaDato tarjetaMovimientos;
    private final TarjetaDato tarjetaCajas;
    private final TarjetaDato tarjetaEstado;
    private final TarjetaDato tarjetaHabilidad;
    private final JButton reiniciar;
    private final JButton btnVelocidad;
    private final JButton btnFuerza;
    private final JButton btnNormal;

    public PanelHUD() {
        setLayout(new BorderLayout(18, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JPanel encabezado = crearEncabezado();
        JPanel metricas   = crearMetricas();
        JPanel acciones   = crearAcciones();

        tarjetaMovimientos = new TarjetaDato("Movimientos", "0");
        tarjetaCajas       = new TarjetaDato("Cajas",       "0/0");
        tarjetaEstado      = new TarjetaDato("Estado",      "Jugando");
        tarjetaHabilidad   = new TarjetaDato("Habilidad",   "Normal");

        reiniciar    = crearBoton("Reiniciar",  new Color(241, 202, 115), new Color(196, 139, 62));
        btnNormal    = crearBoton("Normal",     new Color(160, 174, 160), new Color(100, 120, 100));
        btnVelocidad = crearBoton("⚡ Velocidad", new Color(100, 180, 255), new Color(40, 100, 200));
        btnFuerza    = crearBoton("💪 Fuerza",   new Color(255, 130, 100), new Color(190, 60, 40));

        metricas.add(tarjetaMovimientos);
        metricas.add(tarjetaCajas);
        metricas.add(tarjetaEstado);
        metricas.add(tarjetaHabilidad);

        acciones.add(btnNormal);
        acciones.add(btnVelocidad);
        acciones.add(btnFuerza);
        acciones.add(reiniciar);

        add(encabezado, BorderLayout.WEST);
        add(metricas,   BorderLayout.CENTER);
        add(acciones,   BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, new Color(23, 31, 35), getWidth(), getHeight(), new Color(44, 57, 50)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(255, 255, 255, 30));
        g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g2.setColor(new Color(0, 0, 0, 60));
        g2.drawLine(0, 0, getWidth(), 0);
        g2.dispose();
        super.paintComponent(graphics);
    }

    public void actualizarEstado(int movimientos, int cajasEnMeta, int totalCajas, boolean ganado) {
        tarjetaMovimientos.setValor(String.valueOf(movimientos));
        tarjetaCajas.setValor(cajasEnMeta + "/" + totalCajas);
        tarjetaEstado.setValor(ganado ? "Completado" : "Jugando");
        tarjetaEstado.setDestacado(ganado);
    }

    public void actualizarHabilidad(String nombreHabilidad) {
        tarjetaHabilidad.setValor(nombreHabilidad);
    }

    public void setAccionReiniciar(Runnable accion)   { reiniciar.addActionListener(e -> accion.run()); }
    public void setAccionVelocidad(Runnable accion)   { btnVelocidad.addActionListener(e -> accion.run()); }
    public void setAccionFuerza(Runnable accion)      { btnFuerza.addActionListener(e -> accion.run()); }
    public void setAccionNormal(Runnable accion)      { btnNormal.addActionListener(e -> accion.run()); }

    // ── builders ─────────────────────────────────────────────────────────────

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 1));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(148, 48));
        panel.add(crearEtiqueta("Sokoban",    Font.BOLD,  20, new Color(245, 241, 225)));
        panel.add(crearEtiqueta("Nivel demo", Font.PLAIN, 12, new Color(181, 194, 181)));
        return panel;
    }

    private JPanel crearMetricas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel crearAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panel.setOpaque(false);
        return panel;
    }

    private JLabel crearEtiqueta(String texto, int estilo, int tamanio, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(color);
        etiqueta.setFont(new Font(Font.SANS_SERIF, estilo, tamanio));
        return etiqueta;
    }

    private JButton crearBoton(String texto, Color colorArriba, Color colorAbajo) {
        Color ca = colorArriba, cb = colorAbajo;
        JButton boton = new JButton(texto) {
            private static final long serialVersionUID = 1L;
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D g2 = (Graphics2D) graphics.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, ca, 0, getHeight(), cb));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 239, 184, 100));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(graphics);
            }
        };
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setForeground(new Color(255, 255, 255));
        boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        boton.setMargin(new Insets(0, 4, 0, 4));
        boton.setMinimumSize(new Dimension(110, 36));
        boton.setPreferredSize(new Dimension(110, 36));
        return boton;
    }

    // ── TarjetaDato ───────────────────────────────────────────────────────────

    private static class TarjetaDato extends JPanel {
        private static final long serialVersionUID = 1L;

        private final JLabel etiqueta;
        private final JLabel valor;
        private boolean destacado;

        TarjetaDato(String nombre, String valorInicial) {
            setOpaque(false);
            setLayout(new GridLayout(2, 1, 0, 0));
            setPreferredSize(new Dimension(118, 46));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 5, 12));

            etiqueta = new JLabel(nombre);
            etiqueta.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            etiqueta.setForeground(new Color(178, 190, 177));

            valor = new JLabel(valorInicial);
            valor.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
            valor.setForeground(new Color(244, 239, 219));

            add(etiqueta);
            add(valor);
        }

        void setValor(String nuevoValor) { valor.setText(nuevoValor); repaint(); }

        void setDestacado(boolean d) {
            this.destacado = d;
            valor.setForeground(d ? new Color(143, 229, 144) : new Color(244, 239, 219));
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color fondo = destacado ? new Color(47, 80, 56, 180) : new Color(255, 255, 255, 24);
            Color borde = destacado ? new Color(143, 229, 144, 120) : new Color(255, 255, 255, 42);
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}
