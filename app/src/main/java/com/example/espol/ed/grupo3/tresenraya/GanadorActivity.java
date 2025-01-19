package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.widget.ScrollView;

public class GanadorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganador);

        TextView textoGanador = findViewById(R.id.textoGanador);
        Button botonReiniciar = findViewById(R.id.botonReiniciar);
        Button botonHistorial = findViewById(R.id.botonHistorial);
        View layoutPrincipal = findViewById(R.id.layoutPrincipal);

        // Cargar animación
        Animation expandAnimation = AnimationUtils.loadAnimation(this, R.anim.expand_animation);

        // Iniciar animación en el contenedor principal
        layoutPrincipal.startAnimation(expandAnimation);

        // Configurar texto según el ganador
        String ganador = getIntent().getStringExtra("GANADOR");
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
                default:
                    textoGanador.setText("¡Resultado desconocido!");
                    break;
            }
        }

        // Configurar botón de historial
        botonHistorial.setOnClickListener(v -> {
            mostrarHistorialDialog();
        });

        // Configurar botón para reiniciar
        botonReiniciar.setOnClickListener(v -> {
            Intent intent = new Intent(GanadorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void mostrarHistorialDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historial de la Partida");

        // Obtener el historial del último juego
        String[] historial = HistorialManager.getInstance(this).obtenerHistorialComoArray();

        // Crear un TextView para mostrar el historial
        TextView textView = new TextView(this);
        textView.setPadding(20, 20, 20, 20);
        textView.setTextSize(16);

        // Construir el texto del historial
        StringBuilder historialText = new StringBuilder();
        for (String linea : historial) {
            historialText.append(linea).append("\n");
        }
        textView.setText(historialText.toString());

        // Añadir ScrollView para historiales largos
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);
        builder.setView(scrollView);

        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
