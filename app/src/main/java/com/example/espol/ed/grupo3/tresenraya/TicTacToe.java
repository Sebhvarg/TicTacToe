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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Computadora;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Humano;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Jugador;
import android.media.MediaPlayer;

import java.util.Objects;


public class TicTacToe extends AppCompatActivity {
    private Jugador jugadorActual;
    private Button[][] botones;
    private char[][] tablero = new char[3][3];

    private TextView cputext;

    private TextView playertext;

    private ImageView cpuimg;

    private ImageView playerimg;

    private ImageButton btnexit;

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boton.getText().toString().isEmpty()) {
                    // Realizar el movimiento
                    boton.setTextColor(Color.parseColor("#418FBF"));
                    boton.setText(jugadorActual.getTurno());

                    // Actualizar el tablero
                    tablero[fila][columna] = jugadorActual.getTurno().charAt(0);

                    // Verificar si hay un ganador
                    if (verificarVictoria('O')) {  // Si el jugador humano gana
                        mostrarGanador("¡Jugador Humano Ganador!");
                        deshabilitarBotones();
                    } else if (verificarVictoria('X')) {  // Si la computadora gana
                        mostrarGanador("¡Computadora Ganadora!");
                        deshabilitarBotones();
                    } else if (esEmpate()) {  // Si es empate
                        mostrarGanador("¡Empate!");
                        deshabilitarBotones();
                    } else {
                        // Cambiar el turno y llamar a la computadora si es necesario
                        cambiarTurno();
                        if (jugadorActual.getTurno().equals("X")) {  // Si es el turno de la computadora
                            turnoComputadora();
                        }
                    }
                }
            }
        });
    }


    private void turnoComputadora() {
        if (verificarVictoria('O') || verificarVictoria('X') || esEmpate()) {
            return;  // No hacer nada si ya hay un ganador o empate
        }

        int[] mejorMovimiento = calcularMejorMovimiento();

        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1){
            playertext.animate().alpha(0.2f).setDuration(500).start();
            playerimg.animate().alpha(0.2f).setDuration(500).start();
            cputext.animate().alpha(1f).setDuration(200).start();
            cpuimg.animate().alpha(1f).setDuration(200).start();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                botones[mejorMovimiento[0]][mejorMovimiento[1]].setText("X");
            }, 1000);
                tablero[mejorMovimiento[0]][mejorMovimiento[1]] = 'X';

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                cputext.animate().alpha(0.3f).setDuration(500).start();
                cpuimg.animate().alpha(0.3f).setDuration(500).start();
                playertext.animate().alpha(1f).setDuration(200).start();
                playerimg.animate().alpha(1f).setDuration(200).start();
            }, 800);


            if (verificarVictoria('X')) {
                mostrarGanador("¡Computadora Ganadora!");
                deshabilitarBotones();
            } else if (esEmpate()) {
                mostrarGanador("¡Empate!");
                deshabilitarBotones();
            } else {
                cambiarTurno();
            }
        }
    }

    private void deshabilitarBotones() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setEnabled(false);
                botones[i][j].animate().alpha(0.3f).start();
            }
        }
    }


    private void cambiarTurno(){
        if (jugadorActual instanceof Humano){

            jugadorActual = new Computadora();
        } else{
            jugadorActual = new Humano();
        }
        if(Objects.equals(jugadorActual.getTurno(), "X")){
            cputext.animate().alpha(0.2f).setDuration(500).start();
            cpuimg.animate().alpha(0.2f).setDuration(500).start();
            playertext.animate().alpha(1f).setDuration(200).start();
            playerimg.animate().alpha(1f).setDuration(200).start();

        }
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
                Log.d("VerificarVictoria", "Victoria en columna " + i);
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


    public int[] calcularMejorMovimiento() {

        int[] mejorMovimiento = {-1, -1};


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'X';
                    if (verificarVictoria('X')) {
                        tablero[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    tablero[i][j] = '\0';
                }
            }
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'O';
                    if (verificarVictoria('O')) {
                        tablero[i][j] = '\0';
                        return new int[]{i, j};
                    }
                    tablero[i][j] = '\0';
                }
            }
        }

        int mejorValor = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    tablero[i][j] = 'X';
                    int valorMovimiento = minimax(false);
                    tablero[i][j] = '\0';

                    if (valorMovimiento > mejorValor) {
                        mejorValor = valorMovimiento;
                        mejorMovimiento[0] = i;
                        mejorMovimiento[1] = j;
                    }
                }
            }
        }
        return mejorMovimiento;
    }


    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = '\0';
            }
        }
    }

    private int minimax(boolean esTurnoHumano) {
        if (verificarVictoria('X')) return 10;
        if (verificarVictoria('O')) return -10;
        if (esEmpate()) return 0;

        if (esTurnoHumano) {
            int mejorValor = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') {
                        tablero[i][j] = 'O';
                        mejorValor = Math.min(mejorValor, minimax(false));
                        tablero[i][j] = '\0';
                    }
                }
            }
            return mejorValor;
        } else {
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == '\0') {
                        tablero[i][j] = 'X';
                        mejorValor = Math.max(mejorValor, minimax(true));
                        tablero[i][j] = '\0';
                    }
                }
            }
            return mejorValor;
        }
    }


    private boolean esEmpate() {
        // Verificamos si hay alguna casilla vacía
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == '\0') {
                    return false;  // Si hay alguna casilla vacía, no es empate
                }
            }
        }
        return true;  // Si no hay casillas vacías, es empate
    }


    private void mostrarGanador(String ganador) {
        // Aplicar la animación de transición
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        // Usar Handler para retrasar la llamada a startActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            // Animar al ganador o los elementos de la interfaz
            if (ganador.contains("Humano")) {
                playertext.animate().alpha(1f).setDuration(500).start();
                playerimg.animate().alpha(1f).setDuration(500).start();
            } else if (ganador.contains("Computadora")) {
                cputext.animate().alpha(1f).setDuration(500).start();
                cpuimg.animate().alpha(1f).setDuration(500).start();
            }

            // Llamar a la siguiente actividad
            Intent intent = new Intent(this, GanadorActivity.class);
            intent.putExtra("GANADOR", ganador);
            startActivity(intent);
        }, 2000); // Retraso de 300ms (ajustar si es necesario)
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        jugadorActual = new Humano();
        cputext = findViewById(R.id.cputext);
        cpuimg = findViewById(R.id.cpuimg);
        playertext = findViewById(R.id.playertext);
        playerimg = findViewById(R.id.playerimg);
        btnexit = findViewById(R.id.btnexit);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        botones = new Button[3][3];
        btnexit.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deshabilitarBotones();
                Intent intent = new Intent(TicTacToe.this, MainActivity.class);
                startActivity(intent);
            }
        }));
        //Iniciar
        ReproductorControlador.getInstance().play();
        inicializarTablero();


        cputext.animate().alpha(0.2f).setDuration(100).start();
        cpuimg.animate().alpha(0.2f).setDuration(100).start();

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                int index = i * 3 + j;
                Button boton = (Button) gridLayout.getChildAt(index);
                botones[i][j] = boton;
                configurarBotones(boton, i, j);
            }
        }
    }

}




