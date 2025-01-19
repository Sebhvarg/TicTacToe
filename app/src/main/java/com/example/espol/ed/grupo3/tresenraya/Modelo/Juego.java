package com.example.espol.ed.grupo3.tresenraya.Modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.espol.ed.grupo3.tresenraya.GanadorActivity;
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


    public Tablero getTablero(){
        return tablero;
    }
    public Juego(Context context, boolean experto,TextView playertext, TextView cputext, ImageView playerimg, ImageView cpuimg ) {
        this.context = context;
        tablero = new Tablero();
        jugadorX = new Jugador('X');
        jugadorO = new Jugador('O');
        jugadorActual = jugadorO; // Empieza el jugador o
        esNivelExperto = experto;
        medio = new IA_Medio(tablero);
        this.experto = new IA_Experto(tablero);
        this.playertext = playertext;
        this.cputext = cputext;
        this.playerimg = playerimg;
        this.cpuimg = cpuimg;
    }

    public Jugador getJugadorX() {
        return jugadorX;
    }

    public Jugador getJugadorO() {
        return jugadorO;
    }


    public void turnoComputadora() {
        if (tablero.verificarVictoria('O') || tablero.verificarVictoria('O') || esEmpate()) {
            return;  // No hacer nada si ya hay un ganador o empate
        }

        // Deshabilitar botones mientras la computadora hace su movimiento
        deshabilitarBotones();

        int[] mejorMovimiento = (esNivelExperto) ? experto.calcularMovimiento() : medio.calcularMovimiento();


        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1) {
            playertext.animate().alpha(0.2f).setDuration(500).start();
            playerimg.animate().alpha(0.2f).setDuration(500).start();
            cputext.animate().alpha(1f).setDuration(200).start();
            cpuimg.animate().alpha(1f).setDuration(200).start();

            // Hacer el movimiento de la computadora
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                botones[mejorMovimiento[0]][mejorMovimiento[1]].setText("X");
                tablero.getTablero()[mejorMovimiento[0]][mejorMovimiento[1]] = 'X';

                // Cambiar la visibilidad de los elementos después de un pequeño retraso
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    cputext.animate().alpha(0.3f).setDuration(500).start();
                    cpuimg.animate().alpha(0.3f).setDuration(500).start();
                    playertext.animate().alpha(1f).setDuration(200).start();
                    playerimg.animate().alpha(1f).setDuration(200).start();
                }, 800);

                // Verificar si hay un ganador o empate después del movimiento de la computadora
                if (tablero.verificarVictoria('X')) {
                    mostrarGanador("Computadora");
                    deshabilitarBotones();
                } else if (esEmpate()) {
                    mostrarGanador("Empate");
                    deshabilitarBotones();
                } else {
                    cambiarTurno();
                    habilitarBotonesConRetraso();  // Habilitar botones después del turno de la computadora
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
        // Compara la ficha del jugador actual para cambiar de turno
        if (jugadorActual.getFicha() == 'X') {
            jugadorActual = jugadorO;
        } else {
            jugadorActual = jugadorX;
        }

        if(jugadorActual.getFicha() == 'X'){
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

        // Cambio: Se ha añadido el retraso en la transición del ganador
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (ganador.equals("Humano")) {
                playertext.animate().alpha(1f).setDuration(500).start();
                playerimg.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.equals("Computadora")) {
                cputext.animate().alpha(1f).setDuration(500).start();
                cpuimg.animate().alpha(1f).setDuration(500).start();
            }

            Intent intent = new Intent(context, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador);
            context.startActivity(intent);

            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }, 300); // Cambio: Se ajustó el retraso para la transición de la actividad final
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
                botones[i][j].setEnabled(false); // Cambio: Se deshabilitan todos los botones después de cada movimiento
            }
        }
    }

    private void habilitarBotonesConRetraso() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> habilitarBotones(), 1000); // Cambio: Habilitar los botones con retraso para que el jugador vea el movimiento de la computadora
    }


    public void configurarBotones(Button[][] botones) {
        this.botones = botones;

        // Configurar cada botón en la interfaz
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                Button boton = botones[fila][columna];
                configurarBoton(boton, fila, columna);
            }
        }
    }

    // Método que configura un solo botón (como el que tienes en la clase Juego original)
    private void configurarBoton(Button boton, int fila, int columna) {
        boton.setTag(new int[]{fila, columna});

        boton.setOnClickListener(v -> {
            int[] coordinates = (int[]) boton.getTag();
            int filaSeleccionada = coordinates[0];
            int columnaSeleccionada = coordinates[1];

            if (boton.getText().toString().isEmpty() && jugadorActual.getFicha() == 'O') {
                // Realizar el movimiento
                boton.setTextColor(Color.parseColor("#418FBF"));
                boton.setText(jugadorActual.getFicha());

                // Actualizar el tablero
                tablero.getTablero()[filaSeleccionada][columnaSeleccionada] = jugadorActual.getFicha();

                // Deshabilitar botones inmediatamente después de realizar un movimiento
                deshabilitarBotones();

                // Verificar si hay un ganador
                if (tablero.verificarVictoria('O')) {
                    mostrarGanador("Jugador");
                    deshabilitarBotones();
                } else if (tablero.verificarVictoria('X')) {
                    mostrarGanador("Computadora");
                    deshabilitarBotones();
                } else if (esEmpate()) {
                    mostrarGanador("Empate");
                    deshabilitarBotones();
                } else {
                    // Cambiar el turno y llamar a la computadora si es necesario
                    cambiarTurno();
                    if (jugadorActual.getFicha() == 'X') {
                        turnoComputadora();
                    } else {
                        habilitarBotones();
                    }
                }
            }
        });
    }

}
