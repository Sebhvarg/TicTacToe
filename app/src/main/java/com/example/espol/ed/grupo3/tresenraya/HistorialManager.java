package com.example.espol.ed.grupo3.tresenraya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistorialManager extends AppCompatActivity {
    private static HistorialManager instance;
    private ArrayList<String> jugadas;
    private Context context;
    private String ganador;
    private final String fechaPartida;
    private String modoJuego;
    private TextView txtHistorial;

    // Constructor privado único
    private HistorialManager(Context context) {
        this.context = context;
        this.jugadas = new ArrayList<>();
        this.fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    // Método singleton para obtener la instancia
    public static synchronized HistorialManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistorialManager(context.getApplicationContext());
        }
        return instance;
    }

    // Método para iniciar una nueva partida
    public void iniciarNuevaPartida(String modo) {
        this.jugadas.clear();
        this.ganador = null;
        this.modoJuego = modo;
        guardarHistorialEnPreferencias();
    }

    // Método para guardar el historial en SharedPreferences
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
        if (modoJuego != null) {
            historial.append("🎮 Modo: ").append(modoJuego).append("\n\n");
        }

        // Agregamos las jugadas
        for (int i = 0; i < jugadas.size(); i++) {
            historial.append("► ").append(jugadas.get(i)).append("\n");
        }

        // Agregamos el resultado final si existe
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

        // Actualizamos el TextView si existe
        actualizarTextView();
    }

    // Método para recuperar el historial
    public String obtenerHistorialDePreferencias() {
        if (context == null) {
            Log.e("HistorialManager", "Contexto no disponible");
            return "No hay historial disponible.";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("Historial", MODE_PRIVATE);
        return sharedPreferences.getString("historial", "No hay historial disponible.");
    }

    // Método para registrar una jugada
    public void registrarJugada(String jugador, int fila, int columna) {
        String posicion = String.format("Fila %d, Columna %d", fila + 1, columna + 1);
        String simbolo = jugador.equals("Computadora") ? "X" : "O";
        String jugada = String.format("%s colocó %s en %s", jugador, simbolo, posicion);
        jugadas.add(jugada);
        Log.d("HistorialManager", "Jugada registrada: " + jugada);
        guardarHistorialEnPreferencias();
    }

    // Método para establecer el ganador
    public void setGanador(String ganador) {
        this.ganador = ganador;
        guardarHistorialEnPreferencias();
    }

    // Método para actualizar el TextView
    private void actualizarTextView() {
        if (txtHistorial != null) {
            txtHistorial.setText(obtenerHistorialDePreferencias());
        }
    }

    // Método para establecer el TextView
    public void setTextView(TextView textView) {
        this.txtHistorial = textView;
        actualizarTextView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        txtHistorial = findViewById(R.id.textHistorial);
        actualizarTextView();
    }
}