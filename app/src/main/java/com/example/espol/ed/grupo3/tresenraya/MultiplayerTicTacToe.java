package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MultiplayerTicTacToe extends AppCompatActivity {

    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    private char[][] tablero = new char[3][3];
    private Button[][] botones;
    private char turnoActual = PLAYER_X; // Iniciar con el Jugador 1 (X)
    private TextView player1Text, player2Text;

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boton.getText().toString().isEmpty()) {
                    if (turnoActual == PLAYER_O) {
                        boton.setText("O");
                        boton.setTextColor(Color.parseColor("#418FBF"));
                    } else {
                        boton.setText("X");
                        boton.setTextColor(Color.parseColor("#F3B503"));
                    }
                    tablero[fila][columna] = turnoActual;

                    if (verificarVictoria(PLAYER_O)) { // Si el jugador O gana
                        mostrarGanador("Jugador 1");
                        deshabilitarBotones();
                    } else if (verificarVictoria(PLAYER_X)) { // Si el jugador X gana
                        mostrarGanador("Jugador 2");
                        deshabilitarBotones();
                    } else if (esEmpate()) { // Si es empate
                        mostrarGanador("Empate");
                        deshabilitarBotones();
                    } else {
                        // Cambiar el turno
                        cambiarTurno();
                    }
                }
            }
        });
    }

    private void cambiarTurno() {
        if (turnoActual == PLAYER_X) {
            turnoActual = PLAYER_O;
            player1Text.setAlpha(0.3f);
            player2Text.setAlpha(1.0f);
        } else {
            turnoActual = PLAYER_X;
            player1Text.setAlpha(1.0f);
            player2Text.setAlpha(0.3f);
        }
    }

    private boolean verificarVictoria(char jugador) {

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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (ganador.contains("Jugador 1")) {
                player1Text.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.contains("Jugador 2")) {
                player2Text.animate().alpha(1f).setDuration(500).start();
            }
            Intent intent = new Intent(this, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador);
            startActivity(intent);
            finish();
        }, 300);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_tic_tac_toe);

        ReproductorControlador.getInstance().init(this, R.raw.music);
        ReproductorControlador.getInstance().play();

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

        ImageButton btnexit = findViewById(R.id.btnexit);
        btnexit.setOnClickListener(v -> {
            deshabilitarBotones();
            Intent intent = new Intent(MultiplayerTicTacToe.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReproductorControlador.getInstance().stop();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '\0';
            }
        }
    }












//    private void simularArbol(char[][] tableroActual, char jugador, NodoArbol nodo) {
//        // Recorrer todas las casillas
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (tableroActual[i][j] == '\0') { // Si la casilla está vacía
//                    char[][] nuevoEstado = NodoArbol.copiarTablero(tableroActual);
//                    nuevoEstado[i][j] = jugador; // Marcar movimiento
//                    NodoArbol nuevoNodo = new NodoArbol(nuevoEstado);
//                    nodo.hijos.add(nuevoNodo);
//
//                    // Cambiar turno para simular siguiente movimiento
//                    char siguienteJugador = (jugador == PLAYER_X) ? PLAYER_O : PLAYER_X;
//                    simularArbol(nuevoEstado, siguienteJugador, nuevoNodo);
//                }
//            }
//        }
//    }
//
//    private void dibujarArbol(NodoArbol nodo, int nivel) {
//        // Imprimir el nodo actual
//        StringBuilder logNodo = new StringBuilder();
//        for (char[] fila : nodo.estado) {
//            logNodo.append("| ");
//            for (char celda : fila) {
//                logNodo.append(celda == '\0' ? " " : celda).append(" | ");
//            }
//            logNodo.append("\n");
//        }
//        Log.d("TicTacToeTree", "Nivel " + nivel + ":\n" + logNodo);
//
//        // Dibujar hijos recursivamente
//        for (NodoArbol hijo : nodo.hijos) {
//            dibujarArbol(hijo, nivel + 1);
//        }
//    }
//
//    private void generarYMostrarArbol() {
//        NodoArbol raiz = new NodoArbol(tablero);
//        simularArbol(tablero, turnoActual, raiz);
//        dibujarArbol(raiz, 0);
//    }
//
//    // toy copiando el min max pero a mi mod prueba para resolver el min max no tan
//    // bueno
//    private void movimientoIA() {
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (tablero[i][j] == '\0') {
//                    tablero[i][j] = PLAYER_O;
//                    botones[i][j].setText("O");
//                    botones[i][j].setTextColor(Color.parseColor("#418FBF"));
//                    return;
//                }
//            }
//        }
//    }
//    // presentación de estadisticas
//    private static int victoriasJugador1 = 0;
//    private static int victoriasJugador2 = 0;
//    private static int empates = 0;
//
//    private void registrarEstadisticas(String ganador) {
//        if (ganador.equals("Jugador 1")) {
//            victoriasJugador1++;
//        } else if (ganador.equals("Jugador 2")) {
//            victoriasJugador2++;
//        } else if (ganador.equals("Empate")) {
//            empates++;
//        }
//        Log.d("Estadísticas", "Jugador 1: " + victoriasJugador1 +
//                ", Jugador 2: " + victoriasJugador2 +
//                ", Empates: " + empates);
//    }
//
}
