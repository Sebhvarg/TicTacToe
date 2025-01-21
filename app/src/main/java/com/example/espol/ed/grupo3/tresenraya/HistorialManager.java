package com.example.espol.ed.grupo3.tresenraya;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistorialManager {
    private static HistorialManager instance;
    private ArrayList<String> jugadas;
    private Context context;
    private String ganador;
    private final String fechaPartida;


    private HistorialManager(){
        this.jugadas = new ArrayList<>();
        fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    public static synchronized HistorialManager getInstance() {
        if (instance == null) {
            instance = new HistorialManager();
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public void guardarHistorialEnPreferencias() {
        if (context == null) {
            Log.e("HistorialManager", "Contexto no disponible");
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("Historial", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder historial = new StringBuilder();
        historial.append("=== HISTORIAL DE PARTIDA ===\n\n");
        historial.append("📅 Fecha: ").append(fechaPartida).append("\n");
        for (int i = 0; i < jugadas.size(); i++) {
            historial.append("► ").append(jugadas.get(i)).append("\n");
        }
        if (ganador != null) {
            historial.append("\n=== RESULTADO FINAL ===\n");
            if (ganador.equals("Empate")) {
                historial.append("🤝 ¡Empate!");
            } else {
                historial.append("🏆 ¡").append(ganador).append(" ha ganado!");
            }
        }
        editor.putString("historial", historial.toString());
        editor.apply();
    }

    public String obtenerHistorialDePreferencias() {
        if (context == null) {
            Log.e("HistorialManager", "Contexto no disponible");
            return "No hay historial disponible.";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("Historial", MODE_PRIVATE);
        return sharedPreferences.getString("historial", "No hay historial disponible.");
    }

    public void registrarJugada(String jugador, int fila, int columna) {
        String posicion = String.format("Fila %d, Columna %d", fila + 1, columna + 1);
        String simbolo = jugador.equals("Computadora") ? "X" : "O";
        String jugada = String.format("%s colocó %s en %s", jugador, simbolo, posicion);
        jugadas.add(jugada);
        Log.d("HistorialManager", "Jugada registrada: " + jugada);
        guardarHistorialEnPreferencias();
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
        guardarHistorialEnPreferencias();
    }


}