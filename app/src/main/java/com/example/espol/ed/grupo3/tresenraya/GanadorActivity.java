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

        // Obtén el nombre del ganador desde el intent
        String ganador = getIntent().getStringExtra("GANADOR");

        // Ajustar el mensaje basado en el ganador
        if (ganador != null) {
            if (ganador.equals("Humano")) {
                textoGanador.setText("¡Jugador Humano ha ganado!");
            } else if (ganador.equals("Computadora")) {
                textoGanador.setText("¡Computadora ha ganado!");
            } else if (ganador.equals("Empate")) {
                textoGanador.setText("¡Es un empate!");
            }
        }

        // Acción de reiniciar el juego o regresar al menú principal
        botonReiniciar.setOnClickListener(v -> {
            // Reiniciar el juego o regresar al menú principal
            Intent intent = new Intent(GanadorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

