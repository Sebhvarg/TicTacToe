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

    public Juego(Context context, boolean experto,TextView playertext, TextView cputext, ImageView playerimg, ImageView cpuimg ) {
        this.context = context;
        tablero = new Tablero();
        jugadorX = new Jugador('X');
        jugadorO = new Jugador('O');
        jugadorActual = jugadorX; // Empieza el jugador X
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

    public boolean jugarTurno(int fila, int columna) {
        if (tablero.getTablero()[fila][columna] == '\0') {
            tablero.colocarFicha(fila, columna, jugadorActual.getFicha());
            return true;
        }
        return false;
    }

    public boolean verificarVictoria() {
        return tablero.verificarVictoria(jugadorActual.getFicha());
    }

    public boolean esEmpate() {
        return tablero.esEmpate();
    }

    public void cambiarTurno() {
        jugadorActual = (jugadorActual == jugadorX) ? jugadorO : jugadorX;
    }

    public void iniciarTurnoComputadora() {
        if (verificarVictoria() || esEmpate()) {
            return;
        }

        deshabilitarBotones();

        // Cambio: Se ha agregado un chequeo para determinar si la IA debe jugar en nivel experto o medio
        int[] mejorMovimiento = (esNivelExperto) ? experto.calcularMovimiento() : medio.calcularMovimiento();

        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1) {
            ejecutarMovimientoComputadora(mejorMovimiento);
        }
    }

    private void ejecutarMovimientoComputadora(int[] movimiento) {
        animarTurnoComputadora();

        // Cambio: Animación al mostrar el turno de la computadora antes de realizar el movimiento
        tablero.colocarFicha(movimiento[0], movimiento[1], 'O');
        if (verificarVictoria()) {
            mostrarGanador("Computadora");
        } else if (esEmpate()) {
            mostrarGanador("Empate");
        } else {
            cambiarTurno();
            habilitarBotonesConRetraso(); // Cambio: Retraso para habilitar los botones después del turno de la computadora
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

    private void animarTurnoComputadora() {
        playertext.animate().alpha(0.2f).setDuration(500).start();
        playerimg.animate().alpha(0.2f).setDuration(500).start();
        cputext.animate().alpha(1f).setDuration(200).start();
        cpuimg.animate().alpha(1f).setDuration(200).start(); // Cambio: Se añadió una animación para cambiar el estado visual durante el turno de la computadora
    }

    public void configurarBotones(Button[][] botones) {
        this.botones = botones;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button boton = botones[i][j];
                int fila = i;
                int columna = j;

                // Cuando el humano hace un movimiento
                boton.setOnClickListener(v -> {
                    if (boton.getText().toString().isEmpty() && jugadorActual.getFicha() == 'O') {
                        boton.setTextColor(Color.parseColor("#418FBF"));
                        boton.setText(String.valueOf(jugadorActual.getFicha()));

                        // Coloca la ficha en el tablero
                        tablero.getTablero()[fila][columna] = jugadorActual.getFicha();

                        // Deshabilitar botones después de colocar una ficha
                        deshabilitarBotones();

                        // Verificar si el jugador ganó
                        if (tablero.verificarVictoria('O')) {
                            mostrarGanador("Humano");
                            deshabilitarBotones(); // Deshabilitar si el humano gana
                        } else if (tablero.verificarVictoria('X')) {
                            mostrarGanador("Computadora");
                            deshabilitarBotones(); // Deshabilitar si la computadora gana
                        } else if (esEmpate()) {
                            mostrarGanador("Empate");
                            deshabilitarBotones(); // Deshabilitar si hay empate
                        } else {
                            cambiarTurno(); // Cambiar turno después del movimiento del humano
                            if (jugadorActual.getFicha() == 'X') { // Si es el turno de la computadora
                                iniciarTurnoComputadora(); // La computadora juega
                            } else {
                                habilitarBotones(); // Habilitar botones si es el turno del humano
                            }
                        }
                    }
                });

            }
        }
    }

}
