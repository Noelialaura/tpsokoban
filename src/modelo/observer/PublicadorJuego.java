package modelo.observer;

public interface PublicadorJuego {
    void suscribir(SuscriptorJuego suscriptor);
    void desuscribir(SuscriptorJuego suscriptor);
    void notificar(EventoJuego evento);
}
