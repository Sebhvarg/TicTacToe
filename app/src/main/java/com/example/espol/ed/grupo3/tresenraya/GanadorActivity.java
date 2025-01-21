package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GanadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganador);
        TextView textoGanador = findViewById(R.id.textTitle);
        Button botonReiniciar = findViewById(R.id.botonReiniciar);
        Button botonHistorial = findViewById(R.id.btnhistorial);
        Button botonReiniciarPartida = findViewById(R.id.botonReiniciarPartida);
        View layoutPrincipal = findViewById(R.id.layoutPrincipal);
        Animation expandAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_animation);
        layoutPrincipal.startAnimation(expandAnimation);
        String ganador = getIntent().getStringExtra("GANADOR");
        Log.d("GANADOR_RECEPCIONADO", ganador);

        if (ganador != null) {
            switch (ganador) {
                case "Humano":
                    textoGanador.setText("¡Jugador Humano ha ganado!");
                    break;
                case "Computadora":
                    textoGanador.setText("¡Computadora ha ganado!");
                    break;
                case "Empate":
                    textoGanador.setText("¡Es un empate!");
                    break;
                case "Jugador 1":
                    textoGanador.setText("¡Jugador 1 ha ganado!");
                    break;
                case "Jugador 2":
                    textoGanador.setText("¡Jugador 2 ha ganado!");
                    break;
            }
        }
        botonReiniciar.setOnClickListener(v -> {
            Intent intent = new Intent(GanadorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        botonHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(GanadorActivity.this, HistorialListener.class);
            startActivity(intent);
            finish();
        });
        botonReiniciarPartida.setOnClickListener(v -> reiniciarPartida());

    }
    private void reiniciarPartida() {
        Intent intent = new Intent(GanadorActivity.this, TicTacToeActivity.class);
        startActivity(intent);
        finish();
    }
}
