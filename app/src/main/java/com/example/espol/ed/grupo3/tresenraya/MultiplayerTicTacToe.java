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

import com.example.espol.ed.grupo3.tresenraya.Modelo.Juego;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Tablero;

public class MultiplayerTicTacToe extends AppCompatActivity {

    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';
    private Button[][] botones;
    private char turnoActual = PLAYER_X;
    private TextView player1Text, player2Text;
    private Tablero tableroC;

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
                    tableroC.getTablero()[fila][columna] = turnoActual;

                    if (tableroC.verificarVictoria(PLAYER_O)) {
                        mostrarGanador("Jugador 1");
                    } else if (tableroC.verificarVictoria(PLAYER_X)) {
                        mostrarGanador("Jugador 2");
                    } else if (esEmpate()) {
                        mostrarGanador("Empate");
                    } else {
                        cambiarTurno();
                    }
                }
            }
        });
    }

    private void cambiarTurno() {
        if (turnoActual == PLAYER_X) {
            turnoActual = PLAYER_O;
            player2Text.setAlpha(0.3f);
            player1Text.setAlpha(1.0f);
        } else {
            turnoActual = PLAYER_X;
            player2Text.setAlpha(1.0f);
            player1Text.setAlpha(0.3f);
        }
    }

    private boolean esEmpate() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tableroC.getTablero()[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    private void mostrarGanador(String ganador) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            if (ganador.contains("Jugador 1")) {
                player1Text.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.contains("Jugador 2")) {
                player2Text.animate().alpha(1f).setDuration(500).start();
            }
            // Solo un Intent para iniciar GanadorActivity
            Intent intent = new Intent(this, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador);
            intent.putExtra("MODO_JUEGO", "MULTIJUGADOR"); // Agrega el modo de juego
            startActivity(intent);

            // Finaliza la actividad actual
            finish();
        }, 300);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_tic_tac_toe);
        ReproductorControlador.getInstance().init(this, R.raw.music);
        ReproductorControlador.getInstance().play();
        player1Text = findViewById(R.id.player1text);
        player2Text = findViewById(R.id.player2text);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        botones = new Button[3][3];
        tableroC = new Tablero();
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
            Intent intent = new Intent(MultiplayerTicTacToe.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReproductorControlador.getInstance().stop();
    }

}
