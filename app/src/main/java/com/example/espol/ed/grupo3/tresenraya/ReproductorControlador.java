package com.example.espol.ed.grupo3.tresenraya;
import android.content.Context;
import android.media.MediaPlayer;
public class ReproductorControlador {
    private static ReproductorControlador instance;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    // Constructor privado para el patrón Singleton
    private ReproductorControlador() {}

    public static synchronized ReproductorControlador getInstance() {
        if (instance == null) {
            instance = new ReproductorControlador();
        }
        return instance;
    }

    // Inicializa el MediaPlayer
    public void init(Context context, int resId) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setLooping(true); // Opcional: Música en bucle
        }
    }

    // Reproduce la música
    public void play() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    // Pausa la música
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    // Detiene la música
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null; // Libera recursos
            isPlaying = false;
        }
    }

    // Verifica si está reproduciendo
    public boolean isPlaying() {
        return isPlaying;
    }
}
