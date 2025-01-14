package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GanadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganador);

        TextView textoGanador = findViewById(R.id.textoGanador);
        Button botonReiniciar = findViewById(R.id.botonReiniciar);
        String ganador = getIntent().getStringExtra("GANADOR");
        if (ganador != null) {
            if (ganador.equals("Humano")) {
                textoGanador.setText("¡Jugador Humano ha ganado!");
            } else if (ganador.equals("Computadora")) {
                textoGanador.setText("¡Computadora ha ganado!");
            } else if (ganador.equals("Empate")) {
                textoGanador.setText("¡Es un empate!");
            } else if (ganador.equals("Jugador 1")) {
                textoGanador.setText("¡Jugador 1 ha ganado!");
            } else if (ganador.equals("Jugador 2")) {
                textoGanador.setText("¡Jugador 2 ha ganado!");
            } else {
                textoGanador.setText("¡Resultado desconocido!");
            }
        }

        botonReiniciar.setOnClickListener(v -> {
            // Reiniciar el juego o regresar al menú principal
            Intent intent = new Intent(GanadorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
