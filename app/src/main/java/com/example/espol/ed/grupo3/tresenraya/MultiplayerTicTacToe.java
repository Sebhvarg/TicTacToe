package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MultiplayerTicTacToe extends AppCompatActivity {
    private char[][] tablero = new char[3][3];
    private Button[][] botones;
    private char turnoActual = 'O';
    private TextView player1Text, player2Text;

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(v -> {
            if (boton.getText().toString().isEmpty()) {
                // Realizar el movimiento
                boton.setText(turnoActual == 'O' ? "O" : "X");
                boton.setTextColor(turnoActual == 'O' ? Color.parseColor("#418FBF") : Color.parseColor("#BE0413"));
                tablero[fila][columna] = turnoActual;

                // Verificar si hay un ganador
                if (verificarVictoria(turnoActual)) {
                    mostrarGanador("¡Jugador " + (turnoActual == 'O' ? "1" : "2") + " Ganador!");
                    deshabilitarBotones();
                } else if (esEmpate()) {
                    mostrarGanador("¡Empate!");
                    deshabilitarBotones();
                } else {
                    // Cambiar el turno
                    cambiarTurno();
                }
            }
        });
    }

    private void cambiarTurno() {
        turnoActual = (turnoActual == 'O') ? 'X' : 'O';
        player1Text.setAlpha(turnoActual == 'O' ? 1.0f : 0.3f);
        player2Text.setAlpha(turnoActual == 'X' ? 1.0f : 0.3f);
    }

    private boolean verificarVictoria(char jugador) {
        // Comprobar filas
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == jugador && tablero[i][1] == jugador && tablero[i][2] == jugador) {
                return true;
            }
        }

        // Comprobar columnas
        for (int i = 0; i < 3; i++) {
            if (tablero[0][i] == jugador && tablero[1][i] == jugador && tablero[2][i] == jugador) {
                return true;
            }
        }

        // Comprobar diagonales
        if (tablero[0][0] == jugador && tablero[1][1] == jugador && tablero[2][2] == jugador) {
            return true;
        }
        if (tablero[0][2] == jugador && tablero[1][1] == jugador && tablero[2][0] == jugador) {
            return true;
        }

        return false;
    }

    private boolean esEmpate() {
        // Verificar si hay algún espacio vacío
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void deshabilitarBotones() {
        for (Button[] botonFila : botones) {
            for (Button boton : botonFila) {
                boton.setEnabled(false);
                boton.animate().alpha(0.3f).start();
            }
        }
    }

    private void mostrarGanador(String ganador) {
        // Ajustar el mensaje del ganador según el cambio visual
        if (ganador.contains("2")) {
            ganador = ganador.replace("2", "1");
        } else if (ganador.contains("1")) {
            ganador = ganador.replace("1", "2");
        }

        // Mostrar el ganador
        Intent intent = new Intent(this, GanadorActivity.class);
        intent.putExtra("GANADOR", ganador);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_tic_tac_toe);

        player1Text = findViewById(R.id.player1Text);
        player2Text = findViewById(R.id.player2Text);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        botones = new Button[3][3];

        inicializarTablero();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int index = i * 3 + j;
                Button boton = (Button) gridLayout.getChildAt(index);
                botones[i][j] = boton;
                configurarBotones(boton, i, j);
            }
        }
        cambiarTurno();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '\0';
            }
        }
    }
}


