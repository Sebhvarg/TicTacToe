package com.example.espol.ed.grupo3.tresenraya;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private HistorialManager(Context context) {
        this.context = context;
        this.jugadas = new ArrayList<>();
        this.fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    public HistorialManager(){
        this.jugadas = new ArrayList<>();
        this.fechaPartida = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    public static synchronized HistorialManager getInstance(Context context) {
        if (instance == null) {
            instance = new HistorialManager(context);
        }
        instance.context = context;
        return instance;
    }

    public void iniciarNuevaPartida(String modo) {
        this.jugadas.clear();
        this.ganador = null;
        this.modoJuego = modo;
    }

    // Añade este método en la clase HistorialManager
    public String[] obtenerHistorialComoArray() {
        ArrayList<String> historialCompleto = new ArrayList<>();
        historialCompleto.add("=== Historial de Partida ===");
        historialCompleto.add("Fecha: " + fechaPartida);
        historialCompleto.add("Modo de juego: " + modoJuego + "\n");

        for (int i = 0; i < jugadas.size(); i++) {
            historialCompleto.add("Jugada " + (i + 1) + ": " + jugadas.get(i));
        }

        if (ganador != null) {
            historialCompleto.add("\nResultado final: " +
                    (ganador.equals("Empate") ? "¡Empate!" : "¡" + ganador + " ha ganado!"));
        }

        return historialCompleto.toArray(new String[0]);
    }

    public void registrarJugada(String jugador, int fila, int columna) {
        String posicion = String.format("[%d,%d]", fila + 1, columna + 1);
        String simbolo = (jugador.equals("Computadora") || jugador.equals("Jugador 1")) ? "X" : "O";
        String jugada = String.format("%s colocó su %s en %s", jugador, simbolo, posicion);
        jugadas.add(jugada);
    }

    public void setGanador(String ganador) {
        this.ganador = ganador;
    }

    public void guardarHistorial() {
        try {
            File directorio = new File(context.getExternalFilesDir(null), "historial_tictactoe");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String nombreArchivo = "partida_" + fechaPartida.replace(":", "-").replace(" ", "_") + ".txt";
            File archivo = new File(directorio, nombreArchivo);

            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));

            // Encabezado
            writer.write("=== Historial de Partida ===\n");
            writer.write("Fecha: " + fechaPartida + "\n");
            writer.write("Modo de juego: " + modoJuego + "\n\n");

            // Jugadas
            for (int i = 0; i < jugadas.size(); i++) {
                writer.write("Jugada " + (i + 1) + ": " + jugadas.get(i) + "\n");
            }

            // Resultado
            writer.write("\nResultado final: ");
            if (ganador != null) {
                switch (ganador) {
                    case "Empate":
                        writer.write("¡Empate!");
                        break;
                    default:
                        writer.write("¡" + ganador + " ha ganado!");
                        break;
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        jugadas.clear();
        ganador = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        txtHistorial = findViewById(R.id.textHistorial);

    }

}
