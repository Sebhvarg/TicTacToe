package com.example.espol.ed.grupo3.tresenraya.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.espol.ed.grupo3.tresenraya.GanadorActivity;
import com.example.espol.ed.grupo3.tresenraya.HistorialManager;
import com.example.espol.ed.grupo3.tresenraya.R;

public class Juego {
    private Context context;
    private Tablero tablero;
    private Jugador jugadorX, jugadorO, jugadorActual;
    private IA_Medio medio;
    private IA_Experto experto;
    private boolean esNivelExperto;
    private Button[][] botones;
    private TextView playertext, cputext;
    private ImageView playerimg, cpuimg;
    private HistorialManager historialManager;

    public Tablero getTablero(){
        return tablero;
    }
    public Juego(Context context, boolean experto,TextView playertext, TextView cputext, ImageView playerimg, ImageView cpuimg, HistorialManager historialManager ) {
        this.context = context;
        tablero = new Tablero();
        jugadorX = new Jugador('X');
        jugadorO = new Jugador('O');
        jugadorActual = jugadorO;
        esNivelExperto = experto;
        medio = new IA_Medio(tablero);
        this.experto = new IA_Experto(tablero);
        this.playertext = playertext;
        this.cputext = cputext;
        this.playerimg = playerimg;
        this.cpuimg = cpuimg;
        this.historialManager = historialManager;
    }

    private boolean verificarEstadoDelJuego() {
        if (tablero.verificarVictoria(jugadorO.getFicha())){
            mostrarGanador("Humano");
            deshabilitarBotones();
            return true;
        }
        if (tablero.verificarVictoria(jugadorX.getFicha())) {
            mostrarGanador("Computadora");
            deshabilitarBotones();
            return true;
        }
        if (esEmpate()) {
            mostrarGanador("Empate");
            deshabilitarBotones();
            return true;
        }
        return false;
    }

    public void turnoComputadora() {
        if (verificarEstadoDelJuego()) {
            return;
        }
        deshabilitarBotones();
        int[] mejorMovimiento = (esNivelExperto) ? experto.calcularMovimiento() : medio.calcularMovimiento();
        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1) {
            playertext.animate().alpha(0.2f).setDuration(500).start();
            playerimg.animate().alpha(0.2f).setDuration(500).start();
            cputext.animate().alpha(1f).setDuration(200).start();
            cpuimg.animate().alpha(1f).setDuration(200).start();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                botones[mejorMovimiento[0]][mejorMovimiento[1]].setText("X");
                botones[mejorMovimiento[0]][mejorMovimiento[1]].setTextColor(Color.parseColor("#BF0413"));
                tablero.getTablero()[mejorMovimiento[0]][mejorMovimiento[1]] = jugadorX.getFicha();
                historialManager.registrarJugada("Computadora", mejorMovimiento[0], mejorMovimiento[1]);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    cputext.animate().alpha(0.3f).setDuration(500).start();
                    cpuimg.animate().alpha(0.3f).setDuration(500).start();
                    playertext.animate().alpha(1f).setDuration(200).start();
                    playerimg.animate().alpha(1f).setDuration(200).start();
                }, 800);
                if (!verificarEstadoDelJuego()) {
                    cambiarTurno();
                    habilitarBotonesConRetraso();
                }
            }, 1000);
        }
    }

    public boolean verificarVictoria() {
        return tablero.verificarVictoria(jugadorActual.getFicha());
    }

    public boolean esEmpate() {
        return tablero.esEmpate();
    }

    public void cambiarTurno() {
        if (jugadorActual.getFicha() == jugadorX.getFicha()) {
            jugadorActual = jugadorO;
        } else {
            jugadorActual = jugadorX;
        }
        if(jugadorActual.getFicha() == jugadorX.getFicha()){
            cputext.animate().alpha(0.2f).setDuration(500).start();
            cpuimg.animate().alpha(0.2f).setDuration(500).start();
            playertext.animate().alpha(1f).setDuration(200).start();
            playerimg.animate().alpha(1f).setDuration(200).start();
        }
    }


    public void mostrarGanador(String ganador) {
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        historialManager.setGanador(ganador);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (ganador.equals("Humano")) {
                playertext.animate().alpha(1f).setDuration(500).start();
                playerimg.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.equals("Computadora")) {
                cputext.animate().alpha(1f).setDuration(500).start();
                cpuimg.animate().alpha(1f).setDuration(500).start();
            }
            Intent intent = new Intent(context, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador.trim());
            context.startActivity(intent);
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }, 300);
    }

    public void habilitarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (botones[i][j].getText().toString().isEmpty()) {
                    botones[i][j].setEnabled(true);
                }
            }
        }
    }

    private void deshabilitarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setEnabled(false);
            }
        }
    }

    private void habilitarBotonesConRetraso() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> habilitarBotones(), 1000);
    }

    public void configurarBotones(Button[][] botones) {
        this.botones = botones;
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                Button boton = botones[fila][columna];
                configurarBoton(boton, fila, columna);
            }
        }
    }

    private void configurarBoton(Button boton, int fila, int columna) {
        boton.setTag(new int[]{fila, columna});
        boton.setOnClickListener(v -> {
            int[] coordinates = (int[]) boton.getTag();
            int filaSeleccionada = coordinates[0];
            int columnaSeleccionada = coordinates[1];
            if (boton.getText().toString().isEmpty() && jugadorActual.getFicha() == jugadorO.getFicha()) {
                boton.setTextColor(Color.parseColor("#418FBF"));
                boton.setText(jugadorActual.getFicha());
                tablero.getTablero()[filaSeleccionada][columnaSeleccionada] = jugadorActual.getFicha();
                historialManager.registrarJugada("Humano", filaSeleccionada, columnaSeleccionada); // Usar las variables correctas
                deshabilitarBotones();
                if (verificarEstadoDelJuego()) {
                    return;
                }
                cambiarTurno();
                if (jugadorActual.getFicha() == jugadorX.getFicha()) {
                    turnoComputadora();
                } else {
                    habilitarBotones();
                }
            }
        });
    }
}