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

import java.util.Random;

public class TicTacToe extends AppCompatActivity {
    private Jugador jugadorActual;
    private Button[][] botones;
    private TextView tvTurno;


    private Jugador seleccionarJugador(){
        Random random = new Random();
        if (random.nextBoolean()){
            return new Humano();
        }
        return new Computadora();
    }

    private void configurarBotones(Button boton, int fila, int columna) {
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (boton.getText().toString().isEmpty()) {
                    boton.setText(jugadorActual.getTurno());

                    if (verificarVictoria(jugadorActual.getTurno())) {
                        tvTurno.setText("¡El jugador " + jugadorActual.getTurno() + " ha ganado!");
                        deshabilitarBotones();
                    } else {
                        cambiarTurno();
                    }
                }
            }
        });
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

    private boolean verificarVictoria(String turno) {
        for (int i = 0; i < 3; i++) {
            if (botones[i][0].getText().toString().equals(turno) &&
                    botones[i][1].getText().toString().equals(turno) &&
                    botones[i][2].getText().toString().equals(turno)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (botones[0][j].getText().toString().equals(turno) &&
                    botones[1][j].getText().toString().equals(turno) &&
                    botones[2][j].getText().toString().equals(turno)) {
                return true;
            }
        }

        if (botones[0][0].getText().toString().equals(turno) &&
                botones[1][1].getText().toString().equals(turno) &&
                botones[2][2].getText().toString().equals(turno)) {
            return true;
        }

        if (botones[0][2].getText().toString().equals(turno) &&
                botones[1][1].getText().toString().equals(turno) &&
                botones[2][0].getText().toString().equals(turno)) {
            return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        jugadorActual = seleccionarJugador();
        tvTurno = findViewById(R.id.tv_turn);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        tvTurno.setText("Turno de " + jugadorActual.getTurno());
        botones = new Button[3][3];

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