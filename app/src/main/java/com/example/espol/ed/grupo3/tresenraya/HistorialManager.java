package com.example.espol.ed.grupo3.tresenraya;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    // Constructor privado para inicializar la instancia con el contexto
    public HistorialManager() {
        this.jugadas = new ArrayList<>();
        this.fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // Constructor con contexto
    public HistorialManager(Context context) {
        this.jugadas = new ArrayList<>();
        this.fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        this.context = context;
    }

    // Método estático para obtener la instancia del HistorialManager
    public static synchronized HistorialManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistorialManager(context);
        }
        instance.context = context; // Asignamos el contexto a la instancia
        return instance;
    }

    // Método para guardar el historial en SharedPreferences
    public void guardarHistorialEnPreferencias() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Historial", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder historial = new StringBuilder();
        historial.append("=== Historial de Partida ===\n");
        historial.append("Fecha: " + fechaPartida + "\n");

        // Concatenamos las jugadas
        for (int i = 0; i < jugadas.size(); i++) {
            historial.append("Jugada " + (i + 1) + ": " + jugadas.get(i) + "\n");
        }

        // Agregamos el resultado final
        if (ganador != null) {
            historial.append("\nResultado final: ");
            if (ganador.equals("Empate")) {
                historial.append("¡Empate!");
            } else {
                historial.append("¡" + ganador + " ha ganado!");
            }
        }

        editor.putString("historial", historial.toString());
        editor.apply(); // Aplicamos los cambios
    }

    // Método para recuperar el historial de SharedPreferences
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
        String posicion = String.format("[%d,%d]", fila + 1, columna + 1);
        String simbolo = (jugador.equals("Computadora") || jugador.equals("Humano")) ? "X" : "O";
        String jugada = String.format("%s colocó su %s en %s", jugador, simbolo, posicion);
        jugadas.add(jugada);
        Log.d("HistorialManager", "Jugada registrada: " + jugada);
        guardarHistorialEnPreferencias(); // Guardamos el historial actualizado
    }

    // Método para establecer el ganador
    public void setGanador(String ganador) {
        this.ganador = ganador;
        guardarHistorialEnPreferencias(); // Guardamos el historial con el ganador
    }

    // Método onCreate que se ejecuta al iniciar la actividad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        txtHistorial = findViewById(R.id.textHistorial);

        String historialRecuperado = obtenerHistorialDePreferencias();
        txtHistorial.setText(historialRecuperado); // Mostramos el historial en el TextView
    }
}

