package com.example.espol.ed.grupo3.tresenraya;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Computadora;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Humano;
import com.example.espol.ed.grupo3.tresenraya.Modelo.Jugador;


public class TicTacToe extends AppCompatActivity {
    private Jugador jugadorActual;
    private Button[][] botones;
    private TextView tvTurno;
    private char[][] tablero = new char[3][3];

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boton.getText().toString().isEmpty()) {
                    boton.setText(jugadorActual.getTurno());
                    tablero[fila][columna] = jugadorActual.getTurno().charAt(0);

                    if (verificarVictoria('O')){
                        tvTurno.setText("¡El humano ha ganado!");
                        deshabilitarBotones();
                    }
                    else if (esEmpate()){
                        tvTurno.setText("¡EMPATE!");
                        deshabilitarBotones();
                    } else{
                        cambiarTurno();
                        turnoComputadora();
                    }
                }
            }
        });
    }

    private void turnoComputadora(){
        if (verificarVictoria('O') || verificarVictoria('X') || esEmpate()) {
            return;
        }

        int[] mejorMovimiento = calcularMejorMovimiento();
        if (mejorMovimiento[0] != -1 && mejorMovimiento[1] != -1){
            botones[mejorMovimiento[0]][mejorMovimiento[1]].setText("X");
            tablero[mejorMovimiento[0]][mejorMovimiento[1]] = 'X';

            if (verificarVictoria('X')){
                tvTurno.setText("¡La computadora ha ganado!");
                deshabilitarBotones();
            } else if (esEmpate()){
                tvTurno.setText("¡EMPATE!");
                deshabilitarBotones();
            } else{
                cambiarTurno();
            }
        }
    }

    private void deshabilitarBotones(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                botones[i][j].setEnabled(false);
            }
        }
    }

    private void cambiarTurno(){
        if (jugadorActual instanceof Humano){
            jugadorActual = new Computadora();
        } else{
            jugadorActual = new Humano();
        }
        tvTurno.setText("Turno de " + jugadorActual.getTurno());
    }

    private boolean verificarVictoria(char turno) {
        for (int i = 0; i < 3; i++) {
            if (tablero[i][0] == turno && tablero[i][1] == turno && tablero[i][2] == turno) return true;
        }

        for (int j = 0; j < 3; j++) {
            if (tablero[0][j] == turno && tablero[1][j] == turno && tablero[2][j] == turno) return true;
        }

        if (tablero[0][0] == turno && tablero[1][1] == turno && tablero[2][2] == turno) return true;
        if (tablero[0][2] == turno && tablero[1][1] == turno && tablero[2][0] == turno) return true;

        return false;
    }


    public int[] calcularMejorMovimiento() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == ' ') {
                    tablero[i][j] = 'O';
                    if (verificarVictoria('O')) {
                        tablero[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    tablero[i][j] = ' ';
                }
            }
        }

        int mejorValor = Integer.MIN_VALUE;
        int[] mejorMovimiento = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == ' ') {
                    tablero[i][j] = 'X';
                    int valorMovimiento = minimax(false);
                    tablero[i][j] = ' ';

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
                tablero[i][j] = ' ';
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
                    if (tablero[i][j] == ' ') {
                        tablero[i][j] = 'O'; // Simula turno humano
                        mejorValor = Math.min(mejorValor, minimax(false));
                        tablero[i][j] = ' '; // Deshacer movimiento
                    }
                }
            }
            return mejorValor;
        } else {
            int mejorValor = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (tablero[i][j] == ' ') {
                        tablero[i][j] = 'X'; // Simula turno computadora
                        mejorValor = Math.max(mejorValor, minimax(true));
                        tablero[i][j] = ' '; // Deshacer movimiento
                    }
                }
            }
            return mejorValor;
        }
    }


    private boolean esEmpate(){
        for (int i = 0; i < 3; i++) {
            for (int j=0; j < 3; j++){
                if (botones[i][j].getText().toString().isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        jugadorActual = new Humano();
        tvTurno = findViewById(R.id.tv_turn);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        tvTurno.setText("Turno de " + jugadorActual.getTurno());
        botones = new Button[3][3];
        inicializarTablero();

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




