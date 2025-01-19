package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class SeleccionDificultadActivity extends AppCompatActivity {

    private Button btnMedio, btnExperto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_dificultad);

        // Inicializar los botones
        btnMedio = findViewById(R.id.btnMedio);
        btnExperto = findViewById(R.id.btnExperto);

        // Configurar eventos de clic para los botones
        btnMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Devolver el nivel "Medio" a la actividad principal
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nivel", "Medio");
                setResult(RESULT_OK, resultIntent);
                finish();  // Cierra esta actividad y regresa a TicTacToe
            }
        });

        btnExperto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Devolver el nivel "Experto" a la actividad principal
                Intent resultIntent = new Intent();
                resultIntent.putExtra("nivel", "Experto");
                setResult(RESULT_OK, resultIntent);
                finish();  // Cierra esta actividad y regresa a TicTacToe
            }
        });
    }


}