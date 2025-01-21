package com.example.espol.ed.grupo3.tresenraya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HistorialListener extends AppCompatActivity {

    private TextView txtHistorial;
    private ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        btn = findViewById(R.id.btnVolver);

        txtHistorial = findViewById(R.id.textHistorial);

        // Get the history from HistorialManager
        String historial = HistorialManager.getInstance().obtenerHistorialDePreferencias();

        // Display the history in the TextView
        txtHistorial.setText(historial);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistorialListener.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}