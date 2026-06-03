package vista;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GestorSonido {
    
    private static final String ASSETS_DIR = "aca va el directorio";
    
    private Map<String, Clip> soundCache;

    public GestorSonido() {
        soundCache = new HashMap<>();
    }

    public void loadSound(String alias, String fileName) {
        if (soundCache.containsKey(alias)) {
            return;
        }
        
        try {
            File audioFile = new File(ASSETS_DIR + "/" + fileName);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            soundCache.put(alias, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al cargar el sonido: " + fileName);
            e.printStackTrace();
        }
    }
    
    public void play(String alias) {
        Clip clip = soundCache.get(alias);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Sonido no encontrado en caché: " + alias);
        }
    }

    public void loop(String alias) {
        Clip clip = soundCache.get(alias);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(String alias) {
        Clip clip = soundCache.get(alias);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void cleanup() {
        for (Clip clip : soundCache.values()) {
            clip.stop();
            clip.close();
        }
        soundCache.clear();
    }
}